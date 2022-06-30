package uz.pdp.config;

import java.util.Optional;
import java.util.UUID;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import uz.pdp.entity.User;

public class SpringSecrurityAuditAwareImpl implements AuditorAware<UUID> {

	@Override
	public Optional<UUID> getCurrentAuditor() {
		Authentication authenticateAction = SecurityContextHolder.getContext().getAuthentication();
		if (authenticateAction != null && authenticateAction.isAuthenticated()
				&& !authenticateAction.getPrincipal().equals("anonymousUser")) {
			User user = (User) authenticateAction.getPrincipal();
			return Optional.of(user.getUuid());
		}

		return Optional.empty();
	}

}
