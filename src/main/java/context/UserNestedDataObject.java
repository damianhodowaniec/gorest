package context;

import lombok.Data;

@Data
public class UserNestedDataObject {
    String postTitle;
    String postBody;
    String comment;
    String todoTitle;
    String dueOn;
    String status;
    String postId;
}