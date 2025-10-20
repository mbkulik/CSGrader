package edu.unh.cs.grader;

import java.util.ArrayList;

@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "unused"})
class Collector {
    private static final Collector instance = new Collector();

    private final ArrayList<TestMetaData> tests = new ArrayList<>();
    private double score = 0;
    private final String stdout_visibility = "visible"; // By default
    private final String visibility = "visible"; // By default

    //private constructor to avoid client applications to use constructor
    private Collector(){}

    public static Collector getInstance(){
        return instance;
    }

    public void addTest(TestMetaData t) {
        tests.add(t);
        score += t.score;
    }
}
