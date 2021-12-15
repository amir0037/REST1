 /* @author 
 * Amirova Maria 040980332
 * De Silva Ayesh 040958448
 * Redona Herman 041017699
 * Nguyen Ny Ngoc Han 040972914
 */
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static bloodbank.utility.MyConstants.ADMIN_ROLE;
import static bloodbank.utility.MyConstants.USER_ROLE;
import javax.ws.rs.core.Response.Status;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bloodbank.ejb.BloodBankService;
import bloodbank.entity.BloodBank;
import bloodbank.entity.BloodDonation;

@Path("bloodbank")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BloodBankResource {
	
	private static final Logger LOG = LogManager.getLogger();

	@EJB
	protected BloodBankService service;

	@Inject
	protected SecurityContext sc;

	@GET
	public Response getBloodBanks() {
		LOG.debug("Retrieving all blood banks...");
		List<BloodBank> bloodBanks = service.getAllBloodBanks();
		LOG.debug("Blood banks found = {}", bloodBanks);
		Response response = Response.ok(bloodBanks).build();
		return response;
	}
	
	@GET
	@Path("/{bloodBankID}")
	public Response getBloodBankById(@PathParam("bloodBankID") int bloodBankId) {
		LOG.debug("Retrieving blood bank with id = {}", bloodBankId);
		BloodBank bloodBank = service.getBloodBankById(bloodBankId);
		Response response = Response.ok(bloodBank).build();
		return response;
	}

	@DELETE
	@Path("/{bloodBankID}")
	public Response deleteBloodBank(@PathParam("bloodBankID") int bbID) {
		LOG.debug("Deleting blood bank with id = {}", bbID);
		BloodBank bb = service.deleteBloodBank(bbID);
		Response response = Response.ok(bb).build();
		return response;
		
	}
	
	// Please try to understand and test the below methods:
	@RolesAllowed({ADMIN_ROLE})
	@POST
	public Response addBloodBank(BloodBank newBloodBank) {
		LOG.debug("Adding a new blood bank = {}", newBloodBank);
		if (service.isDuplicated(newBloodBank)) {
			HttpErrorResponse err = new HttpErrorResponse(Status.CONFLICT.getStatusCode(), "entity already exists");
			return Response.status(Status.CONFLICT).entity(err).build();
		}
		else {
			BloodBank tempBloodBank = service.persistBloodBank(newBloodBank);
			return Response.ok( tempBloodBank).build();
		}
	}

	@RolesAllowed({ADMIN_ROLE})
	@POST
	@Path("/{bloodBankID}/blooddonation")
	public Response addBloodDonationToBloodBank(@PathParam("bloodBankID") int bbID, BloodDonation newBloodDonation) {
		LOG.debug( "Adding a new BloodDonation to blood bank with id = {}", bbID);
		
		BloodBank bb = service.getBloodBankById(bbID);
		newBloodDonation.setBank(bb);
		bb.getDonations().add(newBloodDonation);
		service.updateBloodBank( bbID, bb);
		
		return Response.ok( bb).build();
	}

	@RolesAllowed({ADMIN_ROLE, USER_ROLE})
	@PUT
	@Path("/{bloodBankID}")
	public Response updateBloodBank(@PathParam("bloodBankID") int bbID, BloodBank updatingBloodBank) {
		LOG.debug("Updating a specific blood bank with id = {}", bbID);
		Response response = null;
		BloodBank updatedBloodBank = service.updateBloodBank(bbID, updatingBloodBank);
		response = Response.ok(updatedBloodBank).build();
		return response;
	}
	
}
