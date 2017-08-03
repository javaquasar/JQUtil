package com.javaquasar.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Java Quasar
 */
public class FileManager {

    public static List<String> readListStringFromFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            return Files.readAllLines(Paths.get(path));
        }
        return null;
    }

    public static String readFile(String path) throws FileNotFoundException, IOException {
        File file = new File(path);
        if (file.exists()) {
            StringBuilder sb = new StringBuilder();
            String currentLine;
            try (FileReader fr = new FileReader(path);
                    BufferedReader br = new BufferedReader(fr)) {
                while ((currentLine = br.readLine()) != null) {
                    sb.append(currentLine);
                    sb.append("\n");
                }
            }
            return sb.toString();
        }
        return null;
    }

    public static void writeFile(String path, String text) throws FileNotFoundException, IOException {
        File file = createFile(path);
        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.println(text);
        }
    }

    public static File createFile(String pathToFile) throws IOException {
        File file = new File(pathToFile);
        if (!file.exists()) {
            File folder = new File(file.getParent());
            if (!folder.exists()) {
                folder.mkdirs();
            }
            file.createNewFile();
        }
        return file;
    }
    
    public static void deleteFile(String pathToFile) {
        File file = new File(pathToFile);
        if (file.exists()) {
            file.delete();
        }
    }
    
    public static Properties getProperties(String path) {
        Properties prop = new Properties();
	try(InputStream input = new FileInputStream(path)) {
            prop.load(input);
	} catch (FileNotFoundException ex) {
            // TODO
        } catch (IOException ex) {
            // TODO
        }
        return prop;
    }
}
