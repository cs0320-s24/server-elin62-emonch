// package edu.brown.cs.student.main.server;
//
// import edu.brown.cs.student.main.csv.CSVParser;
// import edu.brown.cs.student.main.csv.FactoryFailureException;
// import edu.brown.cs.student.main.csv.MyDataClass;
// import java.io.File;
// import java.io.FileReader;
// import java.io.IOException;
// import java.util.List;
// import java.util.stream.Collectors;
//
// public class Search {
//
//  public Search(String[] args) throws IOException, FactoryFailureException {
//    this.search(args);
//  }
//
//  public void search(String[] args) {
//    // this checks that the user is entering the correct number of arguments and prints
// informative
//    // error message if not
//    if (args.length < 4) {
//      System.err.println(
//          "Usage: java Main <path_to_csv_file> <search_value> <column_identifier> <has_header
// (True/False)>");
//      return;
//    }
//
//    String csvFilePath = args[0]; // file path
//    // Validate the csvFilePath for non-null and existence
//    if (csvFilePath == null || csvFilePath.trim().isEmpty()) {
//      System.err.println("Error: The file path cannot be null or empty.");
//      return;
//    }
//
//    File csvFile = new File(csvFilePath);
//    if (!csvFile.exists() || !csvFile.isFile() || !csvFile.canRead()) {
//      System.err.println("Error: The file path specified does not exist or cannot be read. ");
//      return;
//    }
//    String searchValue = args[1]; // value the user is searching
//    String columnIdentifier =
//        args[2]; // this is an optional argument to limit the search to a column
//
//    String headerArgument = args[3]; // sets boolean to True or False depending on input
//    // Validate the headerArgument for "true" or "false" values explicitly
//    boolean hasHeader;
//    // Convert headerArgument to lowercase (or uppercase) before comparison to ensure
//    // case-insensitive comparison
//    if (headerArgument == null
//        || (!"true".equals(headerArgument.toLowerCase())
//            && !"false".equals(headerArgument.toLowerCase()))) {
//      System.err.println("Error: 'has_header' argument must be 'True' or 'False'.");
//      return;
//    } else {
//      hasHeader =
//          Boolean.parseBoolean(headerArgument.toLowerCase()); // Safe to parse after validation
//    }
//    try (FileReader fileReader = new FileReader(csvFilePath)) {
//      // creates the CSVParser object for parsing the CSV file
//      CSVParser<MyDataClass> parser = new CSVParser<>(fileReader, row -> new MyDataClass(row));
//      List<MyDataClass> data =
//          parser.parse(); // parse the CSV file into a list of MyDataClass objects
//
//      int columnIndex = -1; // default colum index
//      List<String> headers = null;
//
//      if (hasHeader) { // checks if the header boolean is set to true
//        headers = data.get(0).getData(); // gets the data from the header
//        data = data.stream().skip(1).collect(Collectors.toList());
//        // checks if that the column identifier is not set to n/a which is indicated by -
//        if (!columnIdentifier.equals("-")) {
//          if (hasHeader && headers != null) {
//            columnIndex =
//                headers.indexOf(
//                    columnIdentifier); // gets the index of the column identifier passed in
//            if (columnIndex == -1) {
//              System.err.println(
//                  "Error: please provide a valid column identifier, or '-' if n/a"); // might
// remove
//              return;
//            }
//          }
//        }
//        // Skip the header for the data processing
//        data = data.stream().skip(1).collect(Collectors.toList());
//      }
//
//      List<MyDataClass> filteredData;
//      // filter the data based on the specified column
//      if (columnIndex >= 0) {
//        final int idx = columnIndex;
//        filteredData =
//            data.stream()
//                .filter(d -> d.getColumnData(idx).equals(searchValue)) // searches for the exact
//                .collect(Collectors.toList());
//      } else {
//        // Search all columns if no valid header is provided or if the header is not used
//        filteredData =
//            data.stream()
//                .filter(d -> d.getData().contains(searchValue))
//                .collect(Collectors.toList());
//      }
//      // display the filtered data
//      if (filteredData.isEmpty()) {
//        System.out.println("No matches found.");
//      } else {
//        filteredData.forEach(System.out::println);
//      }
//    } catch (Exception e) {
//      // handle any exceptions during the file read or the processing
//      System.err.println("Error: " + e.getMessage());
//      e.printStackTrace();
//    }
//  }
// }
