import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseService {
    // This will create a file named 'finence.db' in your project folder
    private static final String URL = "jdbc:sqlite:finence.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type TEXT NOT NULL," +       // 'INCOME' or 'EXPENSE'
                "category TEXT NOT NULL," +   // e.g., 'Food', 'Salary'
                "amount REAL NOT NULL," +
                "date TEXT," +                // Stored as YYYY-MM-DD
                "note TEXT" +
                ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database connected and checked successfully.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}