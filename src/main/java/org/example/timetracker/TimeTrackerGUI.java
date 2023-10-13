package org.example.timetracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimeTrackerGUI {
    private JFrame frame;
    private JList<Task> taskList;
    private static DefaultListModel<Task> listModel;
    private TaskManager taskManager;

    public TimeTrackerGUI(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.listModel = new DefaultListModel<>();
        initialize();

        // Limpar listModel para evitar duplicatas
        listModel.clear();

        // Carregar tarefas salvas anteriormente
        for (Task task : taskManager.getActiveTasks()) {
            listModel.addElement(task);
        }
        for (Task task : taskManager.getTaskHistory()) {
            listModel.addElement(task);
        }
    }

    private void initialize() {
        frame = new JFrame("Time Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.DARK_GRAY);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.GRAY);
        JLabel titleLabel = new JLabel("Time Tracker");
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.DARK_GRAY);

        taskList = new JList<>(listModel);
        taskList.setBackground(Color.GRAY);
        taskList.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setPreferredSize(new Dimension(550, 200));

        JButton startButton = new JButton("Start Task");
        styleButton(startButton);

        JButton stopButton = new JButton("Stop Task");
        styleButton(stopButton);

        JButton logButton = new JButton("Log Task");
        styleButton(logButton);

        JButton deleteButton = new JButton("Delete Task");
        styleButton(deleteButton);

        centerPanel.add(startButton);
        centerPanel.add(stopButton);
        centerPanel.add(logButton);
        centerPanel.add(deleteButton);
        centerPanel.add(scrollPane);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        frame.add(mainPanel);

        startButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Nome da Tarefa:");
            Task newTask = taskManager.startTask(name);
            listModel.addElement(newTask);
        });

        stopButton.addActionListener(e -> {
            Task selectedTask = taskList.getSelectedValue();
            if (selectedTask != null) {
                taskManager.stopTask(selectedTask);
                listModel.removeElement(selectedTask);
                listModel.addElement(selectedTask);
            }
        });

        deleteButton.addActionListener(e -> {
            Task selectedTask = taskList.getSelectedValue();
            if (selectedTask != null) {
                taskManager.deleteTask(selectedTask);
                listModel.removeElement(selectedTask);
            }
        });

        logButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Nome da Tarefa:");
            String startTimeStr = JOptionPane.showInputDialog("Hora de início (HH:MM):");
            String endTimeStr = JOptionPane.showInputDialog("Hora de término (HH:MM):");
            Task newTask = new Task(name);
            newTask.setTime(startTimeStr, endTimeStr);
            taskManager.logTask(newTask);
            listModel.addElement(newTask);
        });
    }

    private void styleButton(JButton button) {
        button.setBackground(Color.GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        TimeTrackerGUI gui = new TimeTrackerGUI(taskManager);  // Inicializa listModel

        // Limpar listModel para evitar duplicatas
        listModel.clear();

        // Carregar tarefas salvas anteriormente
        for (Task task : taskManager.getActiveTasks()) {
            listModel.addElement(task);
        }
        for (Task task : taskManager.getTaskHistory()) {
            listModel.addElement(task);
        }

        gui.show();

        // Adicionar hook de desligamento para salvar tarefas
        Runtime.getRuntime().addShutdownHook(new Thread(taskManager::saveTasks));
    }

}
