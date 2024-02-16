package edu.brown.cs.student.main.census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * This class contains utility methods for _serializing_ census data to Json and _deserializing_
 * census data from Json
 */
public class CensusDataUtilities {

  private CensusDataUtilities() {}

  /**
   * take a JSON string and convert it into a Java object to deserialize it.
   *
   * @param stateCodesJson
   * @return
   * @throws IOException
   */
  public static Map<String, String> deserializeStateCodes(String stateCodesJson)
      throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, String.class);
    JsonAdapter<Map<String, String>> adapter = moshi.adapter(type);
    return adapter.fromJson(stateCodesJson);
  }

  /**
   * take a Java object and convert it into the JSON string format (or some other known string
   * format) to serialize it
   *
   * @param stateCodes
   * @return
   */
  public static String serializeStateCodes(Map<String, String> stateCodes) {
    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, String.class);
    JsonAdapter<Map<String, String>> adapter = moshi.adapter(type);
    return adapter.toJson(stateCodes);
  }
}
