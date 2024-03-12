package com.gamelist.main.models.game;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin("https://gamelist-app.netlify.app/")
@RequestMapping("/api/v1/games")
@Tag(name = "User")
@RequiredArgsConstructor
public class GameController {
}
