package me.tretyakovv.p3_lesson8.services.impl;

import me.tretyakovv.p3_lesson8.services.FilesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FilesServiceImpl implements FilesService {

    @Value("${path.to.data.files}")
    private String dataFilePath;

    @Override
    public boolean saveToFile(String json, String dataFileName) throws ExceptionService{
        try {
            cleanDataFile(dataFileName);
            Files.writeString(Path.of(dataFilePath, dataFileName), json);
            return true;
        } catch (IOException e) {
            throw new ExceptionService("Ошибка записи в файл - " + dataFileName + "!");
        }
    }

    @Override
    public String readFromFile(String dataFileName) throws ExceptionService {
        try {
            return Files.readString(Path.of(dataFilePath, dataFileName));
        } catch (IOException e) {
            throw new ExceptionService("Ошибка чтения файла - " + dataFileName + "!");
        }
    }

    @Override
    public boolean cleanDataFile(String dataFileName) {
        try {
            Path path = Path.of(dataFilePath, dataFileName);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public File getDataFile(String dataFileName) {
        return new File(dataFilePath + "/" + dataFileName);
    }
}