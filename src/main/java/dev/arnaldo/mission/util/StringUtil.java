package dev.arnaldo.mission.util;

import dev.arnaldo.mission.model.Item;
import dev.arnaldo.mission.model.UserItem;
import dev.arnaldo.mission.model.UserRanking;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class StringUtil {

    private final String[] itemPlaceholders = {"{item_id}", "{item_name}"};
    private final String[] userItemPlaceholders = {"{item_id}", "{item_name}", "{item_collectedAt}"};
    private final String[] rankingPlaceholders = {"{ranking_pos}", "{ranking_name}", "{ranking_completedAt}"};

    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @NotNull
    @Contract("_, _, -> param2")
    public String[] replaceItem(@NonNull Item item, String @NonNull... text) {
        String[] values = {item.getId(), item.getName()};
        return parse(text, values, itemPlaceholders);
    }

    @NotNull
    @Contract("_, _, _, -> param3")
    public String[] replaceUserItem(@NonNull Item item, @NonNull UserItem userItem, String @NonNull... text) {
        String[] values = {item.getId(), item.getName(), dateFormat.format(Date.from(userItem.getCollectedAt()))};
        return parse(text, values, userItemPlaceholders);
    }

    @NotNull
    @Contract("_, _, -> param2")
    public String[] replaceRanking(@NotNull UserRanking ranking, String @NotNull... text) {
        String[] values = {String.valueOf(ranking.getPosition()), ranking.getName(), dateFormat.format(Date.from(ranking.getCompletedAt()))};
        return parse(text, values, rankingPlaceholders);
    }

    @NotNull
    @Contract("_, _, _ -> param1")
    private String[] parse(String @NonNull [] text, String @NonNull [] values, String @NonNull [] placeholders) {
        for (int i = 0; i < text.length; i++) {
            text[i] = StringUtils.replaceEach(text[i], placeholders, values);
        }

        return text;
    }

}
