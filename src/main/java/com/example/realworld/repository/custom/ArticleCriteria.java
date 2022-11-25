package com.example.realworld.repository.custom;

import com.example.realworld.entity.Article;
import com.example.realworld.model.article.dto.ArticleDTOFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ArticleCriteria {
    private final EntityManager em;

    public Map<String, Object> findAll(ArticleDTOFilter filter) {
        //JSPQL
        StringBuilder query = new StringBuilder("select a from Article a left join a.author au left join a.userFavorited ufa where 1=1");
        Map<String, Object> params = new HashMap<>();
        if(filter.getTag()!= null){
            query.append(" and a.tagList like :tag");
            params.put("tag", "%"+filter.getTag()+"%");
        }
        if(filter.getAuthor()!= null){
            query.append(" and au.username = :author");
            params.put("author", filter.getAuthor());
        }
        if(filter.getFavorited()!= null){
            query.append(" and ufa.username = :favorited");
            params.put("favorited",  filter.getFavorited());
        }

        TypedQuery<Article> tQuery = em.createQuery(query.toString(), Article.class);
        Query countQuery = em.createQuery(query.toString().replace("select a", "select count(a.id)"));

        params.forEach((k,v)->{
            tQuery.setParameter(k, v);
            countQuery.setParameter(k,v);
        });

        //Phan trang
        tQuery.setFirstResult(filter.getOffset());
        tQuery.setMaxResults(filter.getLimit());
        long totalArticle = (long) countQuery.getSingleResult();
        List<Article> listArticles = tQuery.getResultList();
        Map<String, Object> results = new HashMap<>();
        results.put("listArticles", listArticles);
        results.put("totalArticle", totalArticle);
        return results;
    }

    public Map<String, Object> getNewFeed(List<String> listUsername, ArticleDTOFilter filter) {

        StringBuilder query = new StringBuilder("select a from Article a where a.author.username in (:followers)");

        Query countTotalQuery = em.createQuery(query.toString().replace("select a", "select count(a.id)"));
        query.append(" order by a.createdAt DESC");
        TypedQuery<Article> tQuery = em.createQuery(query.toString(), Article.class);
        tQuery.setParameter("followers", listUsername);
        countTotalQuery.setParameter("followers", listUsername);

        tQuery.setFirstResult(filter.getOffset());
        tQuery.setMaxResults(filter.getLimit());

        long totalArtical = (long) countTotalQuery.getSingleResult();
        List<Article> listArticle = tQuery.getResultList();

        Map<String, Object> wrapper = new HashMap<String, Object>();
        wrapper.put("articlesCount", totalArtical);
        wrapper.put("listArticle", listArticle);
        return wrapper;
    }
}
