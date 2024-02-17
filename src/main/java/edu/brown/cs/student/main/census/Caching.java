package edu.brown.cs.student.main.census;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.concurrent.TimeUnit;

public class Caching implements Route {
    private CensusDataSource censusDataSource;
    LoadingCache<String, Object> cache;

    public Caching(CensusDataSource dataSource) {
        this.censusDataSource = dataSource;
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(100) // Maximum number of entries in the cache
                .expireAfterWrite(10, TimeUnit.MINUTES) // Cache entries expire after 10 minutes
                .recordStats() // Record cache statistics
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(String key) throws Exception {
                        // Implement the logic to load data into the cache here if not found
                        // This method is called automatically when the cache doesn't contain the requested key
                        return fetchData(key); // Example method to fetch data based on the key
                    }
                });
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        // Retrieve data from cache based on the request parameters
        String state = request.queryParams("state");
        String county = request.queryParams("county");
        String cacheKey = state + "_" + county;

        Object cachedData = cache.get(cacheKey);

        if (cachedData != null) {
            // If data found in cache, return it
            return cachedData;
        } else {
            // If data not found in cache, load it using CacheLoader's load method
            return cache.get(cacheKey);
        }
    }

    private Object fetchData(String cacheKey) {
        // Extract state and county from the cache key
        String[] parts = cacheKey.split("_");
        String state = parts[0];
        String county = parts[1];

        try {
            // Fetch broadband percentage data using CensusDataSource
            String broadbandPercentage = this.censusDataSource.requestBroadbandPercentage(state, county);
            return broadbandPercentage;
        } catch (Exception e) {
            // Handle any errors that may occur during data retrieval
            e.printStackTrace(); // You might want to log the error instead of printing stack trace
            return null; // Return null or handle the error in an appropriate manner
        }
    }
}
