package com.gamelist.main.models.images;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Images {

	@Id
	@UUID
	private String id;
	
	private String name;
	
	private String imageUrl;
	
	private String imageId;
	
	public Images(String name, String url, String imageId) {
		this.name = name;
		this.imageUrl = url;
		this.imageId = imageId;
	}
}
