package pe.jay.board.model;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.Getter;
import pe.jay.member.model.MemberEntity;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BoardEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 20)
	private String title;

	@Column(nullable = false, length = 30)
	private String content;

	@CreatedBy
	@ManyToOne
	private MemberEntity member;

	@CreatedDate
	private LocalDateTime createdDate;

	protected BoardEntity() {
	}

	public BoardEntity(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void update(BoardDto updateRequest) {
		this.title = updateRequest.getTitle();
		this.content = updateRequest.getContent();
	}

	/**
	 *
	 * @return
	 */
	public boolean isEditable() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return false;
		}
		
		MemberEntity member = (MemberEntity) authentication.getPrincipal();
		if (!member.getUsername().equals(getMember().getUsername())) {
			return false;
		}
		return true;
	}
}
