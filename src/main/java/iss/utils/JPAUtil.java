package iss.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class JPAUtil {
    private static EntityManagerFactory emf;

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            try {
                String dbUrl = System.getenv("FCMDB_URL");
                String dbUser = System.getenv("FCMDB_USER");
                String dbPass = System.getenv("FCMDB_PASS");

                Map<String, String> properties = new HashMap<>();

                if (dbUrl != null) properties.put("jakarta.persistence.jdbc.url", dbUrl);
                if (dbUser != null) properties.put("jakarta.persistence.jdbc.user", dbUser);
                if (dbPass != null) properties.put("jakarta.persistence.jdbc.password", dbPass);

                emf = Persistence.createEntityManagerFactory("myPersistenceUnit", properties);
            }
            catch  (Exception e) {
                e.printStackTrace();
            }
        }

        return emf;
    }

    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
