package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.OvertimeRequest;
import com.jomariabejo.motorph.repository.OvertimeRequestRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class OvertimeRequestService {

    private final OvertimeRequestRepository overtimeRequestRepository;

    public OvertimeRequestService(OvertimeRequestRepository overtimeRequestRepository) {
        this.overtimeRequestRepository = overtimeRequestRepository;
    }

    public OvertimeRequest getOvertimeRequestById(Integer id) {
        return overtimeRequestRepository.findById(id);
    }

    public List<OvertimeRequest> getAllOvertimeRequests() {
        return overtimeRequestRepository.findAll();
    }

    public void saveOvertimeRequest(OvertimeRequest overtimeRequest) {
        overtimeRequestRepository.save(overtimeRequest);
    }

    public void updateOvertimeRequest(OvertimeRequest overtimeRequest) {
        overtimeRequestRepository.update(overtimeRequest);
    }

    public void deleteOvertimeRequest(OvertimeRequest overtimeRequest) {
        overtimeRequestRepository.delete(overtimeRequest);
    }

    public Optional<OvertimeRequest> hasOvertimeRequestForDate(Employee employee, Date value) {
        return overtimeRequestRepository.hasOvertimeRequestForDate(employee, value);
    }

    public List<OvertimeRequest> getOvertimeRequestsByEmployeeId(Employee employee, Integer month, Integer year, String status) {
        try (Session session = HibernateUtil.openSession()) {
            session.beginTransaction();

            // HQL query with multiple parameters
            String hql = "FROM OvertimeRequest WHERE employeeID = :employee"
                    + " AND MONTH(overtimeDate) = :month"
                    + " AND YEAR(overtimeDate) = :year"
                    + " AND status = :status";

            Query<OvertimeRequest> query = session.createQuery(hql, OvertimeRequest.class);
            query.setParameter("employee", employee);
            query.setParameter("month", month);
            query.setParameter("year", year);
            query.setParameter("status", status);

            List<OvertimeRequest> results = query.getResultList();
            session.getTransaction().commit();
            return results;
        }
    }

    public List<Integer> getOvertimeRequestYears(Employee employee) {
        try (Session session = HibernateUtil.openSession()) {
            session.beginTransaction();

            // HQL query to get distinct years from dateRequested
            String hql = "SELECT DISTINCT year(dateRequested) FROM OvertimeRequest WHERE employeeID = :employee";
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("employee", employee);

            List<Integer> years = query.getResultList();
            session.getTransaction().commit();
            return years;
        }
    }
}
