package io.github.javapackager.gradle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.gradle.api.Project;
import org.gradle.api.file.DuplicatesStrategy;

import groovy.lang.Closure;
import io.github.javapackager.model.Arch;
import io.github.javapackager.model.LinuxConfig;
import io.github.javapackager.model.MacConfig;
import io.github.javapackager.model.Manifest;
import io.github.javapackager.model.Platform;
import io.github.javapackager.model.Scripts;
import io.github.javapackager.model.WindowsConfig;
import io.github.javapackager.packagers.PackagerSettings;

/**
 * JavaPackager plugin extension for Gradle  
 */
public class PackagePluginExtension extends PackagerSettings {

	private Project project;
	private DuplicatesStrategy duplicatesStrategy;
	
	public PackagePluginExtension(Project project) {
		super();
		this.project = project;
		this.platform = Platform.auto;
		this.additionalJlinkArgs = new ArrayList<>();
		this.additionalModules = new ArrayList<>();
		this.additionalModulePaths = new ArrayList<>();
		this.additionalResources = new ArrayList<>();
		this.administratorRequired = false;
		this.assetsDir = new File(project.getProjectDir(), "assets");
		this.bundleJre = true;
		this.copyDependencies = true;
		this.createTarball = false;
		this.createZipball = false;
		this.customizedJre = true;
		this.description = project.getDescription();
		this.extra = new HashMap<>();
		this.generateInstaller = true;
		this.jreDirectoryName = "jre";
		this.linuxConfig = new LinuxConfig();
		this.macConfig = new MacConfig();
		this.manifest = new Manifest();
		this.modules = new ArrayList<>();
		this.name = project.getName();
		this.organizationEmail = "";
		this.useResourcesAsWorkingDir = true;
		this.vmArgs = new ArrayList<>();
		this.appArgs = new ArrayList<>();
		this.winConfig = new WindowsConfig();
		this.outputDirectory = project.getLayout().getBuildDirectory().getAsFile().get();
		this.scripts = new Scripts();
		this.forceInstaller = false;
		this.arch = Arch.getDefault();
		this.duplicatesStrategy = DuplicatesStrategy.WARN;
	}
	
    public LinuxConfig linuxConfig(Closure<LinuxConfig> closure) {
        linuxConfig = new LinuxConfig();
        project.configure(linuxConfig, closure);
        return linuxConfig;
    }

    public MacConfig macConfig(Closure<MacConfig> closure) {
        macConfig = new MacConfig();
        project.configure(macConfig, closure);
        return macConfig;
    }

    public WindowsConfig winConfig(Closure<WindowsConfig> closure) {
        winConfig = new WindowsConfig();
        project.configure(winConfig, closure);
        return winConfig;
    }
    
    public Manifest manifest(Closure<Manifest> closure) {
        manifest = new Manifest();
        project.configure(manifest, closure);
        return manifest;
    }
    
    public Scripts scripts(Closure<Scripts> closure) {
        scripts = new Scripts();
        project.configure(scripts, closure);
        return scripts;
    }
    
    public void setDuplicatesStrategy(DuplicatesStrategy duplicatesStrategy) {
		this.duplicatesStrategy = duplicatesStrategy;
	}
    
    public DuplicatesStrategy getDuplicatesStrategy() {
		return duplicatesStrategy;
	}

}