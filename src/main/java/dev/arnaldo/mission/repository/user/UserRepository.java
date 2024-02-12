package dev.arnaldo.mission.repository.user;

import dev.arnaldo.mission.model.User;
import dev.arnaldo.mission.model.UserItem;
import dev.arnaldo.mission.model.UserRanking;
import dev.arnaldo.mission.repository.Repository;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserRepository extends Repository<String, User> {

    void save(@NotNull String name, @NotNull List<UserItem> items);

    void save(@NotNull Map<String, List<UserItem>> users);

    Set<UserRanking> findBySize(int size);

    void deleteNotIn(@NotNull String name, @NotNull List<UserItem> items);

    void deleteNotIn(@NotNull Map<String, List<UserItem>> users);
}