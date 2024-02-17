 package edu.brown.cs.student;

 import static org.junit.jupiter.api.Assertions.*;
 import static org.mockito.Mockito.*;

 import edu.brown.cs.student.main.csv.CSVDataSource;
 import edu.brown.cs.student.main.csv.LoadCSVHandler;
 import edu.brown.cs.student.main.csv.MyDataClass;
 import java.util.Collections;
 import java.util.List;
 import org.junit.jupiter.api.Test;
 import org.junit.jupiter.api.extension.ExtendWith;
 import org.mockito.Mock;
 import org.mockito.junit.jupiter.MockitoExtension;
 import spark.Request;
 import spark.Response;

 @ExtendWith(MockitoExtension.class)
 public class LoadCSVHandlerTest {

  @Mock private Request mockRequest;

  @Mock private Response mockResponse;

  @Mock private CSVDataSource<MyDataClass> mockDataSource;

  private final String validFilePath =

 "/Users/ennomonch/Desktop/CS32/server-elin62-emonch/src/main/java/edu/brown/cs/student/main/census/ri_city_town_income_ACS.csv";

  @Test
  void testHandleValidFilePath() throws Exception {
    when(mockRequest.queryParams("filePath")).thenReturn(validFilePath);

    LoadCSVHandler<MyDataClass> handler = new LoadCSVHandler<>(validFilePath, mockDataSource);

    when(mockDataSource.getData())
        .thenReturn(
            List.of(
                new MyDataClass(
                    Collections.singletonList(

 "/Users/ennomonch/Desktop/CS32/server-elin62-emonch/src/main/java/edu/brown/cs/student/main/census/ri_city_town_income_ACS.csv"))));

    Object result = handler.handle(mockRequest, mockResponse);

    assertNotNull(result);
    assertTrue(result instanceof List);
  }

  @Test
  void testHandleInvalidFilePath() throws Exception {
    when(mockRequest.queryParams("filePath")).thenReturn("");

    LoadCSVHandler<MyDataClass> handler = new LoadCSVHandler<>("", mockDataSource);

    Object result = handler.handle(mockRequest, mockResponse);

    assertEquals("File path not provided.", result);
    verify(mockResponse).status(400); // Verify that the correct response status is set
  }
 }
