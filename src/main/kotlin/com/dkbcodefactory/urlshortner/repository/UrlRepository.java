package com.dkbcodefactory.urlshortner.repository;

import com.dkbcodefactory.urlshortner.entity.UrlShortnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository  extends JpaRepository<UrlShortnerEntity,Long> {
}
