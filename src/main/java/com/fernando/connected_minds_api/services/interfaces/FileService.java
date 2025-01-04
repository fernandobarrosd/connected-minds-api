package com.fernando.connected_minds_api.services.interfaces;

import java.io.FileNotFoundException;

public interface FileService {
    String saveFile(String filename);
    void deleteFile(String filename) throws FileNotFoundException;
}