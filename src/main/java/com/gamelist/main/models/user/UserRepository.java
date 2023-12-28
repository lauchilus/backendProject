package com.gamelist.main.models.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

	User getReferenceByUsername(String username);

	boolean existsByUsername(String username);

}
