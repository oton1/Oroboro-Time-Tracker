package org.example.timetracker;
import java.io.*;
import java.util.ArrayList;

public class TaskStorage {
    public void saveTasks(ArrayList<Task> tasks, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Task> loadTasks(String filePath) {
        ArrayList<Task> tasks;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            tasks = (ArrayList<Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            tasks = new ArrayList<>();
        }
        return tasks;
    }
}


