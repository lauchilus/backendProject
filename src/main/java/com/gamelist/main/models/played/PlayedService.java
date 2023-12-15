package com.gamelist.main.models.played;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;

@Service
public class PlayedService {

	@Autowired
	private PlayedRepository playedRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	
	public Played addPlayed(long id, long game) {
		User user = userRepo.getReferenceById(id);
		
		Played played = playedRepo.save(new Played(user,game));
		return played;
	}


	public List<Played> getAllUserPlayed(long userid) {
		User user = userRepo.getReferenceById(userid);
		List<Played> played = playedRepo.findAllByUser(user);
		return played;
	}
}
