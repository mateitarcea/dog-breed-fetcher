package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();
    private final String API_URL = "https://dog.ceo/api/breed/%s/list";

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        Request request = new Request.Builder()
                .url(String.format(API_URL, breed))
                .build();
        List<String> subBreedsList = new ArrayList<>();
        try {
            final Response response = client.newCall(request).execute();
            final JSONObject subBreeds = new JSONObject (response.body().string());
            for (int i = 0; i < subBreeds.getJSONArray("message").length(); i++) {
                subBreedsList.add(subBreeds.getJSONArray("message").getString(i));
            }
        }catch (IOException | JSONException | NullPointerException e) {
            throw new BreedNotFoundException(breed);
        }
        return subBreedsList;
    }
}