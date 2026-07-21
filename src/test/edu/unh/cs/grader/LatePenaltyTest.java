package edu.unh.cs.grader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LatePenaltyTest {

  @LatePenalty
  private static final class DefaultPolicy {
  }

  @LatePenalty(points = {2.0})
  private static final class SingleStepPolicy {
  }

  private static final LatePenalty DEFAULT =
    DefaultPolicy.class.getAnnotation(LatePenalty.class);

  // a submission_metadata.json holding only the fields Submission reads
  private static Path fixture(Path dir, String due, String created) throws IOException {
    String json = """
      {
        "created_at": "%s",
        "users": [{"assignment": {"due_date": "%s"}}]
      }
      """.formatted(created, due);
    return Files.writeString(dir.resolve("submission_metadata.json"), json);
  }

  @Test
  void missingMetadataIsOnTime(@TempDir Path dir) {
    assertEquals(0, Submission.daysLate(dir.resolve("nope.json")));
  }

  @Test
  void onTimeSubmission(@TempDir Path dir) throws IOException {
    var f = fixture(dir, "2026-03-01T23:59:00-05:00", "2026-03-01T18:12:00-05:00");
    assertEquals(0, Submission.daysLate(f));
  }

  @Test
  void submissionExactlyAtTheDeadline(@TempDir Path dir) throws IOException {
    var f = fixture(dir, "2026-03-01T23:59:00-05:00", "2026-03-01T23:59:00-05:00");
    assertEquals(0, Submission.daysLate(f));
  }

  @Test
  void anyPartOfADayCountsAsAWholeDay(@TempDir Path dir) throws IOException {
    var f = fixture(dir, "2026-03-01T23:59:00-05:00", "2026-03-02T00:30:00-05:00");
    assertEquals(1, Submission.daysLate(f));
  }

  @Test
  void exactlyOneDayLateIsOneDay(@TempDir Path dir) throws IOException {
    var f = fixture(dir, "2026-03-01T23:59:00-05:00", "2026-03-02T23:59:00-05:00");
    assertEquals(1, Submission.daysLate(f));
  }

  @Test
  void justPastOneDayLateIsTwoDays(@TempDir Path dir) throws IOException {
    var f = fixture(dir, "2026-03-01T23:59:00-05:00", "2026-03-03T00:00:00-05:00");
    assertEquals(2, Submission.daysLate(f));
  }

  @Test
  void offsetsAreRespected(@TempDir Path dir) throws IOException {
    // 20:00 UTC is 15:00 EST, well inside the deadline
    var f = fixture(dir, "2026-03-01T23:59:00-05:00", "2026-03-01T20:00:00Z");
    assertEquals(0, Submission.daysLate(f));
  }

  @Test
  void threeDaysLate(@TempDir Path dir) throws IOException {
    var f = fixture(dir, "2026-03-01T23:59:00-05:00", "2026-03-04T22:00:00-05:00");
    assertEquals(3, Submission.daysLate(f));
  }

  @Test
  void deductionFollowsThePolicySteps() {
    assertEquals(0.0, Collector.deduction(DEFAULT, 0));
    assertEquals(5.0, Collector.deduction(DEFAULT, 1));
    assertEquals(15.0, Collector.deduction(DEFAULT, 2));
    assertEquals(30.0, Collector.deduction(DEFAULT, 3));
  }

  @Test
  void lateBeyondTheLastStepKeepsTheLastDeduction() {
    assertEquals(30.0, Collector.deduction(DEFAULT, 4));
    assertEquals(30.0, Collector.deduction(DEFAULT, 40));

    var single = SingleStepPolicy.class.getAnnotation(LatePenalty.class);
    assertEquals(2.0, Collector.deduction(single, 1));
    assertEquals(2.0, Collector.deduction(single, 9));
  }

  @Test
  void noEntryWhenTheSubmissionIsOnTime() {
    assertNull(Collector.lateEntry(DEFAULT, 0));
  }

  @Test
  void noEntryWhenNoClassDeclaredAPolicy() {
    assertNull(Collector.lateEntry(null, 3));
  }

  @Test
  void lateEntryDeductsAndDoesNotAddPointsPossible() {
    TestMetaData late = Collector.lateEntry(DEFAULT, 2);
    assertNotNull(late);
    // negative, so allData can add it straight into the running score
    assertEquals(-15.0, late.score());
    assertEquals(0.0, late.max_score());
    assertEquals("visible", late.visibility());
    assertTrue(late.name().contains("2 day(s) late"), late.name());
  }
}
