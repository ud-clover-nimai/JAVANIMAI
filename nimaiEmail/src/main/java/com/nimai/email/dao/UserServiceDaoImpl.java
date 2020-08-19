package com.nimai.email.dao;

import java.util.Date;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nimai.email.entity.NimaiClient;
import com.nimai.email.entity.NimaiFSubsidiaries;
import com.nimai.email.entity.NimaiLC;
import com.nimai.email.entity.NimaiMBranch;
import com.nimai.email.entity.NimaiMLogin;
import com.nimai.email.entity.NimaiMRefer;

@Repository
@Transactional
public class UserServiceDaoImpl implements UserServiceDao {

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

}
