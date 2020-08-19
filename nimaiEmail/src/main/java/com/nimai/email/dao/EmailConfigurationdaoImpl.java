package com.nimai.email.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nimai.email.entity.EmailComponentMaster;

@Repository
@Transactional
public class EmailConfigurationdaoImpl {
	

    private static Logger logger =  LoggerFactory.getLogger(EmailConfigurationdaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;
     

    public EmailComponentMaster findEventConfiguration(String emailEventName) {
        
        System.out.println("In findEventConfiguration...."+emailEventName);
        EmailComponentMaster emailComponentmaster = new EmailComponentMaster();
        try {
//            Query q = sessionFactory.getCurrentSession().createQuery("from EmailComponentMaster where EmailEventMaster.emailEventName=:emailEventName");
//            q.setString("emailEventName", emailEventName);
        	Session session=sessionFactory.getCurrentSession();
        	Query query=session.createQuery("from EmailComponentMaster where emailEventMaster.emailEventName=:emailEventName");
        	query.setParameter("emailEventName", emailEventName);
            emailComponentmaster = (EmailComponentMaster) query.uniqueResult();
           System.out.println("emailComponentmaster..."+emailComponentmaster.getEmailFrom());
        } catch (Exception e) {
            System.out.println("findEventConfiguration exception " + e);           
            e.printStackTrace();
            return null;
        }
        return emailComponentmaster;
    }

    public EmailComponentMaster findByID(Long id) {

        EmailComponentMaster emailcomponentmaster = new EmailComponentMaster();
        try {
            Query q = sessionFactory.getCurrentSession().createQuery("from EmailComponentMaster where eventId=:eventId");
            q.setParameter("eventId", id.intValue());
            emailcomponentmaster = (EmailComponentMaster) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emailcomponentmaster;
    }
    
    public EmailComponentMaster findEventConfigurationId() {
        
//        System.out.println("In findEventConfiguration...."+emailEventName);
        EmailComponentMaster emailComponentmaster = new EmailComponentMaster();
        try {
//            Query q = sessionFactory.getCurrentSession().createQuery("from EmailComponentMaster where EmailEventMaster.emailEventName=:emailEventName");
//            q.setString("emailEventName", emailEventName);
        	Session session=sessionFactory.getCurrentSession();
        	Query query=session.createQuery("from EmailComponentMaster");
//        	query.setParameter("emailEventName", emailEventName);
            emailComponentmaster = (EmailComponentMaster) query.uniqueResult();
            System.out.println("emailComponentmaster..."+emailComponentmaster.getEmailFrom());
        } catch (Exception e) {
            System.out.println("findEventConfiguration exception " + e);           
            e.printStackTrace();
        }
        return emailComponentmaster;
    }


}



