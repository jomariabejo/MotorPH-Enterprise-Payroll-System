package deductionTest;

import com.jomariabejo.motorph.utility.Addition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Testoneplusone {
    private int expectedResult = 3;
    private Addition addition = new Addition(1,1);

    @Test
    void isTwo() {
        int actualResult = addition.add();
        assertEquals(expectedResult, actualResult, "Failed Test");
    }
}
