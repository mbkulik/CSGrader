package edu.unh.cs.grader;

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class Grader implements  AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext context) {
        Method m = context.getRequiredTestMethod();
        Rubric r = m.getAnnotation(Rubric.class);

        Collector c = Collector.getInstance();

        if (r != null) {
            TestMetaData t = new TestMetaData();
            t.name = r.description();

            if (context.getExecutionException().isPresent()) { // test failed?
                t.score = 0;
            } else { //  test succeeded
                t.score = r.points();
            }

            t.max_score = r.points();
            t.visibility = r.visibility();
            t.stdout_visibility = r.stdout_visibility();

            c.tests.add(t);
        }

    }
}
