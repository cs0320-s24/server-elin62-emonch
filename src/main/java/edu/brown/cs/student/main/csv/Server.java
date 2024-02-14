package edu.brown.cs.student.main.csv;

import edu.brown.cs.student.main.census.BroadbandHandler;
import edu.brown.cs.student.main.csv.*;
import spark.Spark;

import java.io.IOException;

import static spark.Spark.after;

public class Server {
  private MyDataClass state;

  // this is the constructor of the Main class that also initializes the args array
  public Server(MyDataClass toUse) {
    this.state = toUse;
    int port = 3232;
    Spark.port(port);

    after(
            (request, response) -> {
              response.header("Access-Control-Allow-Origin", "*");
              response.header("Access-Control-Allow-Methods", "*");
            });

    // Setting up the handler for the GET /order and /activity endpoints
    Spark.get("loadcsv", new LoadCSVHandler(state));
    Spark.get("viewcsv", new ViewCSVHandler(state));
    Spark.get("searchcsv", new SearchCSVHandler(state));
    Spark.get("broadbandhandler", new BroadbandHandler());
    Spark.init();
    Spark.awaitInitialization();

    // Notice this link alone leads to a 404... Why is that?
    System.out.println("Server started at http://localhost:" + port);
  }

  // main method that executes the logic of the program
//  public void run() throws IOException, FactoryFailureException {
//  }

  public static void main(String[] args) throws IOException, FactoryFailureException {
//    new Server(args).run();
  }
}
