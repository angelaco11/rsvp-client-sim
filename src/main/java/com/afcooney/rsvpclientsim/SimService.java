package com.afcooney.rsvpclientsim;

import com.afcooney.rsvpclientsim.model.Point;
import com.afcooney.rsvpclientsim.model.Resource;
import com.afcooney.rsvpclientsim.model.UrlBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class SimService {

    private final SimConfig simConfig;

    public SimService(SimConfig simConfig) {
        this.simConfig = simConfig;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startSim() {
        readResourceData(simConfig.getResourceFilePath())
                .forEach(resource -> putAtInterval(resource, simConfig.getHost()));
    }

    /**
     * Reads in a JSON file containing tags and converts it to a String array
     * @param resourceDataFilePath - json file containing tags
     * @return result - a String array of tags
     */
    public List<Resource> readResourceData(String resourceDataFilePath) {
        List<Resource> resources = new ArrayList<>();

        try {
            Path path = Paths.get(resourceDataFilePath);
            String fileString = Files.readString(path);

            Type listType = new TypeToken<List<Resource>>(){}.getType();
            resources = new Gson().fromJson(fileString, listType);
            return resources;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resources;
    }

    /**
     * Reads in a JSON file containing tags and converts it to a URLBuilder String
     * @param serverURL - the URL of the server
     * @param point - List of latitude and longitude points for a particular resource
     * @return the URL as a String to POST data to
     */
    public static String buildURL(String serverURL, Point point) {

        long currentTime = new Date().getTime();
        double latitude = point.getLatitude();
        double longitude = point.getLongitude();

        return new UrlBuilder()
                .root(serverURL)
                .path("resource/move")
                .queryParam("latitude", Double.toString(latitude))
                .queryParam("longitude", Double.toString(longitude))
                .queryParam("lastseen", Long.toString(currentTime))
                .build();
    }

    /**
     * Makes a PUT request to a provided URL containing coordinate data and tags
     * @param url - The URL to PUT data
     * @param tags - A String array of tags
     * @return response in string format
     * @throws IOException
     */
    public static String put(String url, List<String> tags) throws IOException {
        String json = new Gson().toJson(tags);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .put(body) //PUT
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * Makes a PUT request at a given resource's interval with a resource's points
     * @param resource - The resource that contains the tags to be passed to the PUT request
     * @param serverURL - The URL to pass to the PUT request
     */
    public static void putAtInterval(Resource resource, String serverURL) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Point point = resource.getPoints().size() == 1 ? resource.getPoints().get(0) : resource.getNextPoint();
                String fullURL = buildURL(serverURL, point);
                System.out.println("LAT: " + point.getLatitude() + " LONG: " + point.getLongitude());
                System.out.println("URL: " + fullURL);
                String putResponse = null;
                try {
                    putResponse = put(fullURL, resource.getTags());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(putResponse);
            }
        };
        new Timer().scheduleAtFixedRate(task, 0,
                resource.getInterval());
    }
}
