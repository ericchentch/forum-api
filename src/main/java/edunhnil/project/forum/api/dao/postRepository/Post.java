package edunhnil.project.forum.api.dao.postRepository;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private String id;
    private String authorId;
    private String title;
    private String content;
    private int view;
    private String categoryId;
    private Date created;
    private Date modified;
    private int deleted;
}
