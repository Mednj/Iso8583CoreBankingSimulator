package com.hps.hps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.jpos.iso.*;

@SpringBootApplication
public class HpsApplication {

	public static void main(String[] args) throws ISOException {
		SpringApplication.run(HpsApplication.class, args);

	}

}
