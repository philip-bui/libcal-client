package com.philipbui.libcal;

import com.philipbui.libcal.responses.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("WeakerAccess")
public class LibCalClientTest {

    private final String host = "https://usyd.libcal.com";
    private LibCalClient client;

    @BeforeEach
    void setUp() {
        client = new LibCalClient();
    }

    private String getAuthorizationToken() throws IOException {
        String clientID = System.getenv("LIBCAL_CLIENT_ID");
        String clientSecret = System.getenv("LIBCAL_CLIENT_SECRET");
        if (StringUtils.isBlank(clientID) || StringUtils.isBlank(clientSecret)) {
            return null;
        }
        return client.getAccessToken(host, clientID, clientSecret).getAccessToken();
    }

    @Test
    public void testGetSpaceLocations() throws IOException {
        String authorization = getAuthorizationToken();
        if (authorization == null) {
            return;
        }
        SpaceLocation[] spaceLocations = client.getSpaceLocations(host, authorization);
        assertFalse(spaceLocations.length == 0);
    }

    @Test
    public void testGetSpaceLocationCategories() throws IOException {
        String authorization = getAuthorizationToken();
        if (authorization == null) {
            return;
        }
        SpaceLocationCategoryResponse[] spaceLocationCategories = client.getSpaceLocationCategories(host, authorization, LibCalBookingServiceTest.testLocation);
        assertFalse(spaceLocationCategories.length == 0);
        assertFalse(spaceLocationCategories[0].getCategories().length == 0);
    }

    @Test
    public void testGetSpaceCategoryItems() throws IOException {
        String authorization = getAuthorizationToken();
        if (authorization == null) {
            return;
        }
        SpaceCategoryItemResponse[] spaceCategoryItems = client.getSpaceCategoryItems(host, authorization, LibCalBookingServiceTest.testCategory);
        assertFalse(spaceCategoryItems.length == 0);
        for (SpaceCategoryItemResponse spaceCategoryItem : spaceCategoryItems) {
            assertFalse(spaceCategoryItem.getCategoryID() == 0);
            assertFalse(spaceCategoryItem.getItemIDs().length == 0);
        }
    }

    @Test
    public void testGetSpaceItemDescriptions() throws IOException {
        String authorization = getAuthorizationToken();
        if (authorization == null) {
            return;
        }
        SpaceItemDescription[] spaceItemDescriptions = client.getSpaceItemDescriptions(host, authorization, LibCalBookingServiceTest.testSpace);
        assertFalse(spaceItemDescriptions.length == 0);
        for (SpaceItemDescription spaceItemDescription : spaceItemDescriptions) {
            assertFalse(spaceItemDescription.getId() == 0);
            assertFalse(spaceItemDescription.getName().isEmpty());
        }
    }

    @Test
    public void testGetSpaceCategoryBookings() throws IOException {
        String authorization = getAuthorizationToken();
        if (authorization == null) {
            return;
        }
        SpaceNicknameResponse[] spaceNicknames = client.getSpaceCategoryBookings(host, authorization, new Date(), LibCalBookingServiceTest.testCategory);
        assertFalse(spaceNicknames.length == 0);
        assertFalse(spaceNicknames[0].getCategories().length == 0);
    }

    @Test
    public void testDateFormat() throws Exception {
        SimpleDateFormat dateFormat = LibCalClient.dateFormat;
        Date date = dateFormat.parse("2019-07-31T15:30:00+10:00");
        assertNotNull(date);
    }
}
