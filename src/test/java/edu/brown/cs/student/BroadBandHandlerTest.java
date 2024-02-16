package edu.brown.cs.student;


import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.census.BroadbandHandler;
import edu.brown.cs.student.main.census.CensusDataSource;
import edu.brown.cs.student.main.census.DataSourceException;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.Response;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
public class BroadBandHandlerTest {
    private BroadbandHandler broadbandHandler;
    private CensusDataSource mockDataSource;
    private Request mockRequest;
    private Response mockResponse;

    @Before
    public void setUp() {
        // Mock the dependencies
        mockDataSource = mock(CensusDataSource.class);
        mockRequest = mock(Request.class);
        mockResponse = mock(Response.class);

        // Instantiate the handler with mocked data source
        broadbandHandler = new BroadbandHandler(mockDataSource);
    }

    @Test
    public void testHandle() throws Exception {
        // Setup the mock behavior
        when(mockRequest.queryParams("state")).thenReturn("Rhode Island");
        when(mockRequest.queryParams("county")).thenReturn("Providence");
        when(mockDataSource.requestBroadbandPercentage("Rhode Island", "Providence")).thenReturn("75%");

        // Execute the handle method
        Object result = broadbandHandler.handle(mockRequest, mockResponse);

        // Verify the results

        assertNotNull(result);
        assertTrue(result instanceof String); // Ensure the result is a string (JSON response)

        // Verify that the data source was called with expected parameters
        verify(mockDataSource).requestBroadbandPercentage("Rhode Island", "Providence");


    }
    @Test
    public void testHandleWithDataSourceException() throws Exception {
        // Setup mock to throw an exception
        when(mockRequest.queryParams("state")).thenReturn("Invalid State");
        when(mockRequest.queryParams("county")).thenReturn("Invalid County");
        when(mockDataSource.requestBroadbandPercentage(anyString(), anyString())).thenThrow(new DataSourceException("Data not found"));

        // Execute the handle method
        Object result = broadbandHandler.handle(mockRequest, mockResponse);

        // Verify response
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertTrue(((String) result).contains("Error occurred"));

        // Verify HTTP status set to 500
        verify(mockResponse).status(500);
    }
    @Test
    public void testHandleWithMissingParameters() throws Exception {
        // Do not set up any queryParams

        // Execute the handle method
        Object result = broadbandHandler.handle(mockRequest, mockResponse);

        // Since your handler does not currently handle missing parameters explicitly,
        // this test expects normal execution. Adjust according to your implementation.
        assertNotNull(result);

        // To improve, your handler could check for parameter existence and return appropriate errors.
    }

    @Test
    public void testHandleResponseStructure() throws Exception {
        // Setup the mock behavior
        when(mockRequest.queryParams("state")).thenReturn("Rhode Island");
        when(mockRequest.queryParams("county")).thenReturn("Providence");
        when(mockDataSource.requestBroadbandPercentage("Rhode Island", "Providence")).thenReturn("75%");

        // Execute the handle method
        String result = (String) broadbandHandler.handle(mockRequest, mockResponse);

        // Parse the result to a Map
        Moshi moshi = new Moshi.Builder().build();
        Type mapType = Types.newParameterizedType(Map.class, String.class, Object.class);
        JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapType);
        Map<String, Object> resultMap = adapter.fromJson(result);

        // Verify the structure and content of the response
        assertNotNull(resultMap);
        assertTrue(resultMap.containsKey("Date and Time of Data Retrieval"));
        assertEquals("Rhode Island", resultMap.get("State Name Received"));
        assertEquals("Providence", resultMap.get("County Name Received"));
        assertEquals("75%", resultMap.get("Broadband Percentage"));
    }
}
