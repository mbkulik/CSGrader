import edu.unh.cs.grader.Grader;
import edu.unh.cs.grader.Rubric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(Grader.class)
@Rubric(points = 20)
public class MoreGraderTests {
    @Rubric(description = "this is a small test", points = 1.0)
    @Test
    void smallTest() {
    }

    @Rubric(description = "this is a big test", points = 3.0)
    @Test
    void bigTest() {
    }
}
