package gui_interface;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MyClient {
    public static void initClient(String name) {
        String fuser, fserver;

        Socket fromserver = null;
        try {
            fromserver = new Socket("localhost", 4444);
        } catch (IOException e) {

        }


        try {
            Resources.clientIn = new BufferedReader
                    (new InputStreamReader(fromserver.getInputStream()));
        } catch (IOException e) {

        }
        ;
        try {
            Resources.clientOut = new PrintWriter
                    (fromserver.getOutputStream(), true);
        } catch (IOException e) {

        }
        BufferedReader inu = new BufferedReader
                (new InputStreamReader(System.in));
        //Устанавливаем имя 
        Resources.clientOut.println(name);


    }
    public static void initSystem() throws NullPointerException{
        Socket fromserver = null;
        try {
            fromserver = new Socket("localhost", 4444);
        } catch (IOException e) {

        }


        try {
            Resources.clientIn = new BufferedReader
                    (new InputStreamReader(fromserver.getInputStream()));
        } catch (IOException e) {

        }

        try {
            Resources.clientOut = new PrintWriter
                    (fromserver.getOutputStream(), true);
        } catch (IOException e) {

        }
        BufferedReader inu = new BufferedReader
                (new InputStreamReader(System.in));

        Resources.clientOut.println("initSystem");


    }
    public static void closeSystem() {
        String fuser, fserver;

        Socket fromserver = null;
        try {
            fromserver = new Socket("localhost", 4444);
        } catch (IOException e) {

        }


        try {
            Resources.clientIn = new BufferedReader
                    (new InputStreamReader(fromserver.getInputStream()));
        } catch (IOException e) {

        }
        ;
        try {
            Resources.clientOut = new PrintWriter
                    (fromserver.getOutputStream(), true);
        } catch (IOException e) {

        }
        BufferedReader inu = new BufferedReader
                (new InputStreamReader(System.in));
        //Устанавливаем имя
        Resources.clientOut.println("closeSystem");
        Resources.clientIn = null;
        Resources.clientIn=null;


    }


}