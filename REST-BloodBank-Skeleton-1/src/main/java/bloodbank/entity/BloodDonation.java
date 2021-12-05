/***************************************************************************
 * File: BloodDonation.java Course materials (21F) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date Mar 9, 2021
 * 
 */
package bloodbank.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The persistent class for the blood_donation database table.
 */
@Entity
@Table( name = "blood_donation")
@NamedQuery(name = BloodDonation.FIND_ALL, query = "SELECT b FROM BloodDonation b")
@NamedQuery( name = BloodDonation.FIND_BY_ID, query = "SELECT bb FROM BloodBank bb")
@AttributeOverride( name = "id", column = @Column( name = "donation_id"))
public class BloodDonation extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "BloodDonation.findAll";
	public static final String FIND_BY_ID = "BloodBank.findById";

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="bank_id")
	private BloodBank bank;

	@OneToOne(cascade=CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
	@JoinColumn( name = "donation_id", referencedColumnName = "donation_id", nullable = true, insertable = false, updatable = false)
	private DonationRecord record;

	@Column( name = "milliliters")
	private int milliliters;

	@Embedded
	private BloodType bloodType;

	public BloodDonation() {
		bloodType = new BloodType();
	}

	public BloodBank getBank() {
		return bank;
	}
	public void setBank( BloodBank bank) {
		this.bank = bank;
		//we must manually set the 'other' side of the relationship (JPA does not 'do' auto-management of relationships)
		if (bank != null) {
			bank.getDonations().add(this);
		}
	}

	public DonationRecord getRecord() {
		return record;
	}
	public void setRecord( DonationRecord record) {
		this.record = record;
		//we should (as above) set the 'other' side of this relationship, but that causes an infinite-loop
	}

	public int getMilliliters() {
		return milliliters;
	}
	public void setMilliliters( int milliliters) {
		this.milliliters = milliliters;
	}

	public BloodType getBloodType() {
		return bloodType;
	}
	public void setBloodType( BloodType bloodType) {
		this.bloodType = bloodType;
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
		return prime * result + Objects.hash(getBank().getId(), getBloodType(), getMilliliters());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if ( this == obj) {
			return true;
		}
		if (!(obj instanceof BloodDonation)) {
			return false;
		}
		BloodDonation other = (BloodDonation) obj;
		return Objects.equals(getId(), other.getId()) &&
			Objects.equals(getBank().getId(), other.getBank().getId()) &&
			Objects.equals(getBloodType(), other.getBloodType()) &&
			Objects.equals(getMilliliters(), other.getMilliliters());
	}
}