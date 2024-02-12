package dev.arnaldo.mission.inventory;

import dev.arnaldo.mission.inventory.impl.CollectInventoryImpl;
import dev.arnaldo.mission.inventory.impl.RankingInventoryImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InventoryType {

    COLLECT(new CollectInventoryImpl()),
    RANKING(new RankingInventoryImpl());

    private final Inventory inventory;

}