// package edu.brown.cs.student;
//
<<<<<<< HEAD
=======
// import static org.mockito.Mockito.*;
// import static org.testng.AssertJUnit.assertFalse;
>>>>>>> 1055eb0ee60f8c51e2971511aaf93a6584dfed5b
//
// import edu.brown.cs.student.main.csv.CSVDataSource;
// import edu.brown.cs.student.main.csv.MyDataClass;
// import edu.brown.cs.student.main.csv.SearchCSVHandler;
<<<<<<< HEAD
// import org.eclipse.jetty.client.api.Response;
// import org.junit.Before;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.runner.Request;
// import org.testng.AssertJUnit;
// import static org.mockito.Mockito.*;
// import java.io.ByteArrayOutputStream;
// import java.io.PrintStream;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Random;
//
// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertTrue;
// import static org.testng.AssertJUnit.assertFalse;
//
// public class TestSearchCSVHandler {
//    @Test
//    public void testSearchWithNoMatchingValue() {
//
//        class MyDataClass {
//            private List<String> data;
//            public MyDataClass(List<String> data) {
//                this.data = data;
//            }
//
//        }
//
//
//        CSVDataSource<MyDataClass> dataSource = new CSVDataSource<>() {
//            @Override
//            public List<MyDataClass> getData() {
//                return List.of(
//                        new MyDataClass(List.of("header1", "header2", "header3")),
//                        new MyDataClass(List.of("data1", "data2", "data3")),
//                        new MyDataClass(List.of("data4", "data5", "data6"))
//                );
//            }
//        };
//
//        // Create mock Request and Response objects
//        Request mockRequest = mock(Request.class);
//        Response mockResponse = mock(Response.class);
//
//        // Define the behavior of the mockRequest when specific methods are called
//        when(mockRequest.equals("path")).thenReturn(Boolean.valueOf("valid/path.csv"));
//        when(mockRequest.equals("value")).thenReturn(Boolean.valueOf("nomatch"));
//        when(mockRequest.equals("column")).thenReturn(Boolean.valueOf("header2"));
//        when(mockRequest.equals("header")).thenReturn(Boolean.valueOf("true"));
//
//        // Create an instance of the handler with the stubbed data source
//        SearchCSVHandler<MyDataClass> searchHandler = new SearchCSVHandler<>(dataSource);
//
//        // Execute the handler and capture the result
//        String jsonResult = (String) searchHandler.handle(mockRequest, mockResponse);
//
//        // Assert that the result does not contain the search value 'nomatch'
//        assertFalse(jsonResult.contains("nomatch"));
//    }
//
//
//    @Test
//    public void fuzzTestSearchHandler() {
//        // Setup for fuzz testing
//        Random random = new Random();
//        List<MyDataClass> testData = new ArrayList<>();
//
//        // Generate random test data
//        for (int i = 0; i < 10; i++) { // Generate 10 random data instances
//            List<String> randomData = new ArrayList<>();
//            for (int j = 0; j < 3; j++) { // Each instance with 3 random strings
//                randomData.add(generateRandomString(random));
//            }
//            testData.add(new MyDataClass(randomData));
//        }
//
//        // Create a stub CSVDataSource with the random test data
//        CSVDataSource<MyDataClass> dataSource = () -> testData;
//
//        // Create mock Request and Response objects
//        Request mockRequest = mock(Request.class);
//        Response mockResponse = mock(Response.class);
//
//        // Simulate random request parameters
//        when(mockRequest.equals("path")).thenReturn(generateRandomString(random));
//        when(mockRequest.equals("value")).thenReturn(generateRandomString(random));
//        when(mockRequest.equals("column")).thenReturn("header" + (random.nextInt(3) + 1));
//
//        // Instantiate the handler with the random data source
//        SearchCSVHandler<MyDataClass> searchHandler = new SearchCSVHandler<>(dataSource);
//
//        // Execute the handler with the mock request and response
//        // Wrap in try-catch to ensure no unexpected exceptions are thrown
//        try {
//            String result = searchHandler.handle(mockRequest, mockResponse);
//            // Assert based on expected behavior with random inputs
//            // This could vary based on how you expect your handler to react to fuzzing
//        } catch (Exception e) {
//            AssertJUnit.fail("Handler should not throw an exception with random inputs.");
//        }
//    }
//
//    // Helper method to generate a random string
//    private String generateRandomString(Random random) {
//        return random.ints(97, 122 + 1)
//                .limit(10)
//                .collect(StringBuilder::new, StringBuilder::appendCodePoint,
// StringBuilder::append)
//                .toString();
//    }
=======
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Random;
// import org.eclipse.jetty.client.api.Response;
// import org.junit.jupiter.api.Test;
// import org.junit.runner.Request;
// import org.testng.AssertJUnit;
//
// public class TestSearchCSVHandler {
//  @Test
//  public void testSearchWithNoMatchingValue() {
//
//    class MyDataClass {
//      private List<String> data;
//
//      public MyDataClass(List<String> data) {
//        this.data = data;
//      }
//    }
//
//    CSVDataSource<MyDataClass> dataSource =
//        new CSVDataSource<>() {
//          @Override
//          public List<MyDataClass> getData() {
//            return List.of(
//                new MyDataClass(List.of("header1", "header2", "header3")),
//                new MyDataClass(List.of("data1", "data2", "data3")),
//                new MyDataClass(List.of("data4", "data5", "data6")));
//          }
//        };
//
//    // Create mock Request and Response objects
//    Request mockRequest = mock(Request.class);
//    Response mockResponse = mock(Response.class);
//
//    // Define the behavior of the mockRequest when specific methods are called
//    when(mockRequest.equals("path")).thenReturn(Boolean.valueOf("valid/path.csv"));
//    when(mockRequest.equals("value")).thenReturn(Boolean.valueOf("nomatch"));
//    when(mockRequest.equals("column")).thenReturn(Boolean.valueOf("header2"));
//    when(mockRequest.equals("header")).thenReturn(Boolean.valueOf("true"));
//
//    // Create an instance of the handler with the stubbed data source
//    SearchCSVHandler<MyDataClass> searchHandler = new SearchCSVHandler<>(dataSource);
//
//    // Execute the handler and capture the result
//    String jsonResult = (String) searchHandler.handle(mockRequest, mockResponse);
//
//    // Assert that the result does not contain the search value 'nomatch'
//    assertFalse(jsonResult.contains("nomatch"));
//  }
//
//  @Test
//  public void fuzzTestSearchHandler() {
//    // Setup for fuzz testing
//    Random random = new Random();
//    List<MyDataClass> testData = new ArrayList<>();
//
//    // Generate random test data
//    for (int i = 0; i < 10; i++) { // Generate 10 random data instances
//      List<String> randomData = new ArrayList<>();
//      for (int j = 0; j < 3; j++) { // Each instance with 3 random strings
//        randomData.add(generateRandomString(random));
//      }
//      testData.add(new MyDataClass(randomData));
//    }
//
//    // Create a stub CSVDataSource with the random test data
//    CSVDataSource<MyDataClass> dataSource = () -> testData;
//
//    // Create mock Request and Response objects
//    Request mockRequest = mock(Request.class);
//    Response mockResponse = mock(Response.class);
//
//    // Simulate random request parameters
//    when(mockRequest.equals("path")).thenReturn(generateRandomString(random));
//    when(mockRequest.equals("value")).thenReturn(generateRandomString(random));
//    when(mockRequest.equals("column")).thenReturn("header" + (random.nextInt(3) + 1));
//
//    // Instantiate the handler with the random data source
//    SearchCSVHandler<MyDataClass> searchHandler = new SearchCSVHandler<>(dataSource);
//
//    // Execute the handler with the mock request and response
//    // Wrap in try-catch to ensure no unexpected exceptions are thrown
//    try {
//      String result = searchHandler.handle(mockRequest, mockResponse);
//      // Assert based on expected behavior with random inputs
//      // This could vary based on how you expect your handler to react to fuzzing
//    } catch (Exception e) {
//      AssertJUnit.fail("Handler should not throw an exception with random inputs.");
//    }
//  }
//
//  // Helper method to generate a random string
//  private String generateRandomString(Random random) {
//    return random
//        .ints(97, 122 + 1)
//        .limit(10)
//        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
//        .toString();
//  }
>>>>>>> 1055eb0ee60f8c51e2971511aaf93a6584dfed5b
// }
