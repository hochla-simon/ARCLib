package cz.inqool.uas.bpm.security;

import cz.inqool.uas.security.UserDetails;
import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cz.inqool.uas.util.Utils.asList;
import static cz.inqool.uas.util.Utils.unwrap;

/**
 * Spring MVC and Camunda BPM auth integration filter.
 */
public class BpmAuthenticationFilter extends GenericFilterBean implements ApplicationContextAware {

    private ApplicationContext context;

    /**
     * Sets currently authenticated user to BPM component in every request.
     * @param request incoming http request
     * @param response outgoing http response
     * @param filterChain next filter in line
     * @throws IOException bubble up exception from next filter
     * @throws ServletException bubble up exception from next filter
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        IdentityService identityService = context.getBean(IdentityService.class);
        Map<String, UserDetails> beans = context.getBeansOfType(UserDetails.class);
        if (beans.size() > 0) {
            UserDetails user = unwrap(beans.values().iterator().next());

            if (user != null) {
                List<String> authorities = user.getAuthorities().stream()
                                               .map(GrantedAuthority::getAuthority)
                                               .collect(Collectors.toList());

                String tenantId = user.getTenantId();

                if (tenantId != null) {
                    identityService.setAuthentication(user.getId(), authorities, asList(tenantId));
                } else {
                    identityService.setAuthentication(user.getId(), authorities);
                }
            }
        }

        filterChain.doFilter(request, response);

        identityService.setAuthenticatedUserId(null);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }
}