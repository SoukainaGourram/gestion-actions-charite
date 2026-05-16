package com.example.demo.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Action;
import com.example.demo.entities.Media;
import com.example.demo.repos.ActionRepository;
import com.example.demo.repos.MediaRepository;

@Service
@Transactional
public class MediaService {

    private final MediaRepository mediaRepository;
    private final ActionRepository actionRepository;
    private final Path uploadRoot;

    public MediaService(MediaRepository mediaRepository,
                        ActionRepository actionRepository,
                        @Value("${app.media.upload-dir:uploads}") String uploadDir) {
        this.mediaRepository = mediaRepository;
        this.actionRepository = actionRepository;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(uploadRoot);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le répertoire de stockage des médias.", e);
        }
    }

    public Media uploadMedia(Long actionId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Le fichier ne peut pas être vide.");
        }

        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new RuntimeException("Action introuvable : " + actionId));

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String storedFilename = UUID.randomUUID() + "_" + originalFilename;
        Path target = uploadRoot.resolve(storedFilename);

        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Impossible d\'enregistrer le fichier média.", e);
        }

        Media media = new Media(originalFilename, "/media/" + storedFilename, file.getContentType(), file.getSize());
        media.setAction(action);
        action.getMedia().add(media);
        actionRepository.save(action);

        return media;
    }

    public List<Media> getByActionId(Long actionId) {
        return mediaRepository.findByActionId(actionId);
    }

    public void delete(Long id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media introuvable : " + id));

        if (media.getUrl() != null && media.getUrl().startsWith("/media/")) {
            String storedFilename = Path.of(media.getUrl()).getFileName().toString();
            try {
                Files.deleteIfExists(uploadRoot.resolve(storedFilename));
            } catch (IOException e) {
                throw new RuntimeException("Impossible de supprimer le fichier média.", e);
            }
        }

        mediaRepository.delete(media);
    }
}
