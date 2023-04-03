/*
 *  Copyright © 2005-2019 Amichai Rothman
 *
 *  This file is part of JCharset - the Java Charset package.
 *
 *  JCharset is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  JCharset is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with JCharset.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  For additional info see http://www.freeutils.net/source/jcharset/
 */

package net.freeutils.charset;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * The <b>Utils</b> class contains utility methods used at runtime
 * by charsets, as well as development tools for creating new Charsets.
 *
 * Author Amichai Rothman
 * @since  2015-10-14
 */
public class Utils {

    // prevents instantiation
    private Utils() {}

    /**
     * Returns whether the running JDK version is at least 1.5.
     *
     * @return true if running in JDK 1.5 or higher, false otherwise
     */
    static boolean isJDK15() {
        try {
            float version = Float.parseFloat(System.getProperty("java.class.version"));
            return version >= 49.0; // 49.0 is the class version of JDK 1.5
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns a string containing the Java definitions of the
     * given inverse lookup (char-to-byte) table.
     * <p>
     * This is a convenient utility method for design-time building
     * of charsets based on a lookup table mapping, as an alternative
     * to creating these inverse lookup tables on-the-fly.
     *
     * @param tables the inverse lookup (char-to-byte) table
     * @return the Java definitions of the inverse lookup
     * (char-to-byte) table
     */
    public static String toInverseLookupTableDefinition(int[][] tables) {
        StringBuilder sb = new StringBuilder();
        int nulls = 0;
        sb.append("static final int[][] CHAR_TO_BYTE = {\n\t");
        for (int i = 0; i < tables.length; i++) {
            int[] table = tables[i];
            if (table == null) {
                if (nulls++ % 8 == 0 && nulls > 1)
                    sb.append("\n\t");
                sb.append("null, ");
            } else {
                if (nulls > 0)
                    sb.append("\n\t");
                nulls = 0;
                sb.append("{ // high byte = 0x");
                if (i < 0x10)
                    sb.append('0');
                sb.append(Integer.toHexString(i));
                sb.append("\n\t");
                for (int j = 0; j < table.length; j++) {
                    if (table[j] == -1) {
                        sb.append("  -1, ");
                    } else {
                        sb.append("0x");
                        if (table[j] < 0x10)
                            sb.append('0');
                        sb.append(Integer.toHexString(table[j])).append(", ");
                    }
                    if ((j + 1) % 8 == 0)
                        sb.append("\n\t");
                }
                sb.append("}, \n\t");
            }
        }
        sb.append("\n\t};");
        return sb.toString();
    }

    /**
     * Main entry point for command-line utility.
     *
     * @param args the command line arguments
     * @throws IOException if an error occurs
     */
    public static void main(String[] args) throws IOException {
        // parse arguments
        String from = "UTF-8";
        String to = "UTF-8";
        List<String> inputFiles = new ArrayList<String>();
        String outputFile = null;
        PrintStream so = System.out;
        try {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.startsWith("-") && arg.length() > 1 && inputFiles.isEmpty()) {
                    if (arg.equals("-o")) {
                        outputFile = args[++i]; // throws IOOBE
                    } else if (arg.equals("-f")) {
                        from = args[++i]; // throws IOOBE
                    } else if (arg.equals("-t")) {
                        to = args[++i]; // throws IOOBE
                    } else if (arg.equals("-l") || arg.equals("-ll")) {
                        String filter = arg.equals("-ll") ? Utils.class.getPackage().getName() : "";
                        for (Charset charset : Charset.availableCharsets().values())
                            if (charset.getClass().getName().startsWith(filter))
                                so.println(charset.name() + " " + charset.aliases());
                        System.exit(0);
                    } else if (arg.equals("-?") || arg.equals("-h")) {
                        so.println("Usage: java -jar jcharset.jar [options] [inputFiles...]\n");
                        so.println("Converts the charset encoding of one or more (concatenated) input files.");
                        so.println("If no files or '-' (dash) is specified, input is read from stdin.");
                        so.println("\nOptions:");
                        so.println("  -f <fromCharset>\tthe name of the input charset [default UTF-8]");
                        so.println("  -t <toCharset>\tthe name of the output charset [default UTF-8]");
                        so.println("  -o <outputFile>\tthe output file name [default stdout]");
                        so.println("  -l\t\t\tlist all available charset names and aliases");
                        so.println("  -ll\t\t\tlist all JCharset charset names and aliases");
                        so.println("  -h, -?\t\tshow this help information");
                        System.exit(1);
                    } else {
                        throw new IndexOutOfBoundsException();
                    }
                } else {
                    inputFiles.add(arg); // all remaining args are input files
                }
            }
        } catch (IndexOutOfBoundsException ioobe) {
            System.err.println("Error: invalid argument");
            System.err.println("Use the -h option for help");
            System.exit(2);
        }
        if (inputFiles.isEmpty())
            inputFiles.add("-");
        // perform conversion
        OutputStream os = outputFile == null ? so : new FileOutputStream(outputFile);
        OutputStreamWriter writer = null;
        try {
            char[] buf = new char[16384];
            writer = new OutputStreamWriter(os, to);
            for (String inputFile : inputFiles) {
                InputStream is = inputFile.equals("-") ? System.in : new FileInputStream(inputFile);
                try {
                    int count;
                    Reader reader = new InputStreamReader(is, from);
                    while ((count = reader.read(buf)) > -1)
                        writer.write(buf, 0, count);
                } finally {
                    is.close();
                }
            }
        } finally {
            if (writer != null)
                writer.close(); // also flushes encoder
            os.close();
        }
    }
}
