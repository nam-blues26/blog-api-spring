package com.blog.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "description", nullable = false)
    private String desc;

    @Column(name = "image")
    private String image;

    @Column(name = "slug")
    private String slug;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category postCategory;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User postUser;

}
