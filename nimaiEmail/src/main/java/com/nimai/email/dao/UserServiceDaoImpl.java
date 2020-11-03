package com.nimai.email.dao;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.nimai.email.entity.NimaiClient;
import com.nimai.email.entity.NimaiEmailScheduler;
import com.nimai.email.entity.NimaiEmailSchedulerAlertToBanks;
import com.nimai.email.entity.NimaiFSubsidiaries;
import com.nimai.email.entity.NimaiLC;
import com.nimai.email.entity.NimaiMBranch;
import com.nimai.email.entity.NimaiMLogin;
import com.nimai.email.entity.NimaiMRefer;

@Repository
@Transactional
public class UserServiceDaoImpl implements UserServiceDao {

	private static Logger logger = LoggerFactory.getLogger(UserServiceDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public NimaiMLogin getCustomerDetailsByUserID(String userId) throws Exception {
		NimaiMLogin results = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("from NimaiMLogin n where n.userid.userid = :userId ", NimaiMLogin.class);
			query.setParameter("userId", userId);
			results = (NimaiMLogin) query.getSingleResult();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			return null;
		}

		return results;

	}

	@Override
	public NimaiMLogin update(NimaiMLogin nimaiLogin) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.update(nimaiLogin);
			return nimaiLogin;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return nimaiLogin;

	}

	@Override
	public NimaiClient getClientDetailsbyUserId(String userId) {
		NimaiClient results = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("from NimaiClient n where n.userid = :userId ", NimaiClient.class);
			query.setParameter("userId", userId);
			results = (NimaiClient) query.getSingleResult();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			return null;
		}

		return results;

	}

	@Override
	public boolean checkUserTokenKey(String token) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("FROM NimaiMLogin WHERE token = :token");
			query.setParameter("token", token);
			NimaiMLogin result = (NimaiMLogin) query.getSingleResult();
			if (!result.getToken().equals(token)) {
				return false;
			}
		} catch (NoResultException e) {
			return false;
		}
		return true;
	}

	@Override
	public NimaiMLogin getUserDetailsByTokenKey(String token) {
		NimaiMLogin result = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("FROM NimaiMLogin WHERE token = :token");
			query.setParameter("token", token);
			result = (NimaiMLogin) query.getSingleResult();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			return null;
		}

		return result;
	}

	@Override
	public NimaiClient getcliDetailsByEmailId(String emailId) {

		NimaiClient results = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("from NimaiClient where emailAddress = :emailId ");
			query.setParameter("emailId", emailId);
			results = (NimaiClient) query.getSingleResult();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			return null;
		}

		return results;

	}

	@Override
	public NimaiFSubsidiaries saveSubsidiaryDetails(NimaiFSubsidiaries subsidiaryDetails) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.save(subsidiaryDetails);
			return subsidiaryDetails;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return subsidiaryDetails;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public NimaiFSubsidiaries getSubsidiaryDetailsByToken(String token) {
		NimaiFSubsidiaries result = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("FROM NimaiFSubsidiaries WHERE subsidiaryToken = :token");
			query.setParameter("token", token);
			result = (NimaiFSubsidiaries) query.getSingleResult();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			return null;
		}

		return result;
	}

	@Override
	public boolean isUserIdExisist(String userId) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("FROM NimaiClient WHERE userid = :userId");
			query.setParameter("userId", userId);
			NimaiClient result = (NimaiClient) query.getSingleResult();
			if (!result.getUserid().equals(userId)) {
				return false;
			}
		} catch (NoResultException e) {
			return false;
		}
		return true;
	}

	@Override
	public NimaiMBranch getBranchUserDetails(String emailId) {
		NimaiMBranch result = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("From NimaiMBranch b WHERE emailId=:emailId order by b.insertTime desc");
			query.setParameter("emailId", emailId);
			query.setMaxResults(1);
			result = (NimaiMBranch) query.getSingleResult();
		} catch (NoResultException re) {
			return null;
		}
		return result;
	}

	@Override
	public NimaiMBranch updateBranchUser(NimaiMBranch branchUserDetails) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.saveOrUpdate(branchUserDetails);
			return branchUserDetails;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return branchUserDetails;
	}

	@Override
	public NimaiMBranch updateBranchUserDetails(NimaiMBranch branchUserDetails) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.saveOrUpdate(branchUserDetails);
			return branchUserDetails;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return branchUserDetails;

	}

	@Override
	public NimaiMRefer saveReferTokenDetails(NimaiMRefer referDetails) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.save(referDetails);
			return referDetails;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return referDetails;
	}

	@Override
	public NimaiMRefer getReferDetailsByToken(String token) {
		NimaiMRefer result = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("FROM NimaiMRefer WHERE token = :token");
			query.setParameter("token", token);
			result = (NimaiMRefer) query.getSingleResult();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			return null;
		}
		return result;

	}

	@Override
	public NimaiMBranch getbranchDetailsByToken(String token) {
		NimaiMBranch result = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("FROM NimaiMBranch WHERE token = :token");
			query.setParameter("token", token);
			result = (NimaiMBranch) query.getSingleResult();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			return null;
		}
		return result;
	}

	@Override
	public NimaiMBranch updateBranchUserDetails(String emailId, Date dnow, String passcodeValue) {
		NimaiMBranch result = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(
					"UPDATE NimaiMBranch nb set nb.passcodeValue= :passcodeValue,  nb.insertTime= :dnow, nb.modifyTime= :dnow where nb.emailId= :emailId");
			query.setParameter("passcodeValue", passcodeValue);
			query.setParameter("dnow", dnow);
			query.setParameter("emailId", emailId);
			query.executeUpdate();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			return null;
		}
		return result;
	}

	@Override
	public NimaiMBranch updateBranchUser(String passcode, String tokenKey, Date insertedDate, String emailId, int id,
			Date tokenExpiry) {
		Session session = null;
		boolean newSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			getSessionFactory().openSession();
			newSession = true;

		}
		Query query = session.createQuery("UPDATE NimaiMBranch nb set nb.passcodeValue= :passcode,nb.token= :tokenKey ,"
				+ "nb.insertTime= :insertedDate, nb.emailId= :emailId,nb.expryTime= :tokenExpiry where nb.id= :id");
		query.setParameter("passcode", passcode);
		query.setParameter("tokenKey", tokenKey);
		query.setParameter("insertedDate", insertedDate);
		query.setParameter("emailId", emailId);
		query.setParameter("tokenExpiry", tokenExpiry);
		query.setParameter("id", id);

		query.executeUpdate();
		if (newSession)
			session.close();
		return null;

	}

	@Override
	public boolean isEntryPresent(int id) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("FROM NimaiMBranch WHERE id = :id");
			query.setParameter("id", id);
			NimaiMBranch result = (NimaiMBranch) query.getSingleResult();

		} catch (NoResultException e) {
			return false;
		}
		return true;

	}

	@Override
	public NimaiLC getTransactioDetailsByTransIs(String transactionid) {
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
	@SuppressWarnings("unchecked")
	public List<NimaiEmailScheduler> getSchedulerDetails() {
		// TODO Auto-generated method stub
		logger.info("inside save getTransactionDetail method of UserServiceDaoImpl class");
		List<NimaiEmailScheduler> emailDetailsScheduled = new ArrayList<>();
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("from NimaiEmailScheduler nb where nb.emailStatus = :emailStatus");
			query.setParameter("emailStatus", "pending");
			emailDetailsScheduled = query.getResultList();
			return emailDetailsScheduled;
		} catch (NoResultException nre) {
			nre.printStackTrace();
			return null;
		}
	}

	@Override
	@Transactional(dontRollbackOn = Exception.class)
	public void updateEmailStatus(int scedulerid) {
		// TODO Auto-generated method stub
		logger.info("inside save updateEmailTransactionDetail method of UserServiceDaoImpl class");
		try {
			Session session = sessionFactory.getCurrentSession();
			NimaiEmailScheduler email = (NimaiEmailScheduler) session.load(NimaiEmailScheduler.class,
					new Integer(scedulerid));
			if (null != email) {
				email.setEmailStatus("Sent");
				session.update(email);
			}

		} catch (NoResultException nre) {
			nre.printStackTrace();
		}

	}

	@Override
	public void updateReferTokenDetails(Date tokenExpiry, String refertokenKey, NimaiClient clientUseId,
			Date insertedDate, String emailId, int referenceId) {
		// TODO Auto-generated method stub

		try {
			Session session = sessionFactory.getCurrentSession();
			NimaiMRefer refer = (NimaiMRefer) session.load(NimaiMRefer.class, new Integer(referenceId));
			if (null != refer) {
				refer.setTokenExpiryTime(tokenExpiry);
				refer.setToken(refertokenKey);
				refer.setUserid(clientUseId);
				refer.setTokenInsertedDate(insertedDate);
				refer.setEmailAddress(emailId);
				session.update(refer);
			}

		} catch (NoResultException nre) {
			nre.printStackTrace();
		}

	}

	@Override
	public NimaiMRefer getreferDetails(int referenceId) {
		NimaiMRefer result = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("From NimaiMRefer b WHERE b.id=:referenceId");
			query.setParameter("referenceId", referenceId);
			query.setMaxResults(1);
			result = (NimaiMRefer) query.getSingleResult();
		} catch (NoResultException re) {
			return null;
		}
		return result;
	}

	@Override
	public NimaiMBranch getBranchUserbyUserId(String userid) {
		NimaiMBranch results = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("from NimaiMBranch n where n.userid = :userid ", NimaiMBranch.class);
			query.setParameter("userid", userid);
			results = (NimaiMBranch) query.getSingleResult();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			return null;
		}

		return results;

	}

	@Override
	public void updateEmailStatus(String userid) {
		Session session = null;
		boolean newSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			getSessionFactory().openSession();
			newSession = true;

		}
		Query query = session
				.createQuery("UPDATE NimaiEmailScheduler nb set nb.emailStatus= :emailStatus where nb.userid= :userid");
		query.setParameter("emailStatus", "sent");
		query.setParameter("userid", userid);
		query.executeUpdate();
		if (newSession)
			session.close();

	}

	@Override
	public void updateLoginEmailStatus(String userid) {
		// TODO Auto-generated method stub
		Session session = null;
		boolean newSession = false;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			getSessionFactory().openSession();
			newSession = true;

		}
		Query query = session
				.createQuery("UPDATE NimaiMLogin nb set nb.emailStatus= :emailStatus where nb.userid.userid= :userid");
		query.setParameter("emailStatus", "sent");
		query.setParameter("userid", userid);
		query.executeUpdate();
		if (newSession)
			session.close();

	}

	@Override
	public List<NimaiLC> getCustTransactionList(Date todaysDate) {
		// TODO Auto-generated method stub
		List<NimaiLC> results = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			String hql = "from NimaiLC where insertedDate > :date";
			Query query = session.createQuery(hql);
			query.setParameter("date", todaysDate);
			results = query.getResultList();
		} catch (NoResultException nre) {
			nre.printStackTrace();
			return null;
		}

		return results;

	}
}
