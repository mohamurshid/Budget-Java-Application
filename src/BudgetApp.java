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
    private User currentUser;
    private UserDAO userDAO;

    public BudgetApp() {
        userDAO = new UserDAO();
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

        getContentPane().setBackground(Color.LIGHT_GRAY);
        loginButton.setBackground(Color.DARK_GRAY);
        loginButton.setForeground(Color.WHITE);

        signUpButton.setBackground(Color.DARK_GRAY);
        signUpButton.setForeground(Color.WHITE);

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

        signUpButton.addActionListener(e -> showSignUpForm());
    }

    private void setupBudgetScreen() {
        getContentPane().removeAll();
        revalidate();
        repaint();

        setTitle("Budgeting Application");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.LIGHT_GRAY);

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

        amountLabel.setForeground(Color.BLACK);
        descriptionLabel.setForeground(Color.BLACK);
        typeLabel.setForeground(Color.BLACK);

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

        addButton.setBackground(Color.DARK_GRAY);
        addButton.setForeground(Color.WHITE);

        balanceButton.setBackground(Color.DARK_GRAY);
        balanceButton.setForeground(Color.WHITE);

        buttonPanel.add(addButton);
        buttonPanel.add(balanceButton);

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new AddTransactionListener());
        balanceButton.addActionListener(new BalanceButtonListener());

        setVisible(true);
    }

    private void showSignUpForm() {
        JDialog signUpDialog = new JDialog(this, "Sign Up", true);
        signUpDialog.setSize(400, 300);
        signUpDialog.setLayout(new GridLayout(6, 2));

        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField();

        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");

        signUpDialog.add(firstNameLabel);
        signUpDialog.add(firstNameField);
        signUpDialog.add(lastNameLabel);
        signUpDialog.add(lastNameField);
        signUpDialog.add(emailLabel);
        signUpDialog.add(emailField);
        signUpDialog.add(passwordLabel);
        signUpDialog.add(passwordField);
        signUpDialog.add(confirmPasswordLabel);
        signUpDialog.add(confirmPasswordField);
        signUpDialog.add(submitButton);
        signUpDialog.add(cancelButton);

        submitButton.addActionListener(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (password.equals(confirmPassword)) {
                if (signUp(email, password)) {
                    JOptionPane.showMessageDialog(signUpDialog, "Sign up successful. You can now log in.");
                    signUpDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(signUpDialog, "Email already exists.");
                }
            } else {
                JOptionPane.showMessageDialog(signUpDialog, "Passwords do not match.");
            }
        });

        cancelButton.addActionListener(e -> signUpDialog.dispose());

        signUpDialog.setVisible(true);
    }

    private boolean signUp(String username, String password) {
        return userDAO.signUp(username, password);
    }

    private boolean login(String username, String password) {
        User user = userDAO.getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    private class AddTransactionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String type = (String) typeComboBox.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText());
                String description = descriptionField.getText();
                budget.addTransaction(type, amount, description);

                displayArea.append(type + ": " + amount + " - " + description + "\n");
                amountField.setText("");
                descriptionField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BudgetApp.this, "Invalid amount format.");
            }
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