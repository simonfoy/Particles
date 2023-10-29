package me.simonfoy.bettercosmetics.manager;

import me.simonfoy.bettercosmetics.instance.particles.ParticleType;
import org.bukkit.Material;

import java.sql.*;
import java.util.Collections;
import java.util.UUID;

public class DatabaseManager {

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private Connection connection;

    public DatabaseManager(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public boolean establishConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return true;
            }

            synchronized (this) {
                if (connection != null && !connection.isClosed()) {
                    return true;
                }
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":"
                        + this.port + "/" + this.database, this.username, this.password);

                createTableIfNotExists();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS player_particles (" +
                "player_uuid CHAR(36) PRIMARY KEY," +
                "particle_name VARCHAR(255)," +
                "visibility BOOLEAN DEFAULT TRUE" +
                ")";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPlayerParticle(UUID playerUUID) {
        if (!establishConnection()) return null;

        String particleName = null;

        String query = "SELECT particle_name FROM player_particles WHERE player_uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    particleName = resultSet.getString("particle_name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return particleName;
    }

    public void setPlayerParticle(UUID playerUUID, ParticleType particleType) {
        String particleName = (particleType != null) ? particleType.getName() : "AIR";

        if (!establishConnection()) return;

        String query = "INSERT INTO player_particles(player_uuid, particle_name) VALUES(?, ?) ON DUPLICATE KEY UPDATE particle_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID.toString());
            statement.setString(2, particleName);
            statement.setString(3, particleName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public boolean isParticleVisible(UUID playerUUID) {
        if (!establishConnection()) return true;

        boolean visible = true;

        String query = "SELECT visibility FROM player_particles WHERE player_uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    visible = resultSet.getBoolean("visibility");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return visible;
    }

    public void setParticleVisibility(UUID playerUUID, boolean visibility) {
        if (!establishConnection()) return;

        String query = "INSERT INTO player_particles(player_uuid, visibility) VALUES(?, ?) ON DUPLICATE KEY UPDATE visibility = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID.toString());
            statement.setBoolean(2, visibility);
            statement.setBoolean(3, visibility);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
}
