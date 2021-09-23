package pe.jay.member.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pe.jay.member.model.MemberDto;
import pe.jay.member.model.MemberEntity;
import pe.jay.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

/**
 * 멤버 서비스
 */
@Service
@Transactional
public class MemberService implements UserDetailsService {
	
    private Logger logger = LoggerFactory.getLogger(getClass());

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;

	public MemberService(PasswordEncoder passwordEncoder, MemberRepository memberRepository) {
		this.passwordEncoder = passwordEncoder;
		this.memberRepository = memberRepository;
	}

    /**
     * 편의용으로 멤버들을 많이 만들기 위해 잠시 만든 메서드
     */
	public void createDummy() {		
		for (int i = 0; i < 200; i++) {			
			memberRepository.save(new MemberEntity("user" + i + "@naver.com", passwordEncoder.encode("1234")));
		}		
		
		logger.info("Create Dummy Member: {}", memberRepository.count());
	}
	
	public MemberDto findById(Long id) {
		Optional<MemberEntity> entity = memberRepository.findById(id);
		return entity.map(MemberDto::new).orElse(null);
	}

    /**
     * 멤버 생성
     * @param createRequest
     * @return
     */
	public MemberEntity create(MemberDto createRequest) {

		createRequest.setPassword(passwordEncoder.encode(createRequest.getPassword()));
		MemberEntity member = createRequest.toEntity();
		member.addAdminAuthWhenHasAdminStr();
		return memberRepository.save(member);
	}

    /**
     * 멤버 전체 조회
     * @param pageable
     * @return
     */
	public Page<MemberEntity> findAll(Pageable pageable) {
		return memberRepository.findAll(pageable);
	}

    /**
     * username 을 받아서 중복된 멤버인지 조회
     * @param username
     * @return
     */
	public boolean isDuplicate(String username) {
		return memberRepository.existsByUsername(username);
	}

    /**
     * 스프링 시큐리티에서 사용되는 조회용 메서드
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return memberRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
	}

}