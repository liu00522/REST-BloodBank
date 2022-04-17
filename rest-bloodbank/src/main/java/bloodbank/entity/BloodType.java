/***************************************************************************
 * File: BloodType.java Course materials (22W) CST 8277
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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BloodType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic( optional = false)
    @Column( name = "blood_group", nullable = false)
    private String bloodGroup;

    @Basic( optional = false)
    @Column( name = "rhd", nullable = false, columnDefinition = "BIT(1)")
    private byte rhd;

    public BloodType() {
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup( String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setType( String group, String rhd) {
        setBloodGroup( group);
        byte p = 0b1;
        byte n = 0b0;
        setRhd( ( "+".equals( rhd) ? p : n));
    }

    public byte getRhd() {
        return rhd;
    }

    public void setRhd( byte rhd) {
        this.rhd = rhd;
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
        return prime * result + Objects.hash(getBloodGroup(), getRhd());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (obj instanceof BloodType otherBloodType) {
            // see comment (above) in hashCode(): compare using only member variables that are
            // truely part of an object's identity
            return Objects.equals(this.getBloodGroup(), otherBloodType.getBloodGroup()) &&
                    Objects.equals(this.getRhd(), otherBloodType.getRhd());
        }
        return false;
    }

}