package iss.repository;

import iss.exceptions.DatabaseException;
import iss.model.Club;
import iss.model.Player;
import iss.model.User;
import iss.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class PlayerRepository implements IRepository<Long, Player> {
    @Override
    public Player save(Player entity) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Player savedPlayer = null;

        try {
            em.getTransaction().begin();

            if (entity.getId() == null) {
                em.persist(entity);
                savedPlayer = entity;
            } else {
                savedPlayer = em.merge(entity);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();
            throw new DatabaseException("Error while saving player");
        } finally {
            em.close();
        }

        return savedPlayer;
    }

    @Override
    public Player update(Player entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Cannot update a player with null id");
        }

        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Player updatedPlayer = null;

        try {
            em.getTransaction().begin();
            updatedPlayer = em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();
            throw new DatabaseException("Error while updating player");
        } finally {
            em.close();
        }

        return updatedPlayer;
    }

    @Override
    public boolean delete(Player entity) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        boolean success = false;

        try {
            em.getTransaction().begin();

            Player player = em.find(Player.class, entity.getId());
            if (player != null) {
                em.remove(player);
                success = true;
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();
            throw new DatabaseException("Error while deleting player");
        } finally {
            em.close();
        }

        return success;
    }

    @Override
    public List<Player> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "select p from Player p";
            TypedQuery<Player> query = em.createQuery(jpql, Player.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Error while finding all players");
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Player> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return Optional.ofNullable(em.find(Player.class, id));
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Error while finding player");
        } finally {
            em.close();
        }
    }

    public List<Player> findByClub(Club club) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            return em.createQuery("SELECT p FROM Player p where p.club =  :club", Player.class)
                    .setParameter("club", club)
                    .getResultList();
        }  catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Error while finding players");
        } finally {
            em.close();
        }
    }
}
