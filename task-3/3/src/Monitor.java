public class Monitor implements IProductPart{
    private String displayType;
    private float screenSize;
    private float refreshRate;

    public Monitor(String displayType, float screenSize, float refreshRate) {
        this.displayType = displayType;
        this.screenSize = screenSize;
        this.refreshRate = refreshRate;
    }
}
