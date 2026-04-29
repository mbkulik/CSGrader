package edu.unh.cs.grader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class Collector {
  private Collector() {
  }

  private static final Collector instance = new Collector();

  public static Collector getInstance() {
    return instance;
  }

  // data for all tests in each class
  private final Map<Class<?>, List<TestMetaData>> metaData = new HashMap<>();

  // total weight of each class
  private final Map<Class<?>, Double> weights = new HashMap<>();

  public void addTest(Class<?> testClass, TestMetaData t) {
    metaData.computeIfAbsent(testClass, __ -> new ArrayList<>()).add(t);
  }

  public void addClass(Class<?> testClass, double weight) {
    weights.put(testClass, weight);
  }

  // serializable data, for Json
  public Map<String, Object> allData() {
    ArrayList<TestMetaData> allMetaData = new ArrayList<>();
    double score = 0.0;
    for (var entry : metaData.entrySet()) {
      List<TestMetaData> data = entry.getValue();
      double weight = weights.getOrDefault(entry.getKey(), 100.0);
      double total = data.stream().mapToDouble(TestMetaData::max_score).sum();
      double scale = weight / total;
      for (var test : entry.getValue()) {
        var scaledTest = test.scale(scale);
        score += scaledTest.score();
        allMetaData.add(scaledTest);
      }
    }
    return Map.of(
      "tests", allMetaData,
      "score", score,
      "stdout_visibility", "visible",
      "visibility", "visible");
  }
}
