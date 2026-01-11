package com.podflix.repository;

import com.podflix.entity.Podcast;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PodcastRepository extends JpaRepository<Podcast, Long> {
    Page<Podcast> findByCategory(String category, Pageable pageable);

    boolean existsByVideoId(String videoId);

    Podcast findTopByCategoryOrderByPublishedAtDesc(String category);
}
