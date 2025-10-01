package edu.unh.cs.grader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GraderListener implements TestExecutionListener {

    public void testPlanExecutionFinished(TestPlan testPlan) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(Collector.getInstance());

        try {
            Files.writeString(Path.of("results.json"), json);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
