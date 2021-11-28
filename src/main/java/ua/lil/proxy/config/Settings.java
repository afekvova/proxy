package ua.lil.proxy.config;

import java.io.File;

public class Settings extends Config {

    @Ignore
    public static final Settings IMP = new Settings();

    public int PORT = 5000;

    public void reload(File file) {
        load(file);
        save(file);
    }
}
