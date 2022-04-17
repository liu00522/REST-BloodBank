/***************************************************************************
 * File: Address.java Course materials (22W) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author Mike Norman
 * <p>
 * Updated by:  Group 14
 * 041000651, Chang Liu
 * 040991163, Wontaek Oh
 * 040990427, Mona Mahmoodianfard
 * 040997802, Gabriel Matte
 * ...
 */
package bloodbank.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@SuppressWarnings("unused")

/**
 * The persistent class for the address database table.
 */
//Hint - @Entity marks this class as an entity which needs to be mapped by JPA.
//Hint - @Entity does not need a name if the name of the class is sufficient.
//Hint - @Entity name does not matter as long as it is consistent across the code.
@Entity
//Hint - @Table defines a specific table on DB which is mapped to this entity.
@Table(name = "address")
//Hint - @NamedQuery attached to this class which uses JPQL/HQL. SQL cannot be used with NamedQuery.
//Hint - @NamedQuery uses the name which is defined in @Entity for JPQL, if no name is defined use class name.
//Hint - @NamedNativeQuery can optionally be used if there is a need for SQL query.
@NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a left join fetch a.contacts")
//Hint - @AttributeOverride can overrides column details. This Entity uses address_id as its primary key name, it needs to override the name in the mapped super class.
@AttributeOverride(name = "id", column = @Column(name = "address_id"))
//Hint - PojoBase is inherited by any entity with integer as their primary key.
//Hint - PojoBaseCompositeKey is inherited by any entity with a composite key as their primary key.
public class Address extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String ALL_ADDRESS_QUERY_NAME = "Address.findAll";
    // Hint - @Basic( optional = false) is used when the object cannot be null.
    // Hint - @Basic or none can be used if the object can be null.
    // Hint - @Basic is for checking the state of object at the scope of our code.
    @Basic(optional = false)
    // Hint - @Column is used to define the details of the column which this object will map to.
    // Hint - @Column is for mapping and creation (if needed) of an Object to DB.
    // Hint - @Column can also be used to define a specific name for the column if it is different than our object name.
    @Column(name = "street_number", nullable = false, length = 10)
    private String streetNumber;

    @Basic(optional = false)
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Basic(optional = false)
    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Basic(optional = false)
    @Column(name = "province", nullable = false, length = 100)
    private String province;

    @Basic(optional = false)
    @Column(name = "street", nullable = false, length = 100)
    private String street;

    @Basic(optional = false)
    @Column(name = "zipcode", nullable = false, length = 100)
    private String zipcode;

    // Hint - @OneToMany is used to define 1:M relationship between this entity and another.
    // Hint - @OneToMany option cascade can be added to define if changes to this entity should cascade to objects.
    // Hint - @OneToMany option cascade will be ignored if not added, meaning no cascade effect.
    // Hint - @OneToMany option fetch should be lazy to prevent eagerly initialing all the data.
    //@OneToMany(cascade=CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "address")
    // if mappedBy is not used, need @JoinColumn
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "address_id", insertable = false, updatable = false)
    // Hint - java.util.Set is used as a collection, however List could have been used as well.
    // Hint - java.util.Set will be unique and also possibly can provide better get performance with HashCode.
    private Set<Contact> contacts = new HashSet<>();

    public Address() {
        super();
    }

    public Address(String streetNumber, String city, String country, String province, String street, String zipcode, Set<Contact> contacts) {
        this();
        this.streetNumber = streetNumber;
        this.city = city;
        this.country = country;
        this.province = province;
        this.street = street;
        this.zipcode = zipcode;
        this.contacts = contacts;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public void setAddress(String streetNumber, String street, String city, String province, String country, String zipcode) {
        setStreetNumber(streetNumber);
        setStreet(street);
        setCity(city);
        setProvince(province);
        setCountry(country);
        setZipcode(zipcode);
    }

    //Inherited hashCode/equals is sufficient for this Entity class

}