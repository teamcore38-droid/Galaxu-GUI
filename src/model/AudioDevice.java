package model;

public class AudioDevice extends Product {
    private boolean isWireless;

    public AudioDevice(String brand, String model, double price, int stockQuantity, String specifications, boolean isWireless) {
        super(brand, model, "Audio Device", price, stockQuantity, specifications);
        this.isWireless = isWireless;
    }

    public boolean isWireless() {
        return isWireless;
    }

    @Override
    public String getDetails() {
        return (isWireless ? "Wireless" : "Wired") + "; Specifications: " + getSpecifications();
    }
}
