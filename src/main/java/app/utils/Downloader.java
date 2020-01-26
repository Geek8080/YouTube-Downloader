package app.utils;

import app.models.VideoDownloadLink;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class Downloader {
    public static Downloader downloader;
    private static String URL;

    private Downloader(String URL){
        this.URL = URL;
    }

    public static Downloader getInstance(String URL){
        downloader = new Downloader(URL);
        return downloader;
    }

    public static void download(String urlStr, String file){
        try {
            URL url = new URL(urlStr);
            URLConnection connection = url.openConnection();
            connection.connect();
            String fileType = connection.getContentType();
            if(fileType.length()>4){
                fileType = fileType.substring(0,4);
            }
            int fileSize = connection.getContentLength();
            System.out.println(fileType + ": " + fileSize);
            BufferedInputStream is = new BufferedInputStream(url.openStream());
            FileOutputStream os = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = is.read(buffer, 0, 1024)) != -1) {
                os.write(buffer, 0, count);
            }
            os.close();
            is.close();
            System.out.println("Video Successfully Downloaded...");
        }catch(Exception ex){
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Error in downloading video: " + ex.getMessage(),"Download Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getSize(){
        String size = "Couldn't retrieve the size of file";
        try {
            java.net.URL url = new URL(URL);
            URLConnection connection = url.openConnection();
            connection.connect();
            long fileSize = connection.getContentLength();
            float len = ((float) fileSize)/(float)(1024*1024);
            size = String.format("%.2f", len) + " MB";
            System.out.println(size);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return size;
    }

}
