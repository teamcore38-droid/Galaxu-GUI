package ui;

import model.User;
import storage.FileStorageHandler;
import util.PasswordHasher;
import util.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private StyledButton btnLogin;
    private StyledButton btnCancel;

    public LoginFrame() {
        setTitle("Gadget Galaxy - Secure Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Increased height and width to prevent layout squeezing and text cutoffs
        setSize(460, 340);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        // Main container uses GalaxyPanel
        GalaxyPanel mainPanel = new GalaxyPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Glass Panel Card (glassmorphism effect)
        GlassPanel glassCard = new GlassPanel(new BorderLayout(15, 15), 20);
        glassCard.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Header Title (Galaxy themed)
        JLabel lblHeader = new JLabel("GADGET GALAXY", JLabel.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setForeground(new Color(0, 222, 255)); // Galactic Cyan
        
        JLabel lblSubHeader = new JLabel("Sales & Inventory Management System", JLabel.CENTER);
        lblSubHeader.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblSubHeader.setForeground(new Color(185, 160, 235)); // Soft Purple

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        headerPanel.setOpaque(false);
        headerPanel.add(lblHeader);
        headerPanel.add(lblSubHeader);
        glassCard.add(headerPanel, BorderLayout.NORTH);

        // Form Layout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsername.setForeground(new Color(224, 212, 255));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.weightx = 0.0;
        formPanel.add(lblUsername, gbc);

        txtUsername = new JTextField(15);
        styleTextField(txtUsername);
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPassword.setForeground(new Color(224, 212, 255));
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.weightx = 0.0;
        formPanel.add(lblPassword, gbc);

        txtPassword = new JPasswordField(15);
        styleTextField(txtPassword);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(txtPassword, gbc);

        glassCard.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        btnLogin = new StyledButton("Login", new Color(0, 168, 204), new Color(0, 75, 150));
        btnLogin.setPreferredSize(new Dimension(95, 32));

        btnCancel = new StyledButton("Exit", new Color(200, 50, 80), new Color(120, 20, 40));
        btnCancel.setPreferredSize(new Dimension(95, 32));

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnCancel);
        glassCard.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(glassCard, BorderLayout.CENTER);
        add(mainPanel);

        // Listeners
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        getRootPane().setDefaultButton(btnLogin);
    }

    private void styleTextField(JTextField field) {
        field.setBackground(new Color(30, 20, 55, 190));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        // Explicitly set preferred size to guarantee proper sizing across different systems
        field.setPreferredSize(new Dimension(180, 28));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(130, 49, 211), 1),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (ValidationUtils.isEmpty(username) || ValidationUtils.isEmpty(password)) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<User> users = FileStorageHandler.loadUsers();
        String hashedInput = PasswordHasher.hashPassword(password);
        User authenticatedUser = null;

        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username) && u.authenticate(hashedInput)) {
                authenticatedUser = u;
                break;
            }
        }

        if (authenticatedUser != null) {
            this.dispose();
            if ("Store Manager".equalsIgnoreCase(authenticatedUser.getRole())) {
                new ManagerDashboardFrame(authenticatedUser).setVisible(true);
            } else {
                new SalesRepDashboardFrame(authenticatedUser).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
