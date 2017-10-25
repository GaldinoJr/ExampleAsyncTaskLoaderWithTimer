package com.example.galdino.asinctaskloader;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Galdino on 25/10/2017.
 */

public class DemoConnectionWithJsoup {
    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        Connection.Response response;
        try {
            response = Jsoup.connect("Your URL ").followRedirects(false).execute();

        /* response.statusCode() will return you the status code */
            if (200 == response.statusCode()) {
                Document doc = Jsoup.connect("Your URL").get();

        /* what ever you want to do */
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
