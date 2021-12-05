package bloodbank.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-12-01T18:18:55.888-0500")
@StaticMetamodel(BloodBank.class)
public class BloodBank_ extends PojoBase_ {
	public static volatile SingularAttribute<BloodBank, String> name;
	public static volatile SetAttribute<BloodBank, BloodDonation> donations;
}
