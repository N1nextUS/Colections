package dev.arnaldo.mission.manager;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import dev.arnaldo.mission.model.Config;
import dev.arnaldo.mission.model.User;
import dev.arnaldo.mission.model.UserItem;
import dev.arnaldo.mission.model.UserRanking;
import dev.arnaldo.mission.repository.user.UserRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserManager {

    @Getter(AccessLevel.PRIVATE)
    private final UserRepository userRepository;
    private final AsyncLoadingCache<String, User> cache = Caffeine.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .removalListener((String key, User user, RemovalCause cause) -> {
                if (user != null && cause.wasEvicted()) getUserRepository().save(user);
            }).buildAsync((key, executor) -> CompletableFuture.supplyAsync(() ->
                    getUserRepository().findById(key).orElse(null), executor));

    private Set<UserRanking> userRankings = new HashSet<>();

    public void validate(@NonNull User user) {
        this.cache.synchronous().put(user.getName().toLowerCase(), user);
    }

    public void invalidate(@NonNull String name) {
        this.cache.synchronous().invalidate(name.toLowerCase());
    }

    public CompletableFuture<User> find(@NonNull String name) {
        return cache.get(name.toLowerCase());
    }

    public CompletableFuture<User> find(@NonNull Player player) {
        return find(player.getName()).handle((user, exception) -> {
            if (exception != null) throw new CompletionException(exception);
            return user != null ? user : create(player.getName()).join();
        });
    }

    public Optional<User> findIfPresent(@NonNull String name) {
        return Optional.ofNullable(cache.synchronous().getIfPresent(name.toLowerCase()));
    }

    public CompletableFuture<User> create(@NonNull String name) {
        return cache.get(name).handleAsync((user, exception) -> {
            if (exception != null || user != null) throw new CompletionException(exception);

            user = new User(name);
            userRepository.save(user);
            validate(user);

            return user;
        });
    }

    public void delete(@NonNull String name) {
        this.userRepository.deleteById(name);
    }

    @Unmodifiable
    public Set<UserRanking> findRankings() {
        return Collections.unmodifiableSet(userRankings);
    }

    public void updateRanking() {
        this.userRankings = this.userRepository.findBySize(Config.QUEST_AMOUNT_SETTING.asInt());
    }

    public void save() {
        List<User> users = cache.synchronous().asMap().values().stream().filter(User::isDirty).collect(Collectors.toList());
        Map<String, List<UserItem>> map =  users.stream()
                .collect(Collectors.toMap(User::getName, user -> user.getItems().stream()
                        .filter(UserItem::isDirty)
                        .collect(Collectors.toList())));

        this.userRepository.save(map);
        users.forEach(user -> user.setDirty(false));
    }

}
