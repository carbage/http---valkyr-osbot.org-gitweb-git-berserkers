package org.valkyr.api.framework.webwalker.web;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Downloader {

    public static boolean downloadWeb() {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            URL url = new URL("https://kbve.com/valkyr/web.txt");
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();

            long fileLastModifiedTime = TextIO.getFile().lastModified();
            long urlLastModifiedTime = connection.getLastModified();

            Date fileLastModified = new Date(fileLastModifiedTime);
            Date urlLastModified = new Date(urlLastModifiedTime);

            int status = connection.getResponseCode();
            if (status < 400) {
                boolean newFile = false;
                if (!TextIO.fileExists()) {
                    TextIO.getFile().getParentFile().mkdirs();
                    TextIO.getFile().createNewFile();
                    newFile = true;
                }
                System.out.println("WebWalker: Checking for web updates...");
                if (newFile || fileLastModified.before(urlLastModified) || TextIO.getFile().getTotalSpace() != connection.getContentLengthLong()) {
                    System.out.println("WebWalker: Found update, downloading...");
                    in = new BufferedInputStream(url.openStream());
                    fout = new FileOutputStream(TextIO.WEB);
                    final byte data[] = new byte[1024];
                    int count;
                    while ((count = in.read(data, 0, 1024)) != -1) {
                        fout.write(data, 0, count);
                    }
                    System.out.println("WebWalker: Updated!");
                } else {
                    System.out.println("WebWalker: Web is up to date!");
                }
                if (in != null)
                    in.close();
                if (fout != null)
                    fout.close();
            } else System.out.println("WebWalker: Error " + status);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isWebUpdated(HttpURLConnection connection) throws IOException {
        return connection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED;
    }

}
