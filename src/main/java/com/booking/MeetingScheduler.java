package main.java.com.booking;


import main.java.com.booking.bo.Meeting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MeetingScheduler {
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static Date officeStart, officeEnd;

    public static void main(String[] args) {
        String input = "0900 1730\n" +
                "2011-03-17 10:17:06 EMP001\n" +
                "2011-03-21 09:00 2\n" +
                "2011-03-16 12:34:56 EMP002\n" +
                "2011-03-21 09:00 2\n" +
                "2011-03-16 09:28:23 EMP003\n" +
                "2011-03-22 14:00 2\n" +
                "2011-03-17 11:23:45 EMP004\n" +
                "2011-03-22 16:00 1\n" +
                "2011-03-15 17:29:12 EMP005\n" +
                "2011-03-21 16:00 3";
        processInput(input);
    }

    private static void processInput(String input) {
        Scanner scanner = new Scanner(input);
        String[] officeHours = scanner.nextLine().split(" ");
        try {
            officeStart = new SimpleDateFormat("HHmm").parse(officeHours[0]);
            officeEnd = new SimpleDateFormat("HHmm").parse(officeHours[1]);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        List<BookingRequest> requests = new ArrayList<>();
        while (scanner.hasNextLine()) {
            try {
                String line1 = scanner.nextLine().trim();
                if (line1.isEmpty()) continue;
                String[] parts = line1.split(" ");
                Date submissionTime = dateTimeFormat.parse(parts[0] + " " + parts[1]);
                String employeeId = parts[2];

                String line2 = scanner.nextLine().trim();
                String[] meetingDetails = line2.split(" ");
                Date meetingStartTime = dateFormat.parse(meetingDetails[0]);
                Calendar meetingStartCal = Calendar.getInstance();
                meetingStartCal.setTime(meetingStartTime);
                String[] meetingStartTimeParts = meetingDetails[1].split(":");
                meetingStartCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(meetingStartTimeParts[0]));
                meetingStartCal.set(Calendar.MINUTE, Integer.parseInt(meetingStartTimeParts[1]));
                int durationHours = Integer.parseInt(meetingDetails[2]);

                Calendar meetingEndCal = (Calendar) meetingStartCal.clone();
                meetingEndCal.add(Calendar.HOUR_OF_DAY, durationHours);

                if (meetingStartCal.get(Calendar.HOUR_OF_DAY) < officeStart.getHours() ||
                        meetingEndCal.get(Calendar.HOUR_OF_DAY) > officeEnd.getHours() ||
                        meetingEndCal.get(Calendar.HOUR_OF_DAY) == officeEnd.getHours() && meetingEndCal.get(Calendar.MINUTE) > officeEnd.getMinutes()) {
                    continue; // Skip meetings outside of office hours
                }

                requests.add(new BookingRequest(submissionTime, employeeId, meetingStartCal.getTime(), durationHours));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Sort requests by submission time
        requests.sort(Comparator.comparing(BookingRequest::getSubmissionTime));

        // Process and schedule requests
        Map<String, List<Meeting>> schedule = new LinkedHashMap<>();
        for (BookingRequest request : requests) {
            scheduleMeeting(request, schedule);
        }

        // Print the schedule
        printSchedule(schedule);
    }

    private static void scheduleMeeting(BookingRequest request, Map<String, List<Meeting>> schedule) {
        String requestDate = dateFormat.format(request.getStartTime());
        List<Meeting> meetingsForDay = schedule.computeIfAbsent(requestDate, k -> new ArrayList<>());

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(request.getStartTime());
        Calendar endCal = (Calendar) startCal.clone();
        endCal.add(Calendar.HOUR_OF_DAY, request.getDurationHours());

        for (Meeting meeting : meetingsForDay) {
            if (startCal.getTime().before(meeting.getEndTime()) && endCal.getTime().after(meeting.getStartTime())) {
                return; // Overlaps with an existing meeting
            }
        }

        meetingsForDay.add(new Meeting(request.getStartTime(), endCal.getTime(), request.getEmployeeId()));
        meetingsForDay.sort(Comparator.comparing(Meeting::getStartTime));
    }

    private static void printSchedule(Map<String, List<Meeting>> schedule) {
        for (Map.Entry<String, List<Meeting>> entry : schedule.entrySet()) {
            System.out.println(entry.getKey());
            for (Meeting meeting : entry.getValue()) {
                System.out.println(timeFormat.format(meeting.getStartTime()) + " " + timeFormat.format(meeting.getEndTime()) + " " + meeting.getEmployeeId());
            }
        }
    }
}

class BookingRequest{
    private Date submissionTime;
    private String employeeId;
    private Date startTime;
    private int durationHours;

    public BookingRequest(Date submissionTime, String employeeId, Date startTime, int durationHours) {
        this.submissionTime = submissionTime;
        this.employeeId = employeeId;
        this.startTime = startTime;
        this.durationHours = durationHours;
    }


    public Date getSubmissionTime() {
        return submissionTime;
    }


    public String getEmployeeId() {
        return employeeId;
    }


    public Date getStartTime() {
        return startTime;
    }


    public int getDurationHours() {
        return durationHours;
    }
}


