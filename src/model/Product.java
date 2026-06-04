package model;

public abstract class Product {
    private String brand;
    private String model;
    private String category;
    private double price;
    private int stockQuantity;
    private String specifications;

    public Product(String brand, String model, String category, double price, int stockQuantity, String specifications) {
        this.brand = brand;
        this.model = model;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.specifications = specifications;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getSpecifications() {
        return specifications;
    }

    // Abstract method to be overridden by subclasses (Abstraction and Polymorphism)
    public abstract String getDetails();
}
