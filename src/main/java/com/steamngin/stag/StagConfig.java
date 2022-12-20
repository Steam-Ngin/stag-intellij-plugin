package com.steamngin.stag;

import org.jetbrains.annotations.Nullable;

public class StagConfig {
    public String stagName;
    public String stagPattern;
    public String stagVersion;
    public String stagPath;
    public boolean overrideCfg;


    public StagConfig(String stagName, String stagPattern, String stagVersion, String stagPath, boolean overrideCfg) {
        this.stagName = stagName;
        this.stagPattern = stagPattern;
        this.stagVersion = stagVersion;
        this.stagPath = stagPath;
        this.overrideCfg = overrideCfg;
    }

    @Override
    public String toString() {
        return "StagConfig{" +
                "stagName='" + stagName + '\'' +
                ", stagPattern='" + stagPattern + '\'' +
                ", stagVersion='" + stagVersion + '\'' +
                ", stagPath='" + stagPath + '\'' +
                ", overrideCfg=" + overrideCfg +
                '}';
    }

    @Nullable
    public static StagConfig fromString(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        String[] split = str.split(",");
        if (split.length != 5) {
            return null;
        }
        String stagName = split[0].split("=")[1];
        String stagPattern = split[1].split("=")[1];
        String stagVersion = split[2].split("=")[1];
        String stagPath = split[3].split("=")[1];
        boolean overrideCfg = Boolean.parseBoolean(split[4].split("=")[1]);
        return new StagConfig(stagName, stagPattern, stagVersion, stagPath, overrideCfg);
    }
}
