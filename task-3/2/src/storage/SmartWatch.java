package storage;

public class SmartWatch extends Device {
    private String sensor;


    SmartWatch(String name, float weight, float cost, String brand, String model, String color) {
        super(name, weight, cost, brand, model, color);
    }

    public SmartWatch(String name, float weight, float cost, String brand, String model, String color, String sensor) {
        super(name, weight, cost, brand, model, color);
        this.sensor = sensor;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    @Override
    public String toString() {
        return "storage.SmartWatch{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", cost=" + cost +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", sensor='" + sensor + '\'' +
                '}';
    }
}