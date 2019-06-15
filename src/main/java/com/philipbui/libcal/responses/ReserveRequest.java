package com.philipbui.libcal.responses;

import com.philipbui.libcal.LibCalClient;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class ReserveRequest {

    private final String start;
    private final String fname;
    private final String lname;
    private final String email;
    private final String nickname;
    private final Booking[] bookings;
    private final boolean test;

    public ReserveRequest(Date start, String fname, String lname, String email, int spaceID, Date end, boolean test) {
        this.start = LibCalClient.dateFormat.format(new Date(start.getTime() + TimeUnit.HOURS.toMillis(10))).replace("Z", "+10:00");
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.nickname = String.format("%s %s - myUni Auto Booking", fname, lname);
        this.bookings = new Booking[]{new Booking(spaceID, end)};
        this.test = test;
    }

    public static class Booking {

        private final int id;
        private final String to;

        Booking(int spaceID, Date to) {
            this.id = spaceID;
            this.to = LibCalClient.dateFormat.format(new Date(to.getTime() + TimeUnit.HOURS.toMillis(10))).replace("Z", "+10:00");
        }

        public int getId() {
            return id;
        }

        public String getTo() {
            return to;
        }
    }

    public String getStart() {
        return start;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public Booking[] getBookings() {
        return bookings;
    }

    public boolean isTest() {
        return test;
    }
}
