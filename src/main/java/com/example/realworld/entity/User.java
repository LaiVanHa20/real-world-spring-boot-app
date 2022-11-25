package com.example.realworld.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbluser")
// @Data  Khong nen dung, no ghi de nhieu ham: equals maf Set lai su dung ham nay
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true)
    private String username;
    private String bio;
    private String image;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tbluserfollow", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> followers; // ai dang follow toi

    @ManyToMany(mappedBy = "followers")// dang follow ai
    private Set<User> followings;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_favorite_article", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "article_id"))
    private Set<Article> articleFavorited;

    @OneToMany(mappedBy = "author")
    private List<Article> articles;

    //Comment
    @OneToMany(mappedBy = "author")
    private List<Comment> comments;
}
