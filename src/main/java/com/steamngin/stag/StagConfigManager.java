package com.steamngin.stag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class StagConfigManager {
    public static StagConfig loadStagConfig() throws IOException {
//        final boolean isOsx = Objects.equals(System.getProperty("os.name"), "Mac OS X");
//        final Path path = Path.of(originalPath +(isOsx ? "/" : "\\")+ "stag_config.json");

        final boolean isCfgExists = Files.exists(Constants.stagIntPath);
        if (!isCfgExists) {
            new BaseDialogWrapper("Error", "STAG Config not found, please re-create project", null, null).showAndGet();
            return null;
        } else {
            final String cfg = Files.readString(Constants.stagIntPath);
            return StagConfig.fromString(cfg);
        }
    }

    public static void saveStagConfig(StagConfig stagConfig) throws IOException {
//        final boolean isOsx = Objects.equals(System.getProperty("os.name"), "Mac OS X");
//        Path path = Paths.get(stagConfig.stagPath + (isOsx ? "/" : "\\") + "stag_config.json");

        if (!Constants.stagIntPath.toFile().exists()) {
            Files.createDirectories(Constants.stagIntPath.getParent());
            Files.createFile(Constants.stagIntPath);
        }

        Files.write(Constants.stagIntPath, stagConfig.toString().getBytes());
    }
}

