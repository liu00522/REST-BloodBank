/***************************************************************************
 * File: PublicBloodBank.java Course materials (22W) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author Mike Norman
 * 
 */
package bloodbank.entity;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue( "0")
public class PublicBloodBank extends BloodBank implements Serializable {
    private static final long serialVersionUID = 1L;

    public PublicBloodBank() {
        super(true);
    }
}
