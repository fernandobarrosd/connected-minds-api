package com.fernando.connected_minds_api.utils;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONReader {
    public static <T> T readJSON(Class<T> javaClass, String jsonFile) throws StreamReadException, DatabindException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonFileURL = javaClass
        .getClassLoader()
        .getResource(jsonFile)
        .getFile();

        File file = new File(jsonFileURL);

        return objectMapper.readValue(file, javaClass);

    }   
}