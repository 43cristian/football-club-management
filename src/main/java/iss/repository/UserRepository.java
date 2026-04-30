package iss.repository;

import iss.exceptions.DatabaseException;
import iss.model.Club;
import iss.model.User;
import iss.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class UserRepository implements IRepository<Long, User> {

    @Override
    public User save(User entity) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        User savedUser = null;

        try {
            em.getTransaction().begin();

            if (entity.getId() == null) {
                em.persist(entity);
                savedUser = entity;
            } else {
                savedUser = em.merge(entity);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();
            throw new DatabaseException("Error while saving user");
        } finally {
            em.close();
        }

        return savedUser;
    }

    @Override
    public User update(User entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Cannot update a user with null id");
        }

        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        User updatedUser = null;

        try {
            em.getTransaction().begin();
            updatedUser = em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();
            throw new DatabaseException("Error while updating user");
        } finally {
            em.close();
        }

        return updatedUser;
    }

    @Override
    public boolean delete(User entity) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        boolean success = false;

        try {
            em.getTransaction().begin();

            User user = em.find(User.class, entity.getId());
            if (user != null) {
                em.remove(user);
                success = true;
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();
            throw new DatabaseException("Error while deleting user");
        } finally {
            em.close();
        }

        return success;
    }

    @Override
    public List<User> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "select u from User u";
            TypedQuery<User> query = em.createQuery(jpql, User.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Error while finding all users");
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return Optional.ofNullable(em.find(User.class, id));
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Error while finding user");
        }
        finally {
            em.close();
        }
    }

    public Optional<User> findByUsername(String username) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            User user = em.createQuery("SELECT u FROM User u where u.username =  :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Error while finding user");
        } finally {
            em.close();
        }
    }

    public List<User> findByClub(Club club) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return em.createQuery("SELECT u FROM User u where u.club =  :club", User.class)
                    .setParameter("club", club)
                    .getResultList();
        }  catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Error while finding users");
        } finally {
            em.close();
        }
    }
}
