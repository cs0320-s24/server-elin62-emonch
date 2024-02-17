 package edu.brown.cs.student;

 import edu.brown.cs.student.main.csv.CSVDataSource;
 import edu.brown.cs.student.main.csv.MyDataClass;
 import edu.brown.cs.student.main.csv.ViewCSVHandler;
 import org.junit.Before;
 import org.junit.Test;
 import spark.Request;
 import spark.Response;
 import spark.Route;

 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.List;

 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertTrue;

 public class TestViewCSVHandler {

  private CSVDataSource<MyDataClass> dataSource;
  private ViewCSVHandler<MyDataClass> handler;

  // Inner class that simulates a simple CSVDataSource for test purposes
  public class TestCSVDataSource extends CSVDataSource<MyDataClass> {
    private List<MyDataClass> data;



    @Override
    public List<MyDataClass> getData() {
      return data;
    }
  }

  @Before
  public void setUp() {
    // Initialize with empty data
    dataSource = new TestCSVDataSource(new ArrayList<>());
    handler = new ViewCSVHandler<>(dataSource);
  }

  @Test
  public void testHandle_NoDataLoaded() throws Exception {

    String result = (String) handler.handle(null, null);

    // Expected to receive an error message since no data is loaded
    assertEquals("CSV data not loaded. Load CSV data first.", result);
  }

  @Test
  public void testHandle_DataLoaded() throws Exception {
    // Populate the dataSource with some test data
    List<MyDataClass> testData = new ArrayList<>();
    testData.add(new MyDataClass(Arrays.asList("data1", "data2", "data3"))); // Example row 1
    testData.add(new MyDataClass(Arrays.asList("data4", "data5", "data6"))); // Example row 2
    dataSource = new TestCSVDataSource(testData);

    // Re-initialize handler with new dataSource containing data
    handler = new ViewCSVHandler<>(dataSource);

    // Use the handler to process a simulated request/response

    Request request = new Request() {  };
    Response response = new Response() {  };

    String result = (String) handler.handle(request, response);

    // Check that the result is not null and contains the expected data
    assertTrue(result != null && result.contains("csvData"));
  }
 }
