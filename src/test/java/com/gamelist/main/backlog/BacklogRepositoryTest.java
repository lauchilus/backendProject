package com.gamelist.main.backlog;



import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;

import jakarta.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BacklogRepositoryTest {
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void backlogRepository_Save_ReturnSavedBacklog() {
		User user = User.builder().username("testUser").build();
		Game game  = Game.builder().igdbGameId(733).build();
		Backlog backlog = Backlog.builder().user(user).game(game).build();
		
		Backlog savedBacklog = backlogRepository.save(backlog);
		
		
		Assertions.assertThat(savedBacklog).isNotNull();
		Assertions.assertThat(savedBacklog.getId()).isGreaterThan(0);
		
	}
	
	@Transactional
	@Test
	public void backlogRepository_GetAllFromUser_ReturnAllBacklogs() {
		User user = User.builder().username("testUser").build();
		user = userRepository.save(user);
		System.out.println(user);
		Game game  = Game.builder().igdbGameId(733).build();
		Game game2  = Game.builder().igdbGameId(73).build();
		Backlog backlog = Backlog.builder().user(user).game(game).build();
		Backlog backlog2 = Backlog.builder().user(user).game(game2).build();
		Backlog saved1 = backlogRepository.save(backlog);
		Backlog saved2 = backlogRepository.save(backlog2);
		List<Backlog> list = new ArrayList<>();
		list.add(saved1);
		list.add(saved2);
		
		List<Backlog> response = backlogRepository.getReferenceByUserId(2);
		
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.size()).isEqualTo(2);
		Assertions.assertThat(response).isEqualTo(list);		
	}
	
	//TODO DELETE TESTS

}
