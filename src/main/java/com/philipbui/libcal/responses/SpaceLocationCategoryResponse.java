package com.philipbui.libcal.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpaceLocationCategoryResponse {

    private final int locationID;
    private final SpaceLocationCategory[] categories;

    @JsonCreator
    public SpaceLocationCategoryResponse(
            @JsonProperty("lid") int locationID,
            @JsonProperty("categories") SpaceLocationCategory[] categories) {
        this.locationID = locationID;
        this.categories = categories;
    }

    public int getLocationID() {
        return locationID;
    }

    public SpaceLocationCategory[] getCategories() {
        return categories;
    }
}
