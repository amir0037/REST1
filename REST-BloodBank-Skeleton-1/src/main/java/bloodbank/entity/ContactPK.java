/***************************************************************************
 * File: ContactPK.java Course materials (21F) CST 8277
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
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the contact database table.
 */
@Embeddable
@Access( AccessType.FIELD)
public class ContactPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column( name = "person_id", nullable = false)
	private int personId;

	@Column( name = "phone_id", nullable = false)
	private int phoneId;

	public ContactPK() {
	}

	public ContactPK( int personId, int phoneId) {
		setPersonId( personId);
		setPhoneId( phoneId);
	}

	public int getPersonId() {
		return this.personId;
	}
	public void setPersonId( int personId) {
		this.personId = personId;
	}

	public int getPhoneId() {
		return this.phoneId;
	}
	public void setPhoneId( int phoneId) {
		this.phoneId = phoneId;
	}

	@Override
	public String toString() {
		return String.format( "[Person.id:%d, Phone.id:%d]", personId, phoneId);
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
		return prime * result + Objects.hash(getPhoneId(), getPersonId());
	}

	@Override
	public boolean equals( Object obj) {
		if ( obj == null) {
			return false;
		}
		if ( this == obj) {
			return true;
		}
		if (!(obj instanceof ContactPK)) {
			return false;
		}
		ContactPK other = (ContactPK) obj;
		return Objects.equals(getPhoneId(), other.getPhoneId()) && Objects.equals(getPersonId(), other.getPersonId());
	}

}