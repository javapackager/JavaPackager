package org.example;

import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class App {
    public static void main(String[] args) throws IOException {
        String targetDir;
        // Use hardcoded directories to make the test script simple. This may not run correctly on all systems, as long
        // as it runs correctly on the github actions runners it is good enough
        if(SystemUtils.IS_OS_WINDOWS) {
            targetDir = "C:\\Windows\\Temp";
        } else if(SystemUtils.IS_OS_LINUX) {
            targetDir = "/tmp";
        } else if(SystemUtils.IS_OS_MAC) {
            targetDir = "/tmp";
        } else {
            throw new RuntimeException("Unsupported OS");
        }
        Path targetFile = Path.of(targetDir, "javapackager-testfile.txt");
        Files.write(targetFile, Long.toString(System.currentTimeMillis()).getBytes());
    }
}
