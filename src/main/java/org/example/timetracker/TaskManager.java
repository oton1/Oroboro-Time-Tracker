package org.example.timetracker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class TaskManager {
    private final Map<LocalDate, ArrayList<Task>> tasksByDate;
    private final TaskStorage taskStorage;

    public TaskManager() {
        this.taskStorage = new TaskStorage();
        this.tasksByDate = taskStorage.loadTasks("task_history.dat");
    }

    public void saveTasks() {
        taskStorage.saveTasks(tasksByDate, "task_history.dat");
    }

    public Task startTask(String name, LocalDate date) {
        Task newTask = new Task(name, date);
        newTask.start();
        tasksByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(newTask);
        return newTask;
    }

    public void stopTask(Task task) {
        if (task != null && task.isRunning()) {
            task.stop();
        }
    }

    public void deleteTask(Task task, LocalDate date) {
        ArrayList<Task> tasksForDate = tasksByDate.get(date);
        if (tasksForDate != null) {
            tasksForDate.remove(task);
        }
    }

    public Optional<Task> getActiveTaskByName(String name, LocalDate date) {
        return Optional.ofNullable(tasksByDate.get(date))
                .flatMap(tasks -> tasks.stream().filter(task -> task.getName().equals(name)).findFirst());
    }

    public ArrayList<Task> getTasksByDate(LocalDate date) {
        return tasksByDate.getOrDefault(date, new ArrayList<>());
    }

    public void logTask(Task task, LocalDate date) {
        tasksByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(task);
        saveTasks();
    }

    public long getTotalTimeSpentOnTask(String taskName) {
        long totalElapsedTime = 0;
        for (ArrayList<Task> tasks : tasksByDate.values()) {
            for (Task task : tasks) {
                if (task.getName().equals(taskName)) {
                    totalElapsedTime += task.getElapsedTimeInMinutes();
                }
            }
        }
        return totalElapsedTime;
    }

    public long getTotalTimeSpent() {
        long totalElapsedTime = 0;
        for (ArrayList<Task> tasks : tasksByDate.values()) {
            for (Task task : tasks) {
                totalElapsedTime += task.getElapsedTimeInMinutes();
            }
        }
        return totalElapsedTime;
    }
}
