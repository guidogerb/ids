package com.bluepantsmedia.dev.ids;

/**
 * Project Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 4/7/2020 5:42 PM
 * Copyright 2020 by Bluepants Media, LLC
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A management information base (MIB) is a database used for managing the entities in a
 * communication network.
 *
 * Objects in the MIB are defined using a subset of Abstract Syntax Notation One (ASN.1)
 * called "Structure of Management Information Version 2 (SMIv2)" RFC 2578.
 * This software performs the parsing, and is an MIB compiler.
 *
 * Abstract Syntax Notation One (ASN.1) is a standard and flexible notation that describes
 * data structures for representing, encoding, transmitting, and decoding data.
 * It provides a set of formal rules for describing the structure of objects that are
 * independent of machine-specific encoding techniques and is a precise, formal notation
 * that removes ambiguities.
 *
 * The MIB hierarchy can be depicted as a tree with a nameless root, the levels of which
 * are assigned by different organizations. The top-level MIB OIDs belong to different
 * standards organizations, while lower-level object IDs are allocated by associated
 * organizations. This model permits management across all layers of the OSI reference model,
 * extending into applications such as databases, email, and the Java reference model,
 * as MIBs can be defined for all such area-specific information and operations.
 *
 * There are a large number of MIBs defined by standards organizations like the IETF,
 * private enterprises and other entities. This project is an attempt to standardize the
 * streaming of character sets in a compressed blocks, Object IDs, representing object
 * identifiers allocated according to the rules specified in ASN.1 with bounded
 * precision in the Structure of Management Information (SMI).
 *
 * @see <a href="http://www.icir.org/fenner/mibs/mib-index.html">MIB Index</a>
 * @see <a href="https://tools.ietf.org/html/rfc1658">Definitions of Managed Objects for Character Stream Devices using SMIv2</a>
 * @see <a href="https://www.iana.org/assignments/character-sets/character-sets.xml">Character Sets</a>
 *
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

	private static Logger LOG = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		LOG.info("STARTING IDS");
		SpringApplication.run(Application.class, args);
		LOG.info("APPLICATION FINISHED");
	}

	public Application() {
	}

	@Override
	public void run(String... args) throws Exception {
	}


}
