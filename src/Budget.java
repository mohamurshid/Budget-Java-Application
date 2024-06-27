import java.util.ArrayList; //arrays
import java.util.List; //lists

public class Budget {
    private List<Transaction> transactions; //creating an array

    public Budget() {
        transactions = new ArrayList<>(); // initializing an array
    }

    public void addTransaction(String type, double amount, String description) { //add transaction method
        Transaction transaction = new Transaction(type, amount, description);
        transactions.add(transaction);//add transaction to the array
    }

    public double getIncome() { //get income method
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase("income"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getExpenses() { //get expenses method
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase("expense"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getBalance() { //get balance method
        return getIncome() - getExpenses(); //balance = income-expenses
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
