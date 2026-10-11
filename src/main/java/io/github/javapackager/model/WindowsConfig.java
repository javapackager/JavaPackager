package io.github.javapackager.model;

import static io.github.javapackager.utils.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.apache.commons.collections4.MapUtils;

import io.github.javapackager.packagers.Packager;
import io.github.javapackager.utils.Logger;

/**
 * JavaPackager Windows specific configuration
 */
public class WindowsConfig implements Serializable {
	private static final long serialVersionUID = 2106752412224694318L;

	private File icoFile;
	private HeaderType headerType;
	private String companyName;
	private String copyright;
	private String fileDescription;
	private String fileVersion;
	private String internalName;
	private String language;
	private String originalFilename;
	private String productName;
	private String productVersion;
	private String trademarks;
	private String txtFileVersion;
	private String txtProductVersion;
	private String shortcutName;
	private boolean disableDirPage = true;
	private boolean disableProgramGroupPage = true;
	private boolean disableFinishedPage = true;
	private boolean disableRunAfterInstall = true;
	private boolean disableWelcomePage = true;
	private boolean createDesktopIconTask = true;
	private boolean generateSetup = true;
	private boolean generateMsi = true;
	private boolean generateMsm = false;
	private String msiUpgradeCode;
	private boolean wrapJar = true;
	private LinkedHashMap<String, String> setupLanguages = new LinkedHashMap<>();
	private SetupMode setupMode = SetupMode.installForAllUsers;
	private WindowsSigning signing;
	private Registry registry = new Registry();
	private boolean removeOldLibs = false;
	private WindowsExeCreationTool exeCreationTool = WindowsExeCreationTool.launch4j;
	private String vmLocation;

	public File getIcoFile() {
		return icoFile;
	}

	public void setIcoFile(File icoFile) {
		this.icoFile = icoFile;
	}

	public HeaderType getHeaderType() {
		return headerType;
	}

	public void setHeaderType(HeaderType headerType) {
		this.headerType = headerType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getFileDescription() {
		return fileDescription;
	}

	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}

	public String getFileVersion() {
		return fileVersion;
	}

	public void setFileVersion(String fileVersion) {
		this.fileVersion = fileVersion;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}

	public String getTrademarks() {
		return trademarks;
	}

	public void setTrademarks(String trademarks) {
		this.trademarks = trademarks;
	}

	public String getTxtFileVersion() {
		return txtFileVersion;
	}

	public void setTxtFileVersion(String txtFileVersion) {
		this.txtFileVersion = txtFileVersion;
	}

	public String getTxtProductVersion() {
		return txtProductVersion;
	}

	public void setTxtProductVersion(String txtProductVersion) {
		this.txtProductVersion = txtProductVersion;
	}

	public String getShortcutName() {
		return shortcutName;
	}

	public void setShortcutName(String shortcutName) {
		this.shortcutName = shortcutName;
	}

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	public boolean isDisableDirPage() {
		return disableDirPage;
	}

	public void setDisableDirPage(boolean disableDirPage) {
		this.disableDirPage = disableDirPage;
	}

	public boolean isDisableProgramGroupPage() {
		return disableProgramGroupPage;
	}

	public void setDisableProgramGroupPage(boolean disableProgramGroupPage) {
		this.disableProgramGroupPage = disableProgramGroupPage;
	}

	public boolean isDisableFinishedPage() {
		return disableFinishedPage;
	}

	public void setDisableFinishedPage(boolean disableFinishedPage) {
		this.disableFinishedPage = disableFinishedPage;
	}

	public boolean isCreateDesktopIconTask() {
		return createDesktopIconTask;
	}

	public void setCreateDesktopIconTask(boolean createDesktopIconTask) {
		this.createDesktopIconTask = createDesktopIconTask;
	}

	public boolean isGenerateSetup() {
		return generateSetup;
	}

	public void setGenerateSetup(boolean generateSetup) {
		this.generateSetup = generateSetup;
	}

	public boolean isGenerateMsi() {
		return generateMsi;
	}

	public void setGenerateMsi(boolean generateMsi) {
		this.generateMsi = generateMsi;
	}

	public boolean isGenerateMsm() {
		return generateMsm;
	}

	public void setGenerateMsm(boolean generateMsm) {
		this.generateMsm = generateMsm;
	}

	public String getMsiUpgradeCode() {
		return msiUpgradeCode;
	}

	public void setMsiUpgradeCode(String msiUpgradeCode) {
		this.msiUpgradeCode = msiUpgradeCode;
	}

	public boolean isWrapJar() {
		return wrapJar;
	}

	public void setWrapJar(boolean wrapJar) {
		this.wrapJar = wrapJar;
	}

	public LinkedHashMap<String, String> getSetupLanguages() {
		return setupLanguages;
	}

	public void setSetupLanguages(LinkedHashMap<String, String> setupLanguages) {
		this.setupLanguages = setupLanguages;
	}

	public SetupMode getSetupMode() {
		return setupMode;
	}

	public void setSetupMode(SetupMode setupMode) {
		this.setupMode = setupMode;
	}

	public WindowsSigning getSigning() {
		return signing;
	}

