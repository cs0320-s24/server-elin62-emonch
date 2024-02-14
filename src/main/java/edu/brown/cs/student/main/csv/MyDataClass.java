package edu.brown.cs.student.main.csv;

import java.util.List;

public class MyDataClass {
  // list to store the data elements (cells) in the row
  private List<String> data;

  // Constructor for MyDataClass that takes in a type List<String> as an arg
  public MyDataClass(List<String> data) {
    this.data = data;
    this.makeCopy(this.data);
  }

  public void makeCopy(List data) {
    this.data = List.copyOf(data);
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
