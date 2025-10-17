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
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    private final BreedFetcher fetcher;
    private JSONObject cachedBreeds = new JSONObject();
    List<String> subBreeds = new ArrayList<>();
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        if (!cachedBreeds.has(breed)) {
            callsMade++;
            subBreeds = fetcher.getSubBreeds(breed);
            cachedBreeds.put(breed, subBreeds);
        }else{
            JSONArray cachedArray = cachedBreeds.getJSONArray(breed);
            subBreeds = new ArrayList<>();
            for (int i = 0; i < cachedArray.length(); i++) {
                subBreeds.add(cachedArray.getString(i));
            }
        }
        return subBreeds;
    }

    public int getCallsMade() {
        return callsMade;
    }
}