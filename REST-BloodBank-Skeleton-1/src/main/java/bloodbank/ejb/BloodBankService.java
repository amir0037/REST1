/**
 * File: RecordService.java
 * Course materials (21F) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 * 
 * Updated by:  I. Am. A. Student 040nnnnnnn
 *
 */
package bloodbank.ejb;

import static bloodbank.entity.BloodBank.ALL_BLOODBANKS_QUERY_NAME;
import static bloodbank.entity.BloodBank.SPECIFIC_BLOODBANKS_QUERY_NAME;
import static bloodbank.entity.BloodBank.IS_DUPLICATE_QUERY_NAME;
import static bloodbank.entity.Person.ALL_PERSONS_QUERY_NAME;
import static bloodbank.entity.SecurityRole.ROLE_BY_NAME_QUERY;
import static bloodbank.entity.SecurityUser.USER_FOR_OWNING_PERSON_QUERY;
import static bloodbank.utility.MyConstants.DEFAULT_KEY_SIZE;
import static bloodbank.utility.MyConstants.DEFAULT_PROPERTY_ALGORITHM;
import static bloodbank.utility.MyConstants.DEFAULT_PROPERTY_ITERATIONS;
import static bloodbank.utility.MyConstants.DEFAULT_SALT_SIZE;
import static bloodbank.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static bloodbank.utility.MyConstants.DEFAULT_USER_PREFIX;
import static bloodbank.utility.MyConstants.PARAM1;
import static bloodbank.utility.MyConstants.PROPERTY_ALGORITHM;
import static bloodbank.utility.MyConstants.PROPERTY_ITERATIONS;
import static bloodbank.utility.MyConstants.PROPERTY_KEYSIZE;
import static bloodbank.utility.MyConstants.PROPERTY_SALTSIZE;
import static bloodbank.utility.MyConstants.PU_NAME;
import static bloodbank.utility.MyConstants.USER_ROLE;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;

import bloodbank.entity.Address;
import bloodbank.entity.BloodBank;
import bloodbank.entity.BloodDonation;
import bloodbank.entity.Contact;
import bloodbank.entity.ContactPK;
import bloodbank.entity.DonationRecord;
import bloodbank.entity.Person;
import bloodbank.entity.Phone;
import bloodbank.entity.SecurityRole;
import bloodbank.entity.SecurityUser;


/**
 * Stateless Singleton ejb Bean - BloodBankService
 */
