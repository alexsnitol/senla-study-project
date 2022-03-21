import storage.*;

import static java.lang.System.*;

public class Main {
    public static void main(String[] arg) {
        Storage storage = new Storage(6);

        storage.addProduct(new Refrigerator(
                "Insignia NS-CF43SS9 4.3cu ft. Top Freezer storage.Refrigerator",
                65,
                269.99f,
                "Insignia",
                "NS-CF43SS9",
                "Silver",
                "Top Freezer",
                "Opens Left and Right"
        ));
        storage.addProduct(new Refrigerator(
                "LG 20.2-cu ft Top-Freezer storage.Refrigerator (White) ENERGY STAR",
                74,
                300,
                "LG",
                "20.2-cu",
                "White",
                "Built-in Top Freezer storage.Refrigerator",
                "Top-Freezer"
        ));
        storage.addProduct(new Monitor(
                "Samsung LS34J550WQNXZA 34\" Ultra WQHD Widescreen LCD storage.Monitor",
                5,
                249.99f,
                "Samsung",
                "LS34J550WQNXZA",
                "Black",
                "LCD",
                34,
                75
        ));
        storage.addProduct(new Monitor(
                "Acer ED320QR Sbiipx 31.5\" Full HD Curved storage.Monitor (UM.JE0AA.S01)",
                6.1f,
                249.99f,
                "Acer",
                "Acer ED320QR Sbiipx",
                "Black",
                "Vertically-Aligned panel",
                31.5f,
                165
        ));
        storage.addProduct(new Microwave(
                "Panasonic 2.2 Cu. Ft. Countertop storage.Microwave Oven with Inverter Technology, Black",
                11.5f,
                347.61f,
                "Panasonic",
                "NN-SN936B",
                "Black",
                true
        ));
        storage.addProduct(new SmartWatch(
                "AGPTEK Smart Watch for Android and iOS Phones, Full Touch Fitness Tracker with Heart Rate storage.Monitor, DIY Watch Face and Multiple Sports, IP68 Waterproof Smartwatch Fits for Men Women (LW11, Rose Gold)",
                0.082f,
                41.39f,
                "AGPTEK",
                "LW11",
                "Rose Gold",
                "3 Axis G-sensor"
        ));

        out.println(storage.getInfoOfProducts());
        out.println(storage.getWeightOfProducts());
        out.println("Sum of weight products: " + storage.sumOfWeight());
    }
}
