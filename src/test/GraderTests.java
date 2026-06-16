import edu.unh.cs.grader.Grader;
import edu.unh.cs.grader.Rubric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(Grader.class)
@Rubric(points = 80)
public class GraderTests {
    @Rubric(description = "this is a small test", points = 2.0)
    @Test
    void smallTest() {
    }

    @Rubric(description = "this is a big test", points = 8.0)
    @Test
    void bigTest() {
    }
}
