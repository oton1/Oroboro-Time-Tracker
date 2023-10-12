package org.example.timetracker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class TimeTrackerGUI {
    private JFrame frame;
    private JList<Task> taskList;
    private DefaultListModel<Task> listModel;
    private TaskManager taskManager;

    public TimeTrackerGUI(TaskManager taskManager) {
        this.taskManager = taskManager;
        initialize();
    }

    private void initialize() {
        // Configurações do frame
        frame = new JFrame("Time Tracker");
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);

        // Modo escuro
        frame.getContentPane().setBackground(Color.DARK_GRAY);

        // Configurações dos botões
        JButton btnStart = new JButton("Iniciar Tarefa");
        JButton btnStopSelected = new JButton("Parar Tarefa Selecionada");
        JButton btnLogTask = new JButton("Logar Nova Tarefa");

        btnStart.setForeground(Color.WHITE);
        btnStopSelected.setForeground(Color.WHITE);
        btnLogTask.setForeground(Color.WHITE);

        btnStart.setBackground(Color.GRAY);
        btnStopSelected.setBackground(Color.GRAY);
        btnLogTask.setBackground(Color.GRAY);

        // Configurações da lista de tarefas
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setBackground(Color.DARK_GRAY);
        taskList.setForeground(Color.WHITE);

        // Adicionar componentes ao frame
        frame.getContentPane().add(btnStart);
        frame.getContentPane().add(btnStopSelected);
        frame.getContentPane().add(btnLogTask);
        frame.getContentPane().add(new JScrollPane(taskList));

        // Ações dos botões
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Nome da Tarefa:");
                Task newTask = taskManager.startTask(name, "Categoria Default");
                listModel.addElement(newTask);
            }
        });

        btnStopSelected.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Task selectedTask = taskList.getSelectedValue();
                if (selectedTask != null) {
                    taskManager.stopTask(selectedTask);
                    listModel.removeElement(selectedTask);
                }
            }
        });

        btnLogTask.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Nome da Tarefa:");
                String startTimeStr = JOptionPane.showInputDialog("Hora de início (HH:MM):");
                String endTimeStr = JOptionPane.showInputDialog("Hora de término (HH:MM):");

                Task newTask = new Task(name, "Categoria Default");
                newTask.setTime(startTimeStr, endTimeStr);
                taskManager.logTask(newTask);
                listModel.addElement(newTask);
            }
        });

    }

    public void show() {
        this.frame.setVisible(true);
    }

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        TimeTrackerGUI window = new TimeTrackerGUI(taskManager);
        window.show();
    }
}
