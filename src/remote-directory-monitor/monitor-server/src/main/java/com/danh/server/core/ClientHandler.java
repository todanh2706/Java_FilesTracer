/**
 * Xử lý các tác vụ của một Client
 * @author Danh, To Huu
 */

package com.danh.server.core;

import com.danh.common.enums.ActionType;
import com.danh.common.model.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket; // Socket để liên lạc với Client
    private ServerController serverController; // Server controller
    private ObjectOutputStream out; // Output objet
    private ObjectInputStream in; // Input object
    private String clientName = "Unknown"; // Tên Client mặc định
    private boolean isRunning = true; // Trạng thái của Client

    // Constructor
    public ClientHandler(Socket socket, ServerController controller) {
        this.socket = socket;
        this.serverController = controller;
    }

    // Client name getter
    public String getClientName() {
        return this.clientName;
    }

    @Override
    public void run() {
        try {
            // Tạo luồng dữ liệu
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Lắng nghe Client
            while (isRunning) {
                try {
                    Message msg = (Message) in.readObject(); // Chờ tin nhắn rồi mới làm tiếp
                    processMessage(msg); // Gọi hàm xử lý tin nhắn
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ioe) {
            // Client đã ngắt kết nối
            serverController.removeClient(this);
        } finally {
            closeConnection();
        }
    }

    // Xử lý các loại tin nhắn nhận được
    public void processMessage(Message msg) {
        switch (msg.getAction()) {
            case LOGIN: // Xử lý tin nhắn đăng nhập
                this.clientName = (String) msg.getContent(); // Lấy content của tin nhắn
                serverController.log("Client connected: " + clientName); // Log
                serverController.notifyClientListChanged(); // Đổi danh sách Client

                sendMessage(new Message(ActionType.LOGIN_SUCCESS, "Welcome " + clientName, "Server")); // Gửi lại cho Client báo đã connect thành công
                break;

            case FILE_CHANGED: // Xử lý tin nhắn về thay đổi của files
                serverController.notifyMessageReceived(msg); // Gửi cho GUI hiển thị
                break;

            case ERROR: // Xử lý lỗi từ Client
                serverController.log("Error from " + clientName + ": " + msg.getContent());
                break;

            default: // Xử lý các tin nhắn không rõ
                serverController.log("Unknown action from " + clientName);
        }
    }

    // Gửi tin nhắn xuống Client
    public void sendMessage(Message msg) {
        try {
            out.writeObject(msg); // Ghi xuống output object
            out.flush(); // Đẩy tin nhắn đi
        } catch (IOException ioe) {
            System.err.println("Error sending message to " + clientName);
        }
    }

    // Yêu cầu Client giám sát Folder
    public void sendMonitorRequest(String folderPath) {
        sendMessage(new Message(ActionType.START_MONITOR, folderPath, "Server")); // Gửi tin nhắn yêu cầu Client giám sát
    }

    private void closeConnection() {
        try {
            if (socket != null) socket.close(); // Đóng socket
            if (in != null) in.close(); // Đóng input object
            if (out != null) out.close(); // Đóng output object
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}