package pe.jay.member.type;

import org.springframework.security.core.GrantedAuthority;

/**
 * Type of Member Authority
 */
public enum MemberAuthority implements GrantedAuthority {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String authority;

    MemberAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
