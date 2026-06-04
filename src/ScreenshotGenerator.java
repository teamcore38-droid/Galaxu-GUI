import model.*;
import storage.FileStorageHandler;
import ui.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenshotGenerator {
    private static final String SCREENSHOTS_DIR = "screenshots";

    public static void main(String[] args) {
        System.out.println("Starting Advanced Screenshot Generator (21 Screens)...");
        
        // Set Look and Feel for proper operating system rendering
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        FileStorageHandler.initializeStorage();

        File dir = new File(SCREENSHOTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            System.out.println("1. Capturing Login module screens and validation dialogs...");
            generateLoginScreenshots();

            System.out.println("2. Capturing Store Manager dashboard and registration dialogs...");
            generateManagerScreenshots();

            System.out.println("3. Capturing Sales Representative portal, search states, and alerts...");
            generateSalesRepScreenshots();

            System.out.println("SUCCESS: Generated 21 screenshot files in " + dir.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Capture Error: " + e.getMessage());
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void saveVisibleFrame(JFrame frame, String filename) throws IOException, InterruptedException {
        frame.setVisible(true);
        Thread.sleep(500);

        // Capture to non-transparent TYPE_INT_RGB buffer
        BufferedImage img = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        frame.paint(g2);
        g2.dispose();

        File file = new File(SCREENSHOTS_DIR + "/" + filename);
        ImageIO.write(img, "png", file);
        System.out.println("Saved frame: " + file.getName() + " (" + file.length() + " bytes)");
    }

    private static void captureOpenDialog(String filename) throws Exception {
        // Wait for Swing Thread to lay out and render the JOptionPane JDialog popup
        Thread.sleep(500);
        
        JDialog dialog = null;
        for (Window w : Window.getWindows()) {
            if (w instanceof JDialog && w.isShowing()) {
                dialog = (JDialog) w;
                break;
            }
        }
        
        if (dialog != null) {
            BufferedImage img = new BufferedImage(dialog.getWidth(), dialog.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = img.createGraphics();
            dialog.paint(g2);
            g2.dispose();
            
            File file = new File(SCREENSHOTS_DIR + "/" + filename);
            ImageIO.write(img, "png", file);
            System.out.println("Saved Dialog: " + file.getName() + " (" + file.length() + " bytes)");
            
            // Dispose the popup dialog window to unblock Event Dispatch Thread
            dialog.dispose();
        } else {
            System.err.println("Warning: Could not find active JDialog popup for: " + filename);
        }
    }

    private static void generateLoginScreenshots() throws Exception {
        LoginFrame frame = new LoginFrame();
        
        // Screen 1: Empty Login Form
        saveVisibleFrame(frame, "1_login_empty.png");

        // Screen 8: Trigger Empty Login Error Popup Dialog
        java.lang.reflect.Field btnLoginField = LoginFrame.class.getDeclaredField("btnLogin");
        btnLoginField.setAccessible(true);
        JButton btnLogin = (JButton) btnLoginField.get(frame);
        
        SwingUtilities.invokeLater(() -> btnLogin.doClick());
        captureOpenDialog("8_login_error_empty.png");

        // Screen 9: Trigger Invalid Login Error Popup Dialog
        java.lang.reflect.Field txtUserField = LoginFrame.class.getDeclaredField("txtUsername");
        txtUserField.setAccessible(true);
        JTextField txtUser = (JTextField) txtUserField.get(frame);
        
        java.lang.reflect.Field txtPassField = LoginFrame.class.getDeclaredField("txtPassword");
        txtPassField.setAccessible(true);
        JPasswordField txtPass = (JPasswordField) txtPassField.get(frame);
        
        txtUser.setText("wrong_username");
        txtPass.setText("wrong_password");
        
        SwingUtilities.invokeLater(() -> btnLogin.doClick());
        captureOpenDialog("9_login_error_invalid.png");

        // Screen 2: Login filled with manager credentials
        txtUser.setText("admin");
        txtPass.setText("admin123");
        frame.repaint();
        saveVisibleFrame(frame, "2_login_filled.png");
        
        frame.dispose();
    }

    private static void generateManagerScreenshots() throws Exception {
        StoreManager manager = new StoreManager("admin", "dummy_hash");
        ManagerDashboardFrame frame = new ManagerDashboardFrame(manager);
        
        // Screen 3: Manager Dashboard main view
        saveVisibleFrame(frame, "3_manager_dashboard.png");

        java.lang.reflect.Field txtNewUserField = ManagerDashboardFrame.class.getDeclaredField("txtNewUsername");
        txtNewUserField.setAccessible(true);
        JTextField txtNewUser = (JTextField) txtNewUserField.get(frame);

        java.lang.reflect.Field txtNewPassField = ManagerDashboardFrame.class.getDeclaredField("txtNewPassword");
        txtNewPassField.setAccessible(true);
        JPasswordField txtNewPass = (JPasswordField) txtNewPassField.get(frame);

        java.lang.reflect.Field txtConfField = ManagerDashboardFrame.class.getDeclaredField("txtConfirmPassword");
        txtConfField.setAccessible(true);
        JPasswordField txtConf = (JPasswordField) txtConfField.get(frame);

        // Recursively locate the "Register Account" StyledButton
        JButton btnRegister = null;
        for (Component c : frame.getContentPane().getComponents()) {
            if (c instanceof JPanel) {
                btnRegister = findButton((JPanel) c, "Register Account");
                if (btnRegister != null) break;
            }
        }

        final JButton regBtn = btnRegister;

        // Screen 10: Trigger duplicate user registration dialog
        txtNewUser.setText("admin");
        txtNewPass.setText("somePassword123");
        txtConf.setText("somePassword123");
        if (regBtn != null) {
            SwingUtilities.invokeLater(() -> regBtn.doClick());
            captureOpenDialog("10_manager_error_duplicate.png");
        }

        // Screen 11: Trigger short password error dialog
        txtNewUser.setText("new_sales_rep");
        txtNewPass.setText("123");
        txtConf.setText("123");
        if (regBtn != null) {
            SwingUtilities.invokeLater(() -> regBtn.doClick());
            captureOpenDialog("11_manager_error_password_length.png");
        }

        // Screen 12: Trigger password mismatch error dialog
        txtNewUser.setText("new_sales_rep");
        txtNewPass.setText("password123");
        txtConf.setText("password456");
        if (regBtn != null) {
            SwingUtilities.invokeLater(() -> regBtn.doClick());
            captureOpenDialog("12_manager_error_password_mismatch.png");
        }

        // Screen 4: Filled registration form
        txtNewUser.setText("robert_rep");
        txtNewPass.setText("repSecret123");
        txtConf.setText("repSecret123");
        frame.repaint();
        saveVisibleFrame(frame, "4_manager_registration_filled.png");

        frame.dispose();
    }

    private static void generateSalesRepScreenshots() throws Exception {
        SalesRepresentative rep = new SalesRepresentative("john_rep", "dummy_hash");
        SalesRepDashboardFrame frame = new SalesRepDashboardFrame(rep);
        
        // Screen 5: Sales Rep Dashboard main (displays low-stock warnings highlighted in translucent crimson backgrounds)
        saveVisibleFrame(frame, "5_sales_rep_dashboard.png");

        java.lang.reflect.Field txtSearchField = SalesRepDashboardFrame.class.getDeclaredField("txtSearchQuery");
        txtSearchField.setAccessible(true);
        JTextField txtSearch = (JTextField) txtSearchField.get(frame);

        java.lang.reflect.Field cbFilterField = SalesRepDashboardFrame.class.getDeclaredField("cbFilterCategory");
        cbFilterField.setAccessible(true);
        JComboBox<String> cbFilter = (JComboBox<String>) cbFilterField.get(frame);

        java.lang.reflect.Method searchMethod = SalesRepDashboardFrame.class.getDeclaredMethod("applySearchFilter");
        searchMethod.setAccessible(true);

        // Screen 6: Filtered inventory by category Laptop
        cbFilter.setSelectedItem("Laptop");
        searchMethod.invoke(frame);
        saveVisibleFrame(frame, "6_sales_rep_search_laptop.png");

        // Screen 13: Filtered inventory by brand search "Apple"
        cbFilter.setSelectedItem("All");
        txtSearch.setText("Apple");
        searchMethod.invoke(frame);
        saveVisibleFrame(frame, "13_sales_rep_search_apple.png");

        // Screen 14: Filtered inventory by model search "Pro"
        txtSearch.setText("Pro");
        searchMethod.invoke(frame);
        saveVisibleFrame(frame, "14_sales_rep_search_pro.png");

        // Reset search states
        txtSearch.setText("");
        cbFilter.setSelectedItem("All");
        searchMethod.invoke(frame);

        // Find input text fields for adding products to test validation alerts
        java.lang.reflect.Field txtBrandField = SalesRepDashboardFrame.class.getDeclaredField("txtBrand");
        txtBrandField.setAccessible(true);
        JTextField txtBrand = (JTextField) txtBrandField.get(frame);

        java.lang.reflect.Field txtModelField = SalesRepDashboardFrame.class.getDeclaredField("txtModel");
        txtModelField.setAccessible(true);
        JTextField txtModel = (JTextField) txtModelField.get(frame);

        java.lang.reflect.Field txtPriceField = SalesRepDashboardFrame.class.getDeclaredField("txtPrice");
        txtPriceField.setAccessible(true);
        JTextField txtPrice = (JTextField) txtPriceField.get(frame);

        java.lang.reflect.Field txtStockField = SalesRepDashboardFrame.class.getDeclaredField("txtStock");
        txtStockField.setAccessible(true);
        JTextField txtStock = (JTextField) txtStockField.get(frame);

        // Locate the "Add Product to Inventory" StyledButton
        JButton btnAdd = null;
        for (Component c : frame.getContentPane().getComponents()) {
            if (c instanceof JPanel) {
                btnAdd = findButton((JPanel) c, "Add Product to Inventory");
                if (btnAdd != null) break;
            }
        }

        final JButton addBtn = btnAdd;

        // Screen 15: Invalid price format validation popup dialog
        txtBrand.setText("Test Brand");
        txtModel.setText("Test Model");
        txtPrice.setText("invalid_price");
        txtStock.setText("10");
        if (addBtn != null) {
            SwingUtilities.invokeLater(() -> addBtn.doClick());
            captureOpenDialog("15_sales_rep_error_price.png");
        }

        // Screen 16: Negative stock count validation popup dialog
        txtPrice.setText("199.99");
        txtStock.setText("-5");
        if (addBtn != null) {
            SwingUtilities.invokeLater(() -> addBtn.doClick());
            captureOpenDialog("16_sales_rep_error_stock.png");
        }

        // Clear input boxes
        txtBrand.setText("");
        txtModel.setText("");
        txtPrice.setText("");
        txtStock.setText("");

        // Screens 17-21: Dynamic parameter sub-panels for all 5 product types
        java.lang.reflect.Field cbProductTypeField = SalesRepDashboardFrame.class.getDeclaredField("cbProductType");
        cbProductTypeField.setAccessible(true);
        JComboBox<String> cbProductType = (JComboBox<String>) cbProductTypeField.get(frame);

        java.lang.reflect.Field cardLayoutField = SalesRepDashboardFrame.class.getDeclaredField("cardLayout");
        cardLayoutField.setAccessible(true);
        CardLayout cardLayout = (CardLayout) cardLayoutField.get(frame);

        java.lang.reflect.Field cardPanelField = SalesRepDashboardFrame.class.getDeclaredField("cardPanel");
        cardPanelField.setAccessible(true);
        JPanel cardPanel = (JPanel) cardPanelField.get(frame);

        String[] categories = {"Smartphone", "Laptop", "Tablet", "Audio Device", "Accessory"};
        for (String cat : categories) {
            cbProductType.setSelectedItem(cat);
            cardLayout.show(cardPanel, cat);
            frame.repaint();
            saveVisibleFrame(frame, "7_add_product_form_" + cat.toLowerCase().replace(" ", "_") + ".png");
        }

        frame.dispose();
    }

    private static JButton findButton(Container parent, String text) {
        for (Component c : parent.getComponents()) {
            if (c instanceof JButton && text.equals(((JButton) c).getText())) {
                return (JButton) c;
            } else if (c instanceof Container) {
                JButton b = findButton((Container) c, text);
                if (b != null) return b;
            }
        }
        return null;
    }
}
