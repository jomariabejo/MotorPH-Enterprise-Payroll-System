package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Conversation;
import com.jomariabejo.motorph.model.Employee;
import org.hibernate.Session;

import java.util.List;

public class ConversationRepository extends _AbstractHibernateRepository<Conversation, Integer> {
    public ConversationRepository() {
        super(Conversation.class);
    }

    public List<Conversation> findByEmployee(Employee employee) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                    "FROM Conversation WHERE participant1Employee = :employee OR participant2Employee = :employee ORDER BY lastMessageTimestamp DESC", Conversation.class)
                    .setParameter("employee", employee)
                    .setMaxResults(10)
                    .list();
        } finally {
            session.close();
        }
    }

    public long countUnreadByEmployee(Employee employee) {
        Session session = HibernateUtil.openSession();
        try {
            Long count = session.createQuery(
                    "SELECT SUM(unreadCount) FROM Conversation WHERE (participant1Employee = :employee OR participant2Employee = :employee) AND unreadCount > 0", Long.class)
                    .setParameter("employee", employee)
                    .uniqueResult();
            return count != null ? count : 0L;
        } finally {
            session.close();
        }
    }
}
