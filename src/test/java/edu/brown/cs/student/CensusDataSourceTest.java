 package edu.brown.cs.student;

 import static org.junit.Assert.*;
 import static org.mockito.Mockito.*;

 import edu.brown.cs.student.main.census.CensusDataSource;
 import edu.brown.cs.student.main.census.DataSourceException;
 import java.io.ByteArrayInputStream;
 import java.io.IOException;
 import java.net.HttpURLConnection;
 import org.junit.Before;
 import org.junit.Test;
 import org.mockito.Mock;

 public class CensusDataSourceTest {
  private CensusDataSource dataSource;

  @Mock private HttpURLConnection mockHttpURLConnection;

  @Before
  public void setUp() throws Exception {
    //        MockitoAnnotations.initMocks(this);
    //        dataSource = new CensusDataSource() {
    //            @Override
    //            protected HttpURLConnection connect(URL url) {
    //                return mockHttpURLConnection;
    //            }
    //        };
  }

  @Test
  public void testRequestBroadbandPercentage() throws IOException, DataSourceException {
    // Assuming state codes have been fetched successfully
    String mockStateCodesResponse = "[[\"NAME\",\"state\"],[\"Rhode Island\",\"44\"]]";
    when(mockHttpURLConnection.getInputStream())
        .thenReturn(new ByteArrayInputStream(mockStateCodesResponse.getBytes()));
    when(mockHttpURLConnection.getResponseCode()).thenReturn(200);
    dataSource.requestStateCodes(); // Populate state codes

    // Mocking HTTP connection for county codes request
    String mockCountyCodesResponse =
        "[[\"NAME\",\"state\",\"county\"],[\"Providence County\",\"44\",\"001\"]]";
    when(mockHttpURLConnection.getInputStream())
        .thenReturn(new ByteArrayInputStream(mockCountyCodesResponse.getBytes()));

    // Assume that the request for broadband percentage is successful
    String mockBroadbandResponse =
        "[[\"NAME\",\"S2802_C03_022E\",\"state\",\"county\"],[\"Providence County, " +
                "Rhode Island\",\"75%\",\"44\",\"001\"]]";
    when(mockHttpURLConnection.getInputStream())
        .thenReturn(new ByteArrayInputStream(mockBroadbandResponse.getBytes()));

    // Act
    String broadbandPercentage =
        dataSource.requestBroadbandPercentage("Rhode Island", "Providence");

    // Assert
    assertEquals("Broadband percentage should be 75%", "75%", broadbandPercentage);
  }
 }
