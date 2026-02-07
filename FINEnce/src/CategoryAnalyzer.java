import java.sql.*;
public class CategoryAnalyzer {
    public void printSpendingChart() {
        System.out.println("\n--- FINANCIAL STATISTICS ---");
        // Добавил UPPER(), чтобы поиск не зависел от регистра букв
        String categorySql = "SELECT category, SUM(amount) as total " +
                "FROM transactions " +
                "WHERE UPPER(type) = 'EXPENSE' " +
                "GROUP BY category " +
                "ORDER BY total DESC";
        String maxSingleSql = "SELECT category, amount, date " +
                "FROM transactions " +
                "WHERE UPPER(type) = 'EXPENSE' " +
                "ORDER BY amount DESC LIMIT 1";
        try (Connection conn = DatabaseService.connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs1 = stmt.executeQuery(categorySql);
            System.out.println("Spending analysis (by category):");
            System.out.println("---------------------------------------");
            boolean hasData = false;
            String topCategory = "";
            double topAmount = 0;
            while (rs1.next()) {
                if (!hasData) {
                    topCategory = rs1.getString("category");
                    topAmount = rs1.getDouble("total");
                }
                hasData = true;
                System.out.printf("%-15s | $%.2f\n", rs1.getString("category"), rs1.getDouble("total"));
            }
            if (!hasData) {
                System.out.println("No expense data found. Add some transactions first!");
                return;
            }
            System.out.println("---------------------------------------");
            System.out.println(">>> Summary highlights:");
            System.out.printf("- Most expensive category: %s ($%.2f)\n", topCategory, topAmount);
            ResultSet rs2 = stmt.executeQuery(maxSingleSql);
            if (rs2.next()) {
                System.out.printf("- Highest single expense: $%.2f (in %s on %s)\n",
                        rs2.getDouble("amount"),
                        rs2.getString("category"),
                        rs2.getString("date"));
            }
            System.out.println("---------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error generating analysis: " + e.getMessage());
        }
    }
}