package web.api.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.mindrot.jbcrypt.BCrypt;

import web.api.database.models.Point;
import web.api.database.models.User;

public class PostgreSQLJDBC {
    private static final String CONFIG_FILE_PATH = "db.properties";

    private static String URL;
    private static String USER;
    private static String PASSWORD;
    
    static {
        try (InputStream input = PostgreSQLJDBC.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH)) {
            Properties prop = new Properties();
            prop.load(input);
            URL = prop.getProperty("URL");
            USER = prop.getProperty("USERNAME");
            PASSWORD = prop.getProperty("PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load database properties.");
        }

        try (Connection connection = connect()) {
            createUsersTable(connection);
            createPointsTable(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not create tables.");
        }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void createUsersTable(Connection connection) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                                    + "ID SERIAL PRIMARY KEY, "
                                    + "username VARCHAR(16) NOT NULL UNIQUE, "
                                    + "hashed_password VARCHAR(256) NOT NULL"
                                + ");";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createPointsTable(Connection connection) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS points ("
                                    + "ID SERIAL PRIMARY KEY, "
                                    + "owner_id INTEGER NOT NULL, "
                                    + "x REAL NOT NULL, "
                                    + "y REAL NOT NULL,"
                                    + "r REAL NOT NULL, "
                                    + "result BOOLEAN NOT NULL"
                                + ");";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean usernameExists(Connection connection, String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Integer getUserId(Connection connection, String username) {
        String query = "SELECT ID FROM users WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static boolean checkPassword(Connection connection, User user) {
        String query = "SELECT hashed_password FROM users WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                String storedHashedPassword = resultSet.getString("hashed_password");
                return BCrypt.checkpw(user.getPassword(), storedHashedPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean registerUser(Connection connection, String username, String password) {
        if (usernameExists(connection, username)) return false;
    
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    
        String insertSQL = "INSERT INTO users (username, hashed_password) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void insertPoint(Connection connection, Point point) {
        String insertSQL = "INSERT INTO points (x, y, r, result, owner_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setDouble(1, point.getX());
            preparedStatement.setDouble(2, point.getY());
            preparedStatement.setDouble(3, point.getR());
            preparedStatement.setBoolean(4, point.getResult());
            preparedStatement.setLong(5, point.getOwnerId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Point> getLastPoints(Connection connection, String username) {
        ArrayList<Point> points = new ArrayList<Point>();
        String query = "SELECT p.x, p.y, p.r, p.result FROM points p "
                    + "JOIN users u ON p.owner_id = u.id "
                    + "WHERE u.username = ? "
                    + "ORDER BY p.id DESC LIMIT 11";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double r = resultSet.getDouble("r");
                boolean result = resultSet.getBoolean("result");

                Point point = new Point(x, y, r, result);
                points.add(point);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return points;
    }
}