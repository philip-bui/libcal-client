package com.philipbui.libcal.responses;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("WeakerAccess")
public class SpaceLocationTest {

    @Test
    public void testJackson() throws IOException {
        SpaceLocation[] spaceLocations = new ObjectMapper()
                .readValue(getClass().getClassLoader().getResourceAsStream("space-locations.json"), SpaceLocation[].class);
        assertFalse(spaceLocations.length == 0);
        assertNotNull(spaceLocations[0].getName());
    }
}
