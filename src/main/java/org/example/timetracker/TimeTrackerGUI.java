package org.example.timetracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TimeTrackerGUI {
    private JFrame frame;
    private JList<Task> taskList;
    private DefaultListModel<Task> listModel;
    private TaskManager taskManager;

    public TimeTrackerGUI(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.listModel = new DefaultListModel<>();
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Dark Mode Colors
        Color bgColor = new Color(40, 40, 40);
        Color fgColor = new Color(220, 220, 220);

        // Modern Font
        Font modernFont = new Font("Arial", Font.PLAIN, 16);

        frame = new JFrame("Time Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(bgColor);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(bgColor);
        JLabel titleLabel = new JLabel("Time Tracker");
        titleLabel.setForeground(fgColor);
        titleLabel.setFont(modernFont);
        topPanel.add(titleLabel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(bgColor);

        taskList = new JList<>(listModel);
        taskList.setBackground(bgColor);
        taskList.setForeground(fgColor);
        taskList.setFont(modernFont);
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setPreferredSize(new Dimension(550, 150));  // Reduced height

        JButton startButton = new JButton("Start Task");
        JButton stopButton = new JButton("Stop Task");
        JButton logButton = new JButton("Log Task");
        JButton deleteButton = new JButton("Delete Task");

        // Apply modern font and dark mode to buttons
        for (JButton btn : new JButton[]{startButton, stopButton, logButton, deleteButton}) {
            btn.setFont(modernFont);
            btn.setBackground(bgColor);
            btn.setForeground(fgColor);
        }

        // Create a panel for buttons and set layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(bgColor);

        // Add buttons to the button panel
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(logButton);
        buttonPanel.add(deleteButton);

        centerPanel.add(buttonPanel);  // Add button panel to center panel
        centerPanel.add(scrollPane);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        frame.add(mainPanel);

        startButton.addActionListener((ActionEvent e) -> {
            String name = JOptionPane.showInputDialog("Nome da Tarefa:");
            Task newTask = taskManager.startTask(name);
            listModel.addElement(newTask);
        });

        stopButton.addActionListener((ActionEvent e) -> {
            Task selectedTask = taskList.getSelectedValue();
            if (selectedTask != null) {
                taskManager.stopTask(selectedTask);
                listModel.removeElement(selectedTask);
                listModel.addElement(selectedTask);
            }
        });

        deleteButton.addActionListener((ActionEvent e) -> {
            Task selectedTask = taskList.getSelectedValue();
            if (selectedTask != null) {
                taskManager.deleteTask(selectedTask);
                listModel.removeElement(selectedTask);
            }
        });

        logButton.addActionListener((ActionEvent e) -> {
            String name = JOptionPane.showInputDialog("Nome da Tarefa:");
            String startTimeStr = JOptionPane.showInputDialog("Hora de início (HH:MM):");
            String endTimeStr = JOptionPane.showInputDialog("Hora de término (HH:MM):");
            Task newTask = new Task(name);
            newTask.setTime(startTimeStr, endTimeStr);
            taskManager.logTask(newTask);
            listModel.addElement(newTask);
        });
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        TimeTrackerGUI gui = new TimeTrackerGUI(taskManager);

        gui.show();

        Runtime.getRuntime().addShutdownHook(new Thread(taskManager::saveTasks));
    }
}
