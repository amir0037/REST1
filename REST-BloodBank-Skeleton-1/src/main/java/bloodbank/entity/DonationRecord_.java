package bloodbank.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-12-01T18:18:55.889-0500")
@StaticMetamodel(DonationRecord.class)
public class DonationRecord_ extends PojoBase_ {
	public static volatile SingularAttribute<DonationRecord, BloodDonation> donation;
	public static volatile SingularAttribute<DonationRecord, Person> owner;
	public static volatile SingularAttribute<DonationRecord, Byte> tested;
}
