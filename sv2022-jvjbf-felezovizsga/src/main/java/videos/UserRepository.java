package videos;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class UserRepository {

    private EntityManagerFactory factory;

    public UserRepository(EntityManagerFactory factory) {
        this.factory = factory;
    }

    public User saveUser(User user) {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return user;
        } finally {
            em.close();
        }
    }

    public User findUserWithVideos(Long userId) {
        EntityManager em = factory.createEntityManager();
        try {
            return em.createQuery("select distinct u from User u left join fetch u.videos where u.id =: userId", User.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public void updateUserWithVideo(long userId, Video video) {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            User userFound = em.find(User.class, userId);
            userFound.addVideo(video);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public User updateUserStatus(long userId, UserStatus status) {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            User userFound = em.find(User.class, userId);
            userFound.setUserStatus(status);
            em.getTransaction().commit();
            return userFound;
        } finally {
            em.close();
        }
    }

    public List<User> findUsersWithMoreVideosThan(int amount) {
        EntityManager em = factory.createEntityManager();
        try {
            return em.createQuery("select distinct u from User u left join fetch u.videos where u.videos.size > :amount", User.class)
                    .setParameter("amount", amount)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
