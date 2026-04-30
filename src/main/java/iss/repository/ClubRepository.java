package iss.repository;

import iss.exceptions.DatabaseException;
import iss.model.Club;
import iss.model.User;
import iss.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class ClubRepository implements IRepository<Long, Club> {
    @Override
    public Club save(Club entity) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Club savedClub = null;

        try {
            em.getTransaction().begin();

            if (entity.getId() == null) {
                em.persist(entity);
                savedClub = entity;
            } else {
                savedClub = em.merge(entity);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();
            throw new DatabaseException("Error while saving club");
        } finally {
            em.close();
        }

        return savedClub;
    }

    @Override
    public Club update(Club entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Cannot update a club with null id");
        }

        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Club updatedClub = null;

        try {
            em.getTransaction().begin();
            updatedClub = em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();
            throw new DatabaseException("Error while updating club");
        } finally {
            em.close();
        }

        return updatedClub;
    }

    @Override
    public boolean delete(Club entity) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        boolean success = false;

        try {
            em.getTransaction().begin();

            Club club = em.find(Club.class, entity.getId());
            if (club != null) {
                em.remove(club);
                success = true;
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();
            throw new DatabaseException("Error while deleting club");
        } finally {
            em.close();
        }

        return success;
    }

    @Override
    public List<Club> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "select c from Club c";
            TypedQuery<Club> query = em.createQuery(jpql, Club.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Error while finding all clubs");
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Club> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return Optional.ofNullable(em.find(Club.class, id));
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Error while finding club");
        } finally {
            em.close();
        }
    }
}
