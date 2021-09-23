package pe.jay.member.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import pe.jay.member.type.MemberAuthority;

@Getter
@Entity
public class MemberEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = MemberAuthority.class, fetch=FetchType.EAGER)
    List<MemberAuthority> authorities;


    protected MemberEntity() {
    }

    public MemberEntity(String username, String password) {
    	this(username, password, new ArrayList<>(Arrays.asList(MemberAuthority.ROLE_USER)));        
    }
    
    public MemberEntity(String username, String password, List<MemberAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * 어드민 권한을 추가하기 위한 편의메서드
     */
    public void addAdminAuthWhenHasAdminStr() {
        if (username.contains("admin") && !this.authorities.contains(MemberAuthority.ROLE_ADMIN)) {
            this.authorities.add(MemberAuthority.ROLE_ADMIN);
        }
    }

    /**
     * 하단 시큐리티 UserDetails 구현 메서드
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}