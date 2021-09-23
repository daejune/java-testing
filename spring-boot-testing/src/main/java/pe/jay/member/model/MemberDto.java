package pe.jay.member.model;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 멤버 생성과 수정에 사용될 DTO 객체
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberDto {

	@Length(min = 2, max = 20)
	@Email
	@NotBlank
	private String username;

	@Length(min = 2, max = 20)
	private String password;

	private String passwordConfirm;
	
	public MemberDto(MemberEntity entity) {
		this.username = entity.getUsername();
		this.password = entity.getPassword();
	}

	public MemberEntity toEntity() {
		return new MemberEntity(username, password);
	}
}
