package bloodbank.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2022-04-05T05:07:06.490-0400")
@StaticMetamodel(BloodDonation.class)
public class BloodDonation_ extends PojoBase_ {
	public static volatile SingularAttribute<BloodDonation, BloodBank> bank;
	public static volatile SingularAttribute<BloodDonation, DonationRecord> record;
	public static volatile SingularAttribute<BloodDonation, Integer> milliliters;
	public static volatile SingularAttribute<BloodDonation, BloodType> bloodType;
}
