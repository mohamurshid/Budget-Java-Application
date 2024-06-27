import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BudgetApp extends JFrame {
    private Budget budget;
    private JTextArea displayArea;
    private JTextField amountField;
    private JTextField descriptionField;
    private JComboBox<String> typeComboBox;

    public BudgetApp() {
        budget = new Budget();
        initComponents();
    }

    private void initComponents() {
        setTitle("M-Pesa Budgeting Application");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        inputPanel.add(new JLabel("Type:"));
        typeComboBox = new JComboBox<>(new String[]{"Income", "Expense"});
        inputPanel.add(typeComboBox);

        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        inputPanel.add(descriptionField);

        JButton addButton = new JButton("Add Transaction");
        addButton.addActionListener(new AddButtonListener());
        inputPanel.add(addButton);

        JButton balanceButton = new JButton("Show Balance");
        balanceButton.addActionListener(new BalanceButtonListener());
        inputPanel.add(balanceButton);

        add(inputPanel, BorderLayout.SOUTH);
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String type = (String) typeComboBox.getSelectedItem();
            double amount = Double.parseDouble(amountField.getText());
            String description = descriptionField.getText();

            budget.addTransaction(type, amount, description);

            displayArea.append(type + ": " + amount + " - " + description + "\n");
            amountField.setText("");
            descriptionField.setText("");
        }
    }

    private class BalanceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            double income = budget.getIncome();
            double expenses = budget.getExpenses();
            double balance = budget.getBalance();

            displayArea.append("\nIncome: " + income + "\n");
            displayArea.append("Expenses: " + expenses + "\n");
            displayArea.append("Balance: " + balance + "\n\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BudgetApp app = new BudgetApp();
            app.setVisible(true);
        });
    }
}
