package org.example.timetracker;
import java.util.ArrayList;
import java.util.Optional;

public class TaskManager {
    private ArrayList<Task> activeTasks;
    private ArrayList<Task> taskHistory;

    public TaskManager() {
        this.activeTasks = new ArrayList<>();
        this.taskHistory = new ArrayList<>();
    }

    // Iniciar uma nova tarefa
    public Task startTask(String name, String category) {
        Task newTask = new Task(name, category);
        newTask.start();
        activeTasks.add(newTask);
        return newTask;
    }

    // Parar uma tarefa específica
    public void stopTask(Task task) {
        if (task != null && task.isRunning()) {
            task.stop();
            taskHistory.add(task);
            activeTasks.remove(task);
        }
    }

    // Pausar uma tarefa específica
    public void pauseTask(Task task) {
        if (task != null && task.isRunning()) {
            task.pause();
        }
    }

    // Retomar uma tarefa específica
    public void resumeTask(Task task) {
        if (task != null && !task.isRunning()) {
            task.resume();
        }
    }

    // Obter uma tarefa ativa pelo nome
    public Optional<Task> getActiveTaskByName(String name) {
        return activeTasks.stream().filter(task -> task.getName().equals(name)).findFirst();
    }

    // Obter uma lista de todas as tarefas ativas
    public ArrayList<Task> getActiveTasks() {
        return new ArrayList<>(activeTasks);
    }

    // Obter uma lista de todo o histórico de tarefas
    public ArrayList<Task> getTaskHistory() {
        return new ArrayList<>(taskHistory);
    }

    // Obter o tempo total gasto em todas as tarefas em minutos
    public long getTotalTimeSpent() {
        long total = 0;
        for (Task task : taskHistory) {
            total += task.getElapsedTimeInMinutes();
        }
        for (Task task : activeTasks) {
            total += task.getElapsedTimeInMinutes();
        }
        return total;
    }
    public void logTask(Task task) {
        if (task.getElapsedTimeInMinutes() == 0) {
            activeTasks.add(task);
        } else {
            taskHistory.add(task);
        }
    }

}
