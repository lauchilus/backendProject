package com.gamelist.main.backlog;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.user.User;

@Repository
public interface BacklogRepository extends JpaRepository<Backlog, String> {

	List<Backlog> getReferenceByUserId(String userId, Pageable page);

	Backlog getReferenceByUserAndGame(User user, Game game);

}
