/***************************************************************************
 * File: PojoListener.java Course materials (22W) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 * <p>
 * Updated by:  Group 14
 * 041000651, Chang Liu
 * 040991163, Wontaek Oh
 * 040990427, Mona Mahmoodianfard
 * *********, Gabriel Matte
 * ...
 */
package bloodbank.entity;

import static java.time.LocalDateTime.now;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class PojoCompositeListener {

    @PrePersist
    public void setCreatedOnDate( PojoBaseCompositeKey< ?> pojoBaseComposite) {
        pojoBaseComposite.setCreated(now());
        pojoBaseComposite.setUpdated(now());
    }

    @PreUpdate
    public void setUpdatedDate( PojoBaseCompositeKey< ?> pojoBaseComposite) {
        pojoBaseComposite.setUpdated(now());
    }

}