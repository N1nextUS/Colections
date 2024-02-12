package dev.arnaldo.mission;

import br.com.blecaute.inventory.InventoryHelper;
import com.jaoow.sql.connector.type.impl.MySQLDatabaseType;
import com.jaoow.sql.executor.SQLExecutor;
import dev.arnaldo.mission.manager.ItemManager;
import dev.arnaldo.mission.manager.UserManager;
import dev.arnaldo.mission.repository.user.UserRepository;
import dev.arnaldo.mission.repository.user.impl.UserRepositoryImpl;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Level;

@Getter
public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    private SQLExecutor executor;
    private UserRepository userRepository;

    private UserManager userManager;
    private ItemManager itemManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        try {
            InventoryHelper.enable(this);

            registerDatabase();
            registerRepositories();
            registerManagers();
            registerLoaders();
            registerListeners();
            registerInventories();
            registerCommands();
            registerTasks();
            registerChunks();

        } catch (SQLException exception) {
            getLogger().log(Level.SEVERE, "Failed to connect to database!", exception);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (this.userManager != null) {
            this.userManager.save();
        }
    }

    private void registerDatabase() throws SQLException {
        ConfigurationSection section = getConfig().getConfigurationSection("MySQL");
        MySQLDatabaseType type = MySQLDatabaseType.builder()
                .address(section.getString("host"))
                .password(section.getString("password"))
                .username(section.getString("username"))
                .database(section.getString("database"))
                .build();

        this.executor = new SQLExecutor(type.connect());
    }

    private void registerRepositories() {
        this.userRepository = new UserRepositoryImpl(this.executor);
        this.userRepository.createTable();
    }

    private void registerManagers() {
        this.userManager = new UserManager(this.userRepository);
        this.itemManager = new ItemManager();
    }


}