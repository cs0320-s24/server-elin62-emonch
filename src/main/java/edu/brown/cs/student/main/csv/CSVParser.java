package edu.brown.cs.student.main.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVParser<T> {
  private BufferedReader reader;
  private CreatorFromRow<T> creator;

  // Constructor for CSVParser that takes a Reader and a CreatorFromRow<T>
  // The Reader is wrapped inside a BufferedReader to improve efficiency
  public CSVParser(Reader reader, CreatorFromRow<T> creator) {
    this.reader = new BufferedReader(reader);
    this.creator = creator;
  }

  // The parse method reads lines from the BufferedReader and converts each line into an object of
  // type T
  public List<T> parse() throws IOException, FactoryFailureException {
    // initialize an empty ArrayList that stores the result
    List<T> result = new ArrayList<>();
    String line;
    // reads lines from the BufferedReader until null is returned which happens at the end of the
    // stream
    while ((line = reader.readLine()) != null) {
      // split the line by commas and convert it into a List<String> representing a row of the CSV
      // file
      List<String> row = Arrays.asList(line.split(","));
      // use the creator to create an instance of type T from the row
      T obj = creator.create(row);
      // add the created object to the result list
      result.add(obj);
    }
    // Return the list of created objects.
    return result;
  }
}
