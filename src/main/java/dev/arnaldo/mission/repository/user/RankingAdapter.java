package dev.arnaldo.mission.repository.user;

import com.jaoow.sql.executor.adapter.SQLResultAdapter;
import dev.arnaldo.mission.model.UserRanking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class RankingAdapter implements SQLResultAdapter<UserRanking> {

    @Nullable
    @Override
    public UserRanking adaptResult(@NotNull ResultSet set) throws SQLException {
        String name = set.getString("name");
        int position = set.getInt("position");
        Instant completedAt = set.getTimestamp("completed_at").toInstant();

        return new UserRanking(position, name, completedAt);
    }

}
