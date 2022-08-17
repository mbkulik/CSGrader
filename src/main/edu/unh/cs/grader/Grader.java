package edu.unh.cs.grader;

import java.lang.reflect.*;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.extension.*;

public class Grader implements TestWatcher, AfterAllCallback {

    private class Test {
        String name;
        String visibility;
        String stdout_visibility;
        int score;
        int max_score;
    }


    private class AllTests {


        ArrayList<Test> tests = new ArrayList<>();
    }

    AllTests suite = new AllTests();

    @Override
    public void testSuccessful(ExtensionContext context) {

        Method m = context.getRequiredTestMethod();
        Rubric r =m.getAnnotation(Rubric.class);

        if(r != null) {
            Test t = new Test();
            t.name = r.description();
            t.score = r.points();
            t.max_score = r.points();
            t.visibility = r.visibility();
            t.stdout_visibility = r.stdout_visibility();
            suite.tests.add(t);
        }

    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {

        Method m = context.getRequiredTestMethod();
        Rubric r =m.getAnnotation(Rubric.class);

        if(r != null) {
            Test t = new Test();
            t.name = r.description();
            t.score = 0;
            t.max_score = r.points();
            t.visibility = r.visibility();
            t.stdout_visibility = r.stdout_visibility();
            suite.tests.add(t);
        }
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(suite);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("results.json"));
            writer.write(s);
            writer.close();

        } catch(IOException e) {
            // swallow error :(
        }


    }
}
