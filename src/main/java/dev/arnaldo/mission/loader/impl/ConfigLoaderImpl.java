package dev.arnaldo.mission.loader.impl;

import dev.arnaldo.mission.Main;
import dev.arnaldo.mission.loader.Loader;
import dev.arnaldo.mission.model.Config;
import dev.arnaldo.mission.util.item.ItemUtil;
import dev.arnaldo.mission.util.text.Text;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigLoaderImpl implements Loader {

    @Override @SuppressWarnings("unchecked")
    public void load(@NotNull Main main) {
        FileConfiguration config = main.getConfig();
        for (Config value : Config.values()) {
            Object object = config.get(value.getPath());

            if (value.name().endsWith("_ITEM")) {
                value.setObject(ItemUtil.getItem((ConfigurationSection) object));
                continue;
            }

            if (object instanceof String) {
                value.setObject(Text.colorize(object.toString()));
                continue;
            }

            if (object instanceof List) {
              	List<Object> objects = (List<Object>) object;
                if (!value.name().contains("_MESSAGE")) {
                    value.setObject(objects.stream()
                            .map(text -> Text.colorize( text.toString()))
                            .collect(Collectors.toList()));
                    continue;
                }

                StringBuilder builder = new StringBuilder();
                for (Iterator<Object> iterator = objects.iterator(); iterator.hasNext();) {
                    Object next = iterator.next();
                    if (next != null) {
                        builder.append(next);
                    }

                    if (iterator.hasNext()) {
                        builder.append(System.lineSeparator());
                    }
                }

                value.setObject(Text.colorize(builder.toString()));
                continue;
            }

            value.setObject(object);
        }
    }

}