/***************************************************************************
 * File: ContactPK.java Course materials (22W) CST 8277
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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("unused")

/**
 * The primary key class for the contact database table.
 */
@Embeddable
@Access( AccessType.FIELD)
public class ContactPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Basic( optional = false)
    @Column( name = "person_id", nullable = false)
    private int personId;

    @Basic( optional = false)
    @Column( name = "phone_id", nullable = false)
    private int phoneId;

    public ContactPK() {
    }

    public ContactPK( int personId, int phoneId) {
        setPersonId( personId);
        setPhoneId( phoneId);
    }

    public int getPersonId() {
        return this.personId;
    }

    public void setPersonId( int personId) {
        this.personId = personId;
    }

    public int getPhoneId() {
        return this.phoneId;
    }

    public void setPhoneId( int phoneId) {
        this.phoneId = phoneId;
    }

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
        return prime * result + Objects.hash(getPersonId(), getPhoneId());
    }

    @Override
    public boolean equals( Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
    }
        if (obj instanceof ContactPK otherContactPK) {
            // see comment (above) in hashCode(): compare using only member variables that are
            // truely part of an object's identity
            return Objects.equals(this.getPersonId(), otherContactPK.getPersonId()) &&
                Objects.equals(this.getPhoneId(),  otherContactPK.getPhoneId());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ContactPK [personId=");
        builder.append(personId);
        builder.append(", phoneId=");
        builder.append(phoneId);
        builder.append("]");
        return builder.toString();
    }

}