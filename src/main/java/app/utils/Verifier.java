package app.utils;

import java.net.URL;
import java.net.URLConnection;

public class Verifier {
    public static boolean verifyURL(String urlString) {
        try {
            new URL(urlString).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }
}
