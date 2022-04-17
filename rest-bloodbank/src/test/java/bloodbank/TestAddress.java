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
import static bloodbank.utility.MyConstants.ADDRESS_RESOURCE_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import bloodbank.entity.Address;

@TestMethodOrder(MethodOrderer.MethodName.class)
class TestAddress {

    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);

    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;

    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;
    static public Integer addressId;

    private static final String STREET_NUMBER = "544";
    private static final String STREET = "Test st";
    private static final String CITY = "Ottawa";
    private static final String PROVINCE = "ON";
    private static final String COUNTRY = "CA";
    private static final String ZIPCODE = "H0H0H0";


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
    public void test01_get_all_address_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(ADDRESS_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        List<Address> addresses = response.readEntity(new GenericType<List<Address>>(){});
        assertThat(addresses, is(not(empty())));
        assertThat(addresses, hasSize(1));
    }


    @Test
    public void test02_get_all_address_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userAuth)
            .path(ADDRESS_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(403));
    }


    @Test
    public void test03_get_addressById_adminrole() throws JsonMappingException, JsonProcessingException {
        addressId = 1;
        Response response = webTarget
                .register(adminAuth)
                .path(ADDRESS_RESOURCE_NAME).path(Integer.toString(addressId))
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        Address address = response.readEntity(new GenericType<Address>(){});
        assertEquals("123", address.getStreetNumber());
        assertEquals("Abcd Dr.W", address.getStreet());
        assertEquals("Ottawa", address.getCity());
        assertEquals("ON", address.getProvince());
        assertEquals("CA", address.getCountry());
        assertEquals("A1B2C3", address.getZipcode());
    }


    @Test
    public void test04_get_addressById_userrole() throws JsonMappingException, JsonProcessingException {
        addressId = 1;
        Response response = webTarget
                .register(userAuth)
                .path(ADDRESS_RESOURCE_NAME).path(Integer.toString(addressId))
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        Address address = response.readEntity(new GenericType<Address>(){});
        assertEquals("123", address.getStreetNumber());
        assertEquals("Abcd Dr.W", address.getStreet());
        assertEquals("Ottawa", address.getCity());
        assertEquals("ON", address.getProvince());
        assertEquals("CA", address.getCountry());
        assertEquals("A1B2C3", address.getZipcode());
    }


    @Test
    public void test05_create_address_adminrole() throws JsonMappingException, JsonProcessingException {
        Address addressCreateTest = new Address();
        addressCreateTest.setAddress(STREET_NUMBER, STREET, CITY, PROVINCE, COUNTRY, ZIPCODE);

        Response response = webTarget
                .register(adminAuth)
                .path(ADDRESS_RESOURCE_NAME)
                .request()
                .post(Entity.json(addressCreateTest));

        assertThat(response.getStatus(), is(200));
        assertEquals(response.hasEntity(), true);
        Address newAddress = response.readEntity(new GenericType<Address>(){});
        addressId = newAddress.getId();
    }


    @Test
    public void test06_delete_address_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(ADDRESS_RESOURCE_NAME).path(Integer.toString(addressId))
                .request()
                .delete();
        assertThat(response.getStatus(), is(200));
        assertEquals(response.hasEntity(), false);
    }


    @Test
    public void test07_delete_address_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .path(ADDRESS_RESOURCE_NAME).path(Integer.toString(addressId))
                .request()
                .delete();
        assertThat(response.getStatus(), is(403));
    }
}
