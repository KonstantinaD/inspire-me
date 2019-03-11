package com.inspireme.domainlayer;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity // This tells Hibernate to make a table out of this class
@Data
//@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //JPA annotations to indicate it’s the primary key and automatically populated by the JPA provider
    private Long commentId;

    private String commentText;
    //private Date dateCommentPublished;
    private LocalDateTime dateCommentPublished;

    @ManyToOne //many-to-one relationship between the 2 entities - Comment and Article - many Comments (this class) for 1 Article (this annotated column)
    @JoinColumn //(the entity - Comment - is the owner of the relationship and) the corresponding table to this annotated column - Article - has a column - acticleId - with a foreign key to the referenced table (of this class) - Comment
    @JsonManagedReference
    private Article article;

    @ManyToOne //many Comments (this class) by 1 User(this annotated column)
    @JoinColumn //the corresponding table to this annotated column - User - has a column - userId - with a foreign key to the referenced table (of this class) - Comment
    private User commentPublishedBy;

    public Comment( String commentText, /*Date dateCommentPublished*/ Article article, User commentPublishedBy) {

        this.commentText = commentText;
        this.dateCommentPublished = LocalDateTime.now(); //dateCommentPublished;
        this.article = article;
        this.commentPublishedBy = commentPublishedBy;
    }

    /* normally this class will have getters and/or setters for the fields (variables) (e.g. below) but they are automatically created behind the scenes by Lombok.
    Lombok creates getters and setters but if we were setting them manually, commentId and dateCommentPublished will have only getters (commentId is auto-generated sequentially and  dateCommentPublished is the system date

     public String getCommentText() {  //getter
        return commentText;
     }

     public void setCommentText(String commentText) {  //setter
        this.commentText = commentText;
     }*/
}
