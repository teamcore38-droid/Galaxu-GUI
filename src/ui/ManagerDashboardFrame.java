package ui;

import model.SalesRepresentative;
import model.User;
import storage.FileStorageHandler;
import util.PasswordHasher;
import util.ValidationUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManagerDashboardFrame extends JFrame {
    private User currentUser;
    private JTextField txtNewUsername;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    private JTable tblUsers;
    private DefaultTableModel tableModel;

    public ManagerDashboardFrame(User user) {
        this.currentUser = user;
        setTitle("Gadget Galaxy - Store Manager Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 540);
        setLocationRelativeTo(null);

        initUI();
        refreshUserTable();
    }

    private void initUI() {
        // Main container is GalaxyPanel
        GalaxyPanel mainPanel = new GalaxyPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Banner Panel (Glassmorphism Banner)
        GlassPanel topPanel = new GlassPanel(new BorderLayout(), 12);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topPanel.setPreferredSize(new Dimension(850, 55));

        JLabel lblTitle = new JLabel("GADGET GALAXY - STORE MANAGER PANEL", JLabel.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(new Color(0, 222, 255)); // Galactic Cyan

        JLabel lblUserWelcome = new JLabel("Manager Session: " + currentUser.getUsername(), JLabel.RIGHT);
        lblUserWelcome.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblUserWelcome.setForeground(new Color(224, 212, 255));

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(lblUserWelcome, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center split pane: Form left, table right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(320);
        splitPane.setResizeWeight(0.3);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);

        // Left Panel (Registration Form in a GlassPanel)
        GlassPanel formPanel = new GlassPanel(new GridBagLayout(), 15);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.gridwidth = 2;

        JLabel lblFormTitle = new JLabel("Register Representative", JLabel.CENTER);
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblFormTitle.setForeground(new Color(0, 222, 255));
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblFormTitle, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(6, 4, 6, 4);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(new Color(224, 212, 255));
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(lblUser, gbc);

        txtNewUsername = new JTextField(12);
        styleTextField(txtNewUsername);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtNewUsername, gbc);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(new Color(224, 212, 255));
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(lblPass, gbc);

        txtNewPassword = new JPasswordField(12);
        styleTextField(txtNewPassword);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        formPanel.add(txtNewPassword, gbc);

        JLabel lblConfirm = new JLabel("Confirm:");
        lblConfirm.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblConfirm.setForeground(new Color(224, 212, 255));
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(lblConfirm, gbc);

        txtConfirmPassword = new JPasswordField(12);
        styleTextField(txtConfirmPassword);
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.weightx = 1.0;
        formPanel.add(txtConfirmPassword, gbc);

        StyledButton btnRegister = new StyledButton("Register Account", new Color(0, 184, 148), new Color(0, 99, 70));
        btnRegister.setPreferredSize(new Dimension(150, 32));
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(btnRegister, gbc);

        splitPane.setLeftComponent(formPanel);

        // Right Panel (User Accounts Table in a GlassPanel)
        GlassPanel tablePanel = new GlassPanel(new BorderLayout(8, 8), 15);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTableTitle = new JLabel("Registered System Registries", JLabel.LEFT);
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTableTitle.setForeground(new Color(0, 222, 255));
        tablePanel.add(lblTableTitle, BorderLayout.NORTH);

        String[] columns = {"Username", "Role", "Password Hash (SHA-256)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblUsers = new JTable(tableModel);
        styleTable(tblUsers);
        
        JScrollPane scrollPane = new JScrollPane(tblUsers);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(130, 49, 211, 80)));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        splitPane.setRightComponent(tablePanel);
        
        // Add spacing panel around splitPane to look premium
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        centerPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel containing Logout Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        StyledButton btnLogout = new StyledButton("Logout", new Color(200, 50, 80), new Color(120, 20, 40));
        btnLogout.setPreferredSize(new Dimension(100, 30));
        bottomPanel.add(btnLogout);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Actions
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
    }

    private void styleTextField(JTextField field) {
        field.setBackground(new Color(30, 20, 55, 190));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setPreferredSize(new Dimension(150, 28)); // Explicit size to prevent squishing
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(130, 49, 211), 1),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
    }

    private void styleTable(JTable table) {
        table.setBackground(new Color(30, 20, 50, 130));
        table.setOpaque(false); // Make table non-opaque to avoid dirty repaint smears
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(90, 70, 125, 80));
        table.setRowHeight(26);
        table.setSelectionBackground(new Color(130, 49, 211, 150));
        table.setSelectionForeground(Color.WHITE);

        // Crucial fix: Make cell renderers transparent so they don't smear during updates
        for (Class<?> c : new Class[]{Object.class, Number.class, Double.class, Integer.class, Boolean.class}) {
            TableCellRenderer renderer = table.getDefaultRenderer(c);
            if (renderer instanceof DefaultTableCellRenderer) {
                ((DefaultTableCellRenderer) renderer).setOpaque(false);
            }
        }

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(45, 30, 75));
        header.setForeground(new Color(0, 222, 255));
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBorder(BorderFactory.createLineBorder(new Color(130, 49, 211, 80)));
    }

    private void refreshUserTable() {
        tableModel.setRowCount(0);
        List<User> users = FileStorageHandler.loadUsers();
        for (User u : users) {
            String hashDisp = u.getPasswordHash();
            if (hashDisp.length() > 24) {
                hashDisp = hashDisp.substring(0, 24) + "...";
            }
            tableModel.addRow(new Object[]{u.getUsername(), u.getRole(), hashDisp});
        }
    }

    private void handleRegistration() {
        String username = txtNewUsername.getText().trim();
        String password = new String(txtNewPassword.getPassword());
        String confirm = new String(txtConfirmPassword.getPassword());

        if (ValidationUtils.isEmpty(username) || ValidationUtils.isEmpty(password) || ValidationUtils.isEmpty(confirm)) {
            JOptionPane.showMessageDialog(this, "All registration fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ValidationUtils.isValidUsername(username)) {
            JOptionPane.showMessageDialog(this, "Username must be alphanumeric and between 3 to 20 characters.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<User> users = FileStorageHandler.loadUsers();
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                JOptionPane.showMessageDialog(this, "Username '" + username + "' is already taken.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String hashedPass = PasswordHasher.hashPassword(password);
        User newRep = new SalesRepresentative(username, hashedPass);
        FileStorageHandler.saveUser(newRep);

        JOptionPane.showMessageDialog(this, "Sales Representative successfully registered!", "Success", JOptionPane.INFORMATION_MESSAGE);

        txtNewUsername.setText("");
        txtNewPassword.setText("");
        txtConfirmPassword.setText("");
        refreshUserTable();
    }
}
