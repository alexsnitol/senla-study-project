package storage;

public class Device extends Product {
    protected String brand;
    protected String model;
    protected String color;

    Device(String name, float weight, float cost, String brand, String model, String color) {
        super(name, weight, cost);
        this.brand = brand;
        this.model = model;
        this.color = color;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String newBrand) {
        this.brand = newBrand;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String newModel) {
        this.model = newModel;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "storage.Device{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", cost=" + cost +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
