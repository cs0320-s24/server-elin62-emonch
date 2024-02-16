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
    // Check if state codes map is populated
    if (this.stateCodes.isEmpty()) {
      try {
        this.requestStateCodes();
      } catch (IOException | DataSourceException e) {
        // Catch IOException and DataSourceException
        throw e;
      }
    }

    // Check if county codes for this state have already been fetched
    if (!this.countyCodes.containsKey(state)) {
      try {
        this.fetchCountyCodes(state);
      } catch (IOException | DataSourceException e) {
        // Catch IOException and DataSourceException
        throw e;
      }
    }

    // Return county code if found, otherwise return null
    return this.countyCodes.getOrDefault(state, new HashMap<>()).get(county);
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
        countyMap.put(row.get(0), row.get(1));
      }
      this.countyCodes.put(state, countyMap);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
