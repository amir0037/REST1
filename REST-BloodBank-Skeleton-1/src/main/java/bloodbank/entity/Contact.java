/***************************************************************************
 * File: Contact.java Course materials (21F) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date Mar 9, 2021
 * 
 */
package bloodbank.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the contact database table.
 */
@Entity
@Table( name = "contact")
@Access(AccessType.PROPERTY)
@NamedQuery( name = "Contact.findAll", query = "SELECT c FROM Contact c left JOIN FETCH c.address left JOIN FETCH c.phone left JOIN FETCH c.owner")
@NamedQuery( name = Contact.SPECIFIC_CONTACT_QUERY_ID, query = "SELECT c FROM Contact c left JOIN FETCH c.address left JOIN FETCH c.phone left JOIN FETCH c.owner where c.address.id=:param1")

public class Contact extends PojoBaseCompositeKey< ContactPK> implements Serializable {
	private static final long serialVersionUID = 1L;

	
	public static final String SPECIFIC_CONTACT_QUERY_ID = "Contact.findById";

	private ContactPK id;
	private Person owner;
	private Phone phone;
	private Address address;
	private String email;
	private String contactType;

	public Contact() {
		id = new ContactPK();
	}

	@Override
	@EmbeddedId
	public ContactPK getId() {
		return id;
	}
	@Override
	public void setId( ContactPK id) {
		this.id = id;
	}
	
	@JsonIgnore
	@MapsId( "personId")
	@ManyToOne( cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
	@JoinColumn( name = "person_id", referencedColumnName = "id", nullable = false)
	public Person getOwner() {
		return owner;
	}
	public void setOwner( Person owner) {
		this.owner = owner;
		//we must manually set the 'other' side of the relationship (JPA does not 'do' auto-management of relationships)
		if (owner != null) {
			owner.getContacts().add(this);
		}
	}
	
	@JsonIgnore
	@MapsId( "phoneId")
	@ManyToOne( cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
	@JoinColumn( name = "phone_id", referencedColumnName = "phone_id", nullable = false)
	public Phone getPhone() {
		return phone;
	}
	public void setPhone( Phone phone) {
		this.phone = phone;
		//we must manually set the 'other' side of the relationship (JPA does not 'do' auto-management of relationships)
		if (phone != null) {
			phone.getContacts().add(this);
		}
	}
	
	@JsonIgnore
	@ManyToOne( cascade = CascadeType.ALL, optional = true, fetch = FetchType.LAZY)
	@JoinColumn( name = "address_id", referencedColumnName = "address_id", nullable = true)
	public Address getAddress() {
		return address;
	}
	public void setAddress( Address address) {
		this.address = address;
		//we must manually set the 'other' side of the relationship (JPA does not 'do' auto-management of relationships)
		if (address != null) {
			address.getContacts().add(this);
		}
	}

	@Column( length = 100, name = "email")
	public String getEmail() {
		return email;
	}
	public void setEmail( String email) {
		this.email = email;
	}

	@Column( length = 10, name = "contact_type", nullable = false)
	public String getContactType() {
		return contactType;
	}
	public void setContactType( String contactType) {
		this.contactType = contactType;
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
		return prime * result + Objects.hash(getAddress(), getContactType(), getEmail());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Contact)) {
			return false;
		}
		Contact other = (Contact) obj;
		return Objects.equals(getId(), other.getId()) &&
			Objects.equals(getAddress(), other.getAddress()) &&
			Objects.equals(getContactType(), other.getContactType()) &&
			Objects.equals(getEmail(), other.getEmail());
	}

}