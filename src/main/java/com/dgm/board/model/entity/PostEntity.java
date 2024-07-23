package com.dgm.board.model.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE \"post\" SET deleteddatetime = CURRENT_TIMESTAMP WHERE postid = ?")
@Where(clause = "deleteddatetime IS NULL")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @Column(columnDefinition = "TEXT")
    private String body;
    @Column
    private ZonedDateTime createdDateTime;
    @Column
    private ZonedDateTime updatedDateTime;
    @Column
    private ZonedDateTime deletedDateTime;

}
