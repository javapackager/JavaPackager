package io.github.javapackager;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;

/**
 * Checks the plugin's Velocity templates (run by the checkTemplates Gradle task):
 * every template must parse, and the shell templates, rendered with sample settings,
 * must be valid Bash. The Bash checks are skipped on Windows.
 * <p>
 * Usage: TemplateCheck &lt;templates folder&gt; &lt;output folder&gt;
 */
public class TemplateCheck {

	private static final List<String> failures = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		File templatesDir = new File(args[0]);
		File outputDir = new File(args[1]);
		outputDir.mkdirs();

		VelocityEngine engine = new VelocityEngine();
		engine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "file");
		engine.setProperty("resource.loader.file.class", FileResourceLoader.class.getName());
		engine.setProperty("resource.loader.file.path", templatesDir.getAbsolutePath());
		engine.init();

		List<String> templates;
		try (Stream<Path> files = Files.walk(templatesDir.toPath())) {
			templates = files
				.filter(p -> p.toString().endsWith(".vtl"))
				.map(p -> templatesDir.toPath().relativize(p).toString().replace('\\', '/'))
				.sorted()
				.collect(Collectors.toList());
		}

		// 1. every template parses
		for (String template : templates) {
			try {
				engine.getTemplate(template, "UTF-8");
				System.out.println("PASS: parses: " + template);
			} catch (Exception e) {
				fail(template + " doesn't parse: " + e.getMessage());
			}
		}

		// 2. shell templates are valid Bash with different settings
		boolean bash = !System.getProperty("os.name").toLowerCase().startsWith("windows");
		if (!bash) {
			System.out.println("SKIP: Bash checks (Windows)");
		}
		for (Map<String, Object> info : linuxVariants()) {
			File script = render(engine, "linux/startup.sh.vtl", info, new File(outputDir, "startup-" + info.get("variant") + ".sh"));
			if (bash && script != null) {
				bashSyntax(script);
				if (info.get("jreMinVersion") != null) {
					compareVersionsTests(script, outputDir);
				}
			}
		}
		for (Map<String, Object> info : macVariants()) {
			File script = render(engine, "mac/startup.vtl", info, new File(outputDir, "mac-startup-" + info.get("variant") + ".sh"));
			if (bash && script != null) {
				bashSyntax(script);
			}
		}

		if (!failures.isEmpty()) {
			System.err.println(failures.size() + " template check(s) failed:");
			failures.forEach(f -> System.err.println("  " + f));
			System.exit(1);
		}
		System.out.println("All template checks passed");
	}

	private static List<Map<String, Object>> linuxVariants() {
		// bundled JRE, default settings
		Map<String, Object> bundled = linuxInfo("bundled-jre");
		// system Java with a minimum version, administrator, classpath, bootstrap script, envPath, no wrapped JAR
		Map<String, Object> full = linuxInfo("system-java");
		full.put("bundleJre", false);
		full.put("jreMinVersion", "11");
		full.put("administratorRequired", true);
		full.put("classpath", "extra:lib/other.jar");
		full.put("classpaths", Arrays.asList("extra", "lib/other.jar"));
		full.put("envPath", "/usr/local/bin:$PATH");
		full.put("scripts", map("bootstrap", "bootstrap.sh"));
		full.put("bootstrapFile", map("name", "bootstrap.sh"));
		full.put("linuxConfig", map("wrapJar", false));
		full.put("useResourcesAsWorkingDir", false);
		return Arrays.asList(bundled, full);
	}

	private static Map<String, Object> linuxInfo(String variant) {
		Map<String, Object> info = new HashMap<>();
		info.put("variant", variant);
		info.put("name", "HelloWorld");
		info.put("mainClass", "com.example.Main");
		info.put("bundleJre", true);
		info.put("jreDirectoryName", "jre");
		info.put("administratorRequired", false);
		info.put("useResourcesAsWorkingDir", true);
		info.put("linuxConfig", map("wrapJar", true));
		info.put("executable", map("name", "HelloWorld"));
		info.put("jarFile", map("name", "HelloWorld-1.0.0-runnable.jar"));
		info.put("vmArgs", Arrays.asList("-Xmx1g", "-Dapp.title=Hello World"));
		info.put("appArgs", Arrays.asList("--foo", "hello world"));
		info.put("classpaths", Collections.emptyList());
		info.put("scripts", new HashMap<>());
		return info;
	}

	private static List<Map<String, Object>> macVariants() {
		Map<String, Object> info = new HashMap<>();
		info.put("variant", "admin");
		info.put("name", "HelloWorld");
		info.put("launcher", map("name", "universalJavaApplicationStub"));
		return Collections.singletonList(info);
	}

	private static Map<String, Object> map(String key, Object value) {
		Map<String, Object> map = new HashMap<>();
		map.put(key, value);
		return map;
	}

	private static File render(VelocityEngine engine, String templatePath, Map<String, Object> info, File output) {
		try {
			VelocityContext context = new VelocityContext();
			context.put("info", info);
			Template template = engine.getTemplate(templatePath, "UTF-8");
			StringWriter writer = new StringWriter();
			template.merge(context, writer);
			Files.write(output.toPath(), writer.toString().replace("\r\n", "\n").getBytes(StandardCharsets.UTF_8));
			System.out.println("PASS: renders: " + templatePath + " (" + info.get("variant") + ")");
			return output;
		} catch (Exception e) {
			fail(templatePath + " (" + info.get("variant") + ") doesn't render: " + e.getMessage());
			return null;
		}
	}

	private static void bashSyntax(File script) throws IOException, InterruptedException {
		Result result = run("bash", "-n", script.getAbsolutePath());
		if (result.code == 0) {
			System.out.println("PASS: valid Bash: " + script.getName());
		} else {
			fail(script.getName() + " is not valid Bash: " + result.output);
		}
	}

	/**
	 * Runs the startup script's compareVersions() with known Java and minimum versions (#505)
	 */
	private static void compareVersionsTests(File script, File outputDir) throws IOException, InterruptedException {
		String content = new String(Files.readAllBytes(script.toPath()), StandardCharsets.UTF_8);
		int start = content.indexOf("function compareVersions()");
		int end = content.indexOf("\n}\n", start);
		if (start < 0 || end < 0) {
			fail(script.getName() + ": compareVersions() not found");
			return;
		}
		String[][] cases = {
			// java version, minimum version, expected result (0 = enough, 1 = too old)
			{ "17.0.9", "8", "0" },
			{ "9", "11", "1" },
			{ "1.8.0_392", "8", "0" },
			{ "1.8.0_392", "11", "1" },
			{ "11.0.2", "11", "0" },
			{ "21", "17.0.1", "0" },
			{ "17-ea", "17", "0" },
			{ "17.0.9", "17.0.10", "1" },
		};
		StringBuilder test = new StringBuilder(content.substring(start, end + 3));
		test.append("failed=0\n");
		for (String[] c : cases) {
			test.append("compareVersions '").append(c[0]).append("' '").append(c[1]).append("'; r=$?; ")
				.append("[ \"$r\" = ").append(c[2]).append(" ] || { echo 'java ").append(c[0]).append(" vs ").append(c[1])
				.append(": got '$r', expected ").append(c[2]).append("'; failed=1; }\n");
		}
		test.append("exit $failed\n");
		Path testFile = Paths.get(outputDir.getAbsolutePath(), "compare-versions-test.sh");
		Files.write(testFile, test.toString().getBytes(StandardCharsets.UTF_8));
		Result result = run("bash", testFile.toString());
		if (result.code == 0) {
			System.out.println("PASS: compareVersions() gives the expected results (" + cases.length + " cases)");
		} else {
			fail("compareVersions() gives wrong results: " + result.output);
		}
	}

	private static Result run(String... command) throws IOException, InterruptedException {
		Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
		String output = new String(readAll(process), StandardCharsets.UTF_8).trim();
		return new Result(process.waitFor(), output);
	}

	private static byte[] readAll(Process process) throws IOException {
		java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n;
		while ((n = process.getInputStream().read(buffer)) != -1) {
			out.write(buffer, 0, n);
		}
		return out.toByteArray();
	}

	private static void fail(String message) {
		System.out.println("FAIL: " + message);
		failures.add(message);
	}

	private static class Result {
		final int code;
		final String output;
		Result(int code, String output) {
			this.code = code;
			this.output = output;
		}
	}

}
