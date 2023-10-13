package org.example.timetracker;
import java.io.*;
import java.util.ArrayList;

public class TaskStorage {
    private final String filePath;

    public TaskStorage(String filePath) {
        this.filePath = filePath;
    }

    public void saveTasks(ArrayList<Task> tasks, String s) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Task> loadTasks(String s) {
        ArrayList<Task> tasks;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            tasks = (ArrayList<Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            tasks = new ArrayList<>();
        }
        return tasks;
    }
}

