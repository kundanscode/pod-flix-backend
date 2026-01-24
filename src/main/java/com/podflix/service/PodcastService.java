package com.podflix.service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;

import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.podflix.dto.PodcastDTO;
import com.podflix.entity.Podcast;
import com.podflix.repository.PodcastRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class PodcastService {

    private final PodcastRepository podcastRepository;
    private final YouTube youTube;

    @Value("${youtube.api.key}")
    private String apiKey;

    public PodcastService(PodcastRepository podcastRepository, YouTube youTube) {
        this.podcastRepository = podcastRepository;
        this.youTube = youTube;
    }

    @Cacheable(value = "podcasts", key = "'podcasts:' + #category + ':page:0:size:' + #pageable.pageSize", unless = "#pageable.pageNumber == 0")
    public List<PodcastDTO> getFirstPageCached(String category, Pageable pageable) {
        Page<Podcast> page = fetchFromDb(category, pageable);
        return page.getContent()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public Page<PodcastDTO> getPodcasts(String category, Pageable pageable) {

        if (pageable.getPageNumber() == 0) {
            List<PodcastDTO> cached = getFirstPageCached(category, pageable);

            long total = getTotalCount(category);
            return new PageImpl<>(cached, pageable, total);
        }

        Page<Podcast> page = fetchFromDb(category, pageable);
        return page.map(this::mapToDTO);
    }

    private long getTotalCount(String category) {
        if ("home".equalsIgnoreCase(category)) {
            return podcastRepository.count();
        } else {
            return podcastRepository.countByCategory(category);
        }
    }

    private Page<Podcast> fetchFromDb(String category, Pageable pageable) {
        if ("home".equalsIgnoreCase(category)) {
            return podcastRepository.findAll(pageable);
        } else {
            return podcastRepository.findByCategory(category, pageable);
        }
    }

    private PodcastDTO mapToDTO(Podcast podcast) {
        PodcastDTO dto = new PodcastDTO();
        dto.setVideoId(podcast.getVideoId());
        dto.setTitle(podcast.getTitle());
        dto.setDescription(podcast.getDescription());
        dto.setThumbnailUrl(podcast.getThumbnailUrl());
        dto.setThumbnailUrlHigh(podcast.getThumbnailUrlHigh());
        dto.setCategory(podcast.getCategory());
        dto.setPublishedAt(podcast.getPublishedAt());
        return dto;
    }

    public List<SearchResult> fetchVideosRaw(String query, String publishedAfter) throws IOException {
        try {
            YouTube.Search.List search = youTube.search().list(Collections.singletonList("id,snippet"));
            search.setKey(apiKey);
            search.setQ(query);
            search.setType(Collections.singletonList("video"));
            search.setVideoDuration("long"); // Filter for videos > 20 mins

            if (publishedAfter != null) {
                search.setPublishedAfter(publishedAfter);
            }

            search.setFields(
                    "items(id/kind,id/videoId,snippet/title,snippet/description,snippet/thumbnails/medium/url, snippet/thumbnails/high/url, snippet/publishedAt)");
            search.setMaxResults(50L); // Fetch top 50 per category for daily update

            SearchListResponse searchResponse = search.execute();
            return searchResponse.getItems();
        } catch (GoogleJsonResponseException e) {
            System.err.println("Service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
            throw e;
        } catch (IOException e) {
            System.err.println("IO error: " + e.getCause() + " : " + e.getMessage());
            throw e;
        }
    }
}
