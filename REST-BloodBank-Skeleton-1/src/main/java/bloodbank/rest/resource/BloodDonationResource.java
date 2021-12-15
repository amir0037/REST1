 /* @author 
 * Amirova Maria 040980332
 * De Silva Ayesh 040958448
 * Redona Herman 041017699
 * Nguyen Ny Ngoc Han 040972914
 */
package bloodbank.rest.resource;

import static bloodbank.utility.MyConstants.*;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bloodbank.ejb.BloodBankService;
import bloodbank.entity.BloodDonation;

@Path("BloodDonation")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BloodDonationResource {

	private static final Logger LOG = LogManager.getLogger();

	@EJB
	protected BloodBankService service;

	@Inject
	protected SecurityContext sc;
	
	@GET
	@RolesAllowed({ADMIN_ROLE, USER_ROLE})
	public Response getBloodDonation() {
		LOG.debug("Retrieving all BloodDonations...");
		List<BloodDonation> donation = service.getAll(BloodDonation.class, BloodDonation.FIND_ALL);
		Response response = Response.ok(donation).build();
		return response;
	}
	
	@GET
	@RolesAllowed({ADMIN_ROLE, USER_ROLE})
	@Path("/{id}")
	public Response getBloodDonationById(@PathParam("id") int bloodId) {
		LOG.debug("Retrieving BloodDonation with id = {}", bloodId);
		BloodDonation donation = service.getBloodDonationById(bloodId);
		Response response = Response.ok(donation).build();
		return response;
	}
	
	@POST
	@RolesAllowed({ADMIN_ROLE})
	public Response addBloodDonation(BloodDonation newBlood) {
		LOG.debug("Adding new BloodDonation = {}", newBlood);
		BloodDonation blood = service.persistBloodDonation(newBlood);
		Response response = Response.ok(blood).build();
		return response;
	}
	
	@PUT
	@RolesAllowed({ADMIN_ROLE})
	@Path("/{bloodId}")
	public Response updateBloodDonation(@PathParam("bloodId") int id, BloodDonation bloodToUpdate) {
		LOG.debug("Updating the BloodDonation with id = {}", id);
		BloodDonation bloodUpdated = service.updateBloodDonation(id, bloodToUpdate);
		Response response = Response.ok(bloodUpdated).build();
		return response;
	}
	
	
	@DELETE
	@RolesAllowed({ADMIN_ROLE})
	@Path("/{bloodId}")
	public Response deleteBloodDonation(@PathParam("bloodId") int bloodId) {
		LOG.debug("Deleting BloodDonation with id = {}", bloodId);
		BloodDonation blood = service.deleteBloodDonation(bloodId);
		Response response = Response.ok(blood).build();
		return response;
	}
	
	
	
}






























