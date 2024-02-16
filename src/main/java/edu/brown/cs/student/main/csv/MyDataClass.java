package edu.brown.cs.student.main.csv;

import java.util.ArrayList;
import java.util.List;

public class MyDataClass {
  // list to store the data elements (cells) in the row
  private List<String> data;

  // Constructor for MyDataClass that takes in a type List<String> as an arg
  public MyDataClass(List<String> data) {
    this.data = this.cleanData(data);
    this.data = List.copyOf(this.data);
  }

  private List<String> cleanData(List<String> rawData) {
    List<String> cleanedData = new ArrayList<>();
    for (String value : rawData) {
      // Remove leading and trailing whitespace
      String cleanedValue = value.trim();

      // Remove special characters except for commas
      //      cleanedValue = cleanedValue.replaceAll("[^a-zA-Z0-9,\\s]", "");

      // Convert to lowercase
      cleanedValue = cleanedValue.toLowerCase();

      // Handle missing values
      if (cleanedValue.isEmpty()) {
        cleanedValue = "N/A";
      }

      // Add the cleaned value to the list
      cleanedData.add(cleanedValue);
    }
    return cleanedData;
  }

  // getter method for the data as a List<String>
  public List<String> getData() {
    return data;
  }

  public String getColumnData(int index) {
    // checks if index within the bounds
    if (index >= 0 && index < data.size()) {
      // if index is valid, returns the element at the specified index
      return data.get(index);
    }
    return null; // or throw an exception if that's preferred
  }

  // representing the data in its comma seperated string form
  @Override
  public String toString() {
    return String.join(",", data);
  }
}
