package bloodbank.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2022-04-05T13:08:18.916-0400")
@StaticMetamodel(SecurityRole.class)
public class SecurityRole_ {
	public static volatile SingularAttribute<SecurityRole, Integer> id;
	public static volatile SingularAttribute<SecurityRole, String> roleName;
	public static volatile SetAttribute<SecurityRole, SecurityUser> users;
}
