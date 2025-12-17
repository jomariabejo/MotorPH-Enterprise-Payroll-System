package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.User;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserRepository extends _AbstractHibernateRepository<User, Integer> {
    public UserRepository() {
        super(User.class);
    }

    /**
     * Override findAll() to eagerly load roleID and employee relationships
     * to avoid LazyInitializationException when displaying users in the table.
     */
    @Override
    public List<User> findAll() {
        Session session = HibernateUtil.openSession();
        try {
            // Use JOIN FETCH to eagerly load roleID and employee
            Query<User> query = session.createQuery(
                    "SELECT DISTINCT u FROM User u " +
                    "LEFT JOIN FETCH u.roleID " +
                    "LEFT JOIN FETCH u.roles " +
                    "LEFT JOIN FETCH u.employee",
                    User.class
            );
            return query.getResultList();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Optional<User> findUserByUsernameAndPassword(String username, String password) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            // Use JOIN FETCH to eagerly load roleID and employee to avoid LazyInitializationException
            Query<User> query = session.createQuery(
                    "SELECT u FROM User u " +
                    "JOIN FETCH u.roleID " +
                    "LEFT JOIN FETCH u.roles " +
                    "JOIN FETCH u.employee " +
                    "WHERE u.username = :username AND u.password = :password",
                    User.class
            );
            query.setParameter("username", username);
            query.setParameter("password", password);

            User user = query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace(); // Logging can be enhanced
            return Optional.empty();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Optional<User> findEmailByEmployeeId(Employee employeeID) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<User> query = session.createNamedQuery("findEmployeeEmail", User.class);
            query.setParameter("employee", employeeID);

            User user = query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace(); // Logging can be enhanced
            return Optional.empty();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
