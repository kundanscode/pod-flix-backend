package com.podflix.dto;

import java.io.Serializable;

public class PodcastDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String videoId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String thumbnailUrlHigh;
    private String category;
    private String publishedAt;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailUrlHigh() {
        return thumbnailUrlHigh;
    }

    public void setThumbnailUrlHigh(String thumbnailUrlHigh) {
        this.thumbnailUrlHigh = thumbnailUrlHigh;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}
