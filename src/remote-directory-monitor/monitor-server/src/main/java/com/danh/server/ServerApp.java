/**
 * Lớp chính của App
 * @author Danh, To Huu
 */

package com.danh.server;

import java.util.Scanner;
import com.danh.common.model.Message;
import com.danh.server.core.IServerCallback;
import com.danh.server.core.ServerController;
import com.danh.server.view.ServerFrame;

import javax.swing.*;

public class ServerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServerFrame frame = new ServerFrame();
            frame.setVisible(true);
        });
    }
}