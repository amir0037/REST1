/***************************************************************************
 * File: BloodBank.java Course materials (21F) CST 8277
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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import bloodbank.rest.serializer.BloodBankSerializer;
import bloodbank.rest.serializer.BloodBankDeserializer;

/**
 * The persistent class for the blood_bank database table.
 */
@Entity(name="BloodBank")
@Table(name="blood_bank")
@Access(AccessType.FIELD)
@AttributeOverride(name="id", column=@Column(name="bank_id"))
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@NamedQuery( name = BloodBank.ALL_BLOODBANKS_QUERY_NAME, query = "SELECT distinct b FROM BloodBank b left join fetch b.donations")
@NamedQuery( name = BloodBank.IS_DUPLICATE_QUERY_NAME, query = "SELECT count(b) FROM BloodBank b where b.name=:param1")
@NamedQuery( name = BloodBank.SPECIFIC_BLOODBANKS_QUERY_NAME, query = "SELECT distinct b FROM BloodBank b left join fetch b.donations where b.id=:param1")
@DiscriminatorColumn(name="privately_owned",columnDefinition = "BIT(1)", discriminatorType = DiscriminatorType.INTEGER)
@JsonSerialize(using = BloodBankSerializer.class)
@JsonDeserialize(using = BloodBankDeserializer.class)
public abstract class BloodBank extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String ALL_BLOODBANKS_QUERY_NAME = "BloodBank.findAll";
	public static final String SPECIFIC_BLOODBANKS_QUERY_NAME = "BloodBank.findByName";
	public static final String IS_DUPLICATE_QUERY_NAME = "BloodBank.isDuplicate";

	@Column( name = "name")
	private String name;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
			CascadeType.REMOVE},orphanRemoval=true)
    @JoinColumn(name="bank_id")
	private Set< BloodDonation> donations = new HashSet<>();

	@Transient
	private boolean isPublic;
	
	public BloodBank() {
	}
	

	@JsonIgnore
	public Set< BloodDonation> getDonations() {
		return donations;
	}
	public void setDonations( Set< BloodDonation> donations) {
		this.donations = donations;
	}

	public void setName( String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public boolean isPublic() {
		return isPublic;
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
		return prime * result + Objects.hash(getName());
	}

	@Override
	public boolean equals( Object obj) {
		if (obj == null) {
			return false;
		}
		if ( this == obj) {
			return true;
		}
		if (!(obj instanceof BloodBank)) {
			return false;
		}
		BloodBank other = (BloodBank) obj;
		return Objects.equals(getId(), other.getId()) && Objects.equals(getName(), other.getName());
	}

}