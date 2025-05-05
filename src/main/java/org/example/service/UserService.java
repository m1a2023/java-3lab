package org.example.service;

import org.example.db.SessionUtil;
import org.example.models.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class UserService {

    public User getUser(Long id) {
        try (Session s = SessionUtil.getSessionFactory().openSession()) {
            return s.get(User.class, id);
        }
    }

    public void addUser(User u) {
        Transaction t = null;
        try (Session s = SessionUtil.getSessionFactory().openSession()) {
            t = s.beginTransaction();
            s.persist(u);
            t.commit();
        } catch (HibernateException e) {
            if (t != null) {
                t.rollback();
            }
            e.printStackTrace();
        }
    }

    public User found(String username) {
        String q = "SELECT u FROM User u WHERE u.username = :username";
        User found = null;
        try (Session s = SessionUtil.getSessionFactory().openSession()) {
            found = s.createQuery(q, User.class)
                    .setParameter("username", username)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }

        return found;
    }

    public void closeConnection() {
        SessionUtil.closeSessionFactory();
    }
}

