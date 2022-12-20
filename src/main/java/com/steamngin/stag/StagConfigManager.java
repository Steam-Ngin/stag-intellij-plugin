package com.steamngin.stag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class StagConfigManager {
    public static StagConfig loadStagConfig(String originalPath) throws IOException {
        final boolean isOsx = Objects.equals(System.getProperty("os.name"), "Mac OS X");
        final Path path = Path.of(originalPath +(isOsx ? "/" : "\\")+ "stag_config.json");
        final boolean isCfgExists = Files.exists(path);
        if (!isCfgExists) {
            new BaseDialogWrapper("Error", "STAG Config not found, please re-create project", null, null).showAndGet();
            return null;
        } else {
            final String cfg = Files.readString(path);
            return StagConfig.fromString(cfg);
        }
    }

    public static void saveStagConfig(StagConfig stagConfig) throws IOException {
        final boolean isOsx = Objects.equals(System.getProperty("os.name"), "Mac OS X");
        Path path = Paths.get(stagConfig.stagPath + (isOsx ? "/" : "\\") + "stag_config.json");
        if (!path.toFile().exists()) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }
        Files.write(path, stagConfig.toString().getBytes());
    }
}

