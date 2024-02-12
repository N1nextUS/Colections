package dev.arnaldo.mission.loader.impl;

import dev.arnaldo.mission.Main;
import dev.arnaldo.mission.loader.Loader;
import dev.arnaldo.mission.manager.ItemManager;
import dev.arnaldo.mission.model.Item;
import dev.arnaldo.mission.util.item.ItemBuilder;
import dev.arnaldo.mission.util.item.ItemUtil;
import dev.arnaldo.mission.util.text.Text;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ItemLoaderImpl implements Loader {

    @Override
    public void load(@NonNull Main main) {
        ItemManager itemManager = main.getItemManager();
        main.getConfig().getConfigurationSection("items").getValues(false).values()
                .forEach(object -> {
                    ConfigurationSection section = (ConfigurationSection) object;

                    String name = Text.colorize(section.getString("name"));
                    ItemStack baseItem = ItemUtil.getItem(section.getConfigurationSection("item"));

                    baseItem = Objects.requireNonNull(ItemUtil.setUnbreakable(baseItem, true));
                    baseItem = Objects.requireNonNull(ItemUtil.setInfo(baseItem, Item.NBT_KEY, section.getName()));

                    itemManager.validate(new Item(section.getName(), name, new ItemBuilder(baseItem).addItemFlags().toItemStack()));
                });

        main.getLogger().info("Loaded " + itemManager.findAll().size() + " item(s)");
    }

}