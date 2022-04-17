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

import bloodbank.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;

import static bloodbank.entity.BloodBank.IS_DUPLICATE_QUERY_NAME;
import static bloodbank.entity.BloodBank.SPECIFIC_BLOODBANKS_QUERY_NAME;
import static bloodbank.utility.MyConstants.*;


@Singleton
public class AddressService implements Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(name = PU_NAME)
    protected EntityManager em;
    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;

    public List<Address> getAllAddress() {
        List<Address> addresses = em.createNamedQuery(Address.ALL_ADDRESS_QUERY_NAME, Address.class).getResultList();
        addresses.forEach(address -> address.setContacts(null));
        return addresses;
    }

    public Address getAddressId(int id) {
        Address add = em.find(Address.class, id);
        return add;
    }

    @Transactional
    public Address persistAddress(Address newAddress) {
        em.persist(newAddress);
        return newAddress;
    }

    @Transactional
    public void deleteAddressById(int id) {
        Address person = getAddressId(id);
        if (person != null) {
            em.remove(person);
        }
    }
}
