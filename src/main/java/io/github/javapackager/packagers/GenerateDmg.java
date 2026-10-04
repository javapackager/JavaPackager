package io.github.javapackager.packagers;

import static io.github.javapackager.utils.CommandUtils.execute;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import io.github.javapackager.model.MacConfig;
import io.github.javapackager.model.Platform;
import io.github.javapackager.utils.FileUtils;
import io.github.javapackager.utils.Logger;
import io.github.javapackager.utils.ThreadUtils;
import io.github.javapackager.utils.VelocityUtils;

/**
 * Creates a DMG image file including all app folder's content only for MacOS so
 * app could be easily distributed
 */
public class GenerateDmg extends ArtifactGenerator<MacPackager> {

	private static final int HDIUTIL_ATTEMPTS = 3;

	public GenerateDmg() {
		super("DMG image");
	}
	
	@Override
	public boolean skip(MacPackager packager) {
		
		if (!packager.getMacConfig().isGenerateDmg()) {
			return true;
		}
		
		if (!packager.getPlatform().isCurrentPlatform() && !packager.isForceInstaller()) {
			Logger.warn(getArtifactName() + " cannot be generated due to the target platform (" + packager.getPlatform() + ") is different from the execution platform (" + Platform.getCurrentPlatform() + ")!");
			return true;
		}
		
		return false;		
	}
	
	@Override
	protected File doApply(MacPackager packager) throws Exception {

		File appFolder = packager.getAppFolder();
		File assetsFolder = packager.getAssetsFolder();
		String name = packager.getName();
		File outputDirectory = packager.getOutputDirectory();
		File iconFile = packager.getIconFile();
		String version = packager.getVersion();
		MacConfig macConfig = packager.getMacConfig();
		
		// sets volume name if blank
		String volumeName = defaultIfBlank(macConfig.getVolumeName(), name);
		
		// removes whitespaces from volume name
		if (StringUtils.containsWhitespace(volumeName)) {
			volumeName = volumeName.replaceAll(" ", "");
			Logger.warn("Whitespaces has been removed from volume name: " + volumeName);
		}
		
		// final dmg file
		File dmgFile = new File(outputDirectory, name + "_" + version + ".dmg");
		
		// temp dmg file
		File tempDmgFile = new File(assetsFolder, dmgFile.getName());

		// mount dir
		File mountFolder = new File("/Volumes/" + volumeName);

		// copies background file
		Logger.info("Copying background image");
		File backgroundFolder = FileUtils.mkdir(appFolder, ".background");
		File backgroundFile = new File(backgroundFolder, "background.png");
		if (macConfig.getBackgroundImage() != null)
			FileUtils.copyFileToFile(macConfig.getBackgroundImage(), backgroundFile);
		else 
			FileUtils.copyResourceToFile("/mac/background.png", backgroundFile);
		
		// copies volume icon
		Logger.info("Copying icon file: " + iconFile.getAbsolutePath());
		File volumeIcon = (macConfig.getVolumeIcon() != null) ? macConfig.getVolumeIcon() : iconFile;  
		FileUtils.copyFileToFile(volumeIcon, new File(appFolder, ".VolumeIcon.icns"));

		// creates image
		Logger.info("Creating image: " + tempDmgFile.getAbsolutePath());
		String osArchitecture = System.getProperty("os.arch");
		boolean isAarch64 = osArchitecture.equalsIgnoreCase("aarch64");
		String fileSystem = isAarch64 ? "APFS" : "HFS+";
		Logger.warn(osArchitecture + " architecture detected. Using " + fileSystem + " filesystem");
		hdiutil("create", "-srcfolder", appFolder, "-volname", volumeName, "-ov", "-fs", fileSystem, "-format", "UDRW", tempDmgFile);

		if (mountFolder.exists()) {
			Logger.info("Unmounting volume: " + mountFolder);
			detach(mountFolder);
		}
		
		// mounts image
		Logger.info("Mounting image: " + tempDmgFile.getAbsolutePath());
		String result = hdiutil("attach", "-readwrite", "-noverify", "-noautoopen", tempDmgFile);
		Optional<String> optDeviceName = Arrays.stream(result.split("\n"))
								.filter(s -> s.contains(mountFolder.getAbsolutePath()))
								.map(StringUtils::normalizeSpace)
								.map(s -> s.split(" ")[0])
								.findFirst();
		optDeviceName.ifPresent(deviceName -> Logger.info("- Device name: " + deviceName));
		
		// pause to prevent occasional "Can't get disk" (-1728) issues 
		// https://github.com/seltzered/create-dmg/commit/5fe7802917bb85b40c0630b026d33e421db914ea
		ThreadUtils.sleep(2000L);

		// creates a symlink to Applications folder
		Logger.info("Creating Applications link");
		File targetFolder = new File("/Applications");
		File linkFile = new File(mountFolder, "Applications");
		FileUtils.createSymlink(linkFile, targetFolder);

		// renders applescript 
		Logger.info("Rendering DMG customization applescript ... ");
		File applescriptFile = new File(assetsFolder, "customize-dmg.applescript");
		VelocityUtils.render("/mac/customize-dmg.applescript.vtl", applescriptFile, packager);
		Logger.info("Applescript rendered in " + applescriptFile.getAbsolutePath() + "!");
		
		// runs applescript 
		Logger.info("Running applescript");
		execute("/usr/bin/osascript", applescriptFile, volumeName);
	
		// makes sure it's not world writeable and user readable
		Logger.info("Fixing permissions...");
		execute("chmod", "-Rf", "u+r,go-w", mountFolder);

		if (!isAarch64) {
			// makes the top window open itself on mount:
			Logger.info("Blessing ...");
			try {
				execute("bless", "--folder", mountFolder, "--openfolder", mountFolder); }
			catch (Exception e){
				Logger.warn("Error blessing " + mountFolder + " due to: " + e.getMessage());
			}
		}

		// tells the volume that it has a special file attribute
		execute("SetFile", "-a", "C", mountFolder);
		
		// unmounts
		Logger.info("Unmounting volume: " + mountFolder);
		detach(mountFolder);
		
		// compress image
		Logger.info("Compressing disk image...");
		hdiutil("convert", tempDmgFile, "-ov", "-format", "UDZO", "-imagekey", "zlib-level=9", "-o", dmgFile);
		tempDmgFile.delete();

		// checks if dmg file was created
		if (!dmgFile.exists()) {
			throw new Exception(getArtifactName() + " generation failed!");
		}
		
		return dmgFile;
	}

