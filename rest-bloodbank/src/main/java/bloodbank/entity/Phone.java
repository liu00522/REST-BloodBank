/***************************************************************************
 * File: Phone.java Course materials (22W) CST 8277
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
 * The persistent class for the phone database table.
 */
@Entity
@Table( name = "phone")
@NamedQuery( name = Phone.ALL_PHONES_QUERY, query = "SELECT p FROM Phone p")
@AttributeOverride( name = "id", column = @Column( name = "phone_id"))
public class Phone extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String ALL_PHONES_QUERY = "Phone.findAll";

    @Basic( optional = false)
    @Column( name = "area_code", nullable = false, length = 10)
    private String areaCode;

    @Basic( optional = false)
    @Column( name = "country_code", nullable = false, length = 10)
    private String countryCode;

    @Basic( optional = false)
    @Column( name = "number", nullable = false, length = 10)
    private String number;

    @OneToMany(cascade=CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn( name = "phone_id", referencedColumnName = "phone_id", insertable = false, updatable = false)
    private Set< Contact> contacts = new HashSet<>();

    public Phone() {
        super();
    }

    public Phone(String areaCode, String countryCode, String number) {
        this();
        this.areaCode = areaCode;
        this.countryCode = countryCode;
        this.number = number;
    }

    public Phone setNumber( String countryCode, String areaCode, String number) {
        setCountryCode( countryCode);
        setAreaCode( areaCode);
        setNumber( number);

        return this;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode( String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode( String countryCode) {
        this.countryCode = countryCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber( String number) {
        this.number = number;
    }

    public Set< Contact> getContacts() {
        return contacts;
    }

    public void setContacts( Set< Contact> contacts) {
        this.contacts = contacts;
    }

    //Inherited hashCode/equals is sufficient for this Entity class

}