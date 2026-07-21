package edu.unh.cs.grader;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

// using AnnotationSupport could provide inheritance of Rubric.
//import org.junit.platform.commons.support.AnnotationSupport;

public final class Grader implements BeforeAllCallback, AfterEachCallback {
  public void beforeAll(ExtensionContext context) {
    Class<?> testClass = context.getRequiredTestClass();
    Rubric rubric = testClass.getAnnotation(Rubric.class);
    if (rubric != null)
      Collector.getInstance().addClass(testClass, rubric.points());

    LatePenalty penalty = testClass.getAnnotation(LatePenalty.class);
    if (penalty != null)
      Collector.getInstance().addLatePenalty(penalty);
  }

  public void afterEach(ExtensionContext context) {
    Method testMethod = context.getRequiredTestMethod();
    Class<?> testClass = context.getRequiredTestClass();
    Rubric rubric = testMethod.getAnnotation(Rubric.class);

    if (rubric != null) {
      double score = context.getExecutionException().isPresent() ? 0.0 : rubric.points();
      Collector.getInstance().addTest(testClass, new TestMetaData(rubric, score));
    }
  }
}
