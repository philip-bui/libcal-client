package com.philipbui.libcal.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpaceLocation {

    private final int lid;
    private final String name;
    private final boolean publicity;

    @JsonCreator
    public SpaceLocation(@JsonProperty("lid") int lid, @JsonProperty("name") String name, @JsonProperty("public") boolean publicity) {
        this.lid = lid;
        this.name = name;
        this.publicity = publicity;
    }

    public int getLid() {
        return lid;
    }

    public String getName() {
        return name;
    }

    public boolean isPublicity() {
        return publicity;
    }
}
