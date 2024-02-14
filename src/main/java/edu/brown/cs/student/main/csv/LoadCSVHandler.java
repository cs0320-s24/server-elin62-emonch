package edu.brown.cs.student.main.csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** This class stores the CSV file path and preprocesses the data from the CSV */
public class LoadCSVHandler implements Route {

  boolean fileValid;

  public LoadCSVHandler(MyDataClass state) {}

  @Override
  public Object handle(Request request, Response response) throws Exception {
    // Get Query parameters

    String filePath = request.queryParams("filePath");

    try {
      // Open the CSV file
      FileReader fileReader = new FileReader(filePath);
      CSVParser<MyDataClass> parser = new CSVParser<>(fileReader, row -> new MyDataClass(row));
      List<MyDataClass> data =
          parser.parse(); // parse the CSV file into a list of MyDataClass objects
      // deserialize here

      // return response map?

      Map<String, Object> responseMap = new HashMap<>();

    } catch (FileNotFoundException e) {
      System.out.println("File not found: " + filePath);
      this.fileValid = false;
    }
    return null;
  }
}
