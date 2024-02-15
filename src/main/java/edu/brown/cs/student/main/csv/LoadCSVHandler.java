package edu.brown.cs.student.main.csv;

import java.util.List;
import spark.Request;
import spark.Response;
import spark.Route;

/** This class stores the CSV file path and preprocesses the data from the CSV */
public class LoadCSVHandler<T> implements Route {

  private CSVDataSource<T> state;
  private String filePath;

  public LoadCSVHandler(String filepath, CSVDataSource<T> state) {
    this.state = state;
    this.filePath = filepath;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    // Get Query parameters
    this.filePath = request.queryParams("filePath");

    // Check if filePath is not null and not empty
    if (filePath != null && !filePath.isEmpty()) {
      this.state = new CSVDataSource<>(filePath);
      // open the csv file
      this.state.getData();
      // deserialize the csv file
      List<MyDataClass> data = this.state.getData();
      // return response map?
      //      Map<String, Object> responseMap = new HashMap<>();
      return data;
    } else {
      // Handle case where filePath is not provided
      response.status(400); // Bad Request
      return "File path not provided.";
    }
  }
}
