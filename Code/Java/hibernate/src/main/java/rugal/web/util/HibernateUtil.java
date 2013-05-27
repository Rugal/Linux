package rugal.web.util;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    /** ThreadLocal Session Map */
    public static final ThreadLocal<Session> SESSIONMAP = new ThreadLocal<Session>();
    private static final SessionFactory sessionFactory;
    private static final Logger LOGGER = Logger.getLogger(HibernateUtil.class);

    static {
        try {
            LOGGER.debug("HibernateUti.static - loading cofig");
            sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .buildSessionFactory();
            LOGGER.debug("HibernateUtil.static - end");
        } catch (Throwable ex) {
            ex.printStackTrace();
            LOGGER.error("HibernateUti error : ExceptionInInitializerError");
            throw new ExceptionInInitializerError(ex);
        }
    }

    private HibernateUtil() {
    }

    public static Session getSession() throws HibernateException {
        Session session = SESSIONMAP.get();

        if(session == null) {
            session = sessionFactory.openSession();
            SESSIONMAP.set(session);
        }

        return session;
    }

    public static void closeSession() throws HibernateException {
        Session session = SESSIONMAP.get();
        SESSIONMAP.set(null);

        if(session != null) {
            session.close();
        }
    }
}
