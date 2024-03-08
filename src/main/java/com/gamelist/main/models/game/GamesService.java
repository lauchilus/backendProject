package com.gamelist.main.models.game;

import com.gamelist.main.igbd.SearchGameDetailsDto;
import com.gamelist.main.igbd.SearchGameListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GamesService {

    private final GameRepository gameRepository;

    private final GameDetailsRepository gameDetailsRepository;

    public Game getGame(long game) {
        return gameRepository.getReferenceByIgdbGameId(game);
    }

    public SearchGameDetailsDto getGameDetails(long game) {
        GameDetails gameDetails = gameDetailsRepository.getReferenceById(game);
        return new SearchGameDetailsDto(gameDetails.getId(), gameDetails.getSummary(), gameDetails.getName(),gameDetails.getFirstReleaseDate(), gameDetails.getImageUrl(), gameDetails.getCompany());
    }

    public SearchGameListDto getGameList(long game) {
        GameDetails gameDetails = gameDetailsRepository.getReferenceById(game);
        return new SearchGameListDto(gameDetails.getId(),gameDetails.getName(), gameDetails.getImageUrl());
    }

    public boolean exists(long game) {
        return gameRepository.existsByIgdbGameId(game) && gameDetailsRepository.existsById(game);
    }

    public GameDetails saveGameDetails(GameDetails details) {
        GameDetails game = new GameDetails();
        if(!gameRepository.existsByIgdbGameId(details.getId())){
            gameRepository.save(Game.builder().igdbGameId(details.getId()).build());
            game = gameDetailsRepository.save(details);
        } else if (!gameDetailsRepository.existsById(details.getId()) && gameRepository.existsByIgdbGameId(details.getId())) {
            game = gameDetailsRepository.save(details);
        }

        return game;
    }
}
