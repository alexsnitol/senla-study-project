public class Refrigerator extends Device {
    private String type;
    private String doorStyle;
    
    Refrigerator(String name, float weight, float cost, String brand, String model, String color) {
        super(name, weight, cost, brand, model, color);
    }
    
    Refrigerator(String name, float weight, float cost, String brand, String model, String color, String type, String doorStyle) {
        super(name, weight, cost, brand, model, color);
        this.type = type;
        this.doorStyle = doorStyle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDoorStyle() {
        return doorStyle;
    }

    public void setDoorStyle(String doorStyle) {
        this.doorStyle = doorStyle;
    }

    @Override
    public String toString() {
        return "Refrigerator{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", cost=" + cost +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", type='" + type + '\'' +
                ", doorStyle='" + doorStyle + '\'' +
                '}';
    }
}