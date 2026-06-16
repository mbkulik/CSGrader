package edu.unh.cs.grader;

record TestMetaData(String name, String visibility, double score, double max_score) {
  public TestMetaData(Rubric rubric, double score) {
    this(rubric.description(), rubric.visibility(), score, rubric.points());
  }

  public TestMetaData scale(double scale) {
    return new TestMetaData(name, visibility, score * scale, max_score * scale);
  }
}
