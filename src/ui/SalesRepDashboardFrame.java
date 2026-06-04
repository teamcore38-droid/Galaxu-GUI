package ui;

import model.*;
import storage.FileStorageHandler;
import util.ValidationUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SalesRepDashboardFrame extends JFrame {
    private User currentUser;
    private List<Product> allProducts = new ArrayList<>();

    // Search components
    private JTextField txtSearchQuery;
    private JComboBox<String> cbFilterCategory;
    private JTable tblProducts;
    private DefaultTableModel tableModel;

    // Standard Form components
    private JComboBox<String> cbProductType;
    private JTextField txtBrand;
    private JTextField txtModel;
    private JTextField txtPrice;
    private JTextField txtStock;
    private JTextField txtSpecifications;

    // Subclass Form components (CardLayout)
    private JPanel cardPanel;
    private CardLayout cardLayout;

    // Smartphone fields
    private JTextField txtOS;

    // Laptop fields
    private JTextField txtProcessor;
    private JTextField txtRAM;

    // Tablet fields
    private JTextField txtScreenSize;

    // Audio Device fields
    private JCheckBox chkWireless;

    // Accessory fields
    private JTextField txtAccessoryType;

    public SalesRepDashboardFrame(User user) {
        this.currentUser = user;
        setTitle("Gadget Galaxy - Sales Representative Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 640);
        setLocationRelativeTo(null);

        initUI();
        loadData();
    }

    private void initUI() {
        // Main container uses GalaxyPanel
        GalaxyPanel mainPanel = new GalaxyPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Banner (GlassPanel)
        GlassPanel topPanel = new GlassPanel(new BorderLayout(), 12);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topPanel.setPreferredSize(new Dimension(980, 55));

        JLabel lblTitle = new JLabel("GADGET GALAXY - SALES REPRESENTATIVE PANEL", JLabel.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(new Color(0, 222, 255)); // Galactic Cyan

        JLabel lblUserWelcome = new JLabel("Rep Session: " + currentUser.getUsername(), JLabel.RIGHT);
        lblUserWelcome.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblUserWelcome.setForeground(new Color(224, 212, 255));

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(lblUserWelcome, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center Split Panel: Left is Inventory list, Right is Add Product Form
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(600);
        splitPane.setResizeWeight(0.6);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);

        // --- LEFT SIDE: INVENTORY SEARCH AND TABLE (GlassPanel) ---
        GlassPanel inventoryPanel = new GlassPanel(new BorderLayout(10, 10), 15);
        inventoryPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Search Form Panel (Transparent)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        searchPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSearch.setForeground(new Color(224, 212, 255));
        searchPanel.add(lblSearch);

        txtSearchQuery = new JTextField(12);
        styleTextField(txtSearchQuery);
        searchPanel.add(txtSearchQuery);

        JLabel lblFilter = new JLabel("Category:");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFilter.setForeground(new Color(224, 212, 255));
        searchPanel.add(lblFilter);

        String[] filterCategories = {"All", "Smartphone", "Laptop", "Tablet", "Audio Device", "Accessory"};
        cbFilterCategory = new JComboBox<>(filterCategories);
        cbFilterCategory.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbFilterCategory.setBackground(new Color(40, 30, 70));
        cbFilterCategory.setForeground(Color.WHITE);
        searchPanel.add(cbFilterCategory);

        StyledButton btnSearch = new StyledButton("Search", new Color(0, 168, 204), new Color(0, 80, 150));
        btnSearch.setPreferredSize(new Dimension(80, 26));
        searchPanel.add(btnSearch);

        StyledButton btnReset = new StyledButton("Reset", new Color(130, 49, 211), new Color(74, 18, 142));
        btnReset.setPreferredSize(new Dimension(80, 26));
        searchPanel.add(btnReset);

        inventoryPanel.add(searchPanel, BorderLayout.NORTH);

        // Table (Themed Dark-Mode Style)
        String[] columns = {"Category", "Brand", "Model", "Price ($)", "Stock", "Details"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Integer.class;
                return super.getColumnClass(columnIndex);
            }
        };
        tblProducts = new JTable(tableModel);
        styleTable(tblProducts);
        
        JScrollPane scrollTable = new JScrollPane(tblProducts);
        scrollTable.setOpaque(false);
        scrollTable.getViewport().setOpaque(false);
        scrollTable.setBorder(BorderFactory.createLineBorder(new Color(130, 49, 211, 80)));
        inventoryPanel.add(scrollTable, BorderLayout.CENTER);

        splitPane.setLeftComponent(inventoryPanel);

        // --- RIGHT SIDE: ADD PRODUCT FORM (GlassPanel) ---
        GlassPanel formPanel = new GlassPanel(new BorderLayout(10, 10), 15);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblFormTitle = new JLabel("Add New Product", JLabel.CENTER);
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblFormTitle.setForeground(new Color(0, 222, 255));
        formPanel.add(lblFormTitle, BorderLayout.NORTH);

        // Grid-based Form Inputs
        JPanel inputGrid = new JPanel(new GridBagLayout());
        inputGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 4, 5, 4);

        // Type Dropdown
        JLabel lblType = new JLabel("Product Type:");
        lblType.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblType.setForeground(new Color(224, 212, 255));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.0;
        inputGrid.add(lblType, gbc);

        String[] types = {"Smartphone", "Laptop", "Tablet", "Audio Device", "Accessory"};
        cbProductType = new JComboBox<>(types);
        cbProductType.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbProductType.setBackground(new Color(40, 30, 70));
        cbProductType.setForeground(Color.WHITE);
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        inputGrid.add(cbProductType, gbc);

        // Brand
        JLabel lblBrand = new JLabel("Brand:");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBrand.setForeground(new Color(224, 212, 255));
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        inputGrid.add(lblBrand, gbc);

        txtBrand = new JTextField(12);
        styleTextField(txtBrand);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        inputGrid.add(txtBrand, gbc);

        // Model
        JLabel lblModel = new JLabel("Model:");
        lblModel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblModel.setForeground(new Color(224, 212, 255));
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0;
        inputGrid.add(lblModel, gbc);

        txtModel = new JTextField(12);
        styleTextField(txtModel);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        inputGrid.add(txtModel, gbc);

        // Price
        JLabel lblPrice = new JLabel("Price ($):");
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPrice.setForeground(new Color(224, 212, 255));
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0.0;
        inputGrid.add(lblPrice, gbc);

        txtPrice = new JTextField(12);
        styleTextField(txtPrice);
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.weightx = 1.0;
        inputGrid.add(txtPrice, gbc);

        // Stock Quantity
        JLabel lblStock = new JLabel("Stock Count:");
        lblStock.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStock.setForeground(new Color(224, 212, 255));
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.weightx = 0.0;
        inputGrid.add(lblStock, gbc);

        txtStock = new JTextField(12);
        styleTextField(txtStock);
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.weightx = 1.0;
        inputGrid.add(txtStock, gbc);

        // Specifications
        JLabel lblSpecs = new JLabel("Specifications:");
        lblSpecs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSpecs.setForeground(new Color(224, 212, 255));
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.weightx = 0.0;
        inputGrid.add(lblSpecs, gbc);

        txtSpecifications = new JTextField(12);
        styleTextField(txtSpecifications);
        gbc.gridx = 1; gbc.gridy = 5;
        gbc.weightx = 1.0;
        inputGrid.add(txtSpecifications, gbc);

        // CardLayout Panel for Subclass-Specific Fields (Custom styled)
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        cardPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(130, 49, 211, 80)),
                "Category Parameters", 0, 0, 
                new Font("Segoe UI", Font.BOLD, 11), new Color(0, 222, 255)
        ));

        // Card 1: Smartphone Card
        JPanel cardSmartphone = new JPanel(new GridBagLayout());
        cardSmartphone.setOpaque(false);
        GridBagConstraints cGbc = new GridBagConstraints();
        cGbc.fill = GridBagConstraints.HORIZONTAL;
        cGbc.insets = new Insets(5, 5, 5, 5);
        cGbc.gridx = 0; cGbc.gridy = 0;
        cGbc.weightx = 0.0;
        JLabel lblOS = new JLabel("OS (e.g. iOS):");
        lblOS.setForeground(Color.WHITE);
        cardSmartphone.add(lblOS, cGbc);
        txtOS = new JTextField(10);
        styleTextField(txtOS);
        cGbc.gridx = 1; cGbc.gridy = 0;
        cGbc.weightx = 1.0;
        cardSmartphone.add(txtOS, cGbc);
        cardPanel.add(cardSmartphone, "Smartphone");

        // Card 2: Laptop Card
        JPanel cardLaptop = new JPanel(new GridBagLayout());
        cardLaptop.setOpaque(false);
        cGbc = new GridBagConstraints();
        cGbc.fill = GridBagConstraints.HORIZONTAL;
        cGbc.insets = new Insets(3, 3, 3, 3);
        cGbc.gridx = 0; cGbc.gridy = 0;
        cGbc.weightx = 0.0;
        JLabel lblProc = new JLabel("Processor:");
        lblProc.setForeground(Color.WHITE);
        cardLaptop.add(lblProc, cGbc);
        txtProcessor = new JTextField(10);
        styleTextField(txtProcessor);
        cGbc.gridx = 1; cGbc.gridy = 0;
        cGbc.weightx = 1.0;
        cardLaptop.add(txtProcessor, cGbc);
        
        cGbc.gridx = 0; cGbc.gridy = 1;
        cGbc.weightx = 0.0;
        JLabel lblRam = new JLabel("RAM (GB):");
        lblRam.setForeground(Color.WHITE);
        cardLaptop.add(lblRam, cGbc);
        txtRAM = new JTextField(10);
        styleTextField(txtRAM);
        cGbc.gridx = 1; cGbc.gridy = 1;
        cGbc.weightx = 1.0;
        cardLaptop.add(txtRAM, cGbc);
        cardPanel.add(cardLaptop, "Laptop");

        // Card 3: Tablet Card
        JPanel cardTablet = new JPanel(new GridBagLayout());
        cardTablet.setOpaque(false);
        cGbc = new GridBagConstraints();
        cGbc.fill = GridBagConstraints.HORIZONTAL;
        cGbc.insets = new Insets(5, 5, 5, 5);
        cGbc.gridx = 0; cGbc.gridy = 0;
        cGbc.weightx = 0.0;
        JLabel lblScreen = new JLabel("Screen Size (in):");
        lblScreen.setForeground(Color.WHITE);
        cardTablet.add(lblScreen, cGbc);
        txtScreenSize = new JTextField(10);
        styleTextField(txtScreenSize);
        cGbc.gridx = 1; cGbc.gridy = 0;
        cGbc.weightx = 1.0;
        cardTablet.add(txtScreenSize, cGbc);
        cardPanel.add(cardTablet, "Tablet");

        // Card 4: Audio Device Card
        JPanel cardAudio = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cardAudio.setOpaque(false);
        chkWireless = new JCheckBox("Is Wireless Bluetooth?");
        chkWireless.setOpaque(false);
        chkWireless.setForeground(Color.WHITE);
        cardAudio.add(chkWireless);
        cardPanel.add(cardAudio, "Audio Device");

        // Card 5: Accessory Card
        JPanel cardAccessory = new JPanel(new GridBagLayout());
        cardAccessory.setOpaque(false);
        cGbc = new GridBagConstraints();
        cGbc.fill = GridBagConstraints.HORIZONTAL;
        cGbc.insets = new Insets(5, 5, 5, 5);
        cGbc.gridx = 0; cGbc.gridy = 0;
        cGbc.weightx = 0.0;
        JLabel lblAccType = new JLabel("Type:");
        lblAccType.setForeground(Color.WHITE);
        cardAccessory.add(lblAccType, cGbc);
        txtAccessoryType = new JTextField(10);
        styleTextField(txtAccessoryType);
        cGbc.gridx = 1; cGbc.gridy = 0;
        cGbc.weightx = 1.0;
        cardAccessory.add(txtAccessoryType, cGbc);
        cardPanel.add(cardAccessory, "Accessory");

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        inputGrid.add(cardPanel, gbc);

        // Add Button
        StyledButton btnAddProduct = new StyledButton("Add Product to Inventory", new Color(0, 168, 204), new Color(0, 80, 150));
        btnAddProduct.setPreferredSize(new Dimension(180, 32));
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 4, 4, 4);
        inputGrid.add(btnAddProduct, gbc);

        formPanel.add(inputGrid, BorderLayout.CENTER);
        splitPane.setRightComponent(formPanel);

        // Spacer around the splitPane to fit layout beautifully
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

        // --- CONTROLLER ACTION LISTENERS ---

        // Dropdown card switcher
        cbProductType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = (String) cbProductType.getSelectedItem();
                cardLayout.show(cardPanel, type);
            }
        });

        // Search Button
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applySearchFilter();
            }
        });

        // Reset Button
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSearchQuery.setText("");
                cbFilterCategory.setSelectedIndex(0);
                populateTable(allProducts);
            }
        });

        // Add Product Button
        btnAddProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddProduct();
            }
        });

        // Logout
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

        // Set Custom Stock warning Renderer to paint translucent crimson background rows
        StockTableCellRenderer customRenderer = new StockTableCellRenderer();
        table.setDefaultRenderer(Object.class, customRenderer);
        table.setDefaultRenderer(Integer.class, customRenderer);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(45, 30, 75));
        header.setForeground(new Color(0, 222, 255));
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBorder(BorderFactory.createLineBorder(new Color(130, 49, 211, 80)));
    }

    private void loadData() {
        allProducts = FileStorageHandler.loadProducts();
        populateTable(allProducts);
    }

    private void populateTable(List<Product> products) {
        tableModel.setRowCount(0);
        for (Product p : products) {
            tableModel.addRow(new Object[]{
                    p.getCategory(),
                    p.getBrand(),
                    p.getModel(),
                    String.format("%.2f", p.getPrice()),
                    p.getStockQuantity(),
                    p.getDetails()
            });
        }
    }

    private void applySearchFilter() {
        String query = txtSearchQuery.getText().trim().toLowerCase();
        String cat = (String) cbFilterCategory.getSelectedItem();

        List<Product> filtered = new ArrayList<>();
        for (Product p : allProducts) {
            boolean matchesCat = cat.equals("All") || p.getCategory().equalsIgnoreCase(cat);
            boolean matchesQuery = query.isEmpty() 
                    || p.getBrand().toLowerCase().contains(query) 
                    || p.getModel().toLowerCase().contains(query);

            if (matchesCat && matchesQuery) {
                filtered.add(p);
            }
        }
        populateTable(filtered);
    }

    private void handleAddProduct() {
        String type = (String) cbProductType.getSelectedItem();
        String brand = txtBrand.getText().trim();
        String model = txtModel.getText().trim();
        String priceStr = txtPrice.getText().trim();
        String stockStr = txtStock.getText().trim();
        String specs = txtSpecifications.getText().trim();

        if (ValidationUtils.isEmpty(brand) || ValidationUtils.isEmpty(model) || ValidationUtils.isEmpty(priceStr) || ValidationUtils.isEmpty(stockStr)) {
            JOptionPane.showMessageDialog(this, "Brand, Model, Price, and Stock Count are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price = ValidationUtils.parsePositiveDouble(priceStr);
        if (price < 0) {
            JOptionPane.showMessageDialog(this, "Price must be a valid positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stock = ValidationUtils.parsePositiveInt(stockStr);
        if (stock < 0) {
            JOptionPane.showMessageDialog(this, "Stock Count must be a valid non-negative integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Product newProduct = null;

        switch (type) {
            case "Smartphone":
                String os = txtOS.getText().trim();
                if (ValidationUtils.isEmpty(os)) {
                    JOptionPane.showMessageDialog(this, "Operating System is required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                newProduct = new Smartphone(brand, model, price, stock, specs, os);
                break;

            case "Laptop":
                String processor = txtProcessor.getText().trim();
                String ramStr = txtRAM.getText().trim();
                if (ValidationUtils.isEmpty(processor) || ValidationUtils.isEmpty(ramStr)) {
                    JOptionPane.showMessageDialog(this, "Processor and RAM are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int ram = ValidationUtils.parsePositiveInt(ramStr);
                if (ram <= 0) {
                    JOptionPane.showMessageDialog(this, "RAM must be a valid positive integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                newProduct = new Laptop(brand, model, price, stock, specs, processor, ram);
                break;

            case "Tablet":
                String screenStr = txtScreenSize.getText().trim();
                if (ValidationUtils.isEmpty(screenStr)) {
                    JOptionPane.showMessageDialog(this, "Screen size is required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double screenSize = ValidationUtils.parsePositiveDouble(screenStr);
                if (screenSize <= 0) {
                    JOptionPane.showMessageDialog(this, "Screen size must be a valid positive decimal number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                newProduct = new Tablet(brand, model, price, stock, specs, screenSize);
                break;

            case "Audio Device":
                boolean wireless = chkWireless.isSelected();
                newProduct = new AudioDevice(brand, model, price, stock, specs, wireless);
                break;

            case "Accessory":
                String accType = txtAccessoryType.getText().trim();
                if (ValidationUtils.isEmpty(accType)) {
                    JOptionPane.showMessageDialog(this, "Accessory Type is required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                newProduct = new Accessory(brand, model, price, stock, specs, accType);
                break;
        }

        if (newProduct != null) {
            allProducts.add(newProduct);
            FileStorageHandler.saveProducts(allProducts);
            JOptionPane.showMessageDialog(this, "Product successfully added to inventory!", "Success", JOptionPane.INFORMATION_MESSAGE);

            txtBrand.setText("");
            txtModel.setText("");
            txtPrice.setText("");
            txtStock.setText("");
            txtSpecifications.setText("");
            txtOS.setText("");
            txtProcessor.setText("");
            txtRAM.setText("");
            txtScreenSize.setText("");
            chkWireless.setSelected(false);
            txtAccessoryType.setText("");

            loadData();
        }
    }

    /**
     * Custom TableCellRenderer to highlight low stock rows with different background colors while keeping text white.
     */
    private static class StockTableCellRenderer extends DefaultTableCellRenderer {
        private int stock = 5;
        private boolean isSel = false;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                                                       boolean isSelected, boolean hasFocus, 
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Critical fix to prevent transparency smearing
            setOpaque(false);
            
            this.isSel = isSelected;
            
            try {
                // Get the stock count from column 4
                this.stock = (Integer) table.getModel().getValueAt(row, 4);
            } catch (Exception e) {
                this.stock = 5;
            }
            
            // Keep text color clean white as requested
            c.setForeground(Color.WHITE);
            c.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            return c;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth();
            int h = getHeight();
            
            if (isSel) {
                // Render translucent purple background for selected cell
                g2.setColor(new Color(130, 49, 211, 140));
                g2.fillRect(0, 0, w, h);
            } else if (stock < 5) {
                // Render soft glowing semi-transparent crimson background tint for low stock (under 5 units)
                g2.setColor(new Color(235, 77, 75, 55)); // 55/255 transparency prevents layout occlusion
                g2.fillRect(0, 0, w, h);
            }
            
            g2.dispose();
            
            // Let Swing draw the text centered on top of our manual background paint
            super.paintComponent(g);
        }
    }
}
