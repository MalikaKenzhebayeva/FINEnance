import java.sql.*;

public class CategoryAnalyzer {

    public void printSpendingChart() {
        System.out.println("\n--- FINANCIAL STATISTICS ---");

        // Запрос 1: Суммы по категориям
        String categorySql = "SELECT category, SUM(amount) as total " +
                "FROM transactions " +
                "WHERE type = 'EXPENSE' " +
                "GROUP BY category " +
                "ORDER BY total DESC";

        // Запрос 2: Самый большой разовый расход
        String maxSingleSql = "SELECT category, amount, date " +
                "FROM transactions " +
                "WHERE type = 'EXPENSE' " +
                "ORDER BY amount DESC LIMIT 1";

        try (Connection conn = DatabaseService.connect();
             Statement stmt = conn.createStatement()) {

            // Выполняем первый запрос
            ResultSet rs1 = stmt.executeQuery(categorySql);

            System.out.println("Spending Analysis (By Category):");
            System.out.println("---------------------------------------");

            boolean hasData = false;
            String topCategory = "";
            double topAmount = 0;

            while (rs1.next()) {
                if (!hasData) { // Первая строка в отсортированном списке и есть самая затратная
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
            System.out.println(">>> Summary Highlights:");
            System.out.printf("- Most Expensive Category: %s ($%.2f)\n", topCategory, topAmount);

            // Выполняем второй запрос для самого большого чека
            ResultSet rs2 = stmt.executeQuery(maxSingleSql);
            if (rs2.next()) {
                System.out.printf("- Highest Single Expense: $%.2f (in %s on %s)\n",
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