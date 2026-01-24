package com.podflix.controller;

import com.podflix.dto.PodcastDTO;
import com.podflix.service.PodcastService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/podcasts")
public class PodcastController {

    private final PodcastService podcastService;

    public PodcastController(PodcastService podcastService) {
        this.podcastService = podcastService;
    }

    @GetMapping
    public ResponseEntity<Page<PodcastDTO>> getPodcasts(
            @RequestParam String category, Pageable pageable) {

        return ResponseEntity.ok(podcastService.getPodcasts(category, pageable));
    }

}
