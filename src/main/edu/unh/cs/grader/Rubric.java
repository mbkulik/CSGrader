package edu.unh.cs.grader;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Rubric {
    String description() default "";
    double points() default 0.0;
    // Controlling test case visibility
    //https://gradescope-autograders.readthedocs.io/en/latest/specs/#controlling-test-case-visibility
    String visibility() default "visible";
}
