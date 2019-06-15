package com.philipbui.libcal.responses;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("WeakerAccess")
public class SpaceNicknameTest {

    @Test
    public void testJackson() throws IOException {
        SpaceNicknameResponse[] spaceNicknameResponses = new ObjectMapper()
                .readValue(getClass().getClassLoader().getResourceAsStream("space-nicknames.json"), SpaceNicknameResponse[].class);
        assertFalse(spaceNicknameResponses.length == 0);
        assertFalse(spaceNicknameResponses[0].getCategories().length == 0);
        assertFalse(spaceNicknameResponses[0].getCategories()[0].getSpaces().length == 0);
        assertNotNull(spaceNicknameResponses[0].getCategories()[0].getSpaces()[0].getName());
    }
}
