package edu.brown.cs.student.main.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CSVParser<T> {
  private BufferedReader reader;
  private CreatorFromRow<T> creator;

  public CSVParser(Reader reader, CreatorFromRow<T> creator) {
    this.reader = new BufferedReader(reader);
    this.creator = creator;
  }

  public List<T> parse() throws IOException, FactoryFailureException {
    List<T> result = new ArrayList<>();
    String line;
    while ((line = reader.readLine()) != null) {
      List<String> row = splitCsvLine(line);
      T obj = creator.create(row);
      result.add(obj);
    }
    return result;
  }

  // Splits a CSV line on commas not surrounded by quotation marks
  private List<String> splitCsvLine(String line) {
    List<String> tokens = new ArrayList<>();
    boolean inQuotes = false;
    StringBuilder token = new StringBuilder();
    for (char ch : line.toCharArray()) {
      if (ch == '"') {
        inQuotes = !inQuotes; // Toggle the inQuotes flag
      } else if (ch == ',' && !inQuotes) {
        tokens.add(token.toString());
        token.setLength(0); // Reset the token
      } else {
        token.append(ch);
      }
    }
    tokens.add(token.toString()); // Add the last token
    return tokens;
  }
}
