/***************************************************************************
 * File: PrivateBloodBank.java Course materials (21F) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date Mar 9, 2021
 * 
 */
package bloodbank.entity;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue( "1") //value 1 is private and value 0 is public
public class PrivateBloodBank extends BloodBank implements Serializable {
	private static final long serialVersionUID = 1L;

	public PrivateBloodBank() {
	}
}
