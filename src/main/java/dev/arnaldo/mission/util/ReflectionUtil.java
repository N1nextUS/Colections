package dev.arnaldo.mission.util;

import com.google.common.reflect.ClassPath;
import dev.arnaldo.mission.Main;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
@SuppressWarnings("UnstableApiUsage")
public class ReflectionUtil {

	private final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

	public @NotNull Class<?> getNmsClass(String name) throws ClassNotFoundException {
		return Class.forName("net.minecraft.server." + version + "." + name);
	}

	public @NotNull Class<?> getCraftClass(String name) throws ClassNotFoundException {
		return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
	}

	@SneakyThrows
	@SuppressWarnings("unchecked cast")
	public <T> List<Class<T>> getClasses(@NotNull Class<T> value, String packageName) {
		ClassLoader loader = Main.getInstance().getClass().getClassLoader();
		return ClassPath.from(loader).getTopLevelClasses().stream()
				.filter(classInfo -> classInfo.getName().toLowerCase().startsWith(packageName))
				.map(ReflectionUtil::load)
				.filter(Objects::nonNull)
				.filter(value::isAssignableFrom)
				.map(clazz -> (Class<T>) clazz)
				.collect(Collectors.toList());
	}

	private @Nullable Class<?> load(ClassPath.ClassInfo info) {
		try {
			return info.load();
		} catch (Throwable t) {
			return null;
		}
	}


	@Nullable
	public <T> T instance(@NonNull Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}


}