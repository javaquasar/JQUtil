package com.javaquasar.util.net;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 *
 * @author Java Quasar
 */
public class Downloader {

    public static void download(String url, String pathToFile) throws MalformedURLException, IOException {
        URL website = new URL(url);
        File file = new File(pathToFile);

        if (file.exists()) {
            throw new IOException("File \"" + pathToFile + "\" exists");
        }
        try (InputStream is = website.openStream();
                ReadableByteChannel rbc = Channels.newChannel(is);
                FileOutputStream fos = new FileOutputStream(file);) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }


        /*
        try (
                InputStream is = new URL(String.format(url, s)).openStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                FileOutputStream fout = new FileOutputStream(file)) {

            final byte data[] = new byte[1024];
            int count;
            while ((count = bis.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } catch (Exception ex) {
            
        }*/
    }

    public static void downloadUsingStream(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = bis.read(buffer, 0, 1024)) != -1) {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }

    public static void downloadUsingNIO(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
    }

}
