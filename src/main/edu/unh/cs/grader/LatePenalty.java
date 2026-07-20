package edu.unh.cs.grader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LatePenalty {
  // points deducted per day late; index 0 = 1 day late, the last entry applies
  // to anything later. {5, 15, 30} matches the CS416 policy.
  double[] points() default {5.0, 15.0, 30.0};

  // Controlling test case visibility
  //https://gradescope-autograders.readthedocs.io/en/latest/specs/#controlling-test-case-visibility
  String visibility() default "visible";
}
