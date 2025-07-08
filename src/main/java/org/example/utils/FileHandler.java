package org.example.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {
    private final String filePath = "./task_tracker_cli/tasks.json";

    public FileHandler(){
        try {
            File dataFile = new File(filePath);
            if(!dataFile.exists()) {
                File dir = dataFile.getParentFile();
                if (dir.mkdir()) {
                    createFile(dataFile);
//                    System.out.println("Directory created: " + dataFile.getName());
                } else {
                    error(new Exception("Failed to create directory"));
                }
            }
        } catch (Exception e) {
            error(e);
        }
    }

    private void createFile(File dataFile) {
        try {
            if (dataFile.createNewFile()) {
//                System.out.println("File created: " + dataFile.getName());
                FileWriter fileWriter = new FileWriter(dataFile);
                fileWriter.write("[\n\n]");
                fileWriter.close();
            }
        } catch (IOException e) {
            error(e);
        }
    }

    public void writeToFile(ArrayList<String> tasks) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write("[\n");
            for (int i = 0; i < tasks.size(); i++){
                fileWriter.write(tasks.get(i));
                if(i != tasks.size() - 1){
                    fileWriter.write(",\n");
                }
            }
            fileWriter.write("\n]");
        } catch (IOException e) {
            error(e);
        }
    }

    public ArrayList<String> getTasks() {
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            ArrayList<String> data = new ArrayList<>();
            while (myReader.hasNextLine()) {
                String dataString = myReader.nextLine();
                if(dataString.isEmpty() || dataString.equals("[") || dataString.equals("]")) {
                   continue;
                }
                dataString = dataString.replace("},", "}");
                data.add(dataString);
            }
            myReader.close();
            return data;
        } catch (IOException e) {
            error(e);
            return null;
        }
    }

    public String getLastTask(){
        try {
            ArrayList<String> data = getTasks();
            if (data.isEmpty()) {
                return null;
            }
            return data.getLast();
        } catch (Exception e){error(e); return null;}
    }

    private void error(Exception e){
        System.out.println("Error: " + this.getClass().getSimpleName());
        System.err.println(e.getMessage());
    }
}
