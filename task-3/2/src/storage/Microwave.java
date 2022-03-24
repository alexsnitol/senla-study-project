package storage;

public class Microwave extends Device {
    private boolean programmedCookingModes;

    Microwave(String name, float weight, float cost, String brand, String model, String color) {
        super(name, weight, cost, brand, model, color);
    }

    public Microwave(String name, float weight, float cost, String brand, String model, String color, boolean programmedCookingModes) {
        super(name, weight, cost, brand, model, color);
        this.programmedCookingModes = programmedCookingModes;
    }

    public boolean isProgrammedCookingModes() {
        return programmedCookingModes;
    }

    public void setProgrammedCookingModes(boolean programmedCookingModes) {
        this.programmedCookingModes = programmedCookingModes;
    }

    @Override
    public String toString() {
        return "storage.Microwave{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", cost=" + cost +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", programmedCookingModes=" + programmedCookingModes +
                '}';
    }
}