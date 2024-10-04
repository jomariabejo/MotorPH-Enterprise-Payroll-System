package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Position;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;

public class PositionRepository extends _AbstractHibernateRepository<Position, Integer> {
    public PositionRepository() {
        super(Position.class);
    }

    public Position findByPositionName(String positionName) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<Position> query = session.createNamedQuery("fetchPosition", Position.class);

            query.setParameter("positionName", positionName);

            Position position = query.getSingleResult();
            return (position);
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

}
