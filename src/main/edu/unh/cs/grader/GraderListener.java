package edu.unh.cs.grader;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

public class GraderListener implements TestExecutionListener {

    public void testPlanExecutionFinished(TestPlan testPlan) {

        Collector c = Collector.getInstance();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(c);

        // Print the total score
        System.err.println("STUDENTS TOTAL SCORE:   " + c.score);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("results.json"));
            writer.write(s);
            writer.close();

        } catch(IOException e) {
            // swallow error :(
        }
    }
}
