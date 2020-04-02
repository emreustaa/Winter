package com.fsm;

import org.eclipse.jetty.server.Server;

import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        Server server = new Server(new InetSocketAddress(8080));
        server.setHandler(new HelloWorld());

        server.start();
        server.join();
    }
}