package edu.brown.cs.student.main.census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okio.Buffer;

public class CensusDataSource {
  private Map<String, String> stateCodes;
  private Map<String, Map<String, String>> countyCodes;

  public CensusDataSource() {
    stateCodes = new HashMap<>();
    countyCodes = new HashMap<>();
  }

  /**
   * Fetches state codes from the Census API and populates the stateCodes map.
   *
   * @throws IOException
   * @throws DataSourceException
   */
  public void requestStateCodes() throws IOException, DataSourceException {
    // Build a request to census for state code data
    String apiKey = "ef5a11074c6700392d66277e0f5c7b78bd98c6f8";
    URL requestURL =
        new URL("https", "api.census.gov", "/data/2010/dec/sf1?get=NAME&for=state:*&key=" + apiKey);
    HttpURLConnection clientConnection = connect(requestURL);
    Moshi moshi = new Moshi.Builder().build();

    Type listListString = Types.newParameterizedType(List.class, List.class, String.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(listListString);

    try {
      List<List<String>> body =
          adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
      clientConnection.disconnect();
      System.out.println("Disconnected from client");
      if (body == null) {
        throw new DataSourceException("Malformed response from Census API");
      }
      // Iterate over the response body to extract state codes and names
      for (List<String> row : body) {
        String stateName = row.get(0);
        String stateCode = row.get(1);
        // Store the state code in the map with state name as the key
        this.stateCodes.put(stateName, stateCode);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Private helper method; throws IOException so different callers can handle differently if
   * needed.
   */
  private static HttpURLConnection connect(URL requestURL) throws DataSourceException, IOException {
    try {
      URLConnection urlConnection = requestURL.openConnection();
      if (!(urlConnection instanceof HttpURLConnection))
        throw new DataSourceException("unexpected: result of connection wasn't HTTP");
      HttpURLConnection clientConnection = (HttpURLConnection) urlConnection;
      clientConnection.connect(); // GET
      if (clientConnection.getResponseCode() != 200)
        throw new DataSourceException(
            "unexpected: API connection not success status "
                + clientConnection.getResponseMessage());
      return clientConnection;
    } catch (IOException e) {
      // Catch IOException and rethrow it
      throw new IOException("Error connecting to Census API: " + e.getMessage());
    }
  }

  /**
   * Returns the field variable that stores the state code in the map with state name as the key
   *
   * @return
   */
  public Map<String, String> getStateCodes() {
    return stateCodes;
  }

  /**
   * Checks if the state codes map is populated. If not, it fetches state codes. Then, it checks if
   * county codes for the given state have already been fetched. If not, it fetches county codes for
   * that state. Finally, it returns the county code if found, or null otherwise.
   *
   * @param state
   * @param county
   * @return
   * @throws IOException
   * @throws DataSourceException
   */
  public String getCountyCode(String state, String county) throws IOException, DataSourceException {
    if (this.stateCodes.isEmpty()) {
      try {
        this.requestStateCodes();
      } catch (IOException | DataSourceException e) {
        throw e;
      }
    }

    if (!this.countyCodes.containsKey(state)) {
      try {
        this.fetchCountyCodes(state);
      } catch (IOException | DataSourceException e) {
        throw e;
      }
    }

    Map<String, String> countyMap = this.countyCodes.get(state);
    if (countyMap == null) {
      return null;
    }

    for (Map.Entry<String, String> entry : countyMap.entrySet()) {
      if (entry.getKey().equalsIgnoreCase(county)) {
        return entry.getValue();
      }
    }

    return null;
  }

  /**
   * Makes an API call to fetch county codes for a given state and populates the countyCodes map
   * with the data.
   *
   * @param state
   * @throws IOException
   * @throws DataSourceException
   */
  private void fetchCountyCodes(String state) throws IOException, DataSourceException {
    String apiKey = "ef5a11074c6700392d66277e0f5c7b78bd98c6f8";
    URL requestURL =
        new URL(
            "https",
            "api.census.gov",
            "/data/2010/dec/sf1?get=NAME&for=county:*&in=state:"
                + this.stateCodes.get(state)
                + "&key="
                + apiKey);
    HttpURLConnection clientConnection = connect(requestURL);
    Moshi moshi = new Moshi.Builder().build();

    Type listListString = Types.newParameterizedType(List.class, List.class, String.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(listListString);

    try {
      List<List<String>> body =
          adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
      clientConnection.disconnect();
      if (body == null) {
        throw new DataSourceException("Malformed response from Census API");
      }

      // Populate county codes map for this state
      Map<String, String> countyMap = new HashMap<>();
      for (List<String> row : body) {
        String countyName = row.get(0);
        String countyCode = row.get(1);
        countyMap.put(countyName, countyCode);
      }
      this.countyCodes.put(state, countyMap);
      System.out.println("countyMap: " + countyMap);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    System.out.println("countyCodes: " + countyCodes);
  }

  public String getBroadbandPercentage(String state, String county)
      throws DataSourceException, IOException {
    // Check if county code is available
    String countyCode = this.getCountyCode(state, county);
    if (countyCode == null) {
      throw new DataSourceException("County code not found for the provided state and county");
    }

    // Construct API URL for the broadband percentage data
    String apiKey = "ef5a11074c6700392d66277e0f5c7b78bd98c6f8";
    URL requestURL =
        new URL(
            "https",
            "api.census.gov",
            "/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                + countyCode
                + "&in=state:"
                + stateCodes.get(state)
                + "&key="
                + apiKey);

    // Make API call and parse response
    HttpURLConnection clientConnection = connect(requestURL);
    Moshi moshi = new Moshi.Builder().build();
    Type listListString = Types.newParameterizedType(List.class, List.class, String.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(listListString);

    try {
      List<List<String>> body =
          adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
      clientConnection.disconnect();
      if (body == null || body.isEmpty() || body.get(1).isEmpty()) {
        throw new DataSourceException(
            "No broadband percentage data found for the provided state and county");
      }

      // Extract broadband percentage from response
      return body.get(1).get(0);
    } catch (Exception e) {
      throw new DataSourceException(
          "Error retrieving broadband percentage data from Census API: " + e.getMessage());
    }
  }
}