	public void setSigning(WindowsSigning signing) {
		this.signing = signing;
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public boolean isDisableRunAfterInstall() {
		return disableRunAfterInstall;
	}

	public void setDisableRunAfterInstall(boolean disableRunAfterInstall) {
		this.disableRunAfterInstall = disableRunAfterInstall;
	}

	public void setDisableWelcomePage(boolean disableWelcomePage) {
		this.disableWelcomePage = disableWelcomePage;
	}

	public boolean isDisableWelcomePage() {
		return disableWelcomePage;
	}

	public boolean isRemoveOldLibs() {
		return removeOldLibs;
	}

	public void setRemoveOldLibs(boolean removeOldLibs) {
		this.removeOldLibs = removeOldLibs;
	}

	public WindowsExeCreationTool getExeCreationTool() {
		return exeCreationTool;
	}

	public void setExeCreationTool(WindowsExeCreationTool exeCreationTool) {
		this.exeCreationTool = exeCreationTool;
	}
	
	public String getVmLocation() {
		return vmLocation;
	}
	
	public void setVmLocation(String vmLocation) {
		this.vmLocation = vmLocation;
	}

	@Override
	public String toString() {
		return "WindowsConfig [icoFile=" + icoFile + ", headerType=" + headerType + ", companyName=" + companyName
				+ ", copyright=" + copyright + ", fileDescription=" + fileDescription + ", fileVersion=" + fileVersion
				+ ", internalName=" + internalName + ", language=" + language + ", originalFilename=" + originalFilename
				+ ", productName=" + productName + ", productVersion=" + productVersion + ", trademarks=" + trademarks
				+ ", txtFileVersion=" + txtFileVersion + ", txtProductVersion=" + txtProductVersion
				+ ", shortcutName=" + shortcutName
				+ ", disableDirPage=" + disableDirPage + ", disableProgramGroupPage=" + disableProgramGroupPage
				+ ", disableFinishedPage=" + disableFinishedPage + ", disableRunAfterInstall=" + disableRunAfterInstall
				+ ", disableWelcomePage=" + disableWelcomePage + ", createDesktopIconTask=" + createDesktopIconTask
				+ ", generateSetup=" + generateSetup + ", generateMsi=" + generateMsi + ", generateMsm=" + generateMsm
				+ ", msiUpgradeCode=" + msiUpgradeCode + ", wrapJar=" + wrapJar + ", setupLanguages=" + setupLanguages
				+ ", setupMode=" + setupMode + ", signing=" + signing + ", registry=" + registry + ", removeOldLibs="
				+ removeOldLibs + ", exeCreationTool=" + exeCreationTool + ", vmLocation=" + vmLocation + "]";
	}

	/**
	 * Tests Windows specific config and set defaults if not specified
	 * 
	 * @param packager Packager
	 */
	/**
	 * Converts the app version into a Windows version (x.x.x.x), valid for EXE version info and MSI product versions:
	 * the leading digits of up to four parts, e.g. 2.0.1-SNAPSHOT becomes 2.0.1.0
	 * 
	 * @param version App version
	 * @return Windows version, or 1.0.0.0 if the app version can't be converted
	 */
	static String toWindowsVersion(String version) {
		String[] parts = ("" + version).split("[.-]");
		int[] numbers = new int[4];
		for (int i = 0; i < numbers.length && i < parts.length; i++) {
			String digits = parts[i].replaceAll("^(\\d*).*$", "$1");
			if (digits.isEmpty() || digits.length() > 5) break;
			numbers[i] = Integer.parseInt(digits);
		}
		// MSI product versions: major and minor up to 255, build up to 65535
		if (numbers[0] == 0 || numbers[0] > 255 || numbers[1] > 255 || numbers[2] > 65535 || numbers[3] > 65535) {
			Logger.warn("Version " + version + " can't be used as Windows version: using 1.0.0.0 (set winConfig.productVersion and winConfig.fileVersion)");
			return "1.0.0.0";
		}
		return numbers[0] + "." + numbers[1] + "." + numbers[2] + "." + numbers[3];
	}

	public void setDefaults(Packager packager) {
		this.setHeaderType(defaultIfNull(this.getHeaderType(), HeaderType.gui));
		String windowsVersion = toWindowsVersion(packager.getVersion());
		this.setFileVersion(defaultIfBlank(this.getFileVersion(), windowsVersion));
		this.setTxtFileVersion(defaultIfBlank(this.getTxtFileVersion(), "" + packager.getVersion()));
		this.setProductVersion(defaultIfBlank(this.getProductVersion(), windowsVersion));
		this.setShortcutName(defaultIfBlank(this.getShortcutName(), packager.getDisplayName()));
		this.setTxtProductVersion(defaultIfBlank(this.getTxtProductVersion(), "" + packager.getVersion()));
		this.setCompanyName(defaultIfBlank(this.getCompanyName(), packager.getOrganizationName()));
		this.setTrademarks(defaultIfBlank(this.getTrademarks(), packager.getOrganizationName()));
		this.setCopyright(defaultIfBlank(this.getCopyright(), packager.getOrganizationName()));
		this.setFileDescription(defaultIfBlank(this.getFileDescription(), packager.getDescription()));
		this.setProductName(defaultIfBlank(this.getProductName(), packager.getName()));
		this.setInternalName(defaultIfBlank(this.getInternalName(), packager.getName()));
		this.setOriginalFilename(defaultIfBlank(this.getOriginalFilename(), packager.getName() + ".exe"));
		// a stable upgrade code, so a new MSI of the same app upgrades the installed one
		if (this.getMsiUpgradeCode() == null || this.getMsiUpgradeCode().trim().isEmpty()) {
			String seed = "javapackager:" + packager.getOrganizationName() + ":" + packager.getName();
			this.setMsiUpgradeCode(UUID.nameUUIDFromBytes(seed.getBytes(StandardCharsets.UTF_8)).toString());
			Logger.info("MSI upgrade code: " + this.getMsiUpgradeCode() + " (set winConfig.msiUpgradeCode to keep it if the app name or organization change)");
		}
		// init setup languages
		if (MapUtils.isEmpty(this.getSetupLanguages())) {
			this.getSetupLanguages().put("english", "compiler:Default.isl");
			this.getSetupLanguages().put("spanish", "compiler:Languages\\Spanish.isl");
		}
	}
	
}
