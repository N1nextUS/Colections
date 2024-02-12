package dev.arnaldo.mission.repository.user;

import com.jaoow.sql.executor.adapter.SQLResultAdapter;
import dev.arnaldo.mission.model.User;
import dev.arnaldo.mission.model.UserItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class UserAdapter implements SQLResultAdapter<User> {

    @Nullable
    @Override
    public User adaptResult(@NotNull ResultSet set) throws SQLException {
        User user = new User(set.getString("name"));

        do {
            String item = set.getString("item");
            Instant collectedAt =  set.getTimestamp("collected_at").toInstant();
            user.addItem(new UserItem(item, collectedAt, false));
        } while (set.next());

        return user;
    }

}
