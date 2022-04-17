/**
 * File: BloodBankResource.java Course materials (22W) CST 8277
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
package bloodbank.rest.resource;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
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
import static bloodbank.utility.MyConstants.BLOODBANK_RESOURCE_NAME;
import static bloodbank.utility.MyConstants.ADMIN_ROLE;
import static bloodbank.utility.MyConstants.BLOODBANK_RESOURCE_NAME;
import static bloodbank.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static bloodbank.utility.MyConstants.RESOURCE_PATH_ID_PATH;
import static bloodbank.utility.MyConstants.USER_ROLE;

import javax.ws.rs.core.Response.Status;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bloodbank.ejb.BloodBankService;
import bloodbank.entity.BloodBank;
import bloodbank.entity.BloodDonation;

//@Path("/{bloodbank}}")
@Path(BLOODBANK_RESOURCE_NAME)
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
//        Response response = Response.status(Status.OK).entity(bloodBanks).build();
        return response;
    }

    @GET
//    @Path("/{bloodBankID}")
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getBloodBankById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int bloodBankId) {
        LOG.debug("Retrieving blood bank with id = {}", bloodBankId);
        BloodBank bloodBank = service.getBloodBankById(bloodBankId);
        Response response = Response.ok(bloodBank).build();
        return response;
    }

    @RolesAllowed({ADMIN_ROLE})
    @DELETE
//    @Path("/{bloodBankID}")
    @Path(RESOURCE_PATH_ID_PATH)
    public Response deleteBloodBank(@PathParam(RESOURCE_PATH_ID_ELEMENT) int bbID) {
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
        if (sc.isCallerInRole(ADMIN_ROLE)) {
            if (service.isDuplicated(newBloodBank)) {
                HttpErrorResponse err = new HttpErrorResponse(Status.CONFLICT.getStatusCode(), "entity already exists");
                return Response.status(Status.CONFLICT).entity(err).build();
            } else {
                BloodBank tempBloodBank = service.persistBloodBank(newBloodBank);
                return Response.ok(tempBloodBank).build();
            }
        } else {
            throw new ForbiddenException("Access Denied");
        }

    }

    @RolesAllowed({ADMIN_ROLE})
    @POST
    @Path(RESOURCE_PATH_ID_PATH + "/blooddonation")
//    @Path("/{bloodBankID}/blooddonation")
    public Response addBloodDonationToBloodBank(@PathParam(RESOURCE_PATH_ID_ELEMENT) int bbID, BloodDonation newBloodDonation) {
        LOG.debug("Adding a new BloodDonation to blood bank with id = {}", bbID);

        BloodBank bb = service.getBloodBankById(bbID);
        newBloodDonation.setBank(bb);
        bb.getDonations().add(newBloodDonation);
        service.updateBloodBank(bbID, bb);

        return Response.ok(bb).build();
    }

    @RolesAllowed({ADMIN_ROLE})
    @PUT
    @Path(RESOURCE_PATH_ID_PATH)
    public Response updateBloodBank(@PathParam(RESOURCE_PATH_ID_ELEMENT) int bbID, BloodBank updatingBloodBank) {
        LOG.debug("Updating a specific blood bank with id = {}", bbID);
        if (sc.isCallerInRole(ADMIN_ROLE)) {
            Response response = null;
            BloodBank updatedBloodBank = service.updateBloodBank(bbID, updatingBloodBank);
            response = Response.ok(updatedBloodBank).build();
            return response;
        } else {
            throw new ForbiddenException("Access Denied");
        }

    }

}
