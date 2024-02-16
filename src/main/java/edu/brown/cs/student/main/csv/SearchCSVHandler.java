package edu.brown.cs.student.main.csv;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchCSVHandler<T> implements Route {
  private final CSVDataSource<T> state;

  public SearchCSVHandler(CSVDataSource<T> state) {
    this.state = state;
  }

  @Override
  public Object handle(Request request, Response response) {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    Map<String, Object> responseMap = new HashMap<>();

    String csvFilePath = request.queryParams("path");
    String searchValue = request.queryParams("value");
    String columnIdentifier = request.queryParams("column");
    boolean hasHeader = Boolean.parseBoolean(request.queryParams("header"));

    // Add the request parameters to the response map for reference
    responseMap.put("path", csvFilePath);
    responseMap.put("value", searchValue);
    responseMap.put("column", columnIdentifier);
    responseMap.put("header", hasHeader);

    File csvFile = new File(csvFilePath);
    if (!csvFile.exists() || !csvFile.isFile() || !csvFile.canRead()) {
      response.status(400);
      responseMap.put("error", "Error: The file path specified does not exist or cannot be read.");
      return adapter.toJson(responseMap);
    }

    try {
      //      LoadCSVHandler<T> loadCSV = new LoadCSVHandler<>(csvFilePath, this.state);
      //      List<MyDataClass> data = loadCSV.handle(request, response);
      List<MyDataClass> data = this.state.getData();

      List<String> headers = null;
      int columnIndex = -1;

      if (hasHeader) {
        headers = data.get(0).getData();
        data = data.stream().skip(1).collect(Collectors.toList());
        if (!"-".equals(columnIdentifier)) {
          columnIndex = headers.indexOf(columnIdentifier);
          if (columnIndex == -1) {
            response.status(400);
            responseMap.put("error", "Error: Column identifier is invalid.");
            return adapter.toJson(responseMap);
          }
        }
      }

      List<MyDataClass> filteredData = filterData(data, columnIndex, searchValue);

      // Serialize the filtered data to List<List<String>>
      //      List<List<String>> serializedData = new ArrayList<>();
      //      for (List<String> entry : serializedData) {
      //        serializedData.add(entry);
      //      }

      List<List<String>> serializedData = new ArrayList<>();
      for (MyDataClass entry : filteredData) {
        serializedData.add(entry.getData());
      }

      // Add the serialized data to the response map
      responseMap.put("data", serializedData);

      // Return the response map as JSON
      return adapter.toJson(responseMap);
    } catch (Exception e) {
      response.status(500);
      responseMap.put("error", "Error processing the request: " + e.getMessage());
      return adapter.toJson(responseMap);
    }
  }

  private List<MyDataClass> filterData(
      List<MyDataClass> data, int columnIndex, String searchValue) {
    if (columnIndex >= 0) {
      return data.stream()
          // Strip quotes from the column data before comparing it with the searchValue
          .filter(d -> d.getColumnData(columnIndex).replace("\"", "").equals(searchValue))
          .collect(Collectors.toList());
    } else {
      // Assuming d.getData() returns a List<String> and you want to compare each element
      return data.stream()
          .filter(
              d ->
                  d.getData().stream().anyMatch(cell -> cell.replace("\"", "").equals(searchValue)))
          .collect(Collectors.toList());
    }
  }
}
