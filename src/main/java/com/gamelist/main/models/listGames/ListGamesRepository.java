package com.gamelist.main.models.listGames;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListGamesRepository extends JpaRepository<ListGames, String> {

    List<ListGames> findAllByCollectionId(String collectionid);
}
