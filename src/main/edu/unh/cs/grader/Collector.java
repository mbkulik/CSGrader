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

  // deduction policy for a late submission, if any class declared one
  private LatePenalty penalty;

  public void addLatePenalty(LatePenalty p) {
    this.penalty = p;
  }

  public void addTest(Class<?> testClass, TestMetaData t) {
    metaData.computeIfAbsent(testClass, __ -> new ArrayList<>()).add(t);
  }

  public void addClass(Class<?> testClass, double weight) {
    weights.put(testClass, weight);
  }

  // points off for a submission that is daysLate days late, 0 when on time
  static double deduction(LatePenalty penalty, int daysLate) {
    double[] points = penalty.points();
    if (daysLate <= 0 || points.length == 0)
      return 0.0;
    // anything past the last entry keeps the last entry's deduction
    return points[Math.min(daysLate, points.length) - 1];
  }

  // the entry to append for a late submission, or null when nothing is owed.
  // max_score is 0 so Gradescope shows a pure deduction rather than inflating
  // the points possible.
  static TestMetaData lateEntry(LatePenalty penalty, int daysLate) {
    if (penalty == null)
      return null;
    double points = deduction(penalty, daysLate);
    if (points <= 0.0)
      return null;
    return new TestMetaData(
      "Late Penalty (" + daysLate + " day(s) late)",
      penalty.visibility(), -points, 0.0);
  }

  // serializable data, for Json
  public Map<String, Object> allData() {
    ArrayList<TestMetaData> allMetaData = new ArrayList<>();
    double score = 0.0;
    for (var entry : metaData.entrySet()) {
      List<TestMetaData> data = entry.getValue();
      double total = data.stream().mapToDouble(TestMetaData::max_score).sum();
      double weight = weights.getOrDefault(entry.getKey(), total);
      double scale = weight / total;
      for (var test : entry.getValue()) {
        var scaledTest = test.scale(scale);
        score += scaledTest.score();
        allMetaData.add(scaledTest);
      }
    }

    // appended after scaling, so the deduction stays in absolute points
    TestMetaData late = lateEntry(penalty, Submission.daysLate(Submission.METADATA));
    if (late != null) {
      allMetaData.add(late);
      score += late.score();
    }

    return Map.of(
      "tests", allMetaData,
      "score", score,
      "stdout_visibility", "visible",
      "visibility", "visible");
  }
}
