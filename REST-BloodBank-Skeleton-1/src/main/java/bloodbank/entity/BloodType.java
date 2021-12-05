/***************************************************************************
 * File: BloodType.java Course materials (21F) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date Mar 9, 2021
 * 
 */
package bloodbank.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BloodType {

	@Column( name = "blood_group", nullable = false)
	private String bloodGroup;

	@Column( name = "rhd", nullable = false, columnDefinition = "BIT(1)")
	private byte rhd;

	public BloodType() {
	}

	public String getBloodGroup() {
		return bloodGroup;
	}
	public void setBloodGroup( String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public void setType( String group, String rhd) {
		setBloodGroup( group);
		byte p = 0b1;
		byte n = 0b0;
		setRhd( ( "+".equals( rhd) ? p : n));
	}

	public byte getRhd() {
		return rhd;
	}
	public void setRhd( byte rhd) {
		this.rhd = rhd;
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
		return prime * result + Objects.hash(getBloodGroup(), getRhd());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if ( this == obj) {
			return true;
		}
		if (!(obj instanceof BloodType)) {
			return false;
		}
		BloodType other = (BloodType) obj;
		return Objects.equals(getBloodGroup(), other.getBloodGroup()) && Objects.equals(getRhd(), other.getRhd());
	}

}