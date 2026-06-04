package model;

public class Accessory extends Product {
    private String accessoryType;

    public Accessory(String brand, String model, double price, int stockQuantity, String specifications, String accessoryType) {
        super(brand, model, "Accessory", price, stockQuantity, specifications);
        this.accessoryType = accessoryType;
    }

    public String getAccessoryType() {
        return accessoryType;
    }

    @Override
    public String getDetails() {
        return "Type: " + accessoryType + "; Specifications: " + getSpecifications();
    }
}
