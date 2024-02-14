package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.census.BroadbandHandler;
import edu.brown.cs.student.main.csv.*;
import java.io.IOException;
import spark.Spark;

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

    System.out.println("Server started at http://localhost:" + port);
  }

  public static void main(String[] args) throws IOException, FactoryFailureException {
    //    Server server = new Server();
    System.out.println("Server started; exiting main...");
  }
}
