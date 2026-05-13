package dao;

import config.JPAUtil;
import java.util.List;
import javax.persistence.EntityManager;
import models.Book;

public class BookDAO {

    public List<Integer> getAllbooksids() {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.createQuery(
                    "SELECT b.bookId FROM Book b",
                    Integer.class
            ).getResultList();

        } finally {

            em.close();

        }

    }

    public Book findById(int id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            return em.find(Book.class, id);

        } finally {

            em.close();

        }

    }

}