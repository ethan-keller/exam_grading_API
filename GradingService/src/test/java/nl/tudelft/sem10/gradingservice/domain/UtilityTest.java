package nl.tudelft.sem10.gradingservice.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import nl.tudelft.sem10.gradingservice.domain.utilities.JsonParser;
import nl.tudelft.sem10.gradingservice.domain.utilities.Stats;

class UtilityTest {

  private transient List<Float> items;
  private transient List<Double> itemsDouble;
  // Due to lovely PMD violations
  private transient String test;

  @BeforeEach
  void setUp() {
    items = new ArrayList<>();
    items.add(10.0f);
    items.add(0.0f);
    items.add(5.0f);
    itemsDouble = items.stream().map(x -> (double) x).collect(Collectors.toList());
    test = "test";
  }

  @Test
  void mean() {
    assertEquals(5, Stats.mean(items));
  }

  @Test
  void variance() {
    items.add(1.0f);
    assertEquals(15.5f, Stats.variance(items));
    items.remove(1.0f);
  }

  @Test
  void varianceSingle() {
    assertEquals(0.0f, Stats.variance(Collections.singletonList(10.0f)));
  }

  @Test
  void weightedAverage() {
    List<Double> weights = new ArrayList<>();
    weights.add(0.5);
    weights.add(0.3);
    weights.add(0.2);
    assertEquals(6.0, Stats.weightedAverage(itemsDouble, weights));
  }

  @Test
  void weightedAverageIncorrect() {
    List<Double> weights = new ArrayList<>();
    weights.add(0.5);
    weights.add(0.3);
    assertEquals(-1, Stats.weightedAverage(itemsDouble, weights));
  }

  @Test
  void sum() {
    assertEquals(15, Stats.sum(itemsDouble));
  }

  @Test
  void jsonToString() throws JSONException {
    JSONObject obj = new JSONObject();
    obj.put(test, 1);
    assertTrue(JsonParser.asJsonString(obj).contains("1"));
    assertTrue(JsonParser.asJsonString(obj).contains(test));
  }

  @Test
  void jsonToStringIncorrect() {
    assertThrows(RuntimeException.class,
            () -> JsonParser.asJsonString(null));
  }

  @Test
  void stringToJson() throws JSONException {
    String jsonString = "{\"test\" : 1}";
    JSONObject obj = JsonParser.stringToJson(jsonString);
    assertTrue(obj.has(test));
    assertEquals(1, obj.get(test));
  }
}