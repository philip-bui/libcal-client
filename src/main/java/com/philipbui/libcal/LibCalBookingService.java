package com.philipbui.libcal;

import com.philipbui.libcal.responses.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("WeakerAccess")
public class LibCalBookingService {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        dateFormat.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
    }

    private final String host;
    private final LibCalClient libCalClient;
    private final String clientID;
    private final String clientSecret;

    public LibCalBookingService(String host, LibCalClient libCalClient, String clientID, String clientSecret) {
        this.host = host;
        this.libCalClient = libCalClient;
        this.clientID = clientID;
        this.clientSecret = clientSecret;
    }

    public enum AvailabilityType {
        FREE,
        FULL,
        UNAVAILABLE;

        @Override
        public String toString() {
            switch (this) {
                case FREE:
                    return "FREE";
                case FULL:
                    return "FULL";
                case UNAVAILABLE:
                    return "UNAVAILABLE";
                default:
                    throw new IllegalStateException();
            }
        }
    }

    public String getAccessToken() throws IOException {
        return getLibCalClient().getAccessToken(getHost(), getClientID(), getClientSecret()).getAccessToken();
    }

    public List<SpaceLocation> getBookableLocations(String authorization) throws IOException {
        SpaceLocation[] unfilteredSpaceLocations = getLibCalClient().getSpaceLocations(getHost(), authorization);
        return filterSpaceLocations(unfilteredSpaceLocations);
    }

    private List<SpaceLocation> filterSpaceLocations(SpaceLocation[] spaceLocations) {
        List<SpaceLocation> filteredSpaceLocations = new ArrayList<>(spaceLocations.length);
        for (SpaceLocation spaceLocation : spaceLocations) {
            if (!spaceLocation.isPublicity()) {
                continue;
            }
            filteredSpaceLocations.add(spaceLocation);
        }
        return filteredSpaceLocations;
    }

    public Map<Integer, SpaceLocationCategory[]> getCategoriesForSpaceLocations(List<SpaceLocation> spaceLocations, String authorization) throws IOException {
        int[] locationIDs = new int[spaceLocations.size()];
        for (int i = 0; i < locationIDs.length; i++) {
            locationIDs[i] = spaceLocations.get(i).getLid();
        }
        SpaceLocationCategoryResponse[] spaceLocationCategoryResponses = getLibCalClient().getSpaceLocationCategories(getHost(), authorization, locationIDs);
        Map<Integer, SpaceLocationCategory[]> map = new HashMap<>(spaceLocations.size());
        for (SpaceLocationCategoryResponse spaceLocationCategoryResponse : spaceLocationCategoryResponses) {
            SpaceLocationCategory[] spaceLocationCategories = spaceLocationCategoryResponse.getCategories();
            if (spaceLocationCategories == null || spaceLocationCategories.length == 0) {
                continue;
            }
            map.put(spaceLocationCategoryResponse.getLocationID(), spaceLocationCategories);
        }
        return map;
    }

    public Map<Integer, SpaceItemDescription[]> getItemDescriptionsForSpaceLocationCategories(SpaceLocationCategory[] spaceLocationCategories, String authorization) throws IOException {
        int[] categoryIDs = new int[spaceLocationCategories.length];
        for (int i = 0; i < spaceLocationCategories.length; i++) {
            categoryIDs[i] = spaceLocationCategories[i].getCategoryID();
        }
        SpaceCategoryItemResponse[] spaceCategoryItemResponses = getLibCalClient().getSpaceCategoryItems(getHost(), authorization, categoryIDs);
        Map<Integer, SpaceItemDescription[]> spaceItemDescriptions = new HashMap<>(spaceLocationCategories.length);
        for (SpaceCategoryItemResponse spaceCategoryItemResponse : spaceCategoryItemResponses) {
            if (spaceCategoryItemResponse.getItemIDs().length == 0) {
                spaceItemDescriptions.put(spaceCategoryItemResponse.getCategoryID(), new SpaceItemDescription[0]);
                continue;
            }
            spaceItemDescriptions.put(spaceCategoryItemResponse.getCategoryID(),
                    getLibCalClient().getSpaceItemDescriptions(getHost(), authorization, spaceCategoryItemResponse.getItemIDs()));
        }
        return spaceItemDescriptions;
    }

    public List<Integer> getBookableSpaceIDs(int[] categoryIDs, int[] spaceIDs, String authorization, Date start, Date end) throws IOException {
        SpaceNicknameResponse[] responses = getLibCalClient().getSpaceCategoryBookings(getHost(), authorization, start, categoryIDs);
        Set<Integer> spaceIDSet = new HashSet<>();
        for (int spaceID : spaceIDs) {
            spaceIDSet.add(spaceID);
        }
        List<Integer> bookableSpaceIDs = new ArrayList<>(spaceIDs.length);
        for (SpaceNicknameResponse response : responses) {
            for (SpaceNickname nickname : response.getCategories()) {
                for (SpaceNickname.Space space : nickname.getSpaces()) {
                    if (spaceIDSet.contains(space.getId()) && space.isBookable(start, end)) {
                        bookableSpaceIDs.add(space.getId());
                    }
                }
            }
        }
        return bookableSpaceIDs;
    }

    public List<Integer> getBookableSpaceIDs(int[] categoryID, String authorization, Date start, Date end) throws IOException {
        LibCalClient libCalClient = getLibCalClient();
        SpaceNicknameResponse[] spaceNicknameResponses = libCalClient
                .getSpaceCategoryBookings(host, authorization, start, categoryID);
        if (spaceNicknameResponses.length == 0) {
            throw new IOException("Expected non-empty array, found []");
        }
        List<Integer> bookableSpaceIDs = new ArrayList<>(categoryID.length);
        for (SpaceNicknameResponse spaceNicknameResponse : spaceNicknameResponses) {
            for (SpaceNickname spaceNickname : spaceNicknameResponse.getCategories()) {
                int spaceID = spaceNickname.getBookableSpaceID(start, end);
                if (spaceID != 0) {
                    bookableSpaceIDs.add(spaceID);
                    break;
                }
            }
        }
        return bookableSpaceIDs;
    }

    public int getBookableSpaceID(int categoryID, String authorization, Date start, Date end) throws IOException {
        LibCalClient libCalClient = getLibCalClient();
        SpaceNicknameResponse[] spaceNicknameResponses = libCalClient
                .getSpaceCategoryBookings(host, authorization, start, categoryID);
        for (SpaceNicknameResponse spaceNicknameResponse : spaceNicknameResponses) {
            for (SpaceNickname spaceNickname : spaceNicknameResponse.getCategories()) {
                int spaceID = spaceNickname.getBookableSpaceID(start, end);
                if (spaceID != 0) {
                    return spaceID;
                }
            }
        }
        return 0;
    }

    public String bookForSpace(int spaceID, String authorization, Date start, Date end,
                               String firstName, String lastName, String email, boolean isTest) throws IOException {
        LibCalClient libCalClient = getLibCalClient();
        ReserveRequest reserveRequest = new ReserveRequest(start, firstName, lastName, email, spaceID, end, isTest);
        return libCalClient.reserveSpace(host, reserveRequest, authorization);
    }

    LibCalClient getLibCalClient() {
        return libCalClient;
    }

    private String getHost() {
        return host;
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}