package dev.arnaldo.mission.command.reader;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.locales.LocaleReader;

import java.io.File;
import java.util.*;

public class YamlLocaleReader implements LocaleReader {

    private final File file;
    private final Locale locale;

    private final Map<String, String> messages = new HashMap<>();

    public YamlLocaleReader(@NotNull File file, @NotNull Locale locale) {
        this.file = file;
        this.locale = locale;

        loadMessages();
    }

    @Override
    public boolean containsKey(String s) {
        return messages.containsKey(s);
    }

    @Override
    public String get(String s) {
        return messages.get(s);
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @SuppressWarnings("unchecked")
    public void loadMessages() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("messages");

        for (String key : section.getKeys(false)) {
            Object object = section.get(key);
            String message = object.toString();

            if (object instanceof List) {
                message = coloredList((List<Object>) object);
            }

            messages.put(key, message);
        }
    }

    @NotNull
    private static String coloredList(@NotNull List<Object> object) {
        StringBuilder builder = new StringBuilder();

        Iterator<Object> iterator = object.iterator();
        while (iterator.hasNext()) {
            Object value = iterator.next();
            if (value != null) {
                builder.append(value);
            }

            if (iterator.hasNext()) {
                builder.append(System.lineSeparator());
            }
        }

        return builder.toString();
    }

}