package com.gamelist.main.models.favorites;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.user.User;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	List<Favorite> findAllByUserId(long id);
	
	List<Favorite> findTop4ByUserId(long id);

	boolean existsByGameAndUser(Game game, User user);

}
