import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class BudgetApp extends JFrame {
    private Budget budget;
    private JTextArea displayArea;
    private JTextField amountField;
    private JTextField descriptionField;
    private JComboBox<String> typeComboBox;
    private Map<String, User> users;
    private User currentUser;

    public BudgetApp() {
        users = new HashMap<>();
        budget = new Budget();
        setupLoginScreen();
    }

    private void setupLoginScreen() {
        setTitle("Login / Sign Up");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(signUpButton);
        getContentPane().setBackground(Color.LIGHT_GRAY);//changes background color of the login screen to light gray
        loginButton.setBackground(Color.DARK_GRAY);//sets background of login button to dark gray
        loginButton.setForeground(Color.WHITE); // Sets up foreground color of login button

        signUpButton.setBackground(Color.DARK_GRAY);// changes background color of sign up button to dark gray
        signUpButton.setForeground(Color.WHITE); // changes foreground color of sign up button to white


        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (login(username, password)) {
                setupBudgetScreen();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login credentials");
            }
        });

        signUpButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (signUp(username, password)) {
                JOptionPane.showMessageDialog(this, "Sign up successful. You can now log in.");
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists");
            }
        });
    }

    private void setupBudgetScreen() {
        getContentPane().removeAll();
        revalidate();
        repaint();

        setTitle("Budgeting Application");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());



        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));

        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField();
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionField = new JTextField();
        JLabel typeLabel = new JLabel("Type:");
        String[] types = {"Income", "Expense"};
        typeComboBox = new JComboBox<>(types);

        inputPanel.add(amountLabel);
        inputPanel.add(amountField);
        inputPanel.add(descriptionLabel);
        inputPanel.add(descriptionField);
        inputPanel.add(typeLabel);
        inputPanel.add(typeComboBox);

        add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton addButton = new JButton("Add Transaction");
        JButton balanceButton = new JButton("Show Balance");

        buttonPanel.add(addButton);
        buttonPanel.add(balanceButton);

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new AddTransactionListener());
        balanceButton.addActionListener(new BalanceButtonListener());
        getContentPane().setBackground(Color.lightGray);

        setVisible(true);
    }

    private boolean signUp(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new User(username, password));
        return true;
    }

    private boolean login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    private class AddTransactionListener implements ActionListener {
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