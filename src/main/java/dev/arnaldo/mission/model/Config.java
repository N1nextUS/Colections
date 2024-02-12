package dev.arnaldo.mission.model;

import lombok.*;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Config {

    PLAYER_NOT_FOUND_MESSAGE("messages.player-not-found"),

    INSERT_VALID_ITEM_MESSAGE("messages.insert-valid-item"),

    ITEM_ALREADY_COLLECTED_MESSAGE("messages.item-already-collected"),
    ITEM_COLLECTED_MESSAGE(""),
    ITEM_SENT_MESSAGE("messages.item-sent"),
    ITEMS_REDEFINED_MESSAGE("messages.items-redefined"),

    QUEST_COMPLETED_MESSAGE(""),

    QUEST_AMOUNT_SETTING(""),

    ITEM_COLLECTED_SETTING(""),
    ITEM_COLLECT_SETTING(""),

    USER_RANKING_NAME_SETTING(""),
    USER_RANKING_LORE_SETTING(""),

    ;

    private final String path;

    @Setter
    private Object object;

    public String asString() {
        return object.toString();
    }

    public int asInt() {
        return object instanceof Integer ? (int) object : (int) (object = Integer.parseInt(object.toString()));
    }

    public double asDouble() {
        return object instanceof Double ? (double) object : (double) (object = Double.parseDouble(object.toString()));
    }

    @SuppressWarnings("unchecked")
    public List<String> asList() {
        return (List<String>) object;
    }

}
