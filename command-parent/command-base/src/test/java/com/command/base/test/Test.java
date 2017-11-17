package com.command.base.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
        
        Client client = new Client();
        client.start();
        
        BufferedReader br = new BufferedReader(new  InputStreamReader(System.in));
        String line = null;
        while(null != (line = br.readLine())){
            client.send(line);
        }
    }
}
