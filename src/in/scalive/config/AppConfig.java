package in.scalive.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class AppConfig {
    private static final Properties props = new Properties();
    private static boolean loaded = false;

    private static void load() {
        if (loaded) return;
        // defaults
        props.setProperty("db.url", "jdbc:h2:file:./data/empmgmt;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1");
        props.setProperty("db.user", "sa");
        props.setProperty("db.password", "");
        // optional override from ./config.properties
        Path p = Paths.get("config.properties");
        if (Files.exists(p)) {
            try (FileInputStream fis = new FileInputStream(p.toFile())) {
                props.load(fis);
            } catch (IOException ignored) {
            }
        }
        loaded = true;
    }

    public static String get(String key) {
        load();
        return props.getProperty(key);
    }
}
