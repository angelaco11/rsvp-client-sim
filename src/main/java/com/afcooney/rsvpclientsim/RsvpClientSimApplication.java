package com.afcooney.rsvpclientsim;

import com.afcooney.rsvpclientsim.model.Resource;
import com.afcooney.rsvpclientsim.model.Point;
import com.afcooney.rsvpclientsim.model.UrlBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

@SpringBootApplication
public class RsvpClientSimApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RsvpClientSimApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (args.length == 2) {
			String resourceDataFile = args[0];
			String serverURL = args[1];

			// Loops through each resource, updating the same tags at a given interval with looping latitude and
			// longitude values
            readResourceData(resourceDataFile)
                    .forEach(resource -> {
                        putAtInterval(resource, serverURL);
                    });

		} else {
			System.out.println("You do not have the correct number of arguments. Please enter a path to a JSON file " +
					"containing resources and the server URL as a string respectively. ");
		}
	}

	/**
	 * Reads in a JSON file containing tags and converts it to a String array
	 * @param resourceDataFile - json file containing tags
	 * @return result - a String array of tags
	 */
	public static List<Resource> readResourceData(String resourceDataFile) {

	    List<Resource> resources = new ArrayList<>();

		try (Reader reader = new FileReader(resourceDataFile)) {
			// Convert JSON File to Java Object
			Type listType = new TypeToken<List<Resource>>(){}.getType();
			resources = new Gson().fromJson(reader, listType);
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
                .path("/resource/move")
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
