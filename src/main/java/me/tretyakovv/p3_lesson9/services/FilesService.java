package me.tretyakovv.p3_lesson8.services;

import me.tretyakovv.p3_lesson8.services.impl.ExceptionService;

import java.io.File;

public interface FilesService {

    boolean saveToFile(String json, String dataFileName) throws ExceptionService;

    String readFromFile(String dataFileName) throws ExceptionService;

    boolean cleanDataFile(String dataFileName);

    File getDataFile(String dataFileName);
}