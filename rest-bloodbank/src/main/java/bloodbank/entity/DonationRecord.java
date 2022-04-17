/***************************************************************************
 * File: DonationRecord.java Course materials (22W) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author Mike Norman
 * Updated by:  Group 14
 * 041000651, Chang Liu
 * 040991163, Wontaek Oh
 * 040990427, Mona Mahmoodianfard
 * 040997802, Gabriel Matte
 */
package bloodbank.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("unused")

/**
 * The persistent class for the donation_record database table.
 */
@Entity
@Table(name = "donation_record")
@NamedQuery(name = DonationRecord.ALL_RECORDS_QUERY_NAME, query = "SELECT d FROM DonationRecord d left join fetch d.donation")
@NamedQuery(name = DonationRecord.ID_RECORD_QUERY_NAME, query = "SELECT d FROM DonationRecord d left join fetch d.donation where d.id=:param1")
@AttributeOverride(name = "id", column = @Column(name = "record_id"))
public class DonationRecord extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String ALL_RECORDS_QUERY_NAME = "DonationRecord.findAll";
    public static final String ID_RECORD_QUERY_NAME = "DonationRecord.findById";

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "donation_id", referencedColumnName = "donation_id")
    private BloodDonation donation;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false)
    private Person owner;

    @Column(name = "tested", columnDefinition = "bit(1)")
    private byte tested;

    public DonationRecord() {
        super();
    }

    public DonationRecord(BloodDonation donation, Person owner, byte tested) {
        this();
        this.donation = donation;
        this.owner = owner;
        this.tested = tested;
    }

    public BloodDonation getDonation() {
        return donation;
    }

    public void setDonation(BloodDonation donation) {
        this.donation = donation;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public byte getTested() {
        return tested;
    }

    public void setTested(byte tested) {
        this.tested = tested;
    }
    
    @JsonIgnore
    public void setTested(boolean tested) {
        this.tested = (byte) (tested ? 0b0001 : 0b0000);
    }

    //Inherited hashCode/equals is sufficient for this Entity class

}