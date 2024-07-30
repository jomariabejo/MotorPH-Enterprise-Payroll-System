package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.User;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;

public class UserRepository extends _AbstractHibernateRepository<User, Integer> {
    public UserRepository() {
        super(User.class);
    }

    public Optional<User> findUser(String username, String password) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<User> query = session.createNamedQuery("findUser", User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

            User user = query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            // No user found with given credentials
            return Optional.empty();
        } catch (Exception e) {
            // Handle other exceptions, such as database errors
            e.printStackTrace(); // Logging can be enhanced
            return Optional.empty();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
