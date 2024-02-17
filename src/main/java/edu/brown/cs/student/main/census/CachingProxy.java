package edu.brown.cs.student.main.census;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.TimeUnit;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This class mediates access to the data by intercepting requests for data from the census data
 * source. Instead of directly accessing the data source from BroadbandHandler, the handler
 * interacts with CachingProxy. The proxy then decides whether to fetch the data from the census
 * data source or retrieve it from the cache.
 */
public class CachingProxy implements Route {
  private CensusDataSource censusDataSource;
  private LoadingCache<String, Object> cache;

  public CachingProxy(CensusDataSource dataSource) {
    this.censusDataSource = dataSource;
    this.cache =
        CacheBuilder.newBuilder()
            .maximumSize(100) // Maximum number of entries in the cache
            .expireAfterWrite(10, TimeUnit.MINUTES) // Cache entries expire after 10 minutes
            .recordStats() // Record cache statistics
            .build(
                new CacheLoader<String, Object>() {
                  @Override
                  public Object load(String key) throws Exception {
                    // Implement the logic to load data into the cache here if not found
                    // This method is called automatically when the cache doesn't contain the
                    // requested key
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

    // If data found in cache, return it
    return this.cache.get(cacheKey);
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
