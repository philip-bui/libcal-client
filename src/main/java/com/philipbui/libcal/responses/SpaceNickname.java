package com.philipbui.libcal.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.philipbui.libcal.LibCalClient;

import java.text.ParseException;
import java.util.Date;
import java.util.TreeMap;

@SuppressWarnings({"unused", "WeakerAccess"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpaceNickname {

    private final int categoryID;
    private final Space[] spaces;

    @JsonCreator
    public SpaceNickname(
            @JsonProperty("cid") int categoryID,
            @JsonProperty("spaces") Space[] spaces) {
        this.categoryID = categoryID;
        this.spaces = spaces;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public Space[] getSpaces() {
        return spaces;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Space {

        private final int id;
        private final String name;
        private final Booking[] bookings;

        private final TreeMap<Long, Long> bookingsRanges;

        @JsonCreator
        public Space(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("bookings") Booking[] bookings) {
            this.id = id;
            this.name = name;
            this.bookings = bookings;
            bookingsRanges = new TreeMap<>();
            for (Booking booking : bookings) {
                bookingsRanges.put(booking.start.getTime(), booking.end.getTime());
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Booking {

            private final Date start;
            private final Date end;

            @JsonCreator
            public Booking(@JsonProperty("start") String start, @JsonProperty("end") String end) throws ParseException {
                this.start = LibCalClient.dateFormat.parse(start);
                this.end = LibCalClient.dateFormat.parse(end);
            }
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Booking[] getBookings() {
            return bookings;
        }

        public boolean isBookable(Date start, Date end) {
            Long bookingBefore = bookingsRanges.floorKey(end.getTime() - 1);
            // No bookings before || last previous booking before start.
            return bookingBefore == null || bookingsRanges.get(bookingBefore) < (start.getTime() + 1);
        }
    }

    public int getBookableSpaceID(Date start, Date end) {
        if (spaces == null || spaces.length == 0) {
            return 0;
        }
        for (Space space : getSpaces()) {
            if (space.isBookable(start, end)) {
                return space.getId();
            }
        }
        return 0;
    }
}
