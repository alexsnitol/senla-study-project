public class Main {
    public static void main(String[] argv) {
        Laptop laptop1 = new Laptop();
        Laptop laptop2 = new Laptop();
        Laptop laptop3 = new Laptop();

        AssemblyLineLaptop assemblyLineLaptopType1;
        AssemblyLineLaptop assemblyLineLaptopType2;
        AssemblyLineLaptop assemblyLineLaptopType3;

        LineStepCase        lineStepCasePlasticBlack         = new LineStepCase("plastic", "black");
        LineStepCase        lineStepCasePlasticAndMetalBlack = new LineStepCase("plastic and metal", "black");
        LineStepMotherboard lineStepMotherboardFP6           = new LineStepMotherboard(64,2,"FP6");
        LineStepMotherboard lineStepMotherboardFCBGA1296     = new LineStepMotherboard(16,2,"FCBGA1296");
        LineStepMotherboard lineStepMotherboardFCBGA1787     = new LineStepMotherboard(64,2,"FCBGA1787");
        LineStepMonitor     lineStepMonitorIPS               = new LineStepMonitor("IPS", 15.6f, 60);
        LineStepMonitor     lineStepMonitorAMOLED            = new LineStepMonitor("AMOLED", 15.6f, 60);

        assemblyLineLaptopType1 = new AssemblyLineLaptop(lineStepCasePlasticBlack, lineStepMotherboardFP6, lineStepMonitorIPS);
        assemblyLineLaptopType2 = new AssemblyLineLaptop(lineStepCasePlasticBlack, lineStepMotherboardFCBGA1296, lineStepMonitorIPS);
        assemblyLineLaptopType3 = new AssemblyLineLaptop(lineStepCasePlasticAndMetalBlack, lineStepMotherboardFCBGA1787, lineStepMonitorAMOLED);


        laptop1 = (Laptop)assemblyLineLaptopType1.assembleProduct(laptop1);
        laptop2 = (Laptop)assemblyLineLaptopType2.assembleProduct(laptop2);
        laptop3 = (Laptop)assemblyLineLaptopType3.assembleProduct(laptop3);
    }
}
