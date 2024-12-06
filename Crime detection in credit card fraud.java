package piouy;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CreditCardFraudDetectionSystem {

    private static JFrame frame;
    private static Connection conn;

    public static void main(String[] args) {
        frame = new JFrame("Credit Card Fraud Detection System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        conn = connectDB();
        if (conn != null) {
            System.out.println("Database connected successfully!");
        }

        showHomePage();
    }

    // Database connection method
    private static Connection connectDB() {
        try {
            String url = "jdbc:mysql://localhost:3306/credit_card_fraud_detection";
            String user = "root"; // Update with your database username
            String password = "priya@123"; // Update with your database password
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed!");
            e.printStackTrace();
            return null;
        }
    }

    // Utility method to create a styled title
    static JLabel createTitle(String text) {
        JLabel titleLabel = new JLabel(text, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return titleLabel;
    }

    // Home Page
    static void showHomePage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 228, 225));

        JLabel title = createTitle("CRIME DETECTION IN CREDIT CARD FRAUD");
        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");

        registerButton.addActionListener(e -> showRegisterPage());
        loginButton.addActionListener(e -> showLoginPage());

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        buttonPanel.setOpaque(false);

        panel.add(title, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        updateFrame(panel);
    }

    // Register Page
    static void showRegisterPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));

        JLabel title = createTitle("Register Your Account");
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setOpaque(false);

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JLabel messageLabel = new JLabel();

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (!email.endsWith("@gmail.com")) {
                messageLabel.setText("Invalid mail id.");
            } else if (registerUser(email, password)) {
                messageLabel.setText("Registered successfully!");
                showLoginPage();
            } else {
                messageLabel.setText("Already Registered");
            }
        });

        formPanel.add(new JLabel("Email ID:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel(""));
        formPanel.add(registerButton);
        formPanel.add(messageLabel);

        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);

        updateFrame(panel);
    }

    private static boolean registerUser(String email, String password) {
        try {
            String query = "INSERT INTO users (email, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Login Page
    static void showLoginPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 255, 204));

        JLabel title = createTitle("Login to Your Account");
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setOpaque(false);

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JLabel messageLabel = new JLabel();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (authenticateUser(email, password)) {
                messageLabel.setText("Login successful!");
                showCreditCardPage();
            } else {
                messageLabel.setText("Invalid credentials!");
            }
        });

        formPanel.add(new JLabel("Email ID:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel(""));
        formPanel.add(loginButton);
        formPanel.add(messageLabel);

        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);

        updateFrame(panel);
    }

    private static boolean authenticateUser(String email, String password) {
        try {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Credit Card Page
    static void showCreditCardPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 250, 205));

        JLabel title = createTitle("Enter Credit Card Details");
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setOpaque(false);

        JTextField cardNumberField = new JTextField();
        JPasswordField pinField = new JPasswordField();
        JLabel messageLabel = new JLabel();

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String cardNumber = cardNumberField.getText();
            String pin = new String(pinField.getPassword());

            if (cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
                messageLabel.setText("Card Number must be 16 digits!");
            } else if (pin.length() != 4 || !pin.matches("\\d+")) {
                messageLabel.setText("PIN must be 4 digits!");
            } else {
                showFraudDetectionPage(cardNumber, "TRX0001", 40000.0, "Gold Purchase", "2024-11-25", "10:00:00", false);
            }
        });

        formPanel.add(new JLabel("Credit Card Number:"));
        formPanel.add(cardNumberField);
        formPanel.add(new JLabel("PIN:"));
        formPanel.add(pinField);
        formPanel.add(new JLabel(""));
        formPanel.add(submitButton);
        formPanel.add(messageLabel);

        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);

        updateFrame(panel);
    }

    // Fraud Detection Page (6th Page)
    static void showFraudDetectionPage(String cardNumber, String transactionId, double amount, String location, String date, String time, boolean isFraud) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 228, 225));

        JLabel title = createTitle("Transaction Details");

        // Default predefined transactions
        String[][] predefinedTransactions = {
            {"2024-11-20", "TRX1001", "Online Shopping", "7000.00", "43000.00", "Normal"},
            {"2024-11-21", "TRX1002", "Grocery Purchase", "1500.00", "41500.00", "Normal"},
            {"2024-11-22", "TRX1003", "Bill Payment", "1000.00", "40500.00", "Normal"},
            {"2024-11-23", "TRX1004", "Fuel Purchase", "500.00", "40000.00", "Normal"}
        };

        // Determine fraud status for the current transaction
        String detectionStatus = amount > 10000 ? "Fraud" : "Normal";
        String[] currentTransaction = {date, transactionId, location, String.format("%.2f", amount), "00.00", detectionStatus};

        // Combine predefined transactions with the current transaction
        String[][] allTransactions = new String[predefinedTransactions.length + 1][6];
        System.arraycopy(predefinedTransactions, 0, allTransactions, 0, predefinedTransactions.length);
        allTransactions[predefinedTransactions.length] = currentTransaction;

        // Table to display transactions
        String[] columnNames = {"Date", "Reference No.", "Description", "Amount", "Balance", "Fraud Detection"};
        JTable transactionTable = new JTable(allTransactions, columnNames);
        JScrollPane tableScrollPane = new JScrollPane(transactionTable);

        panel.add(title, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(exitButton, BorderLayout.SOUTH);

        updateFrame(panel);
    }

    // Utility method to update JFrame content
    static void updateFrame(JPanel panel) {
        frame.getContentPane().removeAll();
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}