package org.bluepantsmedia.ids.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Project IDS - Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 3/21/2022 5:10 PM
 * Copyright 2020 by Bluepants Media, LLC
 */
public class WiktionaryFileReader {
    private static final String fullWikitionary = System.getProperty("user.dir") + "\\largeFiles\\dictionaries\\wiktionary\\raw-wiktextract-data.json";
    private static Long recCount = 0l;
    private BufferedInputStream fileIn;
    private long fileLength;
    private int arraySize;
    private byte[] array;
    private StringBuilder fullLine = new StringBuilder();
    private ObjectMapper mapper = new ObjectMapper();

    public WiktionaryFileReader(String fileName, int arraySize) throws IOException {
        this.fileIn = new BufferedInputStream(new FileInputStream(fileName), arraySize);
        this.fileLength = fileIn.available();
        this.arraySize = arraySize;
    }

    private static boolean startsWithSenses(String value) {
        String val = "{\"senses\":";
        return value.indexOf(val) == 0;
    }

    public static void main(String[] args) throws IOException {
        WiktionaryFileReader reader = new WiktionaryFileReader(fullWikitionary, 65536);
        long start = System.nanoTime();
        while (reader.read() != -1) ;
        long end = System.nanoTime();
        reader.close();
        System.out.println("StreamFileReader: " + (end - start));
        System.out.println("Record Count: " + recCount);
        /*
          Record Count: 8198906
         */
    }

    public int read() throws IOException {
        byte[] tmpArray = new byte[arraySize];
        int bytes = fileIn.read(tmpArray);// Temporarily stored in byte array
        if (bytes != -1) {
            array = new byte[bytes];// Byte array length is read length
            System.arraycopy(tmpArray, 0, array, 0, bytes);// Copy read data
            fullLine.append(new String(array));
            while (bytes != -1) {
                bytes = fileIn.read(tmpArray);// Temporarily stored in byte array
                if (bytes != -1) {
                    array = new byte[bytes];// Byte array length is read length
                    System.arraycopy(tmpArray, 0, array, 0, bytes);// Copy read data
                    if (startsWithSenses(new String(array))) {
                        printFullLine();
                        fullLine.append(new String(array));
                    } else {
                        fullLine.append(new String(array));
                    }
                } else {
                    printFullLine();
                }
            }
            return -1;
        }
        return -1;
    }

    private void printFullLine() {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        List<String> nodes = List.of(fullLine.toString().split("\n"));
        nodes.forEach(l -> {
            JsonNode root = null;
            Record record = null;
            String recStr = null;
            try {
                root = mapper.readTree(l);
                record = mapper.treeToValue(root, Record.class);
                //new JSONObject(ow.writeValueAsString(msg))
                recStr = ow.writeValueAsString(record);
                recCount++;
            } catch (JsonProcessingException e) {
                System.out.println("Record Count: " + recCount);
                e.printStackTrace();
            }
            System.out.println("#########################################");
            System.out.println("Record Count: " + recCount);
            System.out.println("root = " + root.toString());
            if (recStr != null) {
                System.out.println("record = " + recStr);
            }

        });
        fullLine = new StringBuilder();
    }

    private void printSenses() {
        System.out.println("############################");
        System.out.println(new String(array));
    }

    public void close() throws IOException {
        fileIn.close();
        array = null;
    }

    public byte[] getArray() {
        return array;
    }

    public long getFileLength() {
        return fileLength;
    }
}

class Record extends Source {
    public String source; // points to parent (Source) record?
}

class Source extends Word {
    public List<String> wikipedia;
    public List<String> categories;
    public List<String> hyphenation;
    public List<Form> forms;
    public List<Sound> sounds;
    public List<Sense> senses;
    public List<Nym> synonyms;
    public List<Nym> hyponyms;
    public List<Nym> meronyms;
    public List<Nym> antonyms;
    public List<Nym> troponyms;
    public List<Nym> holonyms;
    public List<Nym> hypernyms;
    public List<Nym> derived;
    public List<Nym> abbreviations;
    public List<Nym> proverbs;
    public List<Nym> instances;
    public List<Nym> alt_of;
    public List<Nym> form_of;
    public List<Word> translations;
    public List<Word> coordinate_terms;
    public List<Word> related;
    public List<Template> inflection_templates;
    public List<Template> head_templates;
    public List<Template> etymology_templates;
}

class Word extends MetaData {
    public String english;
    public String word;
    public String alt;
    public String pos;
    public String lang;
    public String lang_code;
    public String code;
    public String title;
    public String etymology_text;
    public String redirect;
    public String sense;
    public String roman;
    public String note;
    public String ruby;
    public String source;
    public String taxonomic;
    public String xlit;
}

class MetaData {
    public List<String> tags;
    public List<String> topics;
}

class Nym extends Word {
    public List<String> ref;
    public String text;
    public String extra;
}

class Sound extends MetaData {
    public String ipa;
    public String audio;
    public String text;
    public String rhymes;
    public String ogg_url;
    public String mp3_url;
    public String other;
    public String note;
    public String homophone;
    public String enpr;
    @JsonProperty("audio-ipa")
    public String audioIpa;
    public String form;
}

class Form extends MetaData {
    public String form;
    public String source;
    public String ipa;
    public String roman;
}

class Sense extends MetaData {
    public String english;
    public String taxonomic;
    public String extra;
    public List<String> raw_glosses;
    public List<String> categories;
    public List<String> wikipedia;
    public List<String> wikidata;
    public List<String> senseid;
    public List<String> glosses;
    public List<Nym> hyponyms;
    public List<Nym> hypernyms;
    public List<Nym> synonyms;
    public List<Nym> antonyms;
    public List<Nym> meronyms;
    public List<Nym> instances;
    public List<Nym> related;
    public List<Nym> alt_of;
    public List<Nym> form_of;
    public List<Nym> compound_of;
    public List<Map<String, String>> examples;
}

class Template {
    public String name;
    public Map<String, String> args;
    public String expansion;
}