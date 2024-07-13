package deductionTest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Testoneplusone {
    private final int expectedResult = 3;
    private final Addition addition = new Addition(1,1);

    @Test
    void isTwo() {
        int actualResult = addition.add();
        assertEquals(expectedResult, actualResult, "Failed Test");
    }
}
