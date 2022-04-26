package autoservice.util;

import autoservice.controller.GarageController;
import autoservice.controller.MasterController;
import autoservice.controller.OrderController;
import autoservice.repository.model.Garage;
import autoservice.repository.model.Master;
import autoservice.repository.model.Order;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

public class JsonUtil {

    private JsonUtil() {}

    public static void exportOrderToJsonFile(Order order, String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.writeValue(new File(fileName + ".json"), order);
    }

    public static void importOrderFromJsonFile(String path) throws IOException {
        OrderController orderController = OrderController.getInstance();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JsonNode orderJsonNode = objectMapper.readTree(new File(path));
        Order orderJson = objectMapper.readValue(new File(path), Order.class);

        Order orderByJsonId = orderController.getById(orderJsonNode.get("id").asLong());

        if (orderByJsonId != null) {
            orderController.update(orderByJsonId, orderJson);
        } else {
            orderController.add(orderJson);
        }
    }

    public static void exportMasterToJsonFile(Master master, String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(fileName + ".json"), master);
    }

    public static void importMasterFromJsonFile(String path) throws IOException {
        MasterController masterController = MasterController.getInstance();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode masterJsonNode = objectMapper.readTree(new File(path));
        Master masterJson = objectMapper.readValue(new File(path), Master.class);

        Master masterByJsonId = masterController.getById(masterJsonNode.get("id").asLong());

        if (masterByJsonId != null) {
            masterController.update(masterByJsonId, masterJson);
        } else {
            masterController.add(masterJson);
        }
    }

    public static void exportGarageToJsonFile(Garage garage, String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(fileName + ".json"), garage);
    }

    public static void importGarageFromJsonFile(String path) throws IOException {
        GarageController garageController = GarageController.getInstance();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode garageJsonNode = objectMapper.readTree(new File(path));
        Garage garageJson = objectMapper.readValue(new File(path), Garage.class);

        Garage garageByJsonId = garageController.getById(garageJsonNode.get("id").asLong());

        if (garageByJsonId != null) {
            garageController.update(garageByJsonId, garageJson);
        } else {
            garageController.add(garageJson);
        }
    }

}
