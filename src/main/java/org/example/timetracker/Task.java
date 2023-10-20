package org.example.timetracker;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.ZoneId;
import java.util.TimeZone;


public class Task implements Serializable {
   // @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private LocalDate date;
    private long startTime;
    private Long endTime;
    private long totalElapsedTime;
    private boolean isRunning;

    public Task(String name, LocalDate date) {
        this.name = name;
        this.date = date;
        this.totalElapsedTime = 0;
        this.isRunning = true;
        this.startTime = System.currentTimeMillis();
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.isRunning = true;
        this.totalElapsedTime = 0; // Reset totalElapsedTime

    }

    public void stop() {
        if (!isRunning) {
            return; // Ignora se a tarefa não estiver rodando
        }
        this.endTime = System.currentTimeMillis();
        long elapsed = this.endTime - this.startTime;
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
        if (isRunning && startTime > 0) {
            elapsedTime += System.currentTimeMillis() - startTime;
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
        return this.isRunning;
    }
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = date.format(formatter);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String startTimeStr = sdf.format(new Date(startTime));
        String endTimeStr = (endTime == null) ? "Em andamento" : (isRunning ? "Agora" : sdf.format(new Date(endTime)));
        System.out.println("Start Time Str: " + startTimeStr); // Logging
        System.out.println("End Time Str: " + endTimeStr); // Logging
        long elapsedTimeInMinutes = getElapsedTimeInMinutes();
        System.out.println("Total Elapsed Time in millis: " + totalElapsedTime);  // Debugging
        System.out.println("Elapsed Time in minutes: " + elapsedTimeInMinutes);  // Debugging
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
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));  // Set to UTC so it considers only time

        try {
            Date startDate = sdf.parse(startTimeStr);
            Date endDate = sdf.parse(endTimeStr);

            long currentDayStart = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

            this.startTime = currentDayStart + (startDate.getTime() + TimeZone.getTimeZone("UTC").getRawOffset());
            this.endTime = currentDayStart + (endDate.getTime() + TimeZone.getTimeZone("UTC").getRawOffset());

            this.totalElapsedTime = this.endTime - this.startTime;

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }




    public long getStartTime() {
        return this.startTime;
    }

    public Long getEndTime() {
        return this.endTime;
    }
}
