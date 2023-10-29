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
        System.out.println("Constructor: Setting initial values");
        this.name = name;
        this.date = date;
        this.isRunning = false;
        System.out.println("Constructor: startTime = " + this.startTime);
        System.out.println("Constructor: totalElapsedTime = " + this.totalElapsedTime);
    }


    public void start() {
        this.startTime = System.currentTimeMillis();
        this.isRunning = true;
    }

    public void stop() {
        this.endTime = System.currentTimeMillis();
        this.totalElapsedTime += (this.endTime - this.startTime);
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
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        long currentEndTime = isRunning ? System.currentTimeMillis() : this.endTime;
        long timeOffsetMillis = 3 * 60 * 60 * 1000; // 3 horas em milissegundos

        try {
            Date startDate = sdf.parse(sdf.format(new Date(this.startTime - timeOffsetMillis)));
            Date endDate = sdf.parse(sdf.format(new Date(currentEndTime - timeOffsetMillis)));

            long elapsedTime = endDate.getTime() - startDate.getTime();

            return elapsedTime / 60000;

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
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
            // Handle start time
            String[] startParts = startTimeStr.split(":");
            String startHour = (startParts[0].length() == 1) ? "0" + startParts[0] : startParts[0];
            String startMinute = (startParts.length > 1) ? startParts[1] : "00";
            startTimeStr = startHour + ":" + startMinute;
            Date startDate = sdf.parse(startTimeStr);
            this.startTime = startDate.getTime();

            // Handle end time only if it's not null or empty
            if (endTimeStr != null && !endTimeStr.isEmpty()) {
                String[] endParts = endTimeStr.split(":");
                String endHour = (endParts[0].length() == 1) ? "0" + endParts[0] : endParts[0];
                String endMinute = (endParts.length > 1) ? endParts[1] : "00";
                endTimeStr = endHour + ":" + endMinute;
                Date endDate = sdf.parse(endTimeStr);
                this.endTime = endDate.getTime();
                this.totalElapsedTime = this.endTime - this.startTime;
                this.isRunning = false;
            } else {
                this.endTime = 0;  // or some other default value to indicate "in progress"
                this.isRunning = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    public long getStartTime() {
        return this.startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }
}
