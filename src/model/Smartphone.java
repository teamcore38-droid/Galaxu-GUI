package model;

public class Smartphone extends Product {
    private String operatingSystem;

    public Smartphone(String brand, String model, double price, int stockQuantity, String specifications, String operatingSystem) {
        super(brand, model, "Smartphone", price, stockQuantity, specifications);
        this.operatingSystem = operatingSystem;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    @Override
    public String getDetails() {
        return "OS: " + operatingSystem + "; Specifications: " + getSpecifications();
    }
}
