package com.kh.ep_projekt.controller;


import com.kh.ep_projekt.model.AnimeModel;
import com.kh.ep_projekt.model.Data;
import com.kh.ep_projekt.repository.AnimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@RestController
@RequestMapping("/anime")
public class AnimeController {

    private final WebClient animeWebClientConfig;
    @Autowired
    private final AnimeRepository animeRepository;
    public AnimeController(WebClient.Builder webClient, AnimeRepository animeRepository) {
        this.animeWebClientConfig = webClient
                .baseUrl("https://api.jikan.moe/v4/")
                .build();
        this.animeRepository = animeRepository;
    }


    @GetMapping("/{id}")
        public Object fetchAnimeApi(
                @PathVariable("id") String id) {
        try {
            Optional<AnimeModel> animeModel = Optional.ofNullable(animeWebClientConfig.get()
                    .uri(anime -> anime
                            .path("anime/" + id)
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(AnimeModel.class).block());
            return ResponseEntity.status(200).body(animeModel.get());

        } catch (Exception e) {
            return ResponseEntity.status(404).body("Nothing exist");
        }
    }

    @PostMapping("/{id}")

    public ResponseEntity<Object> insertAnimeToAnime (
            @PathVariable String id) {
        try {
            Optional<AnimeModel> animeModel = Optional.ofNullable(animeWebClientConfig.get()
                    .uri(anime -> anime
                            .path("anime/" + id)
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(AnimeModel.class).block());
                animeRepository.save(animeModel.get());
                return ResponseEntity.status(201).body(animeModel.get());

        } catch (Exception e) {
            return ResponseEntity.status(404).body("Nothing found");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> animeTitleUpdate (
            @PathVariable Long id, @RequestBody String animeTitel) {
        if (animeRepository.findById(id).isPresent()) {
            Optional<AnimeModel> animeDB = animeRepository.findById(id);
            AnimeModel anime = animeDB.get();
            anime.setData(new Data(animeTitel));
            animeRepository.save(anime);
            return ResponseEntity.status(200).body(anime);
        } else {
            return ResponseEntity.status(404).body("Nothing found to update");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> animeTitleDelete (
            @PathVariable Long id) {
        if (animeRepository.findById(id).isPresent()) {
            animeRepository.deleteById(id);
            return ResponseEntity.status(200).body("removed");
        } else {
            return ResponseEntity.status(404).body("Nothing removed");
        }
    }


}
