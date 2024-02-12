package dev.arnaldo.mission.command.replacer;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.annotation.dynamic.Annotations;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class FileCommandReplacer {

    private final File dataFolder;
    private final LoadingCache<String, FileConfiguration> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build(new CacheLoader<String, FileConfiguration>() {
                @Override
                public FileConfiguration load(@NotNull String s) {
                    File file = new File(dataFolder, s);
                    return file.exists() ? YamlConfiguration.loadConfiguration(file) : null;
                }
            });

    @SneakyThrows
    public List<Annotation> replace(@Nullable String file, @NotNull String path, Class<? extends Annotation> command) {
        FileConfiguration config = cache.get(StringUtils.isBlank(file) ? "config.yml" : file);
        if (config == null) return null;

        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) return null;

        List<Annotation> annotations = new ArrayList<>();

        if (section.isSet("command")) {
            String[] values = section.getStringList("command").toArray(new String[0]);
            annotations.add(Annotations.create(command, "value", values));
        }

        if (section.isSet("usage")) {
            String usage = section.getString("usage");
            annotations.add(Annotations.create(Usage.class, "value", usage));
        }

        if (section.isSet("description")) {
            String description = section.getString("description");
            annotations.add(Annotations.create(Description.class, "value", description));
        }

        if (section.isSet("permission")) {
            String permission = section.getString("description");
            annotations.add(Annotations.create(CommandPermission.class, "value", permission));
        }

        return annotations;
    }

    public void invalidate() {
        this.cache.invalidateAll();
    }


}