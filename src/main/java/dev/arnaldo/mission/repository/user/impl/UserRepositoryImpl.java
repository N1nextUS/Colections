package dev.arnaldo.mission.repository.user.impl;

import com.jaoow.sql.executor.SQLExecutor;
import com.jaoow.sql.executor.batch.BatchBuilder;
import com.jaoow.sql.executor.function.StatementConsumer;
import dev.arnaldo.mission.model.User;
import dev.arnaldo.mission.model.UserItem;
import dev.arnaldo.mission.model.UserRanking;
import dev.arnaldo.mission.repository.user.RankingAdapter;
import dev.arnaldo.mission.repository.user.UserAdapter;
import dev.arnaldo.mission.repository.user.UserQueryType;
import dev.arnaldo.mission.repository.user.UserRepository;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepository {

    private final SQLExecutor executor;

    public UserRepositoryImpl(@NotNull SQLExecutor executor) {
        this.executor = executor;
        this.executor.registerAdapter(User.class, new UserAdapter());
        this.executor.registerAdapter(UserRanking.class, new RankingAdapter());
    }

    @Override
    public void createTable() {
        this.executor.execute(UserQueryType.CREATE_TABLE.getQuery());
    }

    @Override
    public Optional<User> findById(@NotNull String s) {
        return this.executor.query(UserQueryType.SELECT.getQuery(),
                statement -> statement.setString(1, s),
                User.class);
    }

    @NotNull
    @Override
    public Set<User> findAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void save(@NotNull User user) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void save(@NotNull String name, @NotNull List<UserItem> items) {
        BatchBuilder builder = this.executor.batch(UserQueryType.INSERT.getQuery());
        items.forEach(item -> builder.batch(statement -> {
            statement.setString(1, name);
            statement.setString(2, item.getId());
            statement.setTimestamp(3, Timestamp.from(item.getCollectedAt()));
        }));

        builder.execute();
    }

    @Override
    public void save(@NotNull Map<String, List<UserItem>> users) {
        if (users.isEmpty()) return;

        BatchBuilder builder = this.executor.batch(UserQueryType.INSERT.getQuery());
        users.forEach((user, items) -> items.forEach(item -> builder.batch(statement -> {
            statement.setString(1, user);
            statement.setString(2, item.getId());
            statement.setTimestamp(3, Timestamp.from(item.getCollectedAt()));
        })));

        builder.execute();
    }

    @Override
    public void save(@NotNull List<User> v) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void delete(@NotNull User user) {
        deleteById(user.getName());
    }

    @Override
    public void deleteById(@NotNull String s) {
        this.executor.execute(UserQueryType.DELETE.getQuery(), (StatementConsumer) statement -> statement.setString(1, s));
    }

    @Override
    public Set<UserRanking> findBySize(int size) {
        return this.executor.queryMany(UserQueryType.SELECT_BY_SIZE.getQuery(),
                statement -> statement.setInt(1, size),
                UserRanking.class);
    }

    @Override
    public void deleteNotIn(@NotNull String name, @NotNull List<UserItem> items) {
        String ids = items.stream().map(UserItem::getId).collect(Collectors.joining("','", "'", "'"));
        this.executor.execute(UserQueryType.DELETE_NOT_IN.getQuery(), (StatementConsumer) statement -> {
            statement.setString(1, name);
            statement.setString(2, ids);
        });
    }

    @Override
    public void deleteNotIn(@NotNull Map<String, List<UserItem>> users) {
        BatchBuilder builder = this.executor.batch(UserQueryType.DELETE_NOT_IN.getQuery());
        users.forEach((user, items) -> {
            if (items.isEmpty()) return;

            String ids = items.stream().map(UserItem::getId).collect(Collectors.joining("','", "'", "'"));
            builder.batch(statement -> {
                statement.setString(1, user);
                statement.setString(2, ids);
            });
        });

        builder.execute();
    }

}