package edu.unh.cs.grader;

import java.util.ArrayList;

class Collector {

    private static final Collector instance = new Collector();
    ArrayList<TestMetaData> tests = new ArrayList<>();

    //private constructor to avoid client applications to use constructor
    private Collector(){}

    public static Collector getInstance(){
        return instance;
    }
}
