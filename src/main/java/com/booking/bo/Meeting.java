package main.java.com.booking.bo;

import java.util.Date;

public class Meeting {
    private Date startTime;
    private Date endTime;
    private String employeeId;

    public Meeting(Date startTime, Date endTime, String employeeId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.employeeId = employeeId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getEmployeeId() {
        return employeeId;
    }
}