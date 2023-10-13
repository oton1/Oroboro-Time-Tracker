package org.example.timetracker;
import java.io.Serial;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.io.Serializable;

public class Task implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private String category;
    private long startTime;
    private long endTime;
    private long totalElapsedTime; // em milissegundos
    private boolean isRunning;

    public Task(String name) {
        this.name = name;
        this.category = category;
        this.totalElapsedTime = 0;
        this.isRunning = false;
    }

    // Iniciar a tarefa
    public void start() {
        if (!isRunning) {
            this.startTime = System.currentTimeMillis();
            this.isRunning = true;
        }
    }

    // Parar a tarefa
    public void stop() {
        if (isRunning) {
            this.endTime = System.currentTimeMillis();
            this.totalElapsedTime += (endTime - startTime);
            this.isRunning = false;
        }
    }

    // Pausar a tarefa
    public void pause() {
        if (isRunning) {
            this.endTime = System.currentTimeMillis();
            this.totalElapsedTime += (endTime - startTime);
            this.isRunning = false;
        }
    }

    // Retomar a tarefa
    public void resume() {
        if (!isRunning) {
            this.startTime = System.currentTimeMillis();
            this.isRunning = true;
        }
    }

    // Obter o tempo total gasto na tarefa em minutos
    public long getElapsedTimeInMinutes() {
        long elapsedTime = this.totalElapsedTime;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date currentDate = sdf.parse(sdf.format(new Date()));
            long currentTime = currentDate.getTime();
            if (isRunning) {
                elapsedTime += (currentTime - startTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return elapsedTime / 60000; // Converter para minutos
    }

    // Getters e setters para nome e categoria
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isRunning() {
        return isRunning;
    }
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String startTimeStr = sdf.format(new Date(startTime));
        String endTimeStr = isRunning ? "Agora" : sdf.format(new Date(endTime));
        long elapsedTimeInMinutes = getElapsedTimeInMinutes();
        String timeStr;

        if (elapsedTimeInMinutes < 60) {
            timeStr = elapsedTimeInMinutes + " min";
        } else {
            long hours = elapsedTimeInMinutes / 60;
            long minutes = elapsedTimeInMinutes % 60;
            if (minutes == 0) {
                timeStr = hours + "h";
            } else {
                timeStr = String.format("%dh %02dmin", hours, minutes);
            }
        }

        return startTimeStr + " - " + endTimeStr + " | " + this.name + " | " + timeStr;
    }


    public void setTime(String startTimeStr, String endTimeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        // Verificar e ajustar o formato da hora
        if (!startTimeStr.matches("\\d{2}:\\d{2}")) {
            startTimeStr = "0" + startTimeStr + ":00";
        }
        if (!endTimeStr.matches("\\d{2}:\\d{2}") && !endTimeStr.isEmpty()) {
            endTimeStr = "0" + endTimeStr + ":00";
        }

        try {
            Date startDate = sdf.parse(startTimeStr);
            Date endDate = endTimeStr.isEmpty() ? null : sdf.parse(endTimeStr);

            this.startTime = startDate.getTime();
            if (endDate != null) {
                this.endTime = endDate.getTime();
                this.totalElapsedTime = this.endTime - this.startTime;
                this.isRunning = false;
            } else {
                this.isRunning = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}


