package edu.unh.cs.grader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

final class Submission {
  private Submission() {
  }

  static final Path METADATA = Path.of("/autograder/submission_metadata.json");

  // 0 when on time, or when not running under Gradescope
  static int daysLate(Path metadata) {
    if (!Files.exists(metadata))
      return 0;

    try (var reader = Files.newBufferedReader(metadata)) {
      JsonObject o = JsonParser.parseReader(reader).getAsJsonObject();

      OffsetDateTime submitted = OffsetDateTime.parse(o.get("created_at").getAsString());

      // no group submissions in CS416, so the first user's due date is the due date
      OffsetDateTime due = OffsetDateTime.parse(o.get("users").getAsJsonArray()
        .get(0).getAsJsonObject().get("assignment").getAsJsonObject()
        .get("due_date").getAsString());

      // whole seconds, so that any part of a day late counts as a full day
      long seconds = ChronoUnit.SECONDS.between(due, submitted);
      return seconds <= 0 ? 0 : (int) Math.ceilDiv(seconds, 86400L);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