	/**
	 * Runs hdiutil, retrying if it fails (e.g. "Resource busy" while the image is being scanned)
	 */
	private String hdiutil(Object... arguments) throws Exception {
		for (int attempt = 1; ; attempt++) {
			try {
				return execute("hdiutil", arguments);
			} catch (Exception e) {
				if (attempt == HDIUTIL_ATTEMPTS) throw e;
				Logger.warn("hdiutil " + arguments[0] + " failed (attempt " + attempt + " of " + HDIUTIL_ATTEMPTS + "), retrying: " + e.getMessage());
				ThreadUtils.sleep(attempt * 5000L);
			}
		}
	}

	/**
	 * Unmounts a volume, retrying if it's busy. A busy volume may still get unmounted
	 * a moment later, so it's done as soon as the mount folder is gone.
	 */
	private void detach(File mountFolder) throws Exception {
		for (int attempt = 1; mountFolder.exists(); attempt++) {
			try {
				execute("hdiutil", "detach", mountFolder);
				return;
			} catch (Exception e) {
				if (!mountFolder.exists()) return;
				if (attempt == HDIUTIL_ATTEMPTS) throw e;
				Logger.warn("hdiutil detach failed (attempt " + attempt + " of " + HDIUTIL_ATTEMPTS + "), retrying: " + e.getMessage());
				ThreadUtils.sleep(attempt * 5000L);
			}
		}
	}

}
