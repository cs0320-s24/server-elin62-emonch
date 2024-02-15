package edu.brown.cs.student.main.csv;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Won't be able to view the CSV without loading it first.
 *
 * @param <T>
 */
public class ViewCSVHandler<T> implements Route {

  private CSVDataSource<T> state;
  private LoadCSVHandler<T> loadCSVHandler;

  public ViewCSVHandler(CSVDataSource state) {
    this.state = state;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    // Check if the CSV data has been loaded
    if (this.state == null) {
      response.status(400); // Bad Request
      return "CSV data not loaded. Load CSV data first.";
    }

    // Get the CSV data
    List<MyDataClass> data = this.state.getData();

    // Prepare Moshi for JSON conversion
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);

    // Prepare response map
    Map<String, Object> responseMap = new HashMap<>();

    // Put CSV data into response map
    responseMap.put("csvData", data);

    // Return response map as JSON
    return adapter.toJson(responseMap);
  }
}
