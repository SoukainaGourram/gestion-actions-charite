package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Media;
import com.example.demo.services.MediaService;

@RestController
@RequestMapping("/api")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping(value = "/actions/{actionId}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Media uploadMedia(@PathVariable Long actionId, @RequestParam("file") MultipartFile file) {
        return mediaService.uploadMedia(actionId, file);
    }

    @GetMapping("/actions/{actionId}/media")
    public List<Media> listMedia(@PathVariable Long actionId) {
        return mediaService.getByActionId(actionId);
    }

    @DeleteMapping("/media/{id}")
    public void deleteMedia(@PathVariable Long id) {
        mediaService.delete(id);
    }
}
