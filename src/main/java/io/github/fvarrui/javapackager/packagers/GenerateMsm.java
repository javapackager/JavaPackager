package io.github.fvarrui.javapackager.packagers;

import java.io.File;

import io.github.fvarrui.javapackager.model.Platform;
import io.github.fvarrui.javapackager.utils.CommandUtils;
import io.github.fvarrui.javapackager.utils.Logger;
import io.github.fvarrui.javapackager.utils.VelocityUtils;
import io.github.fvarrui.javapackager.utils.XMLUtils;
import org.codehaus.plexus.util.cli.CommandLineException;

/**
 * Creates an MSI file including all app folder's content only for Windows so app
 * could be easily distributed
 */
public class GenerateMsm extends ArtifactGenerator<WindowsPackager> {

	public GenerateMsm() {
		super("MSI merge module");
	}

	@Override
	public boolean skip(WindowsPackager packager) {
		
		if (!packager.getWinConfig().isGenerateMsm() && !packager.getWinConfig().isGenerateMsi()) {
			return true;
		}
		
		if (!packager.getPlatform().isCurrentPlatform() && !packager.isForceInstaller()) {
			Logger.warn(getArtifactName() + " cannot be generated due to the target platform (" + packager.getPlatform() + ") is different from the execution platform (" + Platform.getCurrentPlatform() + ")!");
			return true;
		}
		
		return false;
	}

	@Override
	protected File doApply(WindowsPackager packager) throws Exception {
		
		if (packager.getMsmFile() != null) {
			return packager.getMsmFile();
		}

		try {
			String version = CommandUtils.execute("wix", "-version");
			packager.setWixMajorVersion(Integer.parseInt(version.split("\\.")[0]));
		} catch(CommandLineException ex) {
			try {
				CommandUtils.execute("candle", "-?");
				CommandUtils.execute("light", "-?");
				packager.setWixMajorVersion(3);
			} catch(CommandLineException ex2) {
				throw new Exception("Either 'wix' or 'candle' and 'light' must be on PATH");
			}
		}

		File assetsFolder = packager.getAssetsFolder();
		String name = packager.getName();
		File outputDirectory = packager.getOutputDirectory();
		String version = packager.getVersion();
		
		// generates WXS file from velocity template
		File wxsFile = new File(assetsFolder, name + ".msm.wxs");
		VelocityUtils.render("windows/msm.wxs.vtl", wxsFile, packager);
		Logger.info("WXS file generated in " + wxsFile + "!");

		// prettify wxs
		XMLUtils.prettify(wxsFile);

		File msmFile = new File(outputDirectory, name + "_" + version + ".msm");
		if(packager.getWixMajorVersion() == 3) {
			// candle wxs file
			Logger.info("Compiling file " + wxsFile);
			File wixobjFile = new File(assetsFolder, name + ".msm.wixobj");
			CommandUtils.execute("candle", "-arch", "x64", "-out", wixobjFile, wxsFile);
			Logger.info("WIXOBJ file generated in " + wixobjFile + "!");

			// lighting wxs file
			Logger.info("Linking file " + wixobjFile);
			CommandUtils.execute("light", "-sw1076", "-spdb", "-out", msmFile, wixobjFile);
		} else {
			Logger.info("Building file " + wxsFile);
			CommandUtils.execute("wix", "build", "-pdbtype", "none", "-arch", "x64", "-out", msmFile, wxsFile);
		}

		// setup file
		if (!msmFile.exists()) {
			throw new Exception("MSI installer file generation failed!");
		}
		
		packager.setMsmFile(msmFile);
		
		return msmFile;
	}

}
