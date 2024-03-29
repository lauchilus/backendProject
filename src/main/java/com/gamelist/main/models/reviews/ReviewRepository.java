package com.gamelist.main.models.reviews;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.user.User;


@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

	List<Review> findAllByUserId(String user, Pageable page);
	
	List<Review> findByUserId(String user);

	List<Review> findTop3ByUserIdOrderByReviewDateDesc(String user);
	
	boolean existsByGameAndUser(Game game, User user);


    Integer countByUserId(String userId);
}
