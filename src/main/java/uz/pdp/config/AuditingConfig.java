package uz.pdp.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
public class AuditingConfig {

	@Bean
	
	AuditorAware<UUID> auditorAware(){
		return new SpringSecrurityAuditAwareImpl ();
	}
}
