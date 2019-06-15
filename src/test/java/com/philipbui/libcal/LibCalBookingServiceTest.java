package com.philipbui.libcal;

import com.philipbui.libcal.responses.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("WeakerAccess")
public class LibCalBookingServiceTest {

    public static final String host = "https://usyd.libcal.com";
    private LibCalBookingService libCalBookingService;
    static final int testLocation = 6401;
    static final int testCategory = 11319;
    static final int testSpace = 43849;

    @BeforeEach
    void setUp() {
        String clientID = System.getenv("LIBCAL_CLIENT_ID");
        String clientSecret = System.getenv("LIBCAL_CLIENT_SECRET");
        if (StringUtils.isBlank(clientID) || StringUtils.isBlank(clientSecret)) {
            return;
        }
        libCalBookingService = new LibCalBookingService(host, new LibCalClient(), clientID, clientSecret);
    }

    private static Date getStartDate() {
        long current = new Date().getTime();
        // Set minutes and seconds to 00
        return new Date(current + TimeUnit.DAYS.toMillis(1) - (current % 3600000));
    }

    private static Date getEndDate(Date start) {
        return new Date(start.getTime() + TimeUnit.HOURS.toMillis(1));
    }

    @Test
    public void testGetLocations() throws IOException {
        if (libCalBookingService == null) {
            return;
        }
        String authorization = libCalBookingService.getAccessToken();
        List<SpaceLocation> bookableLocations = libCalBookingService.getBookableLocations(authorization);
        assertNotNull(bookableLocations);
        assertFalse(bookableLocations.size() == 0);
    }

    /**
     * Checks Booking Availability
     *
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testBookingAvailability() throws IOException, ParseException {
        if (libCalBookingService == null) {
            return;
        }
        SpaceNicknameResponse[] spaceNicknameResponses = libCalBookingService.getLibCalClient().getMapper().readValue(getClass().getClassLoader().getResourceAsStream("space-nickname-collisions.json"), SpaceNicknameResponse[].class);
        SpaceNickname.Space space = spaceNicknameResponses[0].getCategories()[0].getSpaces()[0];

        String start = "2019-08-06 18:30:00";
        String end = "2019-08-06 19:00:00";

        Date startDate = LibCalBookingService.dateFormat.parse(start);
        Date endDate = LibCalBookingService.dateFormat.parse(end);
        assertTrue(space.isBookable(startDate, endDate));

        start = "2019-08-06 18:00:00";
        end = "2019-08-06 18:30:00";

        startDate = LibCalBookingService.dateFormat.parse(start);
        endDate = LibCalBookingService.dateFormat.parse(end);
        assertFalse(space.isBookable(startDate, endDate));

        start = "2019-08-06 19:00:00";
        end = "2019-08-06 19:30:00";

        startDate = LibCalBookingService.dateFormat.parse(start);
        endDate = LibCalBookingService.dateFormat.parse(end);
        assertFalse(space.isBookable(startDate, endDate));
    }

    @Test
    public void testGetCategoriesAndItemDescriptionsForLocations() throws IOException {
        if (libCalBookingService == null) {
            return;
        }
        String authorization = libCalBookingService.getAccessToken();

        List<SpaceLocation> spaceLocations = libCalBookingService.getBookableLocations(authorization);
        Map<Integer, SpaceLocationCategory[]> locationCategories = libCalBookingService.getCategoriesForSpaceLocations(spaceLocations, authorization);
        assertFalse(spaceLocations.isEmpty());
        for (Map.Entry<Integer, SpaceLocationCategory[]> entry : locationCategories.entrySet()) {
            Map<Integer, SpaceItemDescription[]> spaceItemDescriptions = libCalBookingService.getItemDescriptionsForSpaceLocationCategories(entry.getValue(), authorization);
            assertFalse(spaceItemDescriptions.isEmpty());
        }
    }

    @Test
    public void testCreateBooking() throws IOException {
        if (libCalBookingService == null) {
            return;
        }
        String authorization = libCalBookingService.getAccessToken();

        Date start = getStartDate();
        Date end = getEndDate(start);

        String responseBody = libCalBookingService.bookForSpace(testSpace, authorization,
                start, end, "Philip", "Bui", "philip.bui@sydney.edu.au", true);
        assertNull(responseBody);
    }

    @Test
    public void parseDate() throws ParseException {
        String dateString = "2019-08-06 11:00:00";
        Date date = LibCalBookingService.dateFormat.parse(dateString);
        assertEquals(dateString, LibCalBookingService.dateFormat.format(date));
    }
    //TODO: Test Full Bookings.
}
