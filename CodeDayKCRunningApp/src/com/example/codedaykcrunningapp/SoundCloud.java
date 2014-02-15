package com.example.codedaykcrunningapp;

import com.soundcloud.api.ApiWrapper;

public class SoundCloud {
	private final ApiWrapper wrapper;

	public SoundCloud() {
		wrapper = new ApiWrapper("6ca61d288b2591e70b7212ec723208c3",
				"5de8005efa5c010b496e0b3d37347867", null, null);
	}
}
