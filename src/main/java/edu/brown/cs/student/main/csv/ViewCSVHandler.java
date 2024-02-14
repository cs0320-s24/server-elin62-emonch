package edu.brown.cs.student.main.csv;

import com.google.gson.Gson;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;
import spark.Request;
import spark.Response;
import spark.Route;

public class ViewCSVHandler implements Route {

  private final String csvFilePath; // Assume this is initialized elsewhere

  public ViewCSVHandler(MyDataClass csvFilePath) {
    this.csvFilePath = String.valueOf(csvFilePath);
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    // Assume csvFilePath is initialized and points to the CSV file you want to view
    try (FileReader fileReader = new FileReader(csvFilePath)) {
      CSVParser<MyDataClass> parser = new CSVParser<>(fileReader, MyDataClass::new);
      List<MyDataClass> data = parser.parse();

      // Convert the list of MyDataClass instances into a 2D List or array if necessary
      List<List<String>> csvData =
          data.stream()
              .map(
                  MyDataClass
                      ::getData) // Assuming getData() returns a List<String> representing row data
              .collect(Collectors.toList());

      // Convert the 2D List into a JSON string
      Gson gson = new Gson();
      String jsonResponse = gson.toJson(csvData);

      response.type("application/json");
      return jsonResponse;
    } catch (Exception e) {
      response.status(500);
      return "Error processing the request: " + e.getMessage();
    }
  }
}
