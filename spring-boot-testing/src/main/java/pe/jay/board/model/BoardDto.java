package pe.jay.board.model;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import pe.jay.member.model.MemberEntity;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardDto {
	
	private Long id;

	@Length(min = 2, max = 20)
	private String title;

	@Length(min = 2, max = 300)
	@NotBlank
	private String content;
	
	private String username;
	private LocalDateTime createdDate;
	
    public BoardDto(@NotNull Long id, @Length(min = 2, max = 20) String title, @Length(min = 2, max = 300) @NotBlank String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
	
	public BoardDto(BoardEntity board) {
		this.id = board.getId();
		this.title = board.getTitle();
		this.content = board.getContent();
		this.username = board.getMember().getUsername();
		this.createdDate = board.getCreatedDate();
	}
	
    public BoardEntity toEntity() {
        return new BoardEntity(title, content);
    }

}
