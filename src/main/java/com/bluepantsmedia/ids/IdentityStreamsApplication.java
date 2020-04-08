package com.bluepantsmedia.ids;

/**
 * @Project Identity Streams
 * @Author Gary Gerber
 * @Email garygerber@bluepantsmedia.com
 * @Date 4/7/2020 5:42 PM
 * @Copyright 2020 by Bluepants Media, LLC
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IdentityStreamsApplication implements CommandLineRunner {

	private static Logger LOG = LoggerFactory
			.getLogger(IdentityStreamsApplication.class);


	public static void main(String[] args) {
		LOG.info("STARTING IDS");
		SpringApplication.run(IdentityStreamsApplication.class, args);
		LOG.info("APPLICATION FINISHED");
	}

	public IdentityStreamsApplication() {
	}

	@Override
	public void run(String... args) throws Exception {
	}


}
