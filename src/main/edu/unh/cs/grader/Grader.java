package edu.unh.cs.grader;

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class Grader implements AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext context) {
        Method m = context.getRequiredTestMethod();
        Rubric r = m.getAnnotation(Rubric.class);

        if (r != null) {
            TestMetaData t = new TestMetaData();
            t.name       = r.description();
            t.max_score  = r.points();
            t.visibility = r.visibility();
            t.score      = context.getExecutionException().isPresent() ? 0 // test failed?
                : r.points(); //  test succeeded

            Collector.getInstance().addTest(t);
        }
    }
}
