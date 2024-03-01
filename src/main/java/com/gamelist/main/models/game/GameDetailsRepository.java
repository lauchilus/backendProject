package com.gamelist.main.models.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameDetailsRepository extends JpaRepository<GameDetails, Long> {
}
