package edu.brown.cs.student.main.csv;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchCSVHandler implements Route {
  private final CSVDataSource state;

  public SearchCSVHandler(CSVDataSource state) {
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

    try (FileReader fileReader = new FileReader(csvFilePath)) {
      CSVParser<MyDataClass> parser = new CSVParser<>(fileReader, MyDataClass::new);
      List<MyDataClass> data = parser.parse();

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
          .filter(d -> d.getColumnData(columnIndex).equals(searchValue))
          .collect(Collectors.toList());
    } else {
      return data.stream()
          .filter(d -> d.getData().contains(searchValue))
          .collect(Collectors.toList());
    }
  }
}

//  public SearchCSVHandler(CSVDataSource state) {}
//
//  @Override
//  public Object handle(Request request, Response response) throws Exception {
//    String csvFilePath = request.queryParams("path");
//    String searchValue = request.queryParams("value");
//    String columnIdentifier = request.queryParams("column");
//    boolean hasHeader = Boolean.parseBoolean(request.queryParams("header"));
//
//    File csvFile = new File(csvFilePath);
//    if (!csvFile.exists() || !csvFile.isFile() || !csvFile.canRead()) {
//      response.status(400);
//      return "Error: The file path specified does not exist or cannot be read.";
//    }
//
//    try (FileReader fileReader = new FileReader(csvFilePath)) {
//      CSVParser<MyDataClass> parser = new CSVParser<>(fileReader, MyDataClass::new);
//      List<MyDataClass> data = parser.parse();
//
//      List<String> headers = null;
//      int columnIndex = -1;
//
//      if (hasHeader) {
//        headers = data.get(0).getData();
//        data = data.stream().skip(1).collect(Collectors.toList());
//        if (!"-".equals(columnIdentifier)) {
//          columnIndex = headers.indexOf(columnIdentifier);
//          if (columnIndex == -1) {
//            return "Error: Column identifier is invalid.";
//          }
//        }
//      }
//
//      List<MyDataClass> filteredData = filterData(data, columnIndex, searchValue);
//      return formatResults(filteredData);
//    } catch (Exception e) {
//      response.status(500);
//      return "Error processing the request: " + e.getMessage();
//    }
//  }
//
//  private List<MyDataClass> filterData(
//      List<MyDataClass> data, int columnIndex, String searchValue) {
//    if (columnIndex >= 0) {
//      return data.stream()
//          .filter(d -> d.getColumnData(columnIndex).equals(searchValue))
//          .collect(Collectors.toList());
//    } else {
//      return data.stream()
//          .filter(d -> d.getData().contains(searchValue))
//          .collect(Collectors.toList());
//    }
//  }
//
//  private String formatResults(List<MyDataClass> filteredData) {
//    if (filteredData.isEmpty()) {
//      return "No matches found.";
//    } else {
//      return filteredData.stream().map(MyDataClass::toString).collect(Collectors.joining("\n"));
//    }
//  }
// }
