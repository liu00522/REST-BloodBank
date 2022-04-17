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
import static bloodbank.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static bloodbank.utility.MyConstants.PHONE_RESOURCE_NAME;

import static bloodbank.utility.MyConstants.DEFAULT_USER_PREFIX;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
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
import bloodbank.entity.Phone;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestPhone {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);

    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;

    // test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;

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
        userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER_PREFIX, DEFAULT_USER_PASSWORD);
    }

    protected WebTarget webTarget;

    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder
                .newClient(new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
    }

    @Test
    public void test01_allPhones() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(adminAuth)
                .path(PHONE_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        assertThat(response.getMediaType(), is(not(MediaType.APPLICATION_XML)));
        List<Phone> phones = response.readEntity(new GenericType<List<Phone>>() {
        });
        assertThat(phones, is(not(empty())));
    }

    @Test
    public void test02_phoneID() throws JsonMappingException, JsonProcessingException {
        // expected value below is based on Bloodbank.Phone table
        int id = 1;
        String area_code = "234";
        String number = "5678900";
        Response response = webTarget
                .register(adminAuth).path(PHONE_RESOURCE_NAME).path(Integer.toString(id)).request()
                .get();
        assertThat(response.getStatus(), is(200));
        Phone phone = response.readEntity(Phone.class);
        assertThat(phone, notNullValue());
        assertThat(phone.getId(), is(id));
        assertThat(phone.getAreaCode(), is(area_code));
        assertThat(phone.getNumber(), is(number));
    }

    @Test
    public void test03_deletePhone() throws JsonMappingException, JsonProcessingException {
        Phone ph = new Phone();
        String countryCode = "001";
        String areaCode = "613";
        String number = "111";
        ph.setNumber(countryCode, areaCode, number);
        Response response = webTarget.register(adminAuth).path(PHONE_RESOURCE_NAME).request().post(Entity.json(ph));

        assertThat(response.getStatus(), is(200));
        Phone returnedPhone = response.readEntity(Phone.class);
        assertThat(returnedPhone, notNullValue());
        assertThat(returnedPhone.getCountryCode(), is(countryCode));
        assertThat(returnedPhone.getAreaCode(), is(areaCode));
        assertThat(returnedPhone.getNumber(), is(number));

        response = webTarget.register(adminAuth).path(PHONE_RESOURCE_NAME).path(Integer.toString(returnedPhone.getId()))
                .request().delete();

        assertThat(response.getStatus(), is(200));
    }
}
