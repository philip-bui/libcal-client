package com.philipbui.libcal.responses;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("WeakerAccess")
public class SpaceLocationCategoryTest {

    @Test
    public void testJackson() throws IOException {
        SpaceLocationCategoryResponse[] spaceLocationCategories = new ObjectMapper()
                .readValue(getClass().getClassLoader().getResourceAsStream("space-location-categories.json"), SpaceLocationCategoryResponse[].class);
        assertFalse(spaceLocationCategories.length == 0);
        assertFalse(spaceLocationCategories[0].getCategories().length == 0);
        assertTrue(spaceLocationCategories[0].getCategories()[0].getCategoryID() != 0);
    }
}
