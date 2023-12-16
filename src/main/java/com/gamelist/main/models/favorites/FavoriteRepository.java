package com.gamelist.main.models.favorites;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	List<Favorite> findAllByUserId(long id);

}
