package bloodbank.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2022-04-05T05:07:06.492-0400")
@StaticMetamodel(Contact.class)
public class Contact_ extends PojoBaseCompositeKey_ {
	public static volatile SingularAttribute<Contact, ContactPK> id;
	public static volatile SingularAttribute<Contact, Person> owner;
	public static volatile SingularAttribute<Contact, Phone> phone;
	public static volatile SingularAttribute<Contact, Address> address;
	public static volatile SingularAttribute<Contact, String> email;
	public static volatile SingularAttribute<Contact, String> contactType;
}
