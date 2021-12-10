package bloodbank.rest.resource;

import static bloodbank.utility.MyConstants.*;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bloodbank.ejb.BloodBankService;
import bloodbank.entity.Phone;

@Path("Phone")
public class PhoneResource {
	
	private static final Logger LOG = LogManager.getLogger();

	@EJB
	protected BloodBankService service;

	@Inject
	protected SecurityContext sc;
	
	
	@GET
	@RolesAllowed({ADMIN_ROLE})
	public Response getPhone() {
		LOG.debug("Retrieving all phones...");
		List<Phone> phone = service.getAll(Phone.class, Phone.ALL_PHONES_QUERY);
		Response response = Response.ok(phone).build();
		return response;
	}
	
	
	@GET
	@RolesAllowed({ADMIN_ROLE, USER_ROLE})
	@Path("/{phoneId}")
	public Response getPhoneById(@PathParam("phoneId") int phoneId) {
		LOG.debug("Retrieving phone with id = {}", phoneId);
		Phone phone = service.getPhoneById(phoneId);
		Response response = Response.ok(phone).build();
		return response;
	}
	
	
	@POST
	@RolesAllowed({ADMIN_ROLE})
	public Response addPhone(Phone newPhone) {
		LOG.debug("Adding a new phone = {}", newPhone);
		Phone phone = service.persistPhone(newPhone);
		Response response = Response.ok(phone).build();
		return response;
	}
	
	
	@PUT
	@RolesAllowed({ADMIN_ROLE})
	@Path("/{phoneId}")
	public Response updatePhone(@PathParam("phoneId") int id, Phone phoneToUpdate) {
		LOG.debug("Updating the phone id = {}", id);
		Phone phoneUpdated = service.updatePhone(id, phoneToUpdate);
		Response response = Response.ok(phoneUpdated).build();
		return response;
	}
	
	
	@DELETE
	@RolesAllowed({ADMIN_ROLE})
	@Path("/{phoneId}")
	public Response deletePhone(@PathParam("phoneId") int phoneId) {
		LOG.debug("Deleting phone with id = {}", phoneId);
		Phone phone = service.deletePhoneById(phoneId);
		Response response = Response.ok(phone).build();
		return response;
	}
	
}






















