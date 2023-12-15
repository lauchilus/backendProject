package com.gamelist.main.models.list;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamelist.main.models.user.User;

@Repository
public interface ListRepository extends JpaRepository<Collection, Long> {

	List<Collection> findAllByUser(User user);

}
