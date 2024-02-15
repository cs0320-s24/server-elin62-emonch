package edu.brown.cs.student.main.csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * This is the "shared state." Parses the file so that it can be shared between the handlers. * This
 * class is responsible for reading the CSV file and converting its contents * into Java objects.
 * Within the CSVDataSource class, there should be logic to * parse the CSV file and create objects
 * of type T (or MyDataClass, based on your * example) from the CSV data. * @param <T>
 */
public class CSVDataSource<T> {

  private String csvFilePath;
  private boolean validFilePath;
  private List<MyDataClass> data;

  public CSVDataSource(String csvFilePath) throws IOException, FactoryFailureException {
    this.csvFilePath = csvFilePath;
    this.validFilePath = true;
    this.parseData();
  }

  private void parseData() throws IOException, FactoryFailureException {
    try {
      FileReader fileReader = new FileReader(this.csvFilePath);
      // creates the CSVParser object for parsing the CSV file
      CSVParser<MyDataClass> parser = new CSVParser<>(fileReader, row -> new MyDataClass(row));
      this.data = parser.parse(); // parse the CSV file into a list of MyDataClass objects
      System.out.println("File found.");

    } catch (FileNotFoundException e) {
      System.out.println("File not found: " + this.csvFilePath);
      this.validFilePath = false;
    }
  }

  public List<MyDataClass> getData() {
    return this.data;
  }
}
