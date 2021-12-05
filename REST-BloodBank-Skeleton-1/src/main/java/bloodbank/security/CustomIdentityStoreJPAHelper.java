/**
 * File: CustomIdentityStoreJPAHelper.java <br>
 * Course materials (21F) CST 8277
 * 
 * @author Teddy Yap
 * @author Mike Norman
 */
package bloodbank.security;

import static bloodbank.entity.SecurityUser.SECURITY_USER_BY_NAME_QUERY;
import static bloodbank.utility.MyConstants.PARAM1;
import static bloodbank.utility.MyConstants.PU_NAME;
import static java.util.Collections.emptySet;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bloodbank.entity.SecurityRole;
import bloodbank.entity.SecurityUser;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Singleton
public class CustomIdentityStoreJPAHelper {

	private static final Logger LOG = LogManager.getLogger();

	@PersistenceContext(name = PU_NAME)
	protected EntityManager em;

	public SecurityUser findUserByName(String username) {
		LOG.debug("find a User By the Name={}", username);
		SecurityUser user = null;
		//TODO:  Call the entity manager's createNamedQuery() method to call SecurityUser's named query called "SecurityUser.userByName"
		TypedQuery<SecurityUser> tp  = em.createNamedQuery(SECURITY_USER_BY_NAME_QUERY, SecurityUser.class);
		//       The named query "SecurityUser.userByName" accepts a parameter called "param1" so be sure to set it with the value of parameter "username"
		tp.setParameter("param1", username);
		//       Call the typed query's getSingleResult() inside a try-catch statement
		try {
			tp.getSingleResult();		
		//       The exception to catch is NoResultException
		} catch (NoResultException e) {
			LOG.debug(e);
			user = null;
		}
		//       Be sure to assign the SecurityUser object returned by getSingleResult() to variable "user" inside the try block and return it 
		return user;
	}

	public Set<String> findRoleNamesForUser(String username) {
		LOG.debug("find Roles For Username={}", username);
		Set<String> roleNames = emptySet();
		SecurityUser securityUser = findUserByName(username);
		if (securityUser != null) {
			roleNames = securityUser.getRoles().stream().map(s -> s.getRoleName()).collect(Collectors.toSet());
		}
		return roleNames;
	}

	@Transactional
	public void saveSecurityUser(SecurityUser user) {
		//LOG.debug("adding new user={}", user);
		//em.persist(user);
	}

	@Transactional
	public void saveSecurityRole(SecurityRole role) {
		//LOG.debug("adding new role={}", role);
		//em.persist(role);
	}
}