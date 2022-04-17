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
import static bloodbank.utility.MyConstants.BLOODBANK_RESOURCE_NAME;
import static bloodbank.utility.MyConstants.BLOODDONATION_RESOURCE_NAME;
import static bloodbank.utility.MyConstants.DONATION_RECORD_RESOURCE_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import bloodbank.entity.Person;
import bloodbank.entity.PrivateBloodBank;
import bloodbank.entity.BloodBank;
import bloodbank.entity.BloodDonation;
import bloodbank.entity.BloodType;
import bloodbank.entity.DonationRecord;

@SuppressWarnings("unused")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestBloodBankSystemBB {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);

    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;

    // test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;
    
    static List<BloodBank> allBanks;
    static BloodBank testBank;
    static int testBankId;
    static BloodDonation testDonation;
    
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
    public void test01_read_all_bloodbank_adminrole() throws JsonMappingException, JsonProcessingException {
    	Response response = webTarget
//    		.register(adminAuth)
    		.path(BLOODBANK_RESOURCE_NAME)
    		.request()
    		.get();
    	assertThat(response.getStatus(), is(200));
        allBanks = response.readEntity(new GenericType<List<BloodBank>>() {});
        assertThat(allBanks, is(not(empty())));
        assertThat(allBanks, hasSize(2));
    }
    
    @Test
    public void test02_readById_bloodbank_adminrole() throws JsonMappingException, JsonProcessingException {
    	String id = "2";
    	Response response = webTarget
			.register(adminAuth)
			.path(BLOODBANK_RESOURCE_NAME + "/" + id)
			.request()
			.get();
    	assertThat(response.getStatus(), is(200));
    	BloodBank bank = response.readEntity(new GenericType<BloodBank>() {});
    	assertThat(bank.getName(), is("Cheap Bloody Bank"));
    	assertThat(bank.getId(), is(2));
    }
    
    @Test
    public void test03_readById_bloodbank_userrole() throws JsonMappingException, JsonProcessingException {
    	String id = "2";
    	Response response = webTarget
			.register(userAuth)
			.path(BLOODBANK_RESOURCE_NAME + "/" + id)
			.request()
			.get();
    	assertThat(response.getStatus(), is(200));
    	BloodBank bank = response.readEntity(new GenericType<BloodBank>() {});
    	assertThat(bank.getName(), is("Cheap Bloody Bank"));
    }
    
    @Test
    public void test04_create_bloodbank_userrole() throws JsonMappingException, JsonProcessingException {
    	int preCount = getAllBanks().size();
    	testBank = new PrivateBloodBank();
    	testBank.setName("test fail");
    	Builder builder = webTarget
			.register(userAuth)
			.path(BLOODBANK_RESOURCE_NAME + "/" + testBank.getId())
			.request();
    	assertThat(builder.post(Entity.entity(testBank, MediaType.APPLICATION_JSON)).getStatus(), is(405));
    	int postCount = getAllBanks().size();
    	assertThat(postCount, is(preCount));
    }
    
    @Test
    public void test05_create_bloodbank_adminrole() throws JsonMappingException, JsonProcessingException {    	
    	testBank = new PrivateBloodBank();
    	testBank.setName("test bank");
    	Response response = webTarget
			.register(adminAuth)
			.path(BLOODBANK_RESOURCE_NAME)
			.request()
			.post(Entity.entity(testBank, MediaType.APPLICATION_JSON));
    	assertThat(response.getStatus(), is(200));
    	testBank = response.readEntity(new GenericType<BloodBank>() {});
    	assertThat(testBank.getName(), is("test bank"));
    	testBankId = testBank.getId();
    }
    
    @Test
    public void test06_update_bloodbank_userrole() throws JsonMappingException, JsonProcessingException {
    	int preCount = getAllBanks().size();
    	BloodBank bank = getAllBanks().get(preCount - 1);
    	bank.setName("updated");
//    	Builder builder = webTarget
//			.register(userAuth)
//			.path(BLOODBANK_RESOURCE_NAME + "/" + bank.getId())
//			.request();
//    	assertThat(builder.put(Entity.entity(bank, MediaType.APPLICATION_JSON)).getStatus(), is(403));
    	assertThat(webTarget.register(userAuth).path(BLOODBANK_RESOURCE_NAME + "/" + bank.getId())
				.request().put(Entity.entity(bank, MediaType.APPLICATION_JSON)).getStatus(), is(403));
    }
    
    @Test
    public void test07_update_bloodbank_adminrole() throws JsonMappingException, JsonProcessingException {
    	int preCount = getAllBanks().size();
    	testBank.setName("updated");
    	Response response = webTarget
			.register(adminAuth)
			.path(BLOODBANK_RESOURCE_NAME + "/" + testBankId)
			.request()
			.put(Entity.entity(testBank, MediaType.APPLICATION_JSON));
    	BloodBank updatedBank = getAllBanks().get(preCount - 1);
    	assertThat(response.getStatus(), is(200)); 
    	assertThat(updatedBank.getName(), is("updated"));
    }
    
    @Test
    public void test08_update_add_donation_userrole() throws JsonMappingException, JsonProcessingException {
    	BloodType testType = new BloodType();
    	testType.setType("AB", "+");
    	testDonation = new BloodDonation();
    	testDonation.setBloodType(testType);
    	testDonation.setMilliliters(50);
    	
    	Builder builder = webTarget
			.register(userAuth)
			.path(BLOODBANK_RESOURCE_NAME + "/" + testBankId + "/blooddonation")
			.request();
    	assertThat(builder.post(Entity.entity(testDonation, MediaType.APPLICATION_JSON)).getStatus(), is(403));
    	testDonation = null;
    }
    
    @Test
    public void test09_update_add_donation_adminrole() throws JsonMappingException, JsonProcessingException {
    	BloodType testType = new BloodType();
    	testType.setType("AB", "+");
    	testDonation = new BloodDonation();
    	testDonation.setBloodType(testType);
    	testDonation.setMilliliters(50);
    	
    	Response response = webTarget
			.register(adminAuth)
			.path(BLOODBANK_RESOURCE_NAME + "/" + testBankId + "/blooddonation")
			.request()
			.post(Entity.entity(testDonation, MediaType.APPLICATION_JSON));
    	testBank = response.readEntity(new GenericType<BloodBank>() {});
    	
    	assertThat(response.getStatus(), is(200));
    }
    
    @Test
    public void test10_delete_bloodbank_userrole() throws JsonMappingException, JsonProcessingException {
    	int preCount = getAllBanks().size();
    	BloodBank bank = getAllBanks().get(preCount - 1);
    	int id = testBank.getId();
    	Builder builder = webTarget
			.register(userAuth)
			.path(BLOODBANK_RESOURCE_NAME + "/" + bank.getId())
			.request();
    	assertThat(builder.delete().getStatus(), is(403));
    	int postCount = getAllBanks().size();
    	assertThat(postCount, is(preCount));
    }
    
    @Test
    public void test11_delete_bloodbank_with_dependency_adminrole() throws JsonMappingException, JsonProcessingException {
    	int preSize = getAllBanks().size();
    	int id = testBankId;
    	Response response = webTarget
			.register(adminAuth)
			.path(BLOODBANK_RESOURCE_NAME + "/" + testBankId)
			.request()
			.delete();
    	int postSize = getAllBanks().size();
    	assertThat(response.getStatus(), is(200));
    	assertThat(postSize, is(preSize - 1));
    }
    

    
    public List<BloodBank> getAllBanks() {
    	Response response = webTarget
        		.register(adminAuth)
        		.path(BLOODBANK_RESOURCE_NAME)
        		.request()
        		.get();
        List<BloodBank> banks = response.readEntity(new GenericType<List<BloodBank>>() {});
        return banks;
    }
    
    public BloodBank getBankById(HttpAuthenticationFeature auth, String resource, int id) {
    	Response response = webTarget
    			.register(auth)
    			.path(resource + "/" + id)
    			.request()
    			.get();
    	BloodBank bank = response.readEntity(new GenericType<BloodBank>() {});
    	return bank;
    }
}