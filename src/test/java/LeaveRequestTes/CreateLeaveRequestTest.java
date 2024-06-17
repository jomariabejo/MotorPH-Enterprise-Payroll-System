package LeaveRequestTes;

import com.jomariabejo.motorph.entity.LeaveRequest;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CreateLeaveRequestTest {

    @Test
    public void testDefaultConstructor() {
        LeaveRequest leaveRequest = new LeaveRequest();
        assertEquals(0, leaveRequest.getLeaveRequestID());
        assertEquals(0, leaveRequest.getEmployeeID());
        assertEquals(0, leaveRequest.getLeaveRequestCategoryId());
        assertNull(leaveRequest.getStartDate());
        assertNull(leaveRequest.getEndDate());
        assertNull(leaveRequest.getDateCreated());
        assertNull(leaveRequest.getReason());
        assertNull(leaveRequest.getStatus());
    }

    @Test
    public void testConstructorWithAllParams() {
        int leaveRequestID = 1;
        int employeeID = 123;
        int leaveRequestCategoryId = 456;
        Date startDate = Date.valueOf("2023-06-15");
        Date endDate = Date.valueOf("2023-06-20");
        Timestamp dateCreated = Timestamp.valueOf("2023-06-14 10:10:10.0");
        String reason = "Medical leave";
        LeaveRequest.LeaveRequestStatus status = LeaveRequest.LeaveRequestStatus.Pending;

        LeaveRequest leaveRequest = new LeaveRequest(leaveRequestID, employeeID, leaveRequestCategoryId, startDate, endDate, dateCreated, reason, status);

        assertEquals(leaveRequestID, leaveRequest.getLeaveRequestID());
        assertEquals(employeeID, leaveRequest.getEmployeeID());
        assertEquals(leaveRequestCategoryId, leaveRequest.getLeaveRequestCategoryId());
        assertEquals(startDate, leaveRequest.getStartDate());
        assertEquals(endDate, leaveRequest.getEndDate());
        assertEquals(dateCreated, leaveRequest.getDateCreated());
        assertEquals(reason, leaveRequest.getReason());
        assertEquals(status, leaveRequest.getStatus());
    }

    @Test
    public void testConstructorWithoutLeaveRequestID() {
        int employeeID = 123;
        int leaveRequestCategoryId = 456;
        Date startDate = Date.valueOf("2023-06-15");
        Date endDate = Date.valueOf("2023-06-20");
        Timestamp dateCreated = Timestamp.valueOf("2023-06-14 10:10:10.0");
        String reason = "Vacation";
        LeaveRequest.LeaveRequestStatus status = LeaveRequest.LeaveRequestStatus.Approved;

        LeaveRequest leaveRequest = new LeaveRequest(employeeID, leaveRequestCategoryId, startDate, endDate, dateCreated, reason, status);

        assertEquals(0, leaveRequest.getLeaveRequestID()); // Assuming default value is 0
        assertEquals(employeeID, leaveRequest.getEmployeeID());
        assertEquals(leaveRequestCategoryId, leaveRequest.getLeaveRequestCategoryId());
        assertEquals(startDate, leaveRequest.getStartDate());
        assertEquals(endDate, leaveRequest.getEndDate());
        assertEquals(dateCreated, leaveRequest.getDateCreated());
        assertEquals(reason, leaveRequest.getReason());
        assertEquals(status, leaveRequest.getStatus());
    }

    @Test
    public void testSettersAndGetters() {
        LeaveRequest leaveRequest = new LeaveRequest();

        leaveRequest.setLeaveRequestID(1);
        assertEquals(1, leaveRequest.getLeaveRequestID());

        leaveRequest.setEmployeeID(123);
        assertEquals(123, leaveRequest.getEmployeeID());

        leaveRequest.setLeaveRequestCategoryId(456);
        assertEquals(456, leaveRequest.getLeaveRequestCategoryId());

        Date startDate = Date.valueOf("2023-06-15");
        leaveRequest.setStartDate(startDate);
        assertEquals(startDate, leaveRequest.getStartDate());

        Date endDate = Date.valueOf("2023-06-20");
        leaveRequest.setEndDate(endDate);
        assertEquals(endDate, leaveRequest.getEndDate());

        Timestamp dateCreated = Timestamp.valueOf("2023-06-14 10:10:10.0");
        leaveRequest.setDateCreated(dateCreated);
        assertEquals(dateCreated, leaveRequest.getDateCreated());

        String reason = "Personal leave";
        leaveRequest.setReason(reason);
        assertEquals(reason, leaveRequest.getReason());

        LeaveRequest.LeaveRequestStatus status = LeaveRequest.LeaveRequestStatus.Disapproved;
        leaveRequest.setStatus(status);
        assertEquals(status, leaveRequest.getStatus());
    }
}
