package com.philipbui.libcal.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpaceItemDescription {

    private final int id;
    private final String name;
    private final String description;
    private final String image;
    private final int capacity;
    private final int formID;

    @JsonCreator
    public SpaceItemDescription(
            @JsonProperty("id") int id,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("image") String image,
            @JsonProperty("capacity") int capacity,
            @JsonProperty("formid") int formID) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.capacity = capacity;
        this.formID = formID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFormID() {
        return formID;
    }
}
