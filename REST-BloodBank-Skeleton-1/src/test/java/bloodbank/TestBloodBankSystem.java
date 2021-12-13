/**
 * File: OrderSystemTestSuite.java
 * Course materials (21F) CST 8277
 * Teddy Yap
 * (Original Author) Mike Norman
 *
 * @date 2020 10
 *
 * (Modified) @author Student Name
 */
package bloodbank;

import static bloodbank.utility.MyConstants.APPLICATION_API_VERSION;
import static bloodbank.utility.MyConstants.APPLICATION_CONTEXT_ROOT;
import static bloodbank.utility.MyConstants.CUSTOMER_ADDRESS_SUBRESOURCE_NAME;
import static bloodbank.utility.MyConstants.DEFAULT_ADMIN_USER;
import static bloodbank.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static bloodbank.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static bloodbank.utility.MyConstants.DEFAULT_USERNAME;
import static bloodbank.utility.MyConstants.PERSON_RESOURCE_NAME;
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
import bloodbank.entity.BloodBank;
import bloodbank.entity.BloodDonation;
import bloodbank.entity.Contact;
import bloodbank.entity.DonationRecord;
import bloodbank.entity.Person;
import bloodbank.entity.Phone;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestBloodBankSystem {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);

    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;

    // test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;
    static public Integer createdAddress_id;

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
        userAuth = HttpAuthenticationFeature.basic(DEFAULT_USERNAME, DEFAULT_USER_PASSWORD);
    }

    protected WebTarget webTarget;
    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
    }

    //TESTS findAll
    
    @Test
    public void test01_all_persons_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(PERSON_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<Person> persons = response.readEntity(new GenericType<List<Person>>(){});
        assertThat(persons, is(not(empty())));
        assertThat(persons, hasSize(1));
    }
    
    @Test
    public void test02_all_addresses_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(CUSTOMER_ADDRESS_SUBRESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<Address> addresses = response.readEntity(new GenericType<List<Address>>(){});
        assertThat(addresses, is(not(empty())));
        assertThat(addresses, hasSize(1));
    }
    
    @Test
    public void test03_all_blooddonations_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path("BloodDonation")
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<BloodDonation> blooddonations = response.readEntity(new GenericType<List<BloodDonation>>(){});
        assertThat(blooddonations, is(not(empty())));
        assertThat(blooddonations, hasSize(2));
    }
    
    @Test
    public void test04_all_phones_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path("phone")
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<Phone> phones = response.readEntity(new GenericType<List<Phone>>(){});
        assertThat(phones, is(not(empty())));
        assertThat(phones, hasSize(2));
    }
    
    @Test
    public void test05_all_bloodbanks_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path("bloodbank")
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<BloodBank> bloodbanks = response.readEntity(new GenericType<List<BloodBank>>(){});
        assertThat(bloodbanks, is(not(empty())));
        assertThat(bloodbanks, hasSize(2));
    }
    
    
    @Test
    public void test06_all_contacts_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path("contact")
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<Contact> contacts = response.readEntity(new GenericType<List<Contact>>(){});
        assertThat(contacts, is(not(empty())));
        assertThat(contacts, hasSize(2));
    }
    
    @Test
    public void test07_all_donationrecords_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path("DonationRecord")
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<DonationRecord> donationrecords = response.readEntity(new GenericType<List<DonationRecord>>(){});
        assertThat(donationrecords, is(not(empty())));
        assertThat(donationrecords, hasSize(2));
    }
    
    //TESTS findByID
    
    @Test
    public void test08_get_personById_with_adminrole() throws JsonMappingException, JsonProcessingException {
     	Response response = webTarget
            .register(adminAuth)
            .path("person/").path("1")
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        Person person = response.readEntity(new GenericType<Person>(){});
        assertEquals("Teddy", person.getFirstName());
        assertEquals("Yap", person.getLastName());
    }
    
    @Test
    public void test09_get_addressById_with_adminrole() throws JsonMappingException, JsonProcessingException {
     	Response response = webTarget
            .register(adminAuth)
            .path("address/").path("1")
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        Address address = response.readEntity(new GenericType<Address>(){});
        assertEquals("123", address.getStreetNumber());
        assertEquals("abcd Dr.W", address.getStreet());
        assertEquals("ottawa", address.getCity());
        assertEquals("ON", address.getProvince());
        assertEquals("CA", address.getCountry());
        assertEquals("A1B2C3", address.getZipcode());
    }
    
    @Test
    public void test10_get_blooddonationById_with_adminrole() throws JsonMappingException, JsonProcessingException {
     	Response response = webTarget
            .register(adminAuth)
            .path("BloodDonation/").path("2")
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        BloodDonation blooddonation = response.readEntity(new GenericType<BloodDonation>(){});
        assertEquals(10, blooddonation.getMilliliters());
    }
        
        @Test
        public void test11_get_phoneById_with_adminrole() throws JsonMappingException, JsonProcessingException {
         	Response response = webTarget
                .register(adminAuth)
                .path("phone/").path("2")
                .request()
                .get();
            assertThat(response.getStatus(), is(200));
            Phone phone = response.readEntity(new GenericType<Phone>(){});
            assertEquals("1", phone.getCountryCode());
            assertEquals("432", phone.getAreaCode());
            assertEquals("0098765", phone.getNumber());
        }
        
        @Test
        public void test12_get_bloodbankById_with_adminrole() throws JsonMappingException, JsonProcessingException {
         	Response response = webTarget
                .register(adminAuth)
                .path("bloodbank").path("2")
                .request()
                .get();
            assertThat(response.getStatus(), is(200));
            BloodBank bloodbank = response.readEntity(new GenericType<BloodBank>(){});
            assertEquals("Cheap Bloody Bank", bloodbank.getName());
            }
        
        @Test
        public void test13_get_contactById_with_adminrole() throws JsonMappingException, JsonProcessingException {
         	Response response = webTarget
                .register(adminAuth)
                .path("contact/").path("1")
                .request()
                .get();
            assertThat(response.getStatus(), is(200));
            Contact contact = response.readEntity(new GenericType<Contact>(){});
            assertEquals("Home", contact.getContactType());
            assertEquals("test@test.com", contact.getEmail());

            }
        
        @Test
        public void test14_get_donationrecordById_with_adminrole() throws JsonMappingException, JsonProcessingException {
         	Response response = webTarget
                .register(adminAuth)
                .path("DonationRecord/").path("2")
                .request()
                .get();
            assertThat(response.getStatus(), is(200));
            DonationRecord donationrecord = response.readEntity(new GenericType<DonationRecord>(){});
            assertEquals(0, donationrecord.getTested());
            }

    
    //TESTS DELETE
    
    //person address blooddonation phones bloodbank contact donationrecord
        
        @Test
        public void test16_delete_address_with_adminrole() throws JsonMappingException, JsonProcessingException {
        	Response response = webTarget
        		.register(adminAuth)
                .path("address").path("1")
                .request()
                .delete();
        	assertThat(response.getStatus(), is(200));
        	assertEquals(response.hasEntity(), true);
      }
        
        @Test
        public void test15_delete_address_with_userrole() throws JsonMappingException, JsonProcessingException {
        	Response response = webTarget
        		.register(userAuth)
                .path("address").path("1")
                .request()
                .delete();
        	assertThat(response.getStatus(), is(401));
      }
    
    
    
    
    
    
    
    
    
    
}