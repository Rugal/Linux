package rugal.web.test;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Assert;
import org.junit.Test;

import rugal.web.entity.Person;
import rugal.web.util.HibernateUtil;

public class TestCase {

    @Test
    public void testGetSession() {
        System.out.println("Rugal");
        Session session = HibernateUtil.getSession();

        Assert.assertNotNull(session);
        HibernateUtil.closeSession();
    }


    /*
    @Test
    public void testExport() {
        System.out.println("Rugal");
        new SchemaExport(new Configuration().configure()).create(true , true);
    }
    @Test
    public void testQuery() {
        System.out.println("Bernstein");
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        @SuppressWarnings("unchecked")
        List<Person> personList = session.createQuery("select p from Person p").list();

        for(Person eachPerson : personList) {
            System.out.println(eachPerson);
        }
        //session.getTransaction().commit();
        HibernateUtil.closeSession();
    }


    @Test
    public void testSave() {
        System.out.println("Rugal");
        Person person = new Person();
        //person.setId(100);
        person.setName("路飞");

        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();

        session.save(person);

        tx.commit();
        HibernateUtil.closeSession();
    }
    */
}
