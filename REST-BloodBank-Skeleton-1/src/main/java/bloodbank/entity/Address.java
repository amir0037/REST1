/***************************************************************************
 * File: Address.java Course materials (21F) CST 8277
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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the address database table.
 */
@Entity
@Table( name = "address")
@NamedQuery( name = Address.ADDRESS_FIND_ALL_QUERY, query = "SELECT a FROM Address a") // left JOIN FETCH a.contacts
@NamedQuery( name = Address.SPECIFIC_ADDRESS_QUERY_ID, query = "SELECT a FROM Address a where a.id=:param1") //left JOIN FETCH a.contacts 

@AttributeOverride( name = "id", column = @Column( name = "address_id"))
public class Address extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String SPECIFIC_ADDRESS_QUERY_ID = "Address.findById";
	public static final String ADDRESS_FIND_ALL_QUERY = "Address.findAll";
	
	@Column( name = "street_number", nullable = false, length = 10)
	private String streetNumber;

	@Column( name = "city", nullable = false, length = 100)
	private String city;

	@Column( name = "country", nullable = false, length = 100)
	private String country;

	@Column( name = "province", nullable = false, length = 100)
	private String province;

	@Column( name = "street", nullable = false, length = 100)
	private String street;

	@Column( name = "zipcode", nullable = false, length = 100)
	private String zipcode;

	@OneToMany(cascade=CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn( name = "address_id", referencedColumnName = "address_id", insertable = false, updatable = false)
	private Set< Contact> contacts = new HashSet<>();

	public Address() {
		super();
	}
	
	public Address(String streetNumber, String city, String country, String province, String street, String zipcode, Set<Contact> contacts) {
		this();
		this.streetNumber = streetNumber;
		this.city = city;
		this.country = country;
		this.province = province;
		this.street = street;
		this.zipcode = zipcode;
		this.contacts = contacts;
	}

	public String getCity() {
		return city;
	}
	public void setCity( String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}
	public void setCountry( String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}
	public void setProvince( String province) {
		this.province = province;
	}

	public String getStreet() {
		return street;
	}
	public void setStreet( String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}
	public void setStreetNumber( String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode( String zipcode) {
		this.zipcode = zipcode;
	}

	@JsonIgnore
	public Set< Contact> getContacts() {
		return contacts;
	}
	public void setContacts( Set< Contact> contacts) {
		this.contacts = contacts;
	}

	public void setAddress( String streetNumber, String street, String city, String province, String country, String zipcode) {
		setStreetNumber( streetNumber);
		setStreet( street);
		setCity( city);
		setProvince( province);
		setCountry( country);
		setZipcode( zipcode);
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
		result = prime * result + Objects.hash(getCity(), getCountry(), getProvince(), getStreet(), getStreetNumber(), getZipcode());
		return result;
	}

	@Override
	public boolean equals( Object obj) {
		if (obj == null) {
			return false;
		}
		if ( this == obj) {
			return true;
		}
		if (!(obj instanceof Address)) {
			return false;
		}
		Address other = (Address) obj;
		return Objects.equals(getId(), other.getId()) && Objects.equals(getCity(), other.getCity()) &&
			Objects.equals(getCountry(), other.getCountry()) &&
			Objects.equals(getProvince(), other.getProvince()) && 
			Objects.equals(getStreet(), other.getStreet()) && 
			Objects.equals(getStreetNumber(), other.getStreetNumber()) && 
			Objects.equals(getZipcode(), other.getZipcode());
	}

}