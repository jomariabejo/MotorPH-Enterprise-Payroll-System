import com.jomariabejo.motorph.query.QueryPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TextReader {
    @Test
    void checkEmployeeCount() {
        assertEquals("SELECT COUNT(employee_id) AS NumberOfEmployees FROM employee;", QueryPath.Employee.COUNT_EMPLOYEES);
    }
}
