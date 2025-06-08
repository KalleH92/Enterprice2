package com.kh.ep_projekt.controller;

import com.kh.ep_projekt.model.AnimeModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
@RequestMapping("/anime")
public class AnimePageController {

    private final WebClient webClient;

    public AnimePageController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.jikan.moe/v4").build();
    }

    @GetMapping("/search")
    public String showAnime(@RequestParam(required = false) String id, Model model) {
        if (id != null && !id.isEmpty()) {
            try {
                AnimeModel anime = webClient.get()
                        .uri("/anime/{id}", id)
                        .retrieve()
                        .bodyToMono(AnimeModel.class)
                        .block();
                model.addAttribute("anime", anime);
            } catch (Exception e) {
                model.addAttribute("anime", null);
            }
        }
        return "anime";
    }
}