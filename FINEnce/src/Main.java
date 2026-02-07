import java.util.Scanner;
import java.util.List;
import java.util.InputMismatchException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Main {
    public static void main(String[] args) {
        DatabaseService.initializeDatabase();
        TransactionManager manager = new TransactionManager();
        CategoryAnalyzer analyzer = new CategoryAnalyzer();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to FINEnce — your money will be fine!");

        while (true) {
            // Меню печатается ТОЛЬКО в начале цикла
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Transaction History");
            System.out.println("4. Financial Summary");
            System.out.println("5. Spending Analysis");
            System.out.println("6. Clear data");
            System.out.println("7. Exit");

            int choice = -1;
            boolean validChoice = false;

            // Внутренний цикл для выбора опции (не перепечатывает всё меню)
            while (!validChoice) {
                System.out.print("Select an option: ");
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Чистим буфер
                    if (choice >= 1 && choice <= 7) {
                        validChoice = true;
                    } else {
                        System.out.println("Error: Please choose a number between 1 and 7.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Invalid input. Please enter a NUMBER.");
                    scanner.nextLine(); // Чистим буфер от букв
                }
            }

            if (choice == 7) {
                System.out.println("Exiting FINEnce. Have a great day, Malika!");
                break;
            }

            switch (choice) {
                case 1: processTransaction(scanner, manager, true); break;
                case 2: processTransaction(scanner, manager, false); break;
                case 3:
                    List<Transaction> list = manager.getAllTransactions();
                    System.out.println("\n--- History ---");
                    if (list.isEmpty()) System.out.println("No transactions found.");
                    for (Transaction t : list) System.out.println(t);
                    break;
                case 4: manager.printFinancialSummary(); break;
                case 5: analyzer.printSpendingChart(); break;
                case 6:
                    System.out.print("Are you sure? This will delete everything! (yes/no): ");
                    if (scanner.nextLine().equalsIgnoreCase("yes")) {
                        manager.deleteAllTransactions();
                    } else {
                        System.out.println("Action cancelled.");
                    }
                    break;
            }
        }
    }

    private static void processTransaction(Scanner scanner, TransactionManager manager, boolean isIncome) {
        double amount = 0;
        boolean validAmount = false;

        while (!validAmount) {
            System.out.print("Enter Amount: ");
            try {
                amount = scanner.nextDouble();
                scanner.nextLine();
                if (amount < 0) {
                    System.out.println("Amount cannot be negative.");
                } else {
                    validAmount = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid amount. Use numbers (e.g., 50.50).");
                scanner.nextLine();
            }
        }

        String category = selectCategory(scanner, isIncome);
        String date = "";
        boolean validDate = false;
        LocalDate today = LocalDate.now();

        while (!validDate) {
            System.out.print("Enter Date (yyyy-mm-dd): ");
            date = scanner.nextLine();
            try {
                LocalDate enteredDate = LocalDate.parse(date);
                if (enteredDate.isAfter(today)) {
                    System.out.println("Error: Future dates not allowed! Today is " + today + ". Try again.");
                } else {
                    validDate = true;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Error: Use yyyy-mm-dd format (e.g., 2026-01-31). Try again.");
            }
        }

        String type = isIncome ? "INCOME" : "EXPENSE";
        manager.addTransaction(type, category, amount, date, type);
        System.out.println("Success: " + type + " added!");
    }

    private static String selectCategory(Scanner sc, boolean isIncome) {
        String[] incomeCats = {"Salary", "Freelance", "Business", "Investment", "Gifts", "Refunds", "Benefits", "Other"};
        String[] expenseCats = {"Housing", "Food", "Transport", "Shopping", "Health", "Entertainment", "Subscriptions", "Travel", "Education", "Finance", "Gifts", "Kids", "Pets", "Other"};
        String[] selectedList = isIncome ? incomeCats : expenseCats;

        System.out.println("\nSelect Category:");
        for (int i = 0; i < selectedList.length; i++) {
            System.out.println((i + 1) + ". " + selectedList[i]);
        }

        while (true) {
            System.out.print("Enter number: ");
            try {
                int choice = sc.nextInt();
                sc.nextLine();
                if (choice > 0 && choice <= selectedList.length) {
                    return selectedList[choice - 1];
                }
                System.out.println("Invalid number. Choose 1-" + selectedList.length + ".");
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a number.");
                sc.nextLine();
            }
        }
    }
}