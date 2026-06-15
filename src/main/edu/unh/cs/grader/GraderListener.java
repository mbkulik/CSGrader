package edu.unh.cs.grader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.jspecify.annotations.NonNull;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GraderListener implements TestExecutionListener {

  public void testPlanExecutionFinished(@NonNull TestPlan testPlan) {
    Gson gson = new GsonBuilder()
      .registerTypeAdapter(Double.class, roundingDoubles())
      .setPrettyPrinting()
      .create();
    try (var writer = Files.newBufferedWriter(Path.of("results.json"))) {
      gson.toJson(Collector.getInstance().allData(), writer);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static JsonSerializer<Double> roundingDoubles() {
    return (value, type, context) ->
      new JsonPrimitive(Double.valueOf(String.format("%.2f", value)));
  }
}
