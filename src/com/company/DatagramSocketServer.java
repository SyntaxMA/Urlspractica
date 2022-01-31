package com.company;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

/**
     * Created by Ruben on 26/01/21.
     *
     */

public class DatagramSocketServer {
    DatagramSocket socket;
    SecretNum secretNum = new SecretNum();
    int contador = 0;

    //Instàciar el socket
    public void init(int port) throws SocketException {
        socket = new DatagramSocket(port);

    }

    public void runServer() throws IOException {
        byte [] receivingData = new byte[1024];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;
        secretNum.pensa(100);

        while(true) {
            DatagramPacket packet = new DatagramPacket(receivingData,1024);
            socket.receive(packet);
            sendingData = processData(packet.getData(),packet.getLength());
            //Llegim el port i l'adreça del client per on se li ha d'enviar la resposta
            clientIP = packet.getAddress();
            clientPort = packet.getPort();
            packet = new DatagramPacket(sendingData,sendingData.length,clientIP,clientPort);
            socket.send(packet);
        }
    }

    //El server retorna al client el mateix missatge que li arriba però en majúscules
    private byte[] processData(byte[] data, int lenght) {
        String msg = new String(data,0,lenght);

        int num = ByteBuffer.wrap(data).getInt(); //data és l'array de bytes

        byte[] resposta = ByteBuffer.allocate(4).putInt(num).array(); //num és un int

        contador ++;
        msg = msg.toUpperCase();


        System.out.println(msg);

        String xd = contador + " " +  secretNum.comprova(num);

        int fabrica = secretNum.comprova(num);
        System.out.println(num + " "+ fabrica);

        byte[] resposta2 = ByteBuffer.allocate(4).putInt(fabrica).array();

        //return xd.getBytes();
        return resposta2;



        //Imprimir el missatge rebut i retornar-lo

        // return msg.getBytes();
    }

    public static void main(String[] args) {
        DatagramSocketServer server = new DatagramSocketServer();

        try {
            server.init(5555);
            server.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

