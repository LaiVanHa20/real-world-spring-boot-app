package com.example.realworld.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tblarticle")
// @Data  Khong nen dung, no ghi de nhieu ham: equals maf Set lai su dung ham nay
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String slug;
    private String title;
    private String description;
    private String body;
    private String tagList;
    private Date createdAt;
    private Date updatedAt;

    public List<String> getTagList(){
        return Arrays.asList(this.tagList.split(";"));
    }
    public void setTagList(List<String> tagList){
        StringBuilder str = new StringBuilder();
        for(String tag: tagList){
            str.append(tag).append(";");
        }
        this.tagList = str.substring(0, str.length()-1).toString();
    }

    @ManyToOne
    //Khoa ngoai
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToMany(mappedBy = "articleFavorited")
    private Set<User> userFavorited;

    //Comment
    @OneToMany(mappedBy = "article")
    private List<Comment> comments;
}
