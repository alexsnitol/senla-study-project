public class Motherboard implements IProductPart {
    private int maxRAM;
    private int numberOfSlotsForRAM;
    private String socket;

    public Motherboard(int maxRAM, int numberOfSlotsForRAM, String socket) {
        this.maxRAM = maxRAM;
        this.numberOfSlotsForRAM = numberOfSlotsForRAM;
        this.socket = socket;
    }
}
