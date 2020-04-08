package com.bluepantsmedia.ids.menu;

/**
 * @Project Identity Streams
 * @Author Gary Gerber
 * @Email garygerber@bluepantsmedia.com
 * @Date 4/7/2020 5:42 PM
 * @Copyright 2020 by Bluepants Media, LLC
 */

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ShellComponent
public class ProcessDictionaries {
    private static final String FILES_PATH = "src/main/resources/files/";
    private static final String CONSUMED_FILES_LOG =  FILES_PATH + "consumed-files.log";
    private static final String DICTIONARY_PATH = FILES_PATH + "dictionaries";
    private static final String ENUMS_PATH = "src/main/java/com/bluepantsmedia/ids/enums/";
    private static final String DEFAULT_PACKAGE = "";

    private static final Set<String> filesToRead = new HashSet<String>();
    private static final Set<String> consumedFiles = new HashSet<String>();
    private static final Set<String> sourceCharFiles = new HashSet<String>();

    private static final Map<String, Long> uniqueStringsCounts = new HashMap<String,Long>();
    private static final File consumedFilesLog = new File(CONSUMED_FILES_LOG);

    @ShellMethod("List dictionaries")
    public String all() {
        return "all";
    }

    @ShellMethod("List processed dictionaries")
    public String done() {
        BufferedReader reader = null;
        consumedFiles.clear();

        try {
            reader = new BufferedReader(new FileReader(CONSUMED_FILES_LOG));
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
        return String.format("Consumed Dictionaries:\n%s", consumedFiles.stream()
                .map( Object::toString )
                .collect( Collectors.joining( "\n" )));
    }

    @ShellMethod("Process dictionaries")
    public String execute() {
        if(!Files.exists(Paths.get(ENUMS_PATH + "EnumSizeTest.java"))) {
            try {
                Files.createFile(Paths.get(ENUMS_PATH + "EnumSizeTest.java"));
            } catch (IOException e) {

            }
        }
        System.out.println("public enum EnumSizeTest {");
        int max = 2746;
        for ( int i=0; i<max; i++) {
            System.out.println("VAR"+i+",");
        }
        System.out.println("VAR"+max+"}");

        if(!Files.exists(Paths.get(CONSUMED_FILES_LOG))) {
            try {
                Files.createFile(Paths.get(CONSUMED_FILES_LOG));
            } catch (IOException e) {

            }
        }
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(CONSUMED_FILES_LOG));
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
		return "Success";
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
            Files.write(Paths.get(CONSUMED_FILES_LOG), fileName.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

}