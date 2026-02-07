import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class TransactionManager {
    public void addTransaction(String type, String category, double amount, String date, String note) {
        String sql = "INSERT INTO transactions(type, category, amount, date, note) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseService.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            pstmt.setString(2, category);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, date);
            pstmt.setString(5, note);
            pstmt.executeUpdate();
            System.out.println("Transaction added successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (Connection conn = DatabaseService.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Transaction(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        rs.getString("date"),
                        rs.getString("note")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
    public void printFinancialSummary() {
        String sql = "SELECT type, SUM(amount) as total FROM transactions GROUP BY type";
        double totalIncome = 0;
        double totalExpense = 0;
        try (Connection conn = DatabaseService.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                if (rs.getString("type").equalsIgnoreCase("INCOME")) {
                    totalIncome = rs.getDouble("total");
                } else {
                    totalExpense = rs.getDouble("total");
                }
            }
            System.out.println("--- FINANCIAL SUMMARY ---");
            System.out.println("Total Income:   $" + totalIncome);
            System.out.println("Total Expenses: $" + totalExpense);
            System.out.println("Current Balance: $" + (totalIncome - totalExpense));
            System.out.println("-------------------------");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void deleteAllTransactions() {
        String sql = "DELETE FROM transactions";
        String resetSql = "DELETE FROM sqlite_sequence WHERE name='transactions'";
        try (Connection conn = DatabaseService.connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            stmt.executeUpdate(resetSql);
            System.out.println("All data has been wiped successfully.");
        } catch (SQLException e) {
            System.out.println("Error while clearing data: " + e.getMessage());
        }
    }
}