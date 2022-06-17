package ru.senla.autoservice.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static java.lang.System.err;

public class JsonUtil {

    public static final String ROOT_PATH = Paths.get("").toAbsolutePath().toString() +
            "\\autoserviceAdmin\\src\\main\\java\\ru\\senla\\autoservice";
    public static final String JSON_CONFIGURATION_PATH = ROOT_PATH + "\\configuration\\json\\";

    private JsonUtil() {
    }


    public static <T> T importModelFromJsonFile(T model, String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        JavaType javaType = objectMapper.getTypeFactory().constructType(model.getClass());

        try {
            return objectMapper.readValue(new File(path), javaType);
        } catch (IOException e) {
            err.println(e);
            return null;
        }
    }

    public static <T> void exportModelToJsonFile(T model, String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            objectMapper.writeValue(new File(fileName + ".json"), model);
        } catch (IOException e) {
            err.println(e);
        }
    }

    public static <T> List<T> importModelListFromJsonFile(T model, String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        CollectionType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, model.getClass());

        try {
            return objectMapper.readValue(new File(path), javaType);
        } catch (IOException e) {
            err.println(e);
            return Collections.emptyList();
        }
    }

    public static <T> void exportModelListToJsonFile(List<T> modelList, String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            objectMapper.writeValue(new File(fileName + ".json"), modelList);
        } catch (IOException e) {
            err.println(e);
        }
    }

}
