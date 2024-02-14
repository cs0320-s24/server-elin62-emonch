package edu.brown.cs.student.main.csv;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchCSVHandler implements Route {

  public SearchCSVHandler(MyDataClass state) {}

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String csvFilePath = request.queryParams("path");
    String searchValue = request.queryParams("value");
    String columnIdentifier = request.queryParams("column");
    boolean hasHeader = Boolean.parseBoolean(request.queryParams("header"));

    File csvFile = new File(csvFilePath);
    if (!csvFile.exists() || !csvFile.isFile() || !csvFile.canRead()) {
      response.status(400);
      return "Error: The file path specified does not exist or cannot be read.";
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
            return "Error: Column identifier is invalid.";
          }
        }
      }

      List<MyDataClass> filteredData = filterData(data, columnIndex, searchValue);
      return formatResults(filteredData);
    } catch (Exception e) {
      response.status(500);
      return "Error processing the request: " + e.getMessage();
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

  private String formatResults(List<MyDataClass> filteredData) {
    if (filteredData.isEmpty()) {
      return "No matches found.";
    } else {
      return filteredData.stream().map(MyDataClass::toString).collect(Collectors.joining("\n"));
    }
  }
}
