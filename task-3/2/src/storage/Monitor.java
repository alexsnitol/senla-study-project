package storage;

public class Monitor extends Device {
    private String displayType;
    private float screenSize;
    private float refreshRate;


    Monitor(String name, float weight, float cost, String brand, String model, String color) {
        super(name, weight, cost, brand, model, color);
    }

    public Monitor(String name, float weight, float cost, String brand, String model, String color, String displayType, float screenSize, float refreshRate) {
        super(name, weight, cost, brand, model, color);
        this.displayType = displayType;
        this.screenSize = screenSize;
        this.refreshRate = refreshRate;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public float getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(float screenSize) {
        this.screenSize = screenSize;
    }

    public float getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(float refreshRate) {
        this.refreshRate = refreshRate;
    }

    @Override
    public String toString() {
        return "storage.Monitor{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", cost=" + cost +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", displayType='" + displayType + '\'' +
                ", screenSize=" + screenSize +
                ", refreshRate=" + refreshRate +
                '}';
    }
}