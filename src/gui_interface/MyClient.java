package gui_interface;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MyClient  {
    public static void initClient(String name)  throws IOException{ //иницилизация клиента

        String fuser, fserver;

        Socket fromserver = null;

            fromserver = new Socket("localhost", 4444);


            Resources.clientIn = new BufferedReader
                    (new InputStreamReader(fromserver.getInputStream()));


            Resources.clientOut = new PrintWriter
                    (fromserver.getOutputStream(), true);

        BufferedReader inu = new BufferedReader
                (new InputStreamReader(System.in));
        //Устанавливаем имя 
        Resources.clientOut.println(name);


    }
    public static void initSystem() throws NullPointerException, IOException { //иницилизирование потока системы
        Socket fromserver = null;

            fromserver = new Socket("localhost", 4444);




            Resources.clientIn = new BufferedReader
                    (new InputStreamReader(fromserver.getInputStream()));


            Resources.clientOut = new PrintWriter
                    (fromserver.getOutputStream(), true);

        BufferedReader inu = new BufferedReader
                (new InputStreamReader(System.in));

        Resources.clientOut.println("initSystem");


    }
    public static void closeSystem() throws IOException {//закрытие потока системы
        String fuser, fserver;

        Socket fromserver = null;

            fromserver = new Socket("localhost", 4444);



            Resources.clientIn = new BufferedReader
                    (new InputStreamReader(fromserver.getInputStream()));


            Resources.clientOut = new PrintWriter
                    (fromserver.getOutputStream(), true);

        BufferedReader inu = new BufferedReader
                (new InputStreamReader(System.in));
        //Устанавливаем имя
        Resources.clientOut.println("closeSystem");
        Resources.clientIn = null;
        Resources.clientIn=null;


    }


}