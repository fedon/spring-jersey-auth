package org.fedon.bitwise.rest.config;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

/**
 * @author Dmytro Fedonin
 *
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {
    public static String principalName = "principalName";
    public static String rolesAttr = "rolesAttr";
    @Context
    HttpServletRequest httpRequest;

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        HttpSession session = httpRequest.getSession(true);

        final String name = (String) session.getAttribute(principalName);
        @SuppressWarnings("unchecked")
        final Collection<String> roles = (Collection<String>) session.getAttribute(rolesAttr);
        if (name != null) {
            SecurityContext securityContext = new SecurityContext() {

                @Override
                public boolean isUserInRole(String role) {
                    if (roles != null && roles.contains(role)) {
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean isSecure() {
                    return requestContext.getSecurityContext().isSecure();
                }

                @Override
                public Principal getUserPrincipal() {
                    return new Principal() {

                        @Override
                        public String getName() {
                            return name;
                        }
                    };
                }

                @Override
                public String getAuthenticationScheme() {
                    return requestContext.getSecurityContext().getAuthenticationScheme();
                }
            };

            requestContext.setSecurityContext(securityContext);
        }
    }
}
