package com.gamelist.main.models.played;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamelist.main.models.user.User;

@Repository
public interface PlayedRepository extends JpaRepository<Played, Long> {

	List<Played> findAllByUser(User user);

}
