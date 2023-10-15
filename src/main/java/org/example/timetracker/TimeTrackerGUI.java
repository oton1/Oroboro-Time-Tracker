package org.example.timetracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimeTrackerGUI {
    private JFrame frame;
    private JList<Task> taskList;
    private DefaultListModel<Task> listModel;
    private TaskManager taskManager;
    private LocalDate currentDate;
    private JLabel dateLabel;

    public TimeTrackerGUI(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.listModel = new DefaultListModel<>();
        this.currentDate = LocalDate.now();
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
        Font modernFont = new Font("Helvetica", Font.PLAIN, 16);

        frame = new JFrame("Gerenciador de exploração ☭");  // Símbolo de foice e martelo adicionado
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(bgColor);

        JPanel topPanel = new JPanel(new BorderLayout());  // Alterado para BorderLayout
        topPanel.setBackground(bgColor);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(bgColor);

        taskList = new JList<>(listModel);
        taskList.setBackground(bgColor);
        taskList.setForeground(fgColor);
        taskList.setFont(modernFont);
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setPreferredSize(new Dimension(550, 150));

        JButton startButton = new JButton("Iniciar tarefa");
        JButton stopButton = new JButton("Parar tarefa");
        JButton logButton = new JButton("Logar tarefa");
        JButton deleteButton = new JButton("Deletar tarefa");
        JButton backButton = new JButton("<");
        JButton forwardButton = new JButton(">");
        dateLabel = new JLabel(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Font dateFont = new Font("Helvetica", Font.PLAIN, 16);
        dateLabel.setFont(dateFont);
        dateLabel.setForeground(Color.WHITE);  // Mudar a cor para branco


        for (JButton btn : new JButton[]{startButton, stopButton, logButton, deleteButton}) {
            btn.setFont(modernFont);
            btn.setBackground(bgColor);
            btn.setForeground(fgColor);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(bgColor);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(logButton);
        buttonPanel.add(deleteButton);

        backButton.setForeground(Color.WHITE);
        backButton.setBackground(bgColor);
        forwardButton.setForeground(Color.WHITE);
        forwardButton.setBackground(bgColor);

        JPanel dateNavigationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dateNavigationPanel.setBackground(bgColor);
        dateNavigationPanel.add(backButton);
        dateNavigationPanel.add(dateLabel);
        dateNavigationPanel.add(forwardButton);

        topPanel.add(dateNavigationPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(scrollPane);

        frame.add(mainPanel);

        startButton.addActionListener((ActionEvent e) -> {
            String name = JOptionPane.showInputDialog("Nome da Tarefa:");
            LocalDate date = LocalDate.now();
            Task newTask = taskManager.startTask("Nome da Tarefa", date);
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
                taskManager.deleteTask(selectedTask, currentDate);
                listModel.removeElement(selectedTask);
            }
        });
        logButton.addActionListener((ActionEvent e) -> {
            String name = JOptionPane.showInputDialog("Nome da Tarefa:");
            String startTimeStr;
            String endTimeStr;

            while (true) {
                startTimeStr = JOptionPane.showInputDialog("Hora de início (HH:MM):");
                if (startTimeStr == null) break;  // User cancelled
                if (isValidTimeFormat(startTimeStr)) break;
                JOptionPane.showMessageDialog(frame, "Apenas horas são permitidas nesse campo. Formato: HH ou HH:mm");
            }

            if (startTimeStr == null) return;  // User cancelled

            while (true) {
                endTimeStr = JOptionPane.showInputDialog("Hora de término (HH:MM):");
                if (endTimeStr == null) break;  // User cancelled
                if (isValidTimeFormat(endTimeStr)) break;
                JOptionPane.showMessageDialog(frame, "Apenas horas são permitidas nesse campo. Formato: HH ou HH:mm");
            }

            if (endTimeStr == null) return;  // User cancelled

            Task newTask = new Task(name, currentDate);
            newTask.setTime(startTimeStr, endTimeStr);
            taskManager.logTask(newTask, currentDate);
            listModel.addElement(newTask);
        });
        backButton.addActionListener(e -> {
            currentDate = currentDate.minusDays(1);
            updateDateAndTasks();
        });

        forwardButton.addActionListener(e -> {
            currentDate = currentDate.plusDays(1);
            updateDateAndTasks();
        });

        dateNavigationPanel.setBackground(bgColor);
        dateNavigationPanel.add(backButton);
        dateNavigationPanel.add(dateLabel);
        dateNavigationPanel.add(forwardButton);

        topPanel.add(dateNavigationPanel, BorderLayout.WEST);

    }
    private void updateDateAndTasks() {
        dateLabel.setText(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        listModel.clear();
        for (Task task : taskManager.getTasksByDate(currentDate)) {
            listModel.addElement(task);
        }
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
    private boolean isValidTimeFormat(String timeStr) {
        if (timeStr == null) return false;

        String[] parts = timeStr.split(":");
        if (parts.length > 2) return false;

        try {
            int hour = Integer.parseInt(parts[0]);
            if (hour < 0 || hour > 23) return false;

            if (parts.length > 1) {
                int minute = Integer.parseInt(parts[1]);
                if (minute < 0 || minute > 59) return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
