package com.nimai.email.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nimai.email.controller.EmailController;
import com.nimai.email.entity.NimaiClient;
import com.nimai.email.entity.NimaiEmailSchedulerAlertToBanks;
import com.nimai.email.entity.NimaiLC;
import com.nimai.email.entity.QuotationMaster;

@Repository
@Transactional
public class BanksAlertDaoImpl implements BanksAlertDao {

	private static Logger logger = LoggerFactory.getLogger(BanksAlertDaoImpl.class);
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public NimaiEmailSchedulerAlertToBanks saveSchdulerData(NimaiEmailSchedulerAlertToBanks schedulerEntity) {
		logger.info("inside save saveSchdulerData method of BanksAlertDaoImpl class");
		try {
			Session session = sessionFactory.getCurrentSession();
			session.save(schedulerEntity);
			return schedulerEntity;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return schedulerEntity;
	}

	@Override
	public List<NimaiEmailSchedulerAlertToBanks> getTransactionDetail() {
		// TODO Auto-generated method stub
		logger.info("inside save getTransactionDetail method of BanksAlertDaoImpl class");
		List<NimaiEmailSchedulerAlertToBanks> emailDetailsScheduled = new ArrayList<>();
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("from NimaiEmailSchedulerAlertToBanks");
			emailDetailsScheduled = query.getResultList();
			return emailDetailsScheduled;
		} catch (NoResultException nre) {
			nre.printStackTrace();
			return null;
		}
	}

	@Override
	public void deleteEmailTransactionDetail(int scedulerid) {
		// TODO Auto-generated method stub
		logger.info("inside save deleteEmailTransactionDetail method of BanksAlertDaoImpl class");
		try {
			Session session = sessionFactory.getCurrentSession();
			NimaiEmailSchedulerAlertToBanks email = (NimaiEmailSchedulerAlertToBanks) session
					.load(NimaiEmailSchedulerAlertToBanks.class, new Integer(scedulerid));
			if (null != email) {
				session.delete(email);
			}

		} catch (NoResultException nre) {
			nre.printStackTrace();
		}
	}

	@Override
	public NimaiLC getTransactioDetailsByTransIs(String transactionid) {
		logger.info("inside save getTransactioDetailsByTransIs method of BanksAlertDaoImpl class");
		NimaiLC results = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("from NimaiLC n where n.transactionId = :transactionid ", NimaiLC.class);
			query.setParameter("transactionid", transactionid);
			results = (NimaiLC) query.getSingleResult();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			return null;
		}

		return results;
	}

	@Override
	public NimaiClient getCustDetailsByUserId(String custUserId) {
		logger.info("inside save getCustDetailsByUserId method of BanksAlertDaoImpl class");
		NimaiClient results = null;

		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("from NimaiClient n where n.userid = :custUserId ", NimaiClient.class);
			query.setParameter("custUserId", custUserId);
			results = (NimaiClient) query.getSingleResult();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			return null;
		}

		return results;

	}

	@Override
	public QuotationMaster getDetailsByQuoteId(int quotationId) {
		logger.info("inside save getTransactioDetailsByTransIs method of BanksAlertDaoImpl class");
		QuotationMaster results = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("from QuotationMaster n where n.quotationId = :quotationId ", QuotationMaster.class);
			query.setParameter("quotationId", quotationId);
			results = (QuotationMaster) query.getSingleResult();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			return null;
		}

		return results; 
	}

}
