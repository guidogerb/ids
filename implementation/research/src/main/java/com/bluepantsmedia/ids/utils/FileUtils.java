package com.bluepantsmedia.ids.utils;

import lombok.NonNull;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static final List<String> readFile(@NonNull final File file) {
        List<String> result = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                br.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static void writeToFile(@NonNull final File file, @NonNull final String data) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(data.getBytes());

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                out.flush();
                out.close();
            } catch (Exception e) {
            }
        }
    }

    public static void appendLineToFile(@NonNull final File file, @NonNull final String line) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, true));
            writer.append("\n" + line);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void tryLockAndWriteToFile(@NonNull final File file, @NonNull final String value) {
        RandomAccessFile stream = null;
        FileChannel channel = null;
        FileLock lock = null;
        try {
            channel = stream.getChannel();
            stream = new RandomAccessFile(file, "rw");
            lock = channel.tryLock();
            stream.writeChars(value);
            lock.release();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (final OverlappingFileLockException e) {
            try {
                stream.close();
                channel.close();
            } catch (IOException e1) {
            }
        } catch (IOException e) {
        } finally {
            try {
                stream.close();
                channel.close();
            } catch (IOException e) {
            }
        }
    }
}
