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
import static bloodbank.utility.MyConstants.BLOODBANK_RESOURCE_NAME;
import static bloodbank.utility.MyConstants.DEFAULT_ADMIN_USER;
import static bloodbank.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static bloodbank.utility.MyConstants.DEFAULT_USER;
import static bloodbank.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static bloodbank.utility.MyConstants.PERSON_RESOURCE_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import bloodbank.entity.Person;

@SuppressWarnings("unused")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestBloodBankSystemPerson {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);

    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;


    // test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;

    // test person data
    static Person person;
    static final String LAST_NAME = "Group14";
    static final String FIRST_NAME = "Group14";

    static Person personWithUpdates;
    static final String LAST_NAME_UPDATE = "Group14LN_UPDATE";
    static final String FIRST_NAME_UPDATE = "Group14FN_UPDATE";

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

        person = new Person();
        person.setFullName(FIRST_NAME, LAST_NAME);

        personWithUpdates = new Person();
        personWithUpdates.setFullName(FIRST_NAME_UPDATE, LAST_NAME_UPDATE);

    }

    protected WebTarget webTarget;

    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient(
                new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
    }


    @Order(1)
    @Test
    public void test01_all_persons_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path(PERSON_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        List<Person> persons = response.readEntity(new GenericType<List<Person>>() {
        });
        assertThat(persons, is(not(empty())));
        assertTrue(persons.size() >= 1);
    }

    @Order(2)
    @Test
    public void test02_get_person_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        int personId = 1;
        Response response = webTarget
                .register(adminAuth)
                .path(PERSON_RESOURCE_NAME + "/" + personId)
                .request().get();
        assertThat(response.getStatus(), is(200));
        Person person = response.readEntity(Person.class);
        assertThat(person.getId(), is(personId));
        assertThat(person.getFirstName(), is("Teddy"));
        assertThat(person.getLastName(), is("Yap"));
    }

    @Order(3)
    @Test
    public void test03_add_person_with_adminrole() throws JsonMappingException, JsonProcessingException {

        Response responseGetAll = webTarget
                // .register(userAuth)
                .register(adminAuth)
                .path(PERSON_RESOURCE_NAME)
                .request()
                .get();
        assertThat(responseGetAll.getStatus(), is(200));
        List<Person> persons = responseGetAll.readEntity(new GenericType<List<Person>>() {
        });
        assertThat(persons, is(not(empty())));
        int sizeBeforeAdd = persons.size();

        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path(PERSON_RESOURCE_NAME)
                .request()
                .post(Entity.json(person));

        Response responseGetAllAfterAdd = webTarget
                // .register(userAuth)
                .register(adminAuth).path(PERSON_RESOURCE_NAME).request().get();
        assertThat(responseGetAllAfterAdd.getStatus(), is(200));
        List<Person> personsAfter = responseGetAllAfterAdd.readEntity(new GenericType<List<Person>>() {
        });
        assertThat(personsAfter, is(not(empty())));
        int sizeAfterAdd = personsAfter.size();
        assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);
    }

    @Order(4)
    @Test
    public void test04_get_all_persons_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                //.register(adminAuth)
                .path(PERSON_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(403));
    }

    @Order(5)
    @Test
    public void test05_get_person_by_id_with_userrole() throws JsonMappingException, JsonProcessingException {
        int personId = 1;
        Response response = webTarget
                .register(userAuth)
                .path(PERSON_RESOURCE_NAME + "/" + personId)
                .request().get();
        assertThat(response.getStatus(), is(200));
        Person person = response.readEntity(Person.class);
        assertThat(person.getId(), is(personId));
        assertThat(person.getFirstName(), is("Teddy"));
        assertThat(person.getLastName(), is("Yap"));
    }

    @Order(6)
    @Test
    public void test06_add_person_with_userrole() throws JsonMappingException, JsonProcessingException {

        Response responseGetAll = webTarget
                .register(userAuth)
                //.register(adminAuth)
                .path(PERSON_RESOURCE_NAME)
                .request()
                .get();
        assertThat(responseGetAll.getStatus(), is(403));
    }


    @Order(7)
    @Test
    public void test07_delete_person_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        int personId = 2;
        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path(PERSON_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        List<Person> persons = response.readEntity(new GenericType<List<Person>>() {
        });
        assertThat(persons, is(not(empty())));
        Response deleteresponse = webTarget
                .register(adminAuth)
                .path(PERSON_RESOURCE_NAME + "/" + personId)
                .request().delete();
        assertThat(response.getStatus(), is(200));
    }


    @Order(8)
    @Test
    public void test08_delete_person_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        int personId = 2;
        Response response = webTarget
                .register(userAuth)
//                .register(adminAuth)
                .path(PERSON_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(403));
    }


}