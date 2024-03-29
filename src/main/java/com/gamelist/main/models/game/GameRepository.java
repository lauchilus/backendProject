package com.gamelist.main.models.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, String> {
	
	Game getReferenceByIgdbGameId(long gameId);


	boolean existsByIgdbGameId(long game);
}
