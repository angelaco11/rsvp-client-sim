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
import java.lang.instrument.ClassDefinition;
import java.lang.reflect.Type;
import java.util.*;

@SpringBootApplication
public class RsvpClientSimApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RsvpClientSimApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (args.length == 2) {
			String resourceDataFile = args[0];
			String serverURL = args[1];

			//String putResponse = put(fullURL, resources.get(0).getTags());

			//System.out.println(putResponse);

			// Make a PUT request every interval, passing the whole array of strings, using a point from the points array.

            // UPDATE THE SAME TAGS AT EVERY INTERVAL WITH LOOPING LAT/LONG VALUES
            readResourceData(resourceDataFile)
                    .forEach(resource -> {
                        putAtInterval(resource, serverURL);
                    });

		} else {
			System.out.println("You do not have the correct number of arguments. Please enter a path to JSON file containing tags," +
					"path to JSON file containing coordinates, an integer representing the interval in seconds, and the server URL " +
					" as a string respectively. ");
		}

		//To run this application from the command line,
		//use: mvn spring-boot:run -Dspring-boot.run.arguments=this_is_an_argument,this_is_too
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
     * Makes a put request
     * @param resource
     */
	public static void putAtInterval(Resource resource, String serverURL) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Point point = resource.getPoints().size() == 1 ? resource.getPoints().get(0) : resource.getNextPoint();
                String fullURL = buildURL(serverURL, point);
                System.out.println("LAT: " + point.getLatitude() + " LONG: " + point.getLongitude());
                System.out.println("URL: " + fullURL);
                // String putResponse = put(fullURL, resource.getTags());
                // System.out.println(putResponse);
            }
        };
        new Timer().scheduleAtFixedRate(task, 0,
                resource.getInterval());
    }
}
