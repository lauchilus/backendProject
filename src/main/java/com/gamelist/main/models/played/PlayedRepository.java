package com.gamelist.main.models.played;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamelist.main.models.user.User;

@Repository
public interface PlayedRepository extends JpaRepository<Played, String> {

	
	List<Played> findAllByUserIdOrderByIdDesc(String user,Pageable page);
	
	boolean existsByGameIdAndUser(long game_id, User user);

    Integer countByUserId(String userId);
}
