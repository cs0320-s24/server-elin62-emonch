package edu.brown.cs.student.main.census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * The BroadbandHandler class implements the Spark Route interface to handle HTTP requests. It is
 * responsible for retrieving data related to broadband access percentages for households from the
 * CensusDataSource and providing a response in JSON format.
 */
public class BroadbandHandler implements Route {

  private CensusDataSource censusDataSource;
  private CachingProxy cache;

  public BroadbandHandler(CensusDataSource dataSource) {
    this.censusDataSource = dataSource;
    this.cache = new CachingProxy(dataSource);
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    // Create a response map
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    Map<String, Object> responseMap = new HashMap<>();

    // Extract query parameters for state and county from the request
    String state = request.queryParams("state");
    String county = request.queryParams("county");

    // Get the current date and time
    LocalDateTime currentTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDateTime = currentTime.format(formatter);

    // Get state codes from CensusDataSource
    Map<String, String> stateCodes = censusDataSource.getStateCodes();

    // Serialize stateCodes to JSON
    String stateCodesJson = CensusDataUtilities.serializeStateCodes(stateCodes);

    // Deserialize stateCodes from JSON
    Map<String, String> deserializedStateCodes =
        CensusDataUtilities.deserializeStateCodes(stateCodesJson);

    try {

      // Prepare the response map
      responseMap.put("Date and Time of Data Retrieval", formattedDateTime);
      responseMap.put("State Name Received", state);
      responseMap.put("County Name Received", county);
      // Retrieve the percentage of households with broadband access
      String broadbandPercentage = censusDataSource.requestBroadbandPercentage(state, county);
      responseMap.put("Broadband Percentage", broadbandPercentage);

      // Convert response map to JSON and set it as response body
      String responseBody = adapter.toJson(responseMap);

      // Set response content type
      response.type("application/json");

      // Return the response body
      return responseBody;
    } catch (DataSourceException e) {
      // Handle DataSourceException
      response.status(500); // Internal Server Error
      return "Error occurred: " + e.getMessage();
    }
  }
}
