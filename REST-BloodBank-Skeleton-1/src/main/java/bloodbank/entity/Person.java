/***************************************************************************
 * File: Person.java Course materials (21F) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date Mar 9, 2021
 * 
 */
package bloodbank.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the person database table.
 */
@Entity
@Table(name="person")
@NamedQuery( name = Person.ALL_PERSONS_QUERY_NAME, query = "SELECT p FROM Person p left join fetch p.contacts left join fetch p.donations")
@NamedQuery( name = Person.QUERY_PERSON_BY_ID, query = "SELECT p FROM Person p left join fetch p.contacts left join fetch p.donations where p.id=:param1")

public class Person extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String ALL_PERSONS_QUERY_NAME = "Person.findAll";
	public static final String QUERY_PERSON_BY_ID = "Person.findAllByID";
	@Column( name = "first_name")
	private String firstName;

	@Column( name = "last_name")
	private String lastName;

	@OneToMany(cascade= CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn( name = "person_id", insertable = false, updatable = false)
	private Set< DonationRecord> donations = new HashSet<>();

	@OneToMany(cascade= CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn( name = "person_id",insertable = false, updatable = false)
	private Set< Contact> contacts = new HashSet<>();

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName( String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}
	public void setLastName( String lastName) {
		this.lastName = lastName;
	}

	@JsonIgnore
	public Set< DonationRecord> getDonations() {
		return donations;
	}
	public void setDonations( Set< DonationRecord> donations) {
		this.donations = donations;
	}

	@JsonIgnore
	public Set< Contact> getContacts() {
		return contacts;
	}
	public void setContacts( Set< Contact> contacts) {
		this.contacts = contacts;
	}

	public void setFullName( String firstName, String lastName) {
		setFirstName( firstName);
		setLastName( lastName);
	}
	
	/**
	 * <a href=https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier">How to implement hashCode, quals</a>
	 * <p>
	 * Very important - use getter's for member variables because needs to Hibernate 'traps' those calls and <br/>
	 * figure out some things!
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		return prime * result + Objects.hash(getFirstName(), getLastName());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Person)) {
			return false;
		}
		Person other = (Person) obj;
		return Objects.equals(getId(), other.getId()) &&
			Objects.equals(getFirstName(), other.getFirstName()) && Objects.equals(getLastName(), other.getLastName());
	}

}