@Singleton
public class BloodBankService implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG = LogManager.getLogger();
    
    @PersistenceContext(name = PU_NAME)
    protected EntityManager em;
    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;

    public List<Person> getAllPeople() {
    	//Added the following code
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<Person> cq = cb.createQuery(Person.class);
    	cq.select(cq.from(Person.class));
    	return em.createQuery(cq).getResultList();
    	//return null;
    }

    public Person getPersonId(int id) {
    	return em.find(Person.class, id);
    	//return null;
    }

    @Transactional
    public Person persistPerson(Person newPerson) {
    	em.persist(newPerson);
    	return newPerson;
    	// return null;
    }

    @Transactional
    public void buildUserForNewPerson(Person newPerson) {
        SecurityUser userForNewPerson = new SecurityUser();
        userForNewPerson.setUsername(
            DEFAULT_USER_PREFIX + "_" + newPerson.getFirstName() + "." + newPerson.getLastName());
        Map<String, String> pbAndjProperties = new HashMap<>();
        pbAndjProperties.put(PROPERTY_ALGORITHM, DEFAULT_PROPERTY_ALGORITHM);
        pbAndjProperties.put(PROPERTY_ITERATIONS, DEFAULT_PROPERTY_ITERATIONS);
        pbAndjProperties.put(PROPERTY_SALTSIZE, DEFAULT_SALT_SIZE);
        pbAndjProperties.put(PROPERTY_KEYSIZE, DEFAULT_KEY_SIZE);
        pbAndjPasswordHash.initialize(pbAndjProperties);
        String pwHash = pbAndjPasswordHash.generate(DEFAULT_USER_PASSWORD.toCharArray());
        userForNewPerson.setPwHash(pwHash);
        userForNewPerson.setPerson(newPerson);
        SecurityRole userRole = em.createNamedQuery(ROLE_BY_NAME_QUERY, SecurityRole.class)
            .setParameter(PARAM1, USER_ROLE).getSingleResult();
        userForNewPerson.getRoles().add(userRole);
        userRole.getUsers().add(userForNewPerson);
        em.persist(userForNewPerson);
    }

    @Transactional
    public Address setAddressForPersonPhone(int personId, int phoneId, Address newAddress) {
    	Person personToBeUpdated = em.find(Person.class, personId);
    	if (personToBeUpdated != null) { // Person exists
    		Set<Contact> contacts = personToBeUpdated.getContacts();
    		contacts.forEach(c -> {
    			if (c.getPhone().getId() == phoneId) {
    				if (c.getAddress() != null) { // Address exists
    					Address addr = em.find(Address.class, c.getAddress().getId());
    					addr.setAddress(newAddress.getStreetNumber(),
    									newAddress.getStreet(),
    									newAddress.getCity(),
    									newAddress.getProvince(),
    									newAddress.getCountry(),
    									newAddress.getZipcode());
    					em.merge(addr);
    				}
    				else { // Address does not exist
    					c.setAddress(newAddress);
    					em.merge(personToBeUpdated);
    				}
    			}
    		});
    		return newAddress;
    	}
    	else  // Person doesn't exists
    		return null;
    }

    /**
     * to update a person
     * 
     * @param id - id of entity to update
     * @param personWithUpdates - entity with updated information
     * @return Entity with updated information
     */
    @Transactional
    public Person updatePersonById(int id, Person personWithUpdates) {
        Person personToBeUpdated = getPersonId(id);
        if (personToBeUpdated != null) {
            em.refresh(personToBeUpdated);
            em.merge(personWithUpdates);
            em.flush();
        }
        return personToBeUpdated;
    }

    /**
     * to delete a person by id
     * 
     * @param id - person id to delete
     */
    @Transactional
    public void deletePersonById(int id) {
        Person person = getPersonId(id);
        if (person != null) {
            em.refresh(person);
            TypedQuery<SecurityUser> findUser = em
                .createNamedQuery(USER_FOR_OWNING_PERSON_QUERY, SecurityUser.class)
                .setParameter(PARAM1, person.getId());
            SecurityUser sUser = findUser.getSingleResult();
            em.remove(sUser);
            em.remove(person);
        }
    }
    
    public List<BloodBank> getAllBloodBanks() {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<BloodBank> cq = cb.createQuery(BloodBank.class);
    	cq.select(cq.from(BloodBank.class));
    	return em.createQuery(cq).getResultList();
    }
    
    /*
    public BloodBank getBloodBankById(int id) {
    	return em.find(BloodBank.class, id);
    }
    */
    
    public BloodBank getBloodBankById(int id) {
    	TypedQuery<BloodBank> specificBloodBankQuery = em.createNamedQuery(SPECIFIC_BLOODBANKS_QUERY_NAME, BloodBank.class);
    	specificBloodBankQuery.setParameter(PARAM1, id);
    	return specificBloodBankQuery.getSingleResult();
    }
    
    // These 2 methods are more generic.
    // BEGIN
    public <T> List<T> getAll(Class<T> entity, String namedQuery) {
    	TypedQuery<T> allQuery = em.createNamedQuery(namedQuery, entity);
    	return allQuery.getResultList();
    }
    
    public <T> T getById(Class<T> entity, String namedQuery, int id) {
    	TypedQuery<T> allQuery = em.createNamedQuery(namedQuery, entity);
    	allQuery.setParameter(PARAM1, id);
    	return allQuery.getSingleResult();
    }
    // END
    
    /*
    @Transactional
    // This is not working due to foreign key constraint exception thus replacing this method with the version below.
    public BloodBank deleteBloodBank(int id) {
    	BloodBank bb = getBloodBankById(id);
    	em.remove(bb);
    	return bb;
    }
    */

    @Transactional
    public BloodBank deleteBloodBank(int id) {
    	//BloodBank bb = getBloodBankById(id);
    	BloodBank bb = getById(BloodBank.class, BloodBank.SPECIFIC_BLOODBANKS_QUERY_NAME, id);
    	if (bb != null) {
    		
    		Set<BloodDonation> donations = bb.getDonations();
    		
    		List<BloodDonation> list = new LinkedList();
    		donations.forEach(list::add);
    		
    		list.forEach(bd -> {
    			if (bd.getRecord() != null) {
    				DonationRecord dr = getById(DonationRecord.class, DonationRecord.ID_RECORD_QUERY_ID, bd.getRecord().getId());
    				dr.setDonation(null);
    			}
    			bd.setRecord(null);
    			em.merge(bd);
    		});
    		em.remove(bb);
    		return bb;
    	}
    	return null;
    }
    
    // Please try to understand and test the below methods:
    public boolean isDuplicated(BloodBank newBloodBank) {
        TypedQuery<Long> allBloodBankQuery = em.createNamedQuery(IS_DUPLICATE_QUERY_NAME, Long.class);
        allBloodBankQuery.setParameter(PARAM1, newBloodBank.getName());
        return (allBloodBankQuery.getSingleResult() >= 1);
    }

    @Transactional
    public BloodBank persistBloodBank(BloodBank newBloodBank) {
        em.persist(newBloodBank);
        return newBloodBank;
    }

    @Transactional
    public BloodBank updateBloodBank(int id, BloodBank updatingBloodBank) {
        BloodBank bloodBankToBeUpdated = getBloodBankById(id);
        if (bloodBankToBeUpdated != null) {
            em.refresh(bloodBankToBeUpdated);
            em.merge(updatingBloodBank);
            em.flush();
        }
        return bloodBankToBeUpdated;
    }
    
    @Transactional
    public BloodDonation persistBloodDonation(BloodDonation newBloodDonation) {
        em.persist(newBloodDonation);
        return newBloodDonation;
    }

    public BloodDonation getBloodDonationById(int prodId) {
        TypedQuery<BloodDonation> allBloodDonationQuery = em.createNamedQuery(BloodDonation.FIND_BY_ID, BloodDonation.class);
        allBloodDonationQuery.setParameter(PARAM1, prodId);
        return allBloodDonationQuery.getSingleResult();
    }

    @Transactional
    public BloodDonation updateBloodDonation(int id, BloodDonation bloodDonationWithUpdates) {
        BloodDonation bloodDonationToBeUpdated = getBloodDonationById(id);
        if (bloodDonationToBeUpdated != null) {
            em.refresh(bloodDonationToBeUpdated);
            em.merge(bloodDonationWithUpdates);
            em.flush();
        }
        return bloodDonationToBeUpdated;
    }
    
    @Transactional
    public BloodDonation deleteBloodDonation(int bloodId) {
    	BloodDonation bd = getById(BloodDonation.class, BloodDonation.FIND_BY_ID, bloodId);
    	if (bd != null) {
    		
    		DonationRecord donationRecord = bd.getRecord();
    		donationRecord.setDonation(null);
    		
    			bd.setRecord(null);
    			em.merge(bd);
    	    		return bd;
    	}
    	return null;
    }
    
    
    // Begin Address Resource
    @Transactional
    public Address persistAddress(Address newAddress) {
    	em.persist(newAddress);
    	return newAddress;
    }
    
    public Address getAddressById(int id) {
    	return em.find(Address.class, id);
    	//return null;
    }
    
    
    @Transactional
    public Address updateAddress(int id, Address updateAddress) {
    	Address addressToBeUpdated = getAddressById(id);
    	if (addressToBeUpdated != null) {
    		em.refresh(addressToBeUpdated);
    		em.merge(updateAddress);
    		em.flush();
    	}
    	return addressToBeUpdated;
    }
    
    
    @Transactional
    public Address deleteAddressById(int addressId) {
    	Address address = getAddressById(addressId);
    	em.refresh(address);
    	if (address != null) {
    		Set<Contact> contacts = address.getContacts();
    		List<Contact> list = new LinkedList();
    		contacts.forEach(list::add);
    		
    		list.forEach(c -> {
    			if (c != null) {
                	c.setAddress(null);
                    em.merge(c);
    			}
    		});
    		em.remove(address);
    		return address;
    	}
    	return null;
    }
    
    
    // End Address Resource
    
    // Begin Phone Resource
    public Phone getPhoneById(int phoneId) {
    	return em.find(Phone.class, phoneId);
    }
    
    @Transactional
    public Phone persistPhone(Phone newPhone) {
    	em.persist(newPhone);
    	return newPhone;
    }
    
    @Transactional
    public Phone updatePhone(int id, Phone updatePhone) {
    	Phone phoneToBeUpdated = getPhoneById(id);
    	if (phoneToBeUpdated != null) {
    		em.refresh(phoneToBeUpdated);
    		em.merge(updatePhone);
    		em.flush();
    	}
    	return phoneToBeUpdated;
    }
    
    @Transactional
    public Phone deletePhoneById(int phoneId) {
    	Phone phone = getPhoneById(phoneId);
    	em.refresh(phone);
    	em.remove(phone);
    	
    	return phone;
    }
    
    // End Phone Resource
    
    
    // Begin DonationRecord Resource
    
