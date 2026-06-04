package model;

public class Tablet extends Product {
    private double screenSizeInches;

    public Tablet(String brand, String model, double price, int stockQuantity, String specifications, double screenSizeInches) {
        super(brand, model, "Tablet", price, stockQuantity, specifications);
        this.screenSizeInches = screenSizeInches;
    }

    public double getScreenSizeInches() {
        return screenSizeInches;
    }

    @Override
    public String getDetails() {
        return "Screen Size: " + screenSizeInches + " inches; Specifications: " + getSpecifications();
    }
}
