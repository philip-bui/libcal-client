package com.philipbui.libcal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.philipbui.libcal.responses.*;
import com.philipbui.libcal.utils.ArrayUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class LibCalClient {

    private static final Logger logger = LogManager.getLogger(LibCalClient.class);
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final CloseableHttpClient client;
    private final ObjectMapper mapper;
    private final static String OAUTH = "1.1/oauth/token";
    private final static String LOCATIONS = "1.1/space/locations";
    private final static String CATEGORIES = "1.1/space/categories/";
    private final static String CATEGORY_ITEMS = "1.1/space/category/";
    private final static String ITEMS = "1.1/space/item/";
    private final static String NICKNAME = "1.1/space/nickname/";
    private final static String RESERVE = "1.1/space/reserve";

    public LibCalClient() {
        this(HttpClientBuilder.create().build());
    }

    private LibCalClient(CloseableHttpClient client) {
        this.client = client;
        mapper = new ObjectMapper();
    }

    AccessTokenResponse getAccessToken(String host, String clientID, String clientSecret) throws IOException {
        String uri = host + "/" + OAUTH;
        HttpPost request = new HttpPost(uri);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        List<NameValuePair> urlEncodedForm = new ArrayList<>(3);
        urlEncodedForm.add(new BasicNameValuePair("grant_type", "client_credentials"));
        urlEncodedForm.add(new BasicNameValuePair("client_id", clientID));
        urlEncodedForm.add(new BasicNameValuePair("client_secret", clientSecret));
        request.setEntity(new UrlEncodedFormEntity(urlEncodedForm, "UTF-8"));
        try (CloseableHttpResponse response = getClient().execute(request)) {
            logger.info("GET /" + uri);
            return getMapper().readValue(response.getEntity().getContent(), AccessTokenResponse.class);
        } catch (Exception e) {
            throw new IOException("Error getting Access Token GET /" + uri, e);
        }
    }

    SpaceLocation[] getSpaceLocations(String host, String authorization) throws IOException {
        String uri = host + "/" + LOCATIONS;
        HttpGet request = new HttpGet(uri);
        request.setHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", authorization));
        try (CloseableHttpResponse response = getClient().execute(request)) {
            logger.info("GET /" + uri);
            return getMapper().readValue(response.getEntity().getContent(), SpaceLocation[].class);
        } catch (Exception e) {
            throw new IOException("Error getting Space Locations GET /" + uri, e);
        }
    }

    SpaceLocationCategoryResponse[] getSpaceLocationCategories(String host, String authorization, int... categoryIDs) throws IOException {
        String uri = host + "/" + CATEGORIES + ArrayUtils.join(categoryIDs, ",");
        HttpGet request = new HttpGet(uri);
        request.setHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", authorization));
        try (CloseableHttpResponse response = getClient().execute(request)) {
            logger.info("GET /" + uri);
            return getMapper().readValue(response.getEntity().getContent(), SpaceLocationCategoryResponse[].class);
        } catch (Exception e) {
            throw new IOException("Error getting Space Categories GET /" + uri, e);
        }
    }

    SpaceCategoryItemResponse[] getSpaceCategoryItems(String host, String authorization, int... categoryIDs) throws IOException {
        String uri = host + "/" + CATEGORY_ITEMS + ArrayUtils.join(categoryIDs, ",");
        HttpGet request = new HttpGet(uri);
        request.setHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", authorization));
        try (CloseableHttpResponse response = getClient().execute(request)) {
            logger.info("GET /" + uri);
            return getMapper().readValue(response.getEntity().getContent(), SpaceCategoryItemResponse[].class);
        } catch (Exception e) {
            throw new IOException("Error getting Space Category Items GET /" + uri, e);
        }
    }

    SpaceItemDescription[] getSpaceItemDescriptions(String host, String authorization, int... itemIDs) throws IOException {
        String uri = host + "/" + ITEMS + ArrayUtils.join(itemIDs, ",");
        HttpGet request = new HttpGet(uri);
        request.setHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", authorization));
        try (CloseableHttpResponse response = getClient().execute(request)) {
            logger.info("GET /" + uri);
            return getMapper().readValue(response.getEntity().getContent(), SpaceItemDescription[].class);
        } catch (Exception e) {
            throw new IOException("Error getting Space Category Items GET /" + uri, e);
        }
    }

    SpaceNicknameResponse[] getSpaceCategoryBookings(String host, String authorization, Date start, int... categoryIDs) throws IOException {
        String uri = host + "/" + NICKNAME + "/" + ArrayUtils.join(categoryIDs, ",") + "?date=" + dayFormat.format(start);
        HttpGet request = new HttpGet(uri);
        request.setHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", authorization));
        try (CloseableHttpResponse response = getClient().execute(request)) {
            logger.info("GET /" + uri);
            return getMapper().readValue(response.getEntity().getContent(), SpaceNicknameResponse[].class);
        } catch (Exception e) {
            throw new IOException("Error getting Space Categories Bookings GET /" + uri, e);
        }
    }

    String reserveSpace(String host, ReserveRequest body, String authorization) throws IOException {
        String uri = host + "/" + RESERVE;
        HttpPost request = new HttpPost(uri);
        String requestBody = getMapper().writeValueAsString(body);
        request.setEntity(new StringEntity(requestBody));
        request.setHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", authorization));
        try (CloseableHttpResponse response = getClient().execute(request)) {
            logger.info("POST /" + uri);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                String responseBody = getMapper().readValue(response.getEntity().getContent(), String.class);
                logger.error("Invalid request " + requestBody + "\n" + responseBody);
                return responseBody;
            }
            return null;
        } catch (Exception e) {
            throw new IOException("Error reserving Space /" + uri, e);
        }
    }

    private CloseableHttpClient getClient() {
        return client;
    }

    ObjectMapper getMapper() {
        return mapper;
    }
}