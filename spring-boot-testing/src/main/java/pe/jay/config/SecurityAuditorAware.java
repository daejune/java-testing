package pe.jay.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import pe.jay.member.model.MemberEntity;

@Component
public class SecurityAuditorAware implements AuditorAware<MemberEntity> {

    @Override
    public Optional<MemberEntity> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        MemberEntity member = ((MemberEntity) authentication.getPrincipal());
        return Optional.of(member);
    }
}