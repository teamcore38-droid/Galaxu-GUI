package storage;

import model.*;
import util.PasswordHasher;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorageHandler {
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + "/users.txt";
    private static final String PRODUCTS_FILE = DATA_DIR + "/products.txt";
    private static final String DELIMITER = "\\|";
    private static final String WRITE_DELIMITER = "|";

    /**
     * Ensures directories and files exist, initializing defaults on clean setup.
     */
    public static void initializeStorage() {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File usersFile = new File(USERS_FILE);
            if (!usersFile.exists() || usersFile.length() == 0) {
                if (!usersFile.exists()) {
                    usersFile.createNewFile();
                }
                
                // Write a default store manager account (admin / admin123)
                saveUser(new StoreManager("admin", PasswordHasher.hashPassword("admin123")));
                
                // Write pre-registered sales representatives (all reps password: rep123)
                saveUser(new SalesRepresentative("john_rep", PasswordHasher.hashPassword("rep123")));
                saveUser(new SalesRepresentative("sarah_rep", PasswordHasher.hashPassword("rep123")));
                saveUser(new SalesRepresentative("david_rep", PasswordHasher.hashPassword("rep123")));
            }

            File productsFile = new File(PRODUCTS_FILE);
            if (!productsFile.exists() || productsFile.length() == 0) {
                if (!productsFile.exists()) {
                    productsFile.createNewFile();
                }

                // Add rich set of default products (with varied stock levels to test warnings)
                List<Product> defaultProducts = new ArrayList<>();
                
                // Smartphones
                defaultProducts.add(new Smartphone("Apple", "iPhone 15 Pro", 1199.99, 12, "128GB Titanium", "iOS"));
                defaultProducts.add(new Smartphone("Samsung", "Galaxy S24 Ultra", 1299.99, 3, "256GB, Snapdragon 8 Gen 3", "Android")); // Low stock
                defaultProducts.add(new Smartphone("Google", "Pixel 8 Pro", 999.00, 15, "128GB, Tensor G3, OLED", "Android"));
                defaultProducts.add(new Smartphone("OnePlus", "12", 799.99, 2, "256GB, 16GB RAM, Fluid AMOLED", "Android")); // Low stock
                
                // Laptops
                defaultProducts.add(new Laptop("Lenovo", "ThinkPad T14", 1349.00, 8, "512GB NVMe SSD", "AMD Ryzen 7", 16));
                defaultProducts.add(new Laptop("Apple", "MacBook Pro 14", 1999.99, 4, "Liquid Retina XDR, M3 Chip", "Apple M3 Pro", 18)); // Low stock
                defaultProducts.add(new Laptop("Dell", "XPS 15", 1799.50, 10, "1TB SSD, 16:10 Display", "Intel Core i7", 32));
                defaultProducts.add(new Laptop("ASUS", "ROG Zephyrus G14", 1599.00, 2, "RTX 4060 GPU", "AMD Ryzen 9", 16)); // Low stock
                defaultProducts.add(new Laptop("HP", "Spectre x360", 1249.99, 6, "2-in-1 Convertible, Touchscreen", "Intel Core i5", 8));

                // Tablets
                defaultProducts.add(new Tablet("Apple", "iPad Air", 599.00, 22, "Liquid Retina display", 10.9));
                defaultProducts.add(new Tablet("Samsung", "Galaxy Tab S9", 799.99, 1, "Dynamic AMOLED 2X, S-Pen included", 11.0)); // Low stock
                defaultProducts.add(new Tablet("Microsoft", "Surface Pro 9", 999.00, 7, "PixelSense touchscreen", 13.0));
                defaultProducts.add(new Tablet("Lenovo", "Tab P12", 349.99, 11, "3K screen, stylus included", 12.7));

                // Audio Devices
                defaultProducts.add(new AudioDevice("Sony", "WH-1000XM5", 399.99, 14, "Active Noise Cancelling", true));
                defaultProducts.add(new AudioDevice("Bose", "QuietComfort Ultra", 429.00, 3, "Immersive spatial audio", true)); // Low stock
                defaultProducts.add(new AudioDevice("Apple", "AirPods Pro 2", 249.00, 25, "MagSafe Case USB-C", true));
                defaultProducts.add(new AudioDevice("JBL", "Flip 6", 129.99, 18, "Portable Waterproof Speaker", true));
                defaultProducts.add(new AudioDevice("Sennheiser", "HD 600", 299.99, 5, "Open-back Audiophile Reference", false));

                // Accessories
                defaultProducts.add(new Accessory("Logitech", "MX Master 3S", 99.99, 30, "Ergonomic wireless mouse", "Mouse"));
                defaultProducts.add(new Accessory("Anker", "737 Power Bank", 149.99, 2, "140W Fast Charging 24000mAh Battery", "Power Bank")); // Low stock
                defaultProducts.add(new Accessory("Belkin", "3-in-1 Charging Stand", 119.50, 9, "MagSafe 15W wireless charging", "Charger"));
                defaultProducts.add(new Accessory("Keychron", "K2 Mechanical Keyboard", 89.99, 16, "Gateron Brown switches", "Keyboard"));

                saveProducts(defaultProducts);
            }
        } catch (IOException e) {
            System.err.println("Critical: Error initializing storage files: " + e.getMessage());
        }
    }

    /**
     * Loads all user accounts from users.txt.
     */
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(DELIMITER);
                if (parts.length >= 3) {
                    String username = parts[0];
                    String passwordHash = parts[1];
                    String role = parts[2];

                    if ("Store Manager".equalsIgnoreCase(role)) {
                        users.add(new StoreManager(username, passwordHash));
                    } else if ("Sales Representative".equalsIgnoreCase(role)) {
                        users.add(new SalesRepresentative(username, passwordHash));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users from file: " + e.getMessage());
        }
        return users;
    }

    /**
     * Appends a new user account to users.txt.
     */
    public static void saveUser(User user) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE, true))) {
            writer.println(user.getUsername() + WRITE_DELIMITER + user.getPasswordHash() + WRITE_DELIMITER + user.getRole());
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    }

    /**
     * Loads all products polymorphically based on category text values.
     */
    public static List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        File file = new File(PRODUCTS_FILE);
        if (!file.exists()) return products;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(DELIMITER);
                if (parts.length >= 6) {
                    String category = parts[0];
                    String brand = parts[1];
                    String model = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    int stock = Integer.parseInt(parts[4]);
                    String specifications = parts[5];

                    switch (category) {
                        case "Smartphone":
                            if (parts.length >= 7) {
                                String os = parts[6];
                                products.add(new Smartphone(brand, model, price, stock, specifications, os));
                            }
                            break;
                        case "Laptop":
                            if (parts.length >= 8) {
                                String processor = parts[6];
                                int ram = Integer.parseInt(parts[7]);
                                products.add(new Laptop(brand, model, price, stock, specifications, processor, ram));
                            }
                            break;
                        case "Tablet":
                            if (parts.length >= 7) {
                                double size = Double.parseDouble(parts[6]);
                                products.add(new Tablet(brand, model, price, stock, specifications, size));
                            }
                            break;
                        case "Audio Device":
                            if (parts.length >= 7) {
                                boolean wireless = Boolean.parseBoolean(parts[6]);
                                products.add(new AudioDevice(brand, model, price, stock, specifications, wireless));
                            }
                            break;
                        case "Accessory":
                            if (parts.length >= 7) {
                                String accType = parts[6];
                                products.add(new Accessory(brand, model, price, stock, specifications, accType));
                            }
                            break;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading products: " + e.getMessage());
        }
        return products;
    }

    /**
     * Overwrites products.txt with the current list of products.
     */
    public static void saveProducts(List<Product> products) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PRODUCTS_FILE, false))) {
            for (Product p : products) {
                StringBuilder sb = new StringBuilder();
                sb.append(p.getCategory()).append(WRITE_DELIMITER)
                  .append(p.getBrand()).append(WRITE_DELIMITER)
                  .append(p.getModel()).append(WRITE_DELIMITER)
                  .append(p.getPrice()).append(WRITE_DELIMITER)
                  .append(p.getStockQuantity()).append(WRITE_DELIMITER)
                  .append(p.getSpecifications());

                if (p instanceof Smartphone) {
                    sb.append(WRITE_DELIMITER).append(((Smartphone) p).getOperatingSystem());
                } else if (p instanceof Laptop) {
                    Laptop l = (Laptop) p;
                    sb.append(WRITE_DELIMITER).append(l.getProcessor()).append(WRITE_DELIMITER).append(l.getRamSizeGB());
                } else if (p instanceof Tablet) {
                    sb.append(WRITE_DELIMITER).append(((Tablet) p).getScreenSizeInches());
                } else if (p instanceof AudioDevice) {
                    sb.append(WRITE_DELIMITER).append(((AudioDevice) p).isWireless());
                } else if (p instanceof Accessory) {
                    sb.append(WRITE_DELIMITER).append(((Accessory) p).getAccessoryType());
                }
                writer.println(sb.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving products to file: " + e.getMessage());
        }
    }
}
