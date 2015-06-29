package org.fedon.bitwise.rest.resource;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.fedon.bitwise.persistence.model.Member;
import org.fedon.bitwise.persistence.repository.CredentialsDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Dmytro Fedonin
 *
 */
@Path("/member")
@RolesAllowed("user")
public class MemberResource {
    @Context
    SecurityContext securityContext;
    @Autowired
    CredentialsDao credentialsDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Member getCurrentMember() {
        String userName = securityContext.getUserPrincipal().getName();
        return credentialsDao.oneByUsername(userName).getMember();
    }
}
