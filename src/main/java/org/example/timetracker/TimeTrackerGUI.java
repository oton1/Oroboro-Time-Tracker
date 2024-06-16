package org.example.timetracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;


public class TimeTrackerGUI {
    private static JFrame frame;
    private JList<Task> taskList;
    private DefaultListModel<Task> listModel;
    private TaskManager taskManager;
    private LocalDate currentDate;
    private JLabel dateLabel;
    private JLabel taskDetailsLabel;

    private JLabel sumLabel;
    private javax.swing.Timer timer;



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

        frame = new JFrame("Oroboro Time Tracker");  // Símbolo de foice e martelo removido
        ImageIcon icon = new ImageIcon(getClass().getResource("/119445.png"));
        if (icon.getImage() != null) {
            frame.setIconImage(icon.getImage());
        } else {
            System.out.println("Ícone não encontrado");
        }
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
        JButton editButton = new JButton("Editar tarefa");
        editButton.setFont(modernFont);
        editButton.setBackground(bgColor);
        editButton.setForeground(fgColor);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(editButton);

        dateLabel = new JLabel(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Font dateFont = new Font("Helvetica", Font.PLAIN, 16);
        dateLabel.setFont(dateFont);
        dateLabel.setForeground(Color.WHITE);  // Mudar a cor para branco

        sumLabel = new JLabel("Total: 0h 0min");
        sumLabel.setForeground(Color.WHITE);
        sumLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(sumLabel);
        bottomPanel.setBackground(new Color(40, 40, 40));

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);  // Adicione o novo painel na parte inferior

        for (JButton btn : new JButton[]{startButton, stopButton, logButton, deleteButton}) {
            btn.setFont(modernFont);
            btn.setBackground(bgColor);
            btn.setForeground(fgColor);
        }

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(bgColor);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(logButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);

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

        taskList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {  // Ignora eventos extras
                if (taskList.getSelectedIndex() != -1) {
                    timer.stop();  // Pausar o Timer
                } else {
                    timer.start();  // Reiniciar o Timer
                }
            }
        });
        startButton.addActionListener((ActionEvent e) -> {
            String name = JOptionPane.showInputDialog("Nome da Tarefa:");
            LocalDate date = LocalDate.now();
            Task newTask = taskManager.startTask(name, date);
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
        editButton.addActionListener((ActionEvent e) -> {
            Task selectedTask = taskList.getSelectedValue();
            if (selectedTask != null) {
                String newName = JOptionPane.showInputDialog("Novo nome da Tarefa:", selectedTask.getName());
                if (newName != null) {
                    selectedTask.setName(newName);
                }

                String newStartTimeStr = JOptionPane.showInputDialog("Nova hora de início (HH:MM):", new SimpleDateFormat("HH:mm").format(new Date(selectedTask.getStartTime())));
                String newEndTimeStr = JOptionPane.showInputDialog("Nova hora de término (HH:MM):", new SimpleDateFormat("HH:mm").format(new Date(selectedTask.getEndTime())));

                if (newStartTimeStr != null && newEndTimeStr != null) {
                    try {
                        selectedTask.setTime(newStartTimeStr, newEndTimeStr);
                    } catch (Exception ex) {
                        // Tratar exceção
                    }
                }

                // Atualizar a lista após a edição
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
            String startTimeStr = null;
            String endTimeStr = null;

            while (true) {
                startTimeStr = JOptionPane.showInputDialog("Hora de início (HH:MM):");
                if (startTimeStr == null) return; // User cancelled
                if (isValidTimeFormat(startTimeStr)) break;
                JOptionPane.showMessageDialog(frame, "Apenas horas são permitidas nesse campo. Formato: HH ou HH:mm");
            }

            // Permite que endTimeStr seja null ou vazio
            while (true) {
                endTimeStr = JOptionPane.showInputDialog("Hora de término (HH:MM): (Deixe em branco se a tarefa estiver em andamento)");
                if (endTimeStr == null || endTimeStr.isEmpty()) break; // User cancelled or left it empty
                if (isValidTimeFormat(endTimeStr)) break;
                JOptionPane.showMessageDialog(frame, "Apenas horas são permitidas nesse campo. Formato: HH ou HH:mm");
            }

            Task newTask = new Task(name, currentDate);
            newTask.setTime(startTimeStr, endTimeStr); // Assegure-se que seu método setTime possa lidar com endTimeStr sendo null ou vazio
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
        timer = new javax.swing.Timer(1000, e -> updateDateAndTasks());
        timer.start();
        taskList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    Task selectedTask = taskList.getSelectedValue();
                    if (selectedTask != null) {
                        taskManager.deleteTask(selectedTask, currentDate);
                        listModel.removeElement(selectedTask);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    taskList.clearSelection();
                }
            }
        });
    }
    private void updateDateAndTasks() {
        dateLabel.setText(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        listModel.clear();

        long totalMinutes = 0;
        List<Task> tasks = taskManager.getTasksByDate(currentDate);
        Collections.sort(tasks, Comparator.comparingLong(Task::getStartTime));
        for (Task task : tasks) {
            listModel.addElement(task);
            totalMinutes += task.getElapsedTimeInMinutes();
        }
        updateSumLabel(totalMinutes);  // Atualiza o JLabel sumLabel com a soma atual
    }


    private void updateSumLabel(long totalMinutes) {
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        sumLabel.setText(String.format("Total: %dh %dmin", hours, minutes));
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
