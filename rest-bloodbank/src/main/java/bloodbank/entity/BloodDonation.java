/***************************************************************************
 * File: BloodDonation.java Course materials (22W) CST 8277
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
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@SuppressWarnings("unused")

/**
 * The persistent class for the blood_donation database table.
 */
@Entity
@Table( name = "blood_donation")
@NamedQuery( name = BloodDonation.FIND_ALL, query = "SELECT b FROM BloodDonation b left join fetch b.bank left join fetch b.record")
@NamedQuery( name = BloodDonation.FIND_BY_ID, query = "SELECT b FROM BloodDonation b left join fetch b.bank left join fetch b.record where b.id=:param1")
@AttributeOverride( name = "id", column = @Column( name = "donation_id"))
public class BloodDonation extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "BloodDonation.findAll";
    public static final String FIND_BY_ID = "BloodDonation.findbyId";    

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn( name = "bank_id", referencedColumnName = "bank_id")
    private BloodBank bank;

    //@OneToOne( fetch = FetchType.LAZY, cascade = CascadeType.MERGE, optional = true, mappedBy = "donation")
	@OneToOne(cascade=CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    // if not using mappedBy, need @JoinColumn
	@JoinColumn( name = "donation_id", referencedColumnName = "donation_id", nullable = true, insertable = false, updatable = false)
    private DonationRecord record;

    @Basic( optional = false)
    @Column( name = "milliliters", nullable = false)
    private int milliliters;

    @Embedded
    private BloodType bloodType;

    public BloodDonation() {
        bloodType = new BloodType();
    }

    public BloodBank getBank() {
        return bank;
    }

    public void setBank( BloodBank bank) {
        this.bank = bank;
    }

    public DonationRecord getRecord() {
        return record;
    }

    public void setRecord( DonationRecord record) {
        this.record = record;
    }

    public int getMilliliters() {
        return milliliters;
    }

    public void setMilliliters( int milliliters) {
        this.milliliters = milliliters;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType( BloodType bloodType) {
        this.bloodType = bloodType;
    }


    //Inherited hashCode/equals NOT sufficient for this Entity class
    /**
     * Very important: use getter's for member variables because JPA sometimes needs to intercept those calls<br/>
     * and go to the database to retrieve the value
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        // only include member variables that really contribute to an object's identity
        // i.e. if variables like version/updated/name/etc. change throughout an object's lifecycle,
        // they shouldn't be part of the hashCode calculation
        
        // include BloodType in identity
        return prime * result + Objects.hash(getId(), getBloodType(), getMilliliters());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }

        if (obj instanceof BloodDonation otherBloodDonation) {
            // see comment (above) in hashCode(): compare using only member variables that are
            // truely part of an object's identity
            return Objects.equals(getId(), otherBloodDonation.getId()) &&
              Objects.equals(getBloodType(), otherBloodDonation.getBloodType()) &&
              Objects.equals(getMilliliters(), otherBloodDonation.getMilliliters());
        }
        return false;
    }
}