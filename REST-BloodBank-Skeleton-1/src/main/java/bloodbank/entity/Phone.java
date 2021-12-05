/***************************************************************************
 * File: Phone.java Course materials (21F) CST 8277
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

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the phone database table.
 */
@Entity
@Table(name="phone")
@AttributeOverride(name="id", column=@Column(name="phone_id"))
@NamedQuery( name = Phone.ALL_PHONES_QUERY, query = "SELECT b FROM Phone b")
public class Phone extends PojoBase implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final String ALL_PHONES_QUERY = "Phone.findAll";

	@Column( name = "area_code")
	private String areaCode;

	@Column( name = "country_code")
	private String countryCode;

	@Column( name = "number")
	private String number;

	@OneToMany(cascade=CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn( name = "phone_id", referencedColumnName = "phone_id", insertable = false, updatable = false)
	private Set< Contact> contacts = new HashSet<>();

	public Phone() {
		super();
	}

	public Phone(String areaCode, String countryCode, String number) {
		this();
		this.areaCode = areaCode;
		this.countryCode = countryCode;
		this.number = number;
	}

	public Phone setNumber( String countryCode, String areaCode, String number) {
		setCountryCode( countryCode);
		setAreaCode( areaCode);
		setNumber( number);
		return this;
	}

	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode( String areaCode) {
		this.areaCode = areaCode;
	}

	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode( String countryCode) {
		this.countryCode = countryCode;
	}

	public String getNumber() {
		return number;
	}
	public void setNumber( String number) {
		this.number = number;
	}

	public Set< Contact> getContacts() {
		return contacts;
	}
	public void setContacts( Set< Contact> contacts) {
		this.contacts = contacts;
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
		return prime * result + Objects.hash(getAreaCode(), getCountryCode(), getNumber());
	}

	@Override
	public boolean equals( Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Phone)) {
			return false;
		}
		Phone other = (Phone) obj;
		return Objects.equals(getId(), other.getId()) &&
			Objects.equals( getAreaCode(), other.getAreaCode()) &&
			Objects.equals( getCountryCode(), other.getCountryCode()) &&
			Objects.equals( getNumber(), other.getNumber());
	}

}