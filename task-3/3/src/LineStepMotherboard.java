public class LineStepMotherboard implements ILineStep {
    private int maxRAM;
    private int numberOfSlotsForRAM;
    private String socket;

    public LineStepMotherboard(int maxRAM, int numberOfSlotsForRAM, String socket) {
        this.maxRAM = maxRAM;
        this.numberOfSlotsForRAM = numberOfSlotsForRAM;
        this.socket = socket;
        System.out.println("line step with motherboards (" + this.maxRAM + ", " + this.numberOfSlotsForRAM + ", " + this.socket + ") prepared");
    }

    @Override
    public IProductPart buildProductPart() {
        Motherboard motherboard = new Motherboard(maxRAM, numberOfSlotsForRAM, socket);
        System.out.println("line step transmits the finished motherboard (" + this.maxRAM + ", " + this.numberOfSlotsForRAM + ", " + this.socket + ")");
        return motherboard;
    }
}
