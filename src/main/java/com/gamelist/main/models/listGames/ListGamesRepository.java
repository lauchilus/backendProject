package com.gamelist.main.models.listGames;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListGamesRepository extends JpaRepository<ListGames, Long> {

}
