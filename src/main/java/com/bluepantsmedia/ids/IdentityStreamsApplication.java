package com.bluepantsmedia.ids;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@SpringBootApplication
public class IdentityStreamsApplication implements CommandLineRunner {

	private static final Set<String> filesToRead = new HashSet<String>();
	private static final Set<String> consumedFiles = new HashSet<String>();
	private static final Set<String> sourceCharFiles = new HashSet<String>();

	private static final Map<String, Long> uniqueStringsCounts = new HashMap<String,Long>();

	private static final String FILES_PATH = "files/";
	private static final String LOG =  FILES_PATH + "consumedFiles.log";
	private static final String DICTIONARY_PATH = FILES_PATH + "dictionaries";
	private static final String ENUMS_PATH = "src/main/java/com/bluepantsmedia/ids/enums/";
	private static final String DEFAULT_PACKAGE = "";
	private static final File consumedFilesLog = new File(LOG);

	public static void main(String[] args) {
		SpringApplication.run(IdentityStreamsApplication.class, args);
	}

	@Override
	public void run(String... args) {
		log.info("EXECUTING : command line runner");

		for (int i = 0; i < args.length; ++i) {
			log.info("args[{}]: {}", i, args[i]);
		}
	}

	public IdentityStreamsApplication() {
	}

	private void execute() {
		if(!Files.exists(Paths.get(ENUMS_PATH + "EnumSizeTest.java"))) {
			try {
				Files.createFile(Paths.get(LOG));
			} catch (IOException e) {

			}
		}
		System.out.println("public enum EnumSizeTest {");
		int max = 2746;
		for ( int i=0; i<max; i++) {
			System.out.println("VAR"+i+",");
		}
		System.out.println("VAR"+max+"}");

		if(!Files.exists(Paths.get(LOG))) {
			try {
				Files.createFile(Paths.get(LOG));
			} catch (IOException e) {

			}
		}
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(LOG));
			String file = null;

			while ((file = reader.readLine()) != null) {
				consumedFiles.add(file);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
		}
		getAllFilenames(new File(DICTIONARY_PATH));
		filesToRead.removeAll(consumedFiles);



		/*
		try(FileWriter fw = new FileWriter("myfile.txt", true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw))
		{
		out.println("the text");
		//more code
		out.println("more text");
		//more code
		} catch (IOException e) {
			//exception handling left as an exercise for the reader
		}
		 */
		System.out.println("done");
		System.exit(1);
	}

	public static void getAllFilenames(File node){
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				getAllFilenames(new File(node, filename));
			}
		}
		else {
			filesToRead.add(node.getAbsoluteFile().toString());
		}

	}

	private static void parseIdentity(String fileName) {
		// TODO convert files to individual characters and words and place in uniqueStringsCounts
		final StringBuilder sb = new StringBuilder();
		List<String> words = null;
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			if(sb.length() > 0) {
				sb.delete(0, sb.length() - 1);
			}
			words = stream //.filter(line -> !line.startsWith("line3"))
					.map(String::toLowerCase)
					.collect(Collectors.toList());
			words.replaceAll(String::trim);
			System.out.println(words);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void logFinishedFile(String fileName) {
		try {
			Files.write(Paths.get(LOG), fileName.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
			//exception handling left as an exercise for the reader
		}
	}

}
