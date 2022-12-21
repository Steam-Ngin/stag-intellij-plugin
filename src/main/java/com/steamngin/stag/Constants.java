package com.steamngin.stag;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
    static final String userName = System.getenv("USER");
    static final String osxScriptPath = "/Users/" + Constants.userName + "/.stag/";
    static final String windowsScriptPath = "C:\\Users\\" + Constants.userName + "\\.stag\\";

    static final String userHome = System.getProperty("user.home");

    static final String stagWorkingDirName = ".stag";

    static final String stagDefaultPattern = "rubicon";
    static final String stagDefaultPatternVersion = "1.0.0-alpha";
    static final String stagDefaultName = "petstag";

    static final String workingDirPath = new File(Constants.userHome, Constants.stagWorkingDirName).toString();
    static final String steamworksPath = new File(Constants.userHome, "steamworks").toString();

    static final String steamCtlCfgPath = new File(Constants.steamworksPath, "steamctlcfg").toString();

    static final String stemPath = new File(Constants.steamworksPath, "stem").toString();

    static Path stagDefaultPath = Paths.get(Constants.stemPath,
            Constants.stagDefaultPattern,
            Constants.stagDefaultPatternVersion);

    static Path stagIntPath = Paths.get(Constants.steamworksPath,
            ".stagint.json");

    static final String stagDefaultPathStr = stagDefaultPath.toString();

}
