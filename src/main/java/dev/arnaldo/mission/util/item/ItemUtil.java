package dev.arnaldo.mission.util.item;

import com.google.common.base.Preconditions;
import dev.arnaldo.mission.util.ReflectionUtil;
import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class ItemUtil {

    private static Class<?> NBTTagCompoundClass;

    private static Method asNMSCopy;
    private static Method asCraftMirror;
    private static Method setNBTTagCompound;
    private static Method getNBTTagCompound;

    private static Method hasKey;
    private static Method removeKey;
    private static Method getString;
    private static Method setString;
    private static Method getBoolean;
    private static Method setBoolean;
    private static Method save;

    public static ItemStack getItem(@NonNull ConfigurationSection section) {
        ItemStack item;
        if (section.getBoolean("skull.enabled", false)) {
            item = SkullItem.getByUrl(section.getString("skull.url"));
        } else {
            item = new ItemStack(Material.matchMaterial(section.getString("material")));
            item.setDurability((short) section.getInt("data", 0));
        }

        ItemBuilder builder = new ItemBuilder(item);

        String name = section.getString("name");
        if (StringUtils.isNotBlank(name)) {
            builder.setName(ChatColor.translateAlternateColorCodes('&', name));
        }

        List<String> lore = section.getStringList("lore");
        if (!lore.isEmpty()) {
            builder.setLore(lore.stream()
                    .map(value -> ChatColor.translateAlternateColorCodes('&', value))
                    .collect(Collectors.toList()));
        }

        ConfigurationSection colorSection = section.getConfigurationSection("color");
        if (colorSection != null) {
            builder.setLeatherArmorColor(Color.fromRGB(
                    colorSection.getInt("r"),
                    colorSection.getInt("g"),
                    colorSection.getInt("b")));
        }

        for (String enchant : section.getStringList("enchantments")) {
            String[] split = enchant.split(":");
            if (split.length != 2) {
                throw new IllegalArgumentException("Invalid enchantment format: " + enchant);
            }

            Enchantment enchantment = Enchantment.getByName(split[0].toUpperCase());
            int level = Integer.parseInt(split[1]);

            builder.addUnsafeEnchantment(enchantment, level);
        }

        if (!section.getBoolean("flags", true)) {
            builder.addItemFlags();
        }

        return builder.toItemStack();
    }

    public static @Nullable String getJson(@NotNull ItemStack itemStack) {
        Preconditions.checkNotNull(itemStack, "itemStack cannot be null");

        try {
            Object compound = NBTTagCompoundClass.getConstructor().newInstance();
            Object craftItemStack = asNMSCopy.invoke(null, itemStack);
            Object json = save.invoke(craftItemStack, compound);
            return json.toString();

        } catch (Throwable exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static boolean hasInfo(@NotNull ItemStack item, @NotNull String key) {
        Preconditions.checkNotNull(item, "item cannot be null");
        Preconditions.checkNotNull(key, "key cannot be null");

        try {
            Object craftItem = asNMSCopy.invoke(null, item);
            Object tagCompound = getNBTTagCompound.invoke(craftItem);
            if (tagCompound != null) {
                return (boolean) hasKey.invoke(tagCompound, key);
            }

            return false;

        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Nullable
    public static String getInfo(@NotNull ItemStack item, @NotNull String key) {
        Preconditions.checkNotNull(item, "item cannot be null");
        Preconditions.checkNotNull(key, "key cannot be null");

        try {
            Object craftItem = asNMSCopy.invoke(null, item);
            Object tagCompound = getNBTTagCompound.invoke(craftItem);
            if (tagCompound != null) {
                return getString.invoke(tagCompound, key).toString();
            }

            return null;

        } catch (Throwable exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static @Nullable ItemStack setInfo(@NotNull ItemStack item, @NotNull String key, @NotNull String value) {
        Preconditions.checkNotNull(item, "item cannot be null");
        Preconditions.checkNotNull(key, "key cannot be null");
        Preconditions.checkNotNull(value, "value cannot be null");

        try	{
            Object craftItem = asNMSCopy.invoke(null, item);
            Object tagCompound = getNBTTagCompound.invoke(craftItem);
            if (tagCompound == null) {
                tagCompound = NBTTagCompoundClass.getConstructor().newInstance();
            }

            setString.invoke(tagCompound, key, value);
            setNBTTagCompound.invoke(craftItem, tagCompound);

            return (ItemStack) asCraftMirror.invoke(null, craftItem);

        } catch (Throwable exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static @Nullable ItemStack removeInfo(@NotNull ItemStack item, @NotNull String key) {
        Preconditions.checkNotNull(item, "item cannot be null");
        Preconditions.checkNotNull(key, "key cannot be null");

        try	{
            Object craftItem = asNMSCopy.invoke(null, item);
            Object tagCompound = getNBTTagCompound.invoke(craftItem);
            if (tagCompound != null) {
                removeKey.invoke(tagCompound, key);
                setNBTTagCompound.invoke(craftItem, tagCompound);
            }

            return (ItemStack) asCraftMirror.invoke(null, craftItem);

        } catch (Throwable exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static boolean isUnbreakable(@NotNull ItemStack item) {
        Preconditions.checkNotNull(item, "item cannot be null");

        try {
            Object craftItem = asNMSCopy.invoke(null, item);
            Object tagCompound = getNBTTagCompound.invoke(craftItem);
            return tagCompound != null && (boolean) getBoolean.invoke(tagCompound, "Unbreakable");

        } catch (Throwable exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static @Nullable ItemStack setUnbreakable(@NotNull ItemStack item, boolean breakable) {
        Preconditions.checkNotNull(item, "item cannot be null");

        try	{
            Object craftItem = asNMSCopy.invoke(null, item);
            Object tagCompound = getNBTTagCompound.invoke(craftItem);
            if (tagCompound == null) {
                tagCompound = NBTTagCompoundClass.getConstructor().newInstance();
            }

            setBoolean.invoke(tagCompound, "Unbreakable", breakable);
            setNBTTagCompound.invoke(craftItem, tagCompound);

            return (ItemStack) asCraftMirror.invoke(null, craftItem);

        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    static {
        try {
            // Item Classes
            Class<?> itemStackClass = ReflectionUtil.getNmsClass("ItemStack");
            Class<?> craftItemStackClass = ReflectionUtil.getCraftClass("inventory.CraftItemStack");

            // NBTTag Classes
            NBTTagCompoundClass = ReflectionUtil.getNmsClass("NBTTagCompound");
            Class<?> NBTBaseClass = ReflectionUtil.getNmsClass("NBTBase");

            // Item Handle Methods
            asNMSCopy = craftItemStackClass.getDeclaredMethod("asNMSCopy", ItemStack.class);
            asCraftMirror = craftItemStackClass.getDeclaredMethod("asCraftMirror", itemStackClass);

            // Item NBTTag Methods
            getNBTTagCompound = itemStackClass.getDeclaredMethod("getTag");
            setNBTTagCompound = itemStackClass.getDeclaredMethod("setTag", NBTTagCompoundClass);

            // Basic NBTTag Handle Methods
            hasKey = NBTTagCompoundClass.getDeclaredMethod("hasKey", String.class);
            removeKey = NBTTagCompoundClass.getDeclaredMethod("remove", String.class);
            getString = NBTTagCompoundClass.getDeclaredMethod("getString", String.class);
            setString = NBTTagCompoundClass.getDeclaredMethod("setString", String.class, String.class);
            getBoolean = NBTTagCompoundClass.getDeclaredMethod("getBoolean", String.class);
            setBoolean = NBTTagCompoundClass.getDeclaredMethod("setBoolean", String.class, boolean.class);

            save = itemStackClass.getMethod("save", NBTTagCompoundClass);

            // Item NBTTagCompound Field
            Field map = NBTTagCompoundClass.getDeclaredField("map");
            map.setAccessible(true);

            Method createTag = NBTBaseClass.getDeclaredMethod("createTag", byte.class);
            createTag.setAccessible(true);

        } catch (Throwable exception) {
            exception.printStackTrace();
        }
    }

}