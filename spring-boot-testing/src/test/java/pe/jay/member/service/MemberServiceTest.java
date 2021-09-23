package pe.jay.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import pe.jay.member.model.MemberDto;
import pe.jay.member.model.MemberEntity;
import pe.jay.member.repository.MemberRepository;
import pe.jay.member.type.MemberAuthority;
import pe.jay.support.AbstractBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemberServiceTest extends AbstractBootTest {
	
		@Autowired
	    private MemberService memberService;
		
	    @Autowired
	    private MemberRepository memberRepository;

	    private MemberDto getRequestDto() {
	    	MemberDto request = new MemberDto();
	        request.setUsername("tester@tester.org");
	        request.setPassword("1234");
	        return request;
	    }
	    
	    @Test
	    void create1() {
	    	assertThat(memberRepository.count()).isEqualTo(0L);
	    	MemberDto request = getRequestDto();
	    	memberService.create(request);
	    }
	    @Test
	    void create2() {
	    	assertThat(memberRepository.count()).isEqualTo(0L);
	    	MemberDto request = getRequestDto();
	    	memberService.create(request);
	    }

	    @Test
	    @DisplayName("멤버 서비스 생성")
	    void create() {
	    	// 멤버 요청 생성 후 저장
	    	MemberDto request = new MemberDto();
	        request.setUsername("tester@tester.org");
	        request.setPassword("1234");
	        MemberEntity member = memberService.create(request);

	        assertThat(member).isNotNull(); 
	        assertThat(member.getId()).isNotNull(); // DB에서 auto increment 로 생성된 아이디가 있어 id는 null 아님.
	        assertThat(member.getUsername()).isEqualTo("tester@tester.org"); 
	        assertThat(member.getPassword()).isNotEqualTo("1234"); // 서비스호출되며 비밀번호 암호화되어 입력값과 다른 지 확인
	    }
	    

	    @Test
	    @DisplayName("멤버 생성 실패 : 중복된 유저 이름")
	    void create_duplicate_username() {
	    	assertThatThrownBy(() -> {
	    		MemberDto request = new MemberDto();
	            request.setUsername("tester@tester.org");
	            request.setPassword("1234");
	            
	            // 같은 요청 클래스로 멤버 생성을 두 번 호출하여 중복된 예외 발생 기대
	            memberService.create(request);
	            memberService.create(request);
	        })//.isInstanceOf(DataAccessException.class)
	    	.isInstanceOf(DataIntegrityViolationException.class)
	        .hasMessageContaining("could not execute statement");
	    }
	    
	    @Test
	    void testCreate2times() {
	    	MemberDto request = new MemberDto();
	        request.setUsername("tester@tester.org");
	        request.setPassword("1234");
	        
	        // 같은 요청 클래스로 멤버 생성을 두 번 호출하여 중복된 예외 발생 기대
	        memberService.create(request);
	        memberService.create(request);
	    }
	    

	    @Test
	    @DisplayName("멤버 생성 : 어드민 테스트")
	    void create_admin() {
	    	MemberDto request = new MemberDto();
	    	// admin 이라는 단어가 들어가게 되면 편의성 어드민 권한 추가 되게 처리
	    	request.setUsername("admin@admin.com");
	        request.setPassword("1234");
	        
	        MemberEntity member = memberService.create(request);

	        assertThat(member.getAuthorities()).contains(MemberAuthority.ROLE_ADMIN);
	    }

}
