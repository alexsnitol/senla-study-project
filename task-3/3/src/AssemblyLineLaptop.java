public class AssemblyLineLaptop implements IAssemblyLine {
    private LineStepCase lineStepLaptopCase;
    private LineStepMotherboard lineStepMotherboard;
    private LineStepMonitor lineStepMonitor;

    AssemblyLineLaptop(LineStepCase lineStepLaptopCase, LineStepMotherboard lineStepMotherboard, LineStepMonitor lineStepMonitor) {
        this.lineStepLaptopCase = lineStepLaptopCase;
        this.lineStepMotherboard = lineStepMotherboard;
        this.lineStepMonitor = lineStepMonitor;
        System.out.println("assembly line with laptop prepared");
    }

    public IProduct assembleProduct(IProduct product) {
        product.installFirstPart(lineStepLaptopCase.buildProductPart());
        System.out.println("first component is installed");
        product.installSecondPart(lineStepMotherboard.buildProductPart());
        System.out.println("second component is installed");
        product.installThirdPart(lineStepMonitor.buildProductPart());
        System.out.println("third component is installed");
        System.out.println("assembly line transmits the finished laptop");

        return product;
    }
}
