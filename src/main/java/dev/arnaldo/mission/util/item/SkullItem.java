package dev.arnaldo.mission.util.item;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.arnaldo.mission.util.ReflectionUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkullItem {

	private static final Map<String, ItemStack> cache = new HashMap<>();
	private static final ItemStack base = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

	private static Field profileField;

	@NotNull
	public static ItemStack getByUrl(@NotNull String url) {
		Preconditions.checkNotNull(url, "url cannot be null");

		ItemStack cachedItem = cache.get(url);
		if (cachedItem != null) {
			return cachedItem.clone();
		}

		ItemStack skull = base.clone();

		try {
			SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
			GameProfile profile = new GameProfile(UUID.randomUUID(), null);

			byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
			profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

			profileField.set(skullMeta, profile);
			skull.setItemMeta(skullMeta);

			cache.put(url, skull.clone());

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return skull;
	}

	@NotNull
	public static ItemStack getByName(@NotNull String name) {
		Preconditions.checkNotNull(name, "name cannot be null");
		ItemStack cachedItem = cache.get(name);
		if (cachedItem != null) {
			return cachedItem.clone();
		}

		ItemStack skull = base.clone();
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(name);
		skull.setItemMeta(meta);

		cache.put(name, skull.clone());

		return skull;
	}

	static {
		try {
			Class<?> skullMeta = ReflectionUtil.getCraftClass("inventory.CraftMetaSkull");
			profileField = skullMeta.getDeclaredField("profile");
			profileField.setAccessible(true);

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}