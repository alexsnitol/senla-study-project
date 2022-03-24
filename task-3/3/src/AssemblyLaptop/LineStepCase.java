package AssemblyLaptop;

public class LineStepCase implements ILineStep {
    private String material;
    private String color;

    public LineStepCase(String material, String color) {
        this.material = material;
        this.color = color;
        System.out.println("line step with laptop cases (" + this.material + ", " + this.color + ") prepared");
    }

    @Override
    public IProductPart buildProductPart() {
        LaptopCase laptopCase = new LaptopCase(material, color);
        System.out.println("line step transmits the finished laptop case (" + this.material + ", " + this.color + ")");
        return laptopCase;
    }
}
