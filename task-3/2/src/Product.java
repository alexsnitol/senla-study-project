public class Product {
    protected String name;
    protected float weight;
    protected float cost;

    public Product(String name, float weight, float cost) {
        this.name = name;
        this.weight = weight;
        this.cost = cost;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public float getWeight() {
        return this.weight;
    }

    public void setWeight(float newWeight) {
        this.weight = newWeight;
    }

    public float getCost() {
        return this.cost;
    }

    public void setCost(float newCost) {
        this.cost = newCost;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", cost=" + cost +
                '}';
    }
}