package com.philipbui.libcal.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpaceLocationCategory {

    private final int categoryID;
    private final String name;

    @JsonCreator
    public SpaceLocationCategory(@JsonProperty("cid") int categoryID,
                                 @JsonProperty("name") String name) {
        this.categoryID = categoryID;
        this.name = name;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getName() {
        return name;
    }
}
