package com.timeshare;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by adam on 2016/6/11.
 */
public class AppTest {

    public static void main(String[] args) {
        Server jettyServer = new Server(8080);

        WebAppContext wah = new WebAppContext();
        wah.setContextPath("/time");
        wah.setWar("src/main/webapp");
        jettyServer.setHandler(wah);
        try {
            jettyServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
