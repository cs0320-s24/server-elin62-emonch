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

/** */
public class BroadbandHandler implements Route {

  private CensusDataSource censusDataSource;

  public BroadbandHandler(CensusDataSource dataSource) {
    this.censusDataSource = dataSource;
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

    // Assuming you have a method to get state codes from CensusDataSource
    Map<String, String> stateCodes = censusDataSource.getStateCodes();

    // Assuming you have a method to get county codes from CensusDataSource
    String countyCode = censusDataSource.getCountyCode(state, county);

    // Serialize stateCodes to JSON
    String stateCodesJson = CensusDataUtilities.serializeStateCodes(stateCodes);

    // Deserialize stateCodes from JSON (Example)
    Map<String, String> deserializedStateCodes =
        CensusDataUtilities.deserializeStateCodes(stateCodesJson);

    // Prepare the response map
    responseMap.put("Date and Time of Data Retrieval", formattedDateTime);
    responseMap.put("State Name Received", state);
    responseMap.put("County Name Received", county);

    // Convert response map to JSON and set it as response body
    String responseBody = adapter.toJson(responseMap);

    // Set response content type
    response.type("application/json");

    // Return the response body
    return responseBody;
  }
}
