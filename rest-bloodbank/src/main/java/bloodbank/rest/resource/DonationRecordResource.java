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


import static bloodbank.utility.MyConstants.ADMIN_ROLE;
import static bloodbank.utility.MyConstants.DONATION_RECORD_RESOURCE_NAME;
import static bloodbank.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static bloodbank.utility.MyConstants.RESOURCE_PATH_ID_PATH;
import static bloodbank.utility.MyConstants.USER_ROLE;
import static bloodbank.utility.MyConstants.ACCESS_TO_THE_SPECIFIED_RESOURCE_HAS_BEEN_FORBIDDEN;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.soteria.WrappingCallerPrincipal;

import bloodbank.entity.SecurityUser;

import bloodbank.ejb.BloodRecordService;
import bloodbank.entity.Address;
import bloodbank.entity.DonationRecord;
import bloodbank.entity.Person;

@Path(DONATION_RECORD_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DonationRecordResource {
    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected BloodRecordService service;

    @Inject
    protected SecurityContext sc;

    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getRecords() {
        LOG.debug("retrieving all donations ...");
        List<DonationRecord> records = service.getAllRecord();
        Response response = Response.ok(records).build();
        return response;
    }

    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getRecordById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("try to retrieve specific donation " + id);
        Response response;
        Person person;
        DonationRecord record;
        
        //Any admin may read any DonationRecord
        if (sc.isCallerInRole(ADMIN_ROLE)) {
        	record = service.getRecordId(id);
        	if (record == null)
        		return Response.status(Status.NOT_FOUND).build();
        	response = Response.ok(record).build();
    	//A user may only read their own DonationRecord(s)
        } else if (sc.isCallerInRole(USER_ROLE)) {
        	WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal)sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser)wCallerPrincipal.getWrapped();
            person = sUser.getPerson();
            record = service.getRecordId(id);
            if (record == null) {
        		return Response.status(Status.NOT_FOUND).build();
            //A user may only read their own DonationRecord
            } else if (person != null && record != null && person.getId() == record.getOwner().getId()) {
            	response = Response.ok(record).build();
            } else {
            	response = Response.status(FORBIDDEN).entity(ACCESS_TO_THE_SPECIFIED_RESOURCE_HAS_BEEN_FORBIDDEN).build();
            }
    	} else {
        	response = Response.status(FORBIDDEN).entity(ACCESS_TO_THE_SPECIFIED_RESOURCE_HAS_BEEN_FORBIDDEN).build();
        }
        return response;
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addRecord(DonationRecord newRecord) {
        Response response = null;
        service.persistRecord(newRecord);
        response = Response.ok(newRecord).build();
        return response;
    }

    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response deleteRecord(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        Response response = null;
        service.deleteRecordById(id);
        response = Response.ok().build();
        return response;
    }
}
