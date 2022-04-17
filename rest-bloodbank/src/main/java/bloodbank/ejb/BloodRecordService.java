/**
 * File: BloodBankService.java
 * Course materials (22W) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 * <p>
 * Updated by:  Group 14
 * 041000651, Chang Liu
 * 040991163, Wontaek Oh
 * 040990427, Mona Mahmoodianfard
 * 040997802, Gabriel Matte
 * ...
 */
package bloodbank.ejb;


import static bloodbank.utility.MyConstants.PU_NAME;

import java.util.List;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

import bloodbank.entity.DonationRecord;

@Singleton
public class BloodRecordService {

    @PersistenceContext(name = PU_NAME)
    protected EntityManager em;
    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;

    public List<DonationRecord> getAllRecord() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DonationRecord> cq = cb.createQuery(DonationRecord.class);
        Root<DonationRecord> root = cq.from(DonationRecord.class);
        cq.select(root);
        TypedQuery<DonationRecord> q = em.createQuery(cq);
        List<DonationRecord> ret = q.getResultList();
        return ret;
    }

    public DonationRecord getRecordId(int id) {
        return em.find(DonationRecord.class, id);
    }

    public DonationRecord persistRecord(DonationRecord newRecord) {
        em.persist(newRecord);
        return newRecord;
    }

    public void deleteRecordById(int id) {
        // TODO: there's probably relationships that need to be handled in this deltion
        DonationRecord record = getRecordId(id);
        if (record != null) {
            em.remove(record);
        }
    }

}
