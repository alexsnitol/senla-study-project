public class LineStepMonitor implements ILineStep {
    private String displayType;
    private float screenSize;
    private float refreshRate;

    LineStepMonitor(String displayType, float screenSize, float refreshRate) {
        this.displayType = displayType;
        this.screenSize = screenSize;
        this.refreshRate = refreshRate;
        System.out.println("line step with monitors (" + this.displayType + ", " + this.screenSize + ", " + this.refreshRate + ") prepared");
    }

    @Override
    public IProductPart buildProductPart() {
        Monitor monitor = new Monitor(displayType, screenSize, refreshRate);
        System.out.println("line step transmits the finished monitor (" + this.displayType + ", " + this.screenSize + ", " + this.refreshRate + ")");
        return monitor;
    }
}
