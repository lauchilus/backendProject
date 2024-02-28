package com.gamelist.main.models.listGames;

import com.gamelist.main.exceptions.PersonalizedExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListGamesService {

    private final ListGamesRepository listGamesRepository;

    public List<ListGames> getAllGamesFromCollection(String collectionid){
        List<ListGames> games = listGamesRepository.findAllByCollectionId(collectionid);
        if(games.isEmpty()) {
            return new ArrayList<>();
        }


        return games;
    }
}
