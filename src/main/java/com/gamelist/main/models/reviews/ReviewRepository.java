package com.gamelist.main.models.reviews;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.user.User;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

	List<Review> findAllByUserId(String user); 
	
	List<Review> findTop3ByUserIdOrderByIdDesc(String user);
	
	boolean existsByGameAndUser(Game game, User user);


	
	
}
