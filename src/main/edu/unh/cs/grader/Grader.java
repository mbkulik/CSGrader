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
            t.max_score = r.points();
            t.visibility = r.visibility();

            if (context.getExecutionException().isPresent()) { // test failed?
                t.score = 0;
            } else { //  test succeeded
                t.score = r.points();
            }

            c.tests.add(t);
            c.score += t.score;
        }
    }
}
