import java.util.Date;

public class Transaction {
    private static int counter = 0;
    private int id;
    private String type; // "income" or "expense"
    private double amount;
    private String description;
    private Date date;

    public Transaction(String type, double amount, String description) {
        this.id = ++counter;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = new Date();
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Transaction{id=" + id + ", type='" + type + "', amount=" + amount +
                ", description='" + description + "', date=" + date + '}';
    }
}
