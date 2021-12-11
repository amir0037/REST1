/***************************************************************************
 * File: DonationRecord.java Course materials (21F) CST 8277
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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the donation_record database table.
 */
@Entity
@Table( name = "donation_record")
@NamedQuery( name = DonationRecord.ALL_RECORDS_QUERY_NAME, query = "SELECT d FROM DonationRecord d")
@NamedQuery( name = DonationRecord.ID_RECORD_QUERY_ID, query = "SELECT d FROM DonationRecord d where d.id=:param1")
@AttributeOverride( name = "id", column = @Column( name = "record_id"))
public class DonationRecord extends PojoBase implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String ALL_RECORDS_QUERY_NAME = "DonationRecord.findAll";
	public static final String ID_RECORD_QUERY_ID = "DonationRecord.findById";

	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="donation_id", referencedColumnName="donation_id")
	private BloodDonation donation;

	@ManyToOne(cascade=CascadeType.MERGE,fetch = FetchType.LAZY)
	@JoinColumn(name="person_id")
	private Person owner;

	@Column(name = "tested", columnDefinition = "bit(1)")
	private byte tested;

	public DonationRecord() {
		super();
	}
	
	public DonationRecord(BloodDonation donation, Person owner, byte tested) {
		this();
		this.donation = donation;
		this.owner = owner;
		this.tested = tested;
	}

	@JsonIgnore
	public BloodDonation getDonation() {
		return donation;
	}
	public void setDonation( BloodDonation donation) {
		this.donation = donation;
		//we must manually set the 'other' side of the relationship (JPA does not 'do' auto-management of relationships)
		if (donation != null) {
			donation.setRecord(this);
		}
	}

	@JsonIgnore
	public Person getOwner() {
		return owner;
	}
	public void setOwner( Person owner) {
		this.owner = owner;
		//we must manually set the 'other' side of the relationship (JPA does not 'do' auto-management of relationships)
		if (owner != null) {
			owner.getDonations().add(this);
		}
	}

	public byte getTested() {
		return tested;
	}
	public void setTested(byte tested) {
		this.tested = tested;
	}
	public void setTested( boolean tested) {
		this.tested = (byte) ( tested ? 0b0001 : 0b0000);
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
		return prime * result + Objects.hash(getOwner().getId(), getTested());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof DonationRecord)) {
			return false;
		}
		DonationRecord other = (DonationRecord) obj;
		return Objects.equals(getId(), other.getId()) && 
			Objects.equals(getOwner().getId(), other.getOwner().getId()) && Objects.equals(getTested(), other.getTested());
	}

}