package com.gamelist.main.models.favorites;

import org.springframework.stereotype.Service;

import com.api.igdb.utils.ImageBuilderKt;
import com.api.igdb.utils.ImageSize;
import com.api.igdb.utils.ImageType;

@Service
public class IgdbHelpers {

	
	public String imageBuilder(String ss) {
		return ImageBuilderKt.imageBuilder(ss, ImageSize.COVER_BIG, ImageType.PNG);
	}
}
