package com.podflix.scheduler;

import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.podflix.entity.Podcast;
import com.podflix.repository.PodcastRepository;
import com.podflix.service.PodcastService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class PodcastScheduler {

    private final PodcastService podcastService;
    private final PodcastRepository podcastRepository;

    private final List<String> CATEGORIES = Arrays.asList("Technology", "Finance", "Health", "Travel");

    public PodcastScheduler(PodcastService podcastService, PodcastRepository podcastRepository) {
        this.podcastService = podcastService;
        this.podcastRepository = podcastRepository;
    }

    // Runs every hour to fetch fresh content
    @Scheduled(cron = "0 0 * * * *")
    // @EventListener(ApplicationReadyEvent.class) // Also run on startup
    public void fetchPodcasts() {
        System.out.println("Starting hourly podcast fetch...");
        for (String category : CATEGORIES) {
            try {
                // Find the latest podcast in this category to get the last published date
                Podcast latestPodcast = podcastRepository.findTopByCategoryOrderByPublishedAtDesc(category);
                String publishedAfter = null;

                if (latestPodcast != null) {
                    publishedAfter = latestPodcast.getPublishedAt();
                    System.out.println("Fetching " + category + " podcasts published after: " + publishedAfter);
                } else {
                    System.out.println("Fetching all " + category + " podcasts (initial load).");
                }

                // "podcast" keyword added to ensure we get podcasts
                List<SearchResult> results = podcastService.fetchVideosRaw(category + " podcast", publishedAfter);

                if (results != null) {
                    for (SearchResult result : results) {
                        ResourceId resourceId = result.getId();
                        SearchResultSnippet snippet = result.getSnippet();

                        if (resourceId == null || snippet == null)
                            continue;

                        String videoId = resourceId.getVideoId();
                        if (videoId == null)
                            continue;

                        if (!podcastRepository.existsByVideoId(videoId)) {
                            Podcast podcast = new Podcast();
                            podcast.setVideoId(videoId);
                            podcast.setTitle(snippet.getTitle());
                            podcast.setDescription(snippet.getDescription());

                            ThumbnailDetails thumbnails = snippet.getThumbnails();
                            if (thumbnails != null) {
                                Thumbnail medium = thumbnails.getMedium();
                                if (medium != null) {
                                    podcast.setThumbnailUrl(medium.getUrl());
                                }
                                Thumbnail high = thumbnails.getHigh();
                                if (high != null) {
                                    podcast.setThumbnailUrlHigh(high.getUrl());
                                }
                            }

                            if (snippet.getPublishedAt() != null) {
                                podcast.setPublishedAt(snippet.getPublishedAt().toString());
                            }
                            podcast.setCategory(category);
                            podcastRepository.save(podcast);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Scheduler cycle done.");
    }
}
