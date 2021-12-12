package bloodbank.rest.resource;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import static bloodbank.utility.MyConstants.ADMIN_ROLE;
import static bloodbank.utility.MyConstants.USER_ROLE;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bloodbank.ejb.BloodBankService;
import bloodbank.entity.Contact;

@Path("contact")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContactResource {
	
	private static final Logger LOG = LogManager.getLogger();
	
	@EJB
	protected BloodBankService service;
	
	@Inject
	protected SecurityContext sc;
	
	@GET
	@RolesAllowed({ADMIN_ROLE}) 
	public Response getContact() {
		LOG.debug("Retreiving all contacts...");
		List<Contact> contact = service.getAll(Contact.class, "Contact.findAll");
		LOG.debug("Contact found = {}", contact);
		Response response = Response.ok(contact).build();
		return response;
	}
	
	@GET
	@Path("/{contactID}")
	@RolesAllowed({ADMIN_ROLE, USER_ROLE})
	public Response getContactById(@PathParam("contactID") int contactID) {
		LOG.debug("Retrieving contact with id = {}", contactID);
		Contact contact = service.getById(Contact.class, Contact.SPECIFIC_CONTACT_QUERY_ID, contactID);
		Response response = Response.ok(contact).build();
		return response;

	}
	
	@DELETE
	@Path("/{contactID}")
	@RolesAllowed({ADMIN_ROLE}) 
	public Response deleteContact(@PathParam("contactID") int cId) {
		LOG.debug("Deleting contact with id = {}", cId);
		Contact contact = service.deleteContactById(cId);
		Response response = Response.ok(contact).build();
		return response;

	}
	
	@RolesAllowed({ADMIN_ROLE})
	@POST
	public Response addContact(Contact newContact) {
		LOG.debug("Adding a new contact = {}", newContact);
		Contact addcontact = service.persistContact(newContact);
		return Response.ok(addcontact).build();
		
	}
	
	@RolesAllowed({ADMIN_ROLE})
	@PUT
	@Path("/{contactID}")
	public Response updateContact(@PathParam("contactID") int contactId, Contact contactToUpdate) {
		LOG.debug("Updating the contact with id = {}", contactId);
		Contact contactUpdated = service.updateContact(contactId, contactToUpdate);
		Response response = Response.ok(contactUpdated).build();
		return response;
		
	}
	

	

	
	

}
