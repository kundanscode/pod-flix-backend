package com.podflix.dto;

public record PodcastDTO(
                String videoId,
                String title,
                String description,
                String thumbnailUrl,
                String thumbnailUrlHigh,
                String category,
                String publishedAt) {
}
