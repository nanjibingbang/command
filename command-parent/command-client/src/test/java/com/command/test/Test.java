package com.command.test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class Test {

    public static void main(String[] args) throws IOException {
        Enumeration<URL> resources = Test.class.getClassLoader().getResources("a.txt");
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            System.out.println(resource.toString());
        }
    }
}
