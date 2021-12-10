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
import bloodbank.entity.DonationRecord;

@Path("DonationRecord")
public class DonationRecordResource {

	private static final Logger LOG = LogManager.getLogger();

	@EJB
	protected BloodBankService service;

	@Inject
	protected SecurityContext sc;
	
	@GET
	@RolesAllowed({ADMIN_ROLE})
	public Response getDonationRecord() {
		LOG.debug("Retrieving all DonationRecords...");
		List<DonationRecord> donationRecord = service.getAll(DonationRecord.class, DonationRecord.ALL_RECORDS_QUERY_NAME);
		Response response = Response.ok(donationRecord).build();
		return response;
	}
	
	
	//need to change for USER_ROLE
	@GET
	@RolesAllowed({ADMIN_ROLE})
	@Path("/{recordId}")
	public Response getDonationRecordById(@PathParam("recordId") int recordId) {
		LOG.debug("Retrieving Donation Record with id = {}", recordId);
		DonationRecord donationRecord = service.getDonationRecordById(recordId);
		Response response = Response.ok(donationRecord).build();
		return response;
	}
	
	
	@POST
	@RolesAllowed({ADMIN_ROLE})
	@Path("/{recordId}")
	public Response addDonationRecord(DonationRecord newDonationRecord) {
		LOG.debug("Adding new Donation Record = {}", newDonationRecord);
		DonationRecord donation = service.persistDonationRecord(newDonationRecord);
		Response response = Response.ok(donation).build();
		return response;
	}
	
	@PUT
	@RolesAllowed({ADMIN_ROLE})
	@Path("/{recordId}")
	public Response updateDonationRecord(@PathParam("recordId") int recordId, DonationRecord newDonationRecord) {
		LOG.debug("Updating the Donation Record id = {}", recordId);
		DonationRecord donationUpdated = service.updateDonationRecord(recordId, newDonationRecord);
		Response response = Response.ok(donationUpdated).build();
		return response;
	}
	
	@DELETE
	@RolesAllowed({ADMIN_ROLE})
	@Path("/{recordId}")
	public Response deleteDonationRecord(@PathParam("recordId") int recordId) {
		LOG.debug("Deleting DonationRecord with id = {}", recordId);
		DonationRecord record = service.deleteDonationRecordById(recordId);
		Response response = Response.ok(record).build();
		return response;
	}
	
	
}
