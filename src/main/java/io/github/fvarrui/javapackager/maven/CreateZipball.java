package io.github.fvarrui.javapackager.maven;

import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executeMojo;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.groupId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;
import static org.twdata.maven.mojoexecutor.MojoExecutor.version;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import io.github.fvarrui.javapackager.model.Platform;
import io.github.fvarrui.javapackager.packagers.ArtifactGenerator;
import io.github.fvarrui.javapackager.packagers.Context;
import io.github.fvarrui.javapackager.packagers.Packager;
import io.github.fvarrui.javapackager.utils.VelocityUtils;

/**
 * Creates zipball (zip file) on Maven context 
 */
public class CreateZipball extends ArtifactGenerator<Packager> {
	
	public CreateZipball() {
		super("Zipball");
	}

	@Override
	public boolean skip(Packager packager) {
		return !packager.getCreateZipball();
	}

	@Override
	protected File doApply(Packager packager) {
		
		File assetsFolder = packager.getAssetsFolder();
		String name = packager.getName();
		String version = packager.getVersion();
		Platform platform = packager.getPlatform();
		File outputDirectory = packager.getOutputDirectory(); 

		try {

			// generate assembly.xml file 
			File assemblyFile = new File(assetsFolder, "assembly-zipball-" + platform + ".xml");
			VelocityUtils.render(platform + "/assembly.xml.vtl", assemblyFile, packager);
			
			// zip file name and format
			String format = "zip";
			
			// invokes plugin to assemble zipball and/or tarball
			executeMojo(
					plugin(
							groupId("org.apache.maven.plugins"), 
							artifactId("maven-assembly-plugin"), 
							version("3.1.1")
					),
					goal("single"),
					configuration(
							element("outputDirectory", outputDirectory.getAbsolutePath()),
							element("formats", element("format", format)),
							element("descriptors", element("descriptor", assemblyFile.getAbsolutePath())),
							element("appendAssemblyId", "false")							
					),
					Context.getMavenContext().getEnv()
				);

			// gets generated filename
			String finalName = Context.getMavenContext().getEnv().getMavenProject().getBuild().getFinalName();
			File finalFile = new File(outputDirectory, finalName + "." + format);

			// gets desired file name
			String zipName = packager.getZipballName() != null ? packager.getZipballName() : name + "-" + version + "-" + platform;
			File zipFile = new File(outputDirectory, zipName + "." + format);
			
			// rename generated to desired, replacing an archive left by a previous build
			Files.move(finalFile.toPath(), zipFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			return zipFile;
			
		} catch (Exception e) {
			
			throw new RuntimeException(e);
			
		}

	}

}
