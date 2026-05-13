package dao;

import config.JPAUtil;
import java.util.List;
import javax.persistence.EntityManager;
import models.Borrow;

public class BorrowDAO {

    public List<Borrow> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT b FROM Borrow b",
                    Borrow.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public boolean insert(Borrow b) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(b);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        return false;
    }

    public boolean update(Borrow b) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(b);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        return false;
    }

    public boolean delete(Borrow b) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Borrow bor = em.find(Borrow.class, b.getBorrowId());
            em.remove(bor);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        return false;
    }

    public List<Borrow> getBorrowedBooks() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT b FROM Borrow b WHERE b.status = false",
                    Borrow.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Borrow> searchByIds(int bookId, int studentId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT b FROM Borrow b " +
                    "WHERE b.book.bookId = :bookId " +
                    "AND b.student.studentId = :studentId",
                    Borrow.class
            )
                    .setParameter("bookId", bookId)
                    .setParameter("studentId", studentId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

}