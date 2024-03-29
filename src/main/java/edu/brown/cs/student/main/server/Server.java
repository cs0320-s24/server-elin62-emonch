package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.census.BroadbandHandler;
import edu.brown.cs.student.main.census.CensusDataSource;
import edu.brown.cs.student.main.census.DataSourceException;
import edu.brown.cs.student.main.csv.*;
import java.io.IOException;
import spark.Spark;

public class Server {

  public static CSVDataSource state;
  public static CensusDataSource censusDataSource;

  public static void main(String[] args)
      throws IOException, FactoryFailureException, DataSourceException {

    if (args.length != 1) {
      System.err.println("Usage: java -jar your_application.jar <file_path>");
      System.exit(1);
    }

    String CSVFilePath = args[0];
    int port = 3232;
    Spark.port(port);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // Sets up data needed for the BroadbandHandler.

    state = new CSVDataSource<>(CSVFilePath);
    censusDataSource = new CensusDataSource();
    censusDataSource.requestStateCodes();

    // Setting up the handler for the GET /order and /activity endpoints
    Spark.get("/loadcsv", new LoadCSVHandler(CSVFilePath, state));
    Spark.get("/viewcsv", new ViewCSVHandler(state));
    Spark.get("/searchcsv", new SearchCSVHandler(state));
    Spark.get("/broadbandhandler", new BroadbandHandler(censusDataSource));
    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
    System.out.println("Server started; exiting main...");
  }
}