//    public boolean isDuplicated(BloodBank newBloodBank) {
//        TypedQuery<Long> allBloodBankQuery = em.createNamedQuery(IS_DUPLICATE_QUERY_NAME, Long.class);
//        allBloodBankQuery.setParameter(PARAM1, newBloodBank.getName());
//        return (allBloodBankQuery.getSingleResult() >= 1);
//    }
    
    public DonationRecord getDonationRecordById(int recordId) {
    	return em.find(DonationRecord.class, recordId);
    }
    
    @Transactional
    public DonationRecord persistDonationRecord(DonationRecord newDonation) {
    	em.persist(newDonation);
    	return newDonation;
    }
    
    @Transactional
    public DonationRecord updateDonationRecord(int id, DonationRecord updateDonation) {
    	DonationRecord donationToBeUpdated = getDonationRecordById(id);
    	if (donationToBeUpdated != null) {
    		em.refresh(donationToBeUpdated);
    		em.merge(donationToBeUpdated);
    		em.flush();
    	}
    	return donationToBeUpdated;
    }
    
    
    // Working on this
    @Transactional
    public DonationRecord deleteDonationRecordById(int recordId) {
    	DonationRecord record = getDonationRecordById(recordId);
    	em.refresh(record);
    	if (record != null) {
    		record.setOwner(null);
    		record.setDonation(null);
        	em.remove(record);
    	}
    	
    	return null;
    }
    
    // End DonationRecord Resource
    
    //Begin Contact Resource
    public Contact getContactById(int contactId) {
    	return em.find(Contact.class, contactId);
    }
    
    @Transactional
    public Contact persistContact(Contact newContact) {
    	em.persist(newContact);
    	return newContact;
    }
    
    @Transactional
    public Contact updateContact(int contactId, Contact contact) {
    	Contact contactToBeUpdated = getContactById(contactId);
    	if (contactToBeUpdated != null) {
    		em.refresh(contactToBeUpdated);
    		em.merge(contactToBeUpdated);
    		em.flush();
    	}
    	return contactToBeUpdated;
    }
    
    @Transactional
    public Contact deleteContactById(int cId) {
    	Contact record = getContactById(cId);
        if (record != null) {
        	record.setPhone(null);
        	record.setAddress(null);
            em.merge(record);
            em.remove(record);
        }
    	
    	return record;
    }
    
    //End Contact Resource

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}