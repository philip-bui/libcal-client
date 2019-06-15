package com.philipbui.libcal.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpaceCategoryItemResponse {

    private final int categoryID;
    private final int[] itemIDs;

    @JsonCreator
    public SpaceCategoryItemResponse(@JsonProperty("cid") int categoryID, @JsonProperty("items") int[] itemIDs) {
        this.categoryID = categoryID;
        this.itemIDs = itemIDs;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public int[] getItemIDs() {
        return itemIDs;
    }
}
