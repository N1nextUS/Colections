package dev.arnaldo.mission.model;

import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItem;
import dev.arnaldo.mission.util.StringUtil;
import dev.arnaldo.mission.util.item.ItemBuilder;
import dev.arnaldo.mission.util.item.SkullItem;
import lombok.Data;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
public class UserRanking implements InventoryItem {

    private final int position;
    private final String name;
    private final Instant completedAt;

    @Override
    public @Nullable ItemStack getItem(@NotNull Inventory inventory, @NotNull InventoryProperty property) {
        return new ItemBuilder(SkullItem.getByName(this.name))
                .setName(StringUtil.replaceRanking(this, Config.USER_RANKING_NAME_SETTING.asString())[0])
                .setLore(StringUtil.replaceRanking(this, Config.USER_RANKING_LORE_SETTING.asList().toArray(new String[0])))
                .toItemStack();
    }

}