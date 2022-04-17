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


package bloodbank;

import static bloodbank.utility.MyConstants.APPLICATION_API_VERSION;
import static bloodbank.utility.MyConstants.APPLICATION_CONTEXT_ROOT;
import static bloodbank.utility.MyConstants.DEFAULT_ADMIN_USER;
import static bloodbank.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static bloodbank.utility.MyConstants.DEFAULT_USER;
import static bloodbank.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static bloodbank.utility.MyConstants.PERSON_RESOURCE_NAME;
import static bloodbank.utility.MyConstants.BLOODDONATION_RESOURCE_NAME;
import static bloodbank.utility.MyConstants.DONATION_RECORD_RESOURCE_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.invoke.MethodHandles;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import bloodbank.entity.Person;
import bloodbank.entity.Phone;
import bloodbank.entity.Address;
import bloodbank.entity.BloodBank;
import bloodbank.entity.BloodDonation;
import bloodbank.entity.BloodType;
import bloodbank.entity.DonationRecord;

@TestMethodOrder(MethodOrderer.MethodName.class)
class TestDonationRecord {

    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);

    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;

    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;

    static Person person;
    static BloodDonation donation;
    static DonationRecord record;
    static BloodType bloodType;
    static BloodBank bank;
    static Address address;
    static Phone phone;
    static final String LAST_NAME = "Brown";
    static final String FIRST_NAME = "Tom";


    @BeforeAll
    public static void oneTimeSetUp() throws Exception {
        logger.debug("oneTimeSetUp");

        uri = UriBuilder
                .fromUri(APPLICATION_CONTEXT_ROOT + APPLICATION_API_VERSION)
                .scheme(HTTP_SCHEMA)
                .host(HOST)
                .port(PORT)
                .build();
        adminAuth = HttpAuthenticationFeature.basic(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_PASSWORD);
        userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER, DEFAULT_USER_PASSWORD);
    }

    protected WebTarget webTarget;

    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient(
                new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
    }


    @Test
    public void test01_get_personById_adminrole() throws JsonMappingException, JsonProcessingException {
        int personId = 1;
        Response response = webTarget
                .register(adminAuth)
                .path(PERSON_RESOURCE_NAME).path(Integer.toString(personId))
                .request().get();
        assertThat(response.getStatus(), is(200));
        person = response.readEntity(new GenericType<Person>() {
        });
        assertEquals("Teddy", person.getFirstName());
        assertEquals("Yap", person.getLastName());
    }

    @Test
    public void test02_get_personById_userrole() throws JsonMappingException, JsonProcessingException {
        int personId = 1;
        Response response = webTarget
                .register(userAuth)
                .path(PERSON_RESOURCE_NAME).path(Integer.toString(personId))
                .request().get();
        assertThat(response.getStatus(), is(200));
        person = response.readEntity(new GenericType<Person>() {
        });
        assertEquals("Teddy", person.getFirstName());
        assertEquals("Yap", person.getLastName());
    }


    @Test
    public void test03_get_donationById_adminrole() throws JsonMappingException, JsonProcessingException {
        int donationId = 1;
        Response response = webTarget
                .register(adminAuth)
                .path(BLOODDONATION_RESOURCE_NAME).path(Integer.toString(donationId))
                .request().get();
        assertThat(response.getStatus(), is(200));
        donation = response.readEntity(BloodDonation.class);
        assertEquals(10, donation.getMilliliters());
        assertEquals("B", donation.getBloodType());
    }


    @Test
    public void test04_get_recordById_adminrole() throws JsonMappingException, JsonProcessingException {
        int recordId = 1;
        byte tested = 1;
        Response response = webTarget
                .register(adminAuth)
                .path(DONATION_RECORD_RESOURCE_NAME).path(Integer.toString(recordId))
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        record = response.readEntity(new GenericType<DonationRecord>() {
        });
        assertEquals(tested, record.getTested());
    }

}
