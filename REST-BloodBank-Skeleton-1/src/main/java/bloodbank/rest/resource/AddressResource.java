package bloodbank.rest.resource;

import java.util.List;
import static bloodbank.utility.MyConstants.*;

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
import bloodbank.entity.Address;

@Path(CUSTOMER_ADDRESS_SUBRESOURCE_NAME)
public class AddressResource {
	
	private static final Logger LOG = LogManager.getLogger();

	@EJB
	protected BloodBankService service;

	@Inject
	protected SecurityContext sc;
	
	@GET
	@RolesAllowed({ADMIN_ROLE})
	public Response getAddress() {
		LOG.debug("retrieving all addresses...");
		List<Address> address = service.getAll(Address.class, "Address.findAll");
		Response response = Response.ok(address).build();
		return response;
	}
	
	
	@GET
	@RolesAllowed({ADMIN_ROLE, USER_ROLE})
	@Path("/{addressID}")
	public Response getAddressById(@PathParam("addressID") int addressID) {
		LOG.debug("Retrieving address with id = {}", addressID);
		Address address = service.getAddressById(addressID);
		Response response = Response.ok(address).build();
		return response;
	}
	
	
	@POST
	@RolesAllowed({ADMIN_ROLE})
	public Response addAddress(Address newAddress) {
		LOG.debug("Adding a new address = {}", newAddress);
		Address address = service.persistAddress(newAddress);
		Response response = Response.ok(address).build();
		return response;
	}
	
	
	@PUT
	@RolesAllowed({ADMIN_ROLE})
	@Path("/{addressId}")
	public Response updateAddress(@PathParam("addressId") int id, Address addressUpdate) {
		LOG.debug("Updating the address id = {}", id);
		Address addressUpdated = service.updateAddress(id, addressUpdate);
		Response response = Response.ok(addressUpdated).build();
		return response;
	}
	
	
	@DELETE
	@RolesAllowed({ADMIN_ROLE})
	@Path("/{addressId}")
	public Response deleteAddress(@PathParam("addressId") int addressId) {
		LOG.debug("Deleting address with id = {}", addressId);
		Address add = service.deleteAddressById(addressId);
		Response response = Response.ok(add).build();
		return response;
	}
	

}
















