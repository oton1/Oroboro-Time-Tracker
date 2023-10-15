package org.example.timetracker;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Task implements Serializable {
   // @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private LocalDate date;
    private long startTime;
    private long endTime;
    private long totalElapsedTime;
    private boolean isRunning;

    public Task(String name, LocalDate date) {
        this.name = name;
        this.date = date;
        this.totalElapsedTime = 0;
        this.isRunning = false;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.isRunning = true;
    }

    public void stop() {
        this.endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;
        if (elapsed > 0) {
            this.totalElapsedTime += elapsed;
        }
        this.isRunning = false;
    }

    public void pause() {
        this.endTime = System.currentTimeMillis();
        this.totalElapsedTime += (endTime - startTime);
        this.isRunning = false;
    }

    public void resume() {
        this.startTime = System.currentTimeMillis();
        this.isRunning = true;
    }

    public long getElapsedTimeInMinutes() {
        long elapsedTime = this.totalElapsedTime;
        if (isRunning) {
            elapsedTime += (System.currentTimeMillis() - startTime);
        }
        return elapsedTime / 60000;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = date.format(formatter);

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
            timeStr = String.format("%dh %02dmin", hours, minutes);
        }

        return dateStr + " | " + startTimeStr + " - " + endTimeStr + " | " + this.name + " | " + timeStr;
    }

    public void setTime(String startTimeStr, String endTimeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            // Garante que as horas e minutos tenham dois dígitos
            String[] startParts = startTimeStr.split(":");
            String[] endParts = endTimeStr.split(":");

            String startHour = (startParts[0].length() == 1) ? "0" + startParts[0] : startParts[0];
            String startMinute = (startParts.length > 1) ? startParts[1] : "00";

            String endHour = (endParts[0].length() == 1) ? "0" + endParts[0] : endParts[0];
            String endMinute = (endParts.length > 1) ? endParts[1] : "00";

            startTimeStr = startHour + ":" + startMinute;
            endTimeStr = endHour + ":" + endMinute;

            Date startDate = sdf.parse(startTimeStr);
            Date endDate = sdf.parse(endTimeStr);
            this.startTime = startDate.getTime();
            this.endTime = endDate.getTime();
            this.totalElapsedTime = this.endTime - this.startTime;
            this.isRunning = false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
