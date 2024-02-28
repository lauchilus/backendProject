package com.gamelist.main.models.list;

import java.util.List;

import com.gamelist.main.models.listGames.ListGames;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamelist.main.models.user.User;

@Repository
public interface ListRepository extends JpaRepository<Collection, String> {

	List<Collection> findAllByUser(User user, Pageable page);


}
