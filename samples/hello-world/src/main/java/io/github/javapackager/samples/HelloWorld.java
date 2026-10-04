package io.github.javapackager.samples;

/**
 * Sample app used by JavaPackager smoke tests: prints a known line and exits
 */
public class HelloWorld {

	public static void main(String[] args) {
		System.out.println("JavaPackager smoke test OK");
		System.out.println("os.arch=" + System.getProperty("os.arch"));
		System.out.println("java.version=" + System.getProperty("java.version"));
		System.out.println("args=" + java.util.Arrays.toString(args));
		System.out.println("smoke.prop=" + System.getProperty("smoke.prop"));
		System.out.println("java.class.path=" + System.getProperty("java.class.path"));
	}

}
