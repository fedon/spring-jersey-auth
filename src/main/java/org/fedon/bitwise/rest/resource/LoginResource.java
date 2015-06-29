package org.fedon.bitwise.rest.resource;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.codec.digest.DigestUtils;
import org.fedon.bitwise.persistence.model.Credentials;
import org.fedon.bitwise.persistence.model.Member;
import org.fedon.bitwise.persistence.repository.CredentialsDao;
import org.fedon.bitwise.rest.config.AuthFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Dmytro Fedonin
 *
 */
@Path("/login")
public class LoginResource {
    static public String invalidURI = "Invalid redirect URI. If the error persists please contact administrator.";
    Logger log = LoggerFactory.getLogger(getClass());

    @Context
    HttpServletRequest httpRequest;
    @Autowired
    CredentialsDao credentialsDao;

    @POST
    @Path("register")
    public Response register(
            @FormParam("username") String username,
            @FormParam("password") String password,

            @FormParam("name") String name,
            @FormParam("surname") String surname,
            @FormParam("email") String email
    ){
        Credentials credentials = new Credentials();
        credentials.setPasswdHash(DigestUtils.sha256Hex(password));
        credentials.setUsername(username);
        Member member = new Member();
        member.setEmail(email);
        member.setFirstName(name);
        member.setLastName(surname);
        credentials.setMember(member);
        credentialsDao.saveAndFlush(credentials);
        
        return login(username, password);
    }

    @POST
    public Response login(@FormParam("username") String username, @FormParam("password") String password) {
        Credentials credentials = credentialsDao.oneByUsername(username);
        String hash = DigestUtils.sha256Hex(password);
        log.info("cred: " + credentials);
        if (credentials == null || !hash.equals(credentials.getPasswdHash())) { // back to login page
            log.info("pass: " + (credentials == null ? null : credentials.getPasswdHash()));
            URI location = UriBuilder.fromPath("../login.html").build();
            return Response.seeOther(location).build();
        }

        HttpSession session = httpRequest.getSession(true);
        // FIXME for oauth id may be better
        session.setAttribute(AuthFilter.principalName, username);
        // TODO implement roles
        Collection<String> roles = Collections.singletonList("user");
        session.setAttribute(AuthFilter.rolesAttr, roles);

        URI location = UriBuilder.fromPath("../demo.html").build();
        return Response.seeOther(location).build();
    }

    @GET
    @Path("out")
    public Response logout() {
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(AuthFilter.principalName, null);

        URI location = UriBuilder.fromPath("../login.html").build();
        return Response.temporaryRedirect(location).build();
    }
}
