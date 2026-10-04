package io.github.javapackager.utils;

import io.github.javapackager.model.Platform;

/**
 * Icon utils
 */
public class IconUtils {

	public static String getIconFileExtensionByPlatform(Platform platform) {
		switch (platform) {
		case linux: 	return ".png";
		case mac: 		return ".icns";
		case windows: 	return ".ico";
		default:		return null;
		}
	}
	
}
