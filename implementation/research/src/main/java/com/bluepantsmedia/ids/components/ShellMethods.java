package com.bluepantsmedia.ids.components;

/**
 * Project Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 4/7/2020 5:42 PM
 * Copyright 2020 by Bluepants Media, LLC
 */


import com.bluepantsmedia.ids.Version;
import com.bluepantsmedia.ids.enums.CharsetNames;
import lombok.extern.slf4j.Slf4j;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@ShellComponent
@PropertySource("classpath:application.properties")
public class ShellMethods {

    @Value( "${files.path}" )
    private String filesPath;

    @Value( "${files.consumed.log}" )
    private String filesConsumedLog;

    @Value( "${files.dictionary.path}" )
    private String filesDictionaryPath;

    @Value( "${files.enums.path}" )
    private String filesEnumsPath;

    private static final Set<String> filesToRead = new HashSet<String>();
    private static final Set<String> consumedFiles = new HashSet<String>();
    private static final Set<String> sourceCharFiles = new HashSet<String>();
    private static final Map<String, Long> uniqueStringsCounts = new HashMap<String,Long>();

    private static File consumedFilesLog = null;

    @ShellMethod("Test code snippets")
    public String snippet() {
        try {
            String str1 = "\u0000";
            String str2 = "\uFFFF";
            byte[] arr = str1.getBytes("UTF-8");
            byte[] brr = str2.getBytes("UTF-8");
            log.info("UTF-8 for \\u0000");
            for(byte a: arr) {
                log.info(String.valueOf(a));
            }
            log.info("\nUTF-8 for \\uffff" );
            for(byte b: brr) {
                log.info(String.valueOf(b));
            }
            String str = "\u003c";
            log.info("\u003c is " + str);
            byte[] utf8 = str.getBytes("UTF-8");
            for(byte c: utf8) {
                log.info(String.valueOf(c));
            }
            str = new String(utf8, "UTF-8");
            log.info(str);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "done.";
    }

    @ShellMethod("Get Charset IDS-64 version")
    public String version() {
        return "Jar Version: " + Version.VERSION + ", IDS-64 Version: " + CharsetNames.IDS_64.version;
    }

    @ShellMethod("List dictionaries")
    public String all() {
        return "all";
    }

    @ShellMethod("List processed dictionaries")
    public String done() {
        BufferedReader reader = null;
        consumedFiles.clear();
        initConsumedFilesLog();

        try {
            reader = new BufferedReader(new FileReader(consumedFilesLog));
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
        initConsumedFilesLog();

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(consumedFilesLog));
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
        getAllFilenames(new File(filesDictionaryPath));
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

    @ShellMethod("Detect Charset")
    public String detect() {
        byte[] buf = new byte[4096];
        List<String> fileNames = new ArrayList<>();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("fileName");

            // (1)
            UniversalDetector detector = new UniversalDetector(null);

            // (2)
            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }

            // (3)
            detector.dataEnd();

            // (4)
            String encoding = detector.getDetectedCharset();
            if (encoding != null) {
                log.info("Detected encoding = " + encoding);
            } else {
                log.info("No encoding detected.");
            }

            // (5)
            detector.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Success";
    }

    @ShellMethod("Test Filesystem capability with large enum class files")
    public String test(@ShellOption(defaultValue= "2745") String maxString) {
        int max = new Integer(maxString).intValue(); // 2746 includes class declaration?
        log.info("testing enum with " + max + " elements...");
        try {
            // zap and create enum file
            Files.write(Paths.get(filesEnumsPath + "EnumSizeTest.java"), "".getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Writer output = null;
        try {
            output = new BufferedWriter(new FileWriter(filesEnumsPath + "EnumSizeTest.java", true));  //clears file every time
            output.write("package com.bluepantsmedia.ids.enums;\n");
            output.write("public enum EnumSizeTest {\n\t");
            for ( int i=0; i<max; i++) {
                output.write("VAR"+i+",");
            }
            output.write("VAR"+max+"\n}");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Success";
    }

    private void initConsumedFilesLog() {
        consumedFilesLog = new File(filesConsumedLog);

        if(!Files.exists(Paths.get(filesConsumedLog))) {
            try {
                Files.createFile(Paths.get(filesConsumedLog));
            } catch (IOException e) {

            }
        }
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

    private void logFinishedFile(String fileName) {
        try {
            Files.write(Paths.get(filesConsumedLog), fileName.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

}