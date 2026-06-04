package model;

public class Laptop extends Product {
    private String processor;
    private int ramSizeGB;

    public Laptop(String brand, String model, double price, int stockQuantity, String specifications, String processor, int ramSizeGB) {
        super(brand, model, "Laptop", price, stockQuantity, specifications);
        this.processor = processor;
        this.ramSizeGB = ramSizeGB;
    }

    public String getProcessor() {
        return processor;
    }

    public int getRamSizeGB() {
        return ramSizeGB;
    }

    @Override
    public String getDetails() {
        return "Processor: " + processor + ", RAM: " + ramSizeGB + "GB; Specifications: " + getSpecifications();
    }
}
