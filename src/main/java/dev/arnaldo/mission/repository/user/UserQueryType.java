package dev.arnaldo.mission.repository.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserQueryType {

    CREATE_TABLE("CREATE TABLE IF NOT EXISTS `sunrise_collections` (" +
            "`name` VARCHAR(16) NOT NULL, " +
            "`item` VARCHAR(255) NOT NULL, " +
            "`collected_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(), " +
            "PRIMARY KEY (`name`, `item`));"),

    INSERT("INSERT INTO `sunrise_collections` (`name`, `item`, `collected_at`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `collected_at` = VALUES(`collected_at`);"),
    SELECT("SELECT * FROM `sunrise_collections` WHERE `name` = ?"),
    DELETE("DELETE FROM `sunrise_collections` WHERE `name` = ?"),
    DELETE_NOT_IN("DELETE FROM `sunrise_collections` WHERE `name` = ? AND `item` NOT IN (?)"),
    SELECT_BY_SIZE("SELECT `name`, MAX(`collected_at`) `completed_at`, ROW_NUMBER() over (ORDER BY `completed_at`, `name`) `position` " +
            "FROM `sunrise_collections` " +
            "GROUP BY `name` " +
            "HAVING COUNT(*) >= ? " +
            "ORDER BY `position`"),
    ;

    private final String query;
}
