package com.inspireme.domainlayer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity //a JPA annotation to make this object ready for storage in a JPA-based data store
@Data //Lombok annotation to create all the getters, setters, equals, hash, and toString methods, based on the fields
//@NoArgsConstructor
@EqualsAndHashCode(exclude = "comments") //In your entities you have to exclude the (toString,) hascode and equals method of the entity that you have the one-to-many relationship represented in this class (in this case, local variable 'comments', because if not, they will start trying to create the toString and will use the entity you have relationship and this one will do the same with you and then...... infinitive loop and stack-overflow
@ToString(exclude = "comments")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //JPA annotations to indicate it’s the primary key and automatically populated by the JPA provider
    private Long articleId;

    private String articleTitle;
    private String articleText;
    private String imageUrl;
    //private Date dateArticlePublished;
    private LocalDateTime dateArticlePublished;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL) //one Article (this class) with many Comments (this annotated set); mappedBy - the entity(join column) on the other side of the relationship - Many to One - Article; CascadeType.ALL - the persistence will propagate (cascade) all EntityManager operations (PERSIST, REMOVE, REFRESH, MERGE, DETACH) of the owner - Article (this class) - to the associated entities (Comment) - if an Article is deleted, the set of Comments will be deleted; the associated entity Comment will be saved at the same time with the owner Article without the need of calling its own save function explicitly
    private Set<Comment> comments;

    @ManyToOne //many Articles (this class) in 1 Category(this annotated column)
    @JoinColumn //The corresponding table to this annotated column - Category - has a column - categoryId - with a foreign key to the referenced table (of this class) - Article
    private Category category;

    @ManyToOne //many Articles (this class) by 1 User(this annotated column)
    @JoinColumn //the corresponding table to this annotated column - User - has a column - userId - with a foreign key to the referenced table (of this class) - Article
    private User articlePublishedBy;

    //include the set of Comments in the constructor
    public Article (String articleTitle, String articleText, String imageUrl, /*Date dateArticlePublished,*/ Category category, User articlePublishedBy, Comment... comments) {
        this.articleTitle = articleTitle;
        this.articleText = articleText;
        this.imageUrl = imageUrl;
        this.dateArticlePublished = LocalDateTime.now(); //dateArticlePublished;
        this.category = category;
        this.articlePublishedBy = articlePublishedBy;
        this.comments = Stream.of(comments).collect(Collectors.toSet());
        this.comments.forEach(c -> c.setArticle(this));
    }
}
