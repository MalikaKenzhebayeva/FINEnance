public class Transaction {
    private int id;
    private String type;
    private String category;
    private double amount;
    private String date;
    private String note;
    public Transaction(int id, String type, String category, double amount, String date, String note) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    @Override
    public String toString() {
        return String.format("ID: %d | %s | %s | $%.2f | Date: %s | Note: %s",
                id, type, category, amount, date, note);
    }
}