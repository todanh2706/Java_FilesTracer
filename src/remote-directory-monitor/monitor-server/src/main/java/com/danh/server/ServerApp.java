/**
 * Lớp chính của App
 * @author Danh, To Huu
 */

package com.danh.server;

import java.util.Scanner;
import com.danh.common.model.Message;
import com.danh.server.core.IServerCallback;
import com.danh.server.core.ServerController;

public class ServerApp {
    public static void main(String[] args) {
        // ***************** TẠM THỜI ĐỂ TEST *****************
        IServerCallback consoleCallback = new IServerCallback() {
            @Override
            public void onLog(String log) {
                System.out.println("[SERVER LOG]: " + log);
            }

            @Override
            public void onClientListUpdated() {
                System.out.println("[EVENT]: Client list updated");
            }

            @Override
            public void onMessageReceived(Message message) {
                System.out.println("[MSG]: " + message.toString());
            }
        };

        ServerController server = new ServerController(consoleCallback);
        server.startServer();

        System.out.println("Press Enter to stop...");
        new Scanner(System.in).nextLine();
    }
}