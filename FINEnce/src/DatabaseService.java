import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class DatabaseService {
    private static final String URL = "jdbc:sqlite:finence.db";
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }
    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type TEXT NOT NULL," +
                "category TEXT NOT NULL," +
                "amount REAL NOT NULL," +
                "date TEXT," +
                "note TEXT" +
                ");";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}