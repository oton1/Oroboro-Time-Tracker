package org.example.timetracker;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskStorage {
    public void saveTasks(Map<LocalDate, ArrayList<Task>> tasksByDate, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(tasksByDate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<LocalDate, ArrayList<Task>> loadTasks(String filePath) {
        Map<LocalDate, ArrayList<Task>> tasksByDate;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            tasksByDate = (Map<LocalDate, ArrayList<Task>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            tasksByDate = new HashMap<>();
        }
        return tasksByDate;
    }
}

