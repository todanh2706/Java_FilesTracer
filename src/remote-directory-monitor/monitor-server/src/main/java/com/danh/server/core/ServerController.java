/**
 * Điều hành và quản lý các Client
 * @author Danh, To Huu
 */

package com.danh.server.core;

import com.danh.common.constants.AppConfig;
import com.danh.common.model.Message;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerController {
    private ServerSocket serverSocket; // Socket để giao tiếp
    private List<ClientHandler> clients; // Danh sách Client
    private IServerCallback callback; // Callback để gọi GUI
    private boolean isRunning = false; // Trạng thái Client

    // Constructor
    public ServerController(IServerCallback callback) {
        this.callback = callback;
        this.clients = new ArrayList<>();
    }

    // Hàm khởi động Server
    public void startServer() {
        if (isRunning) return; // Nếu server đã khởi động thì ngừng

        isRunning = true; // Đổi cờ

        new Thread(() -> {
            try {
                // Khởi tạo socket phía Server
                serverSocket = new ServerSocket(AppConfig.SERVER_PORT);
                this.log("Server started on port " + AppConfig.SERVER_PORT);

                while (isRunning) {
                    // Dừng, chờ Client kết nối
                    Socket clientSocket = serverSocket.accept();

                    // Tạo handler cho Client vừa kết nối
                    ClientHandler handler = new ClientHandler(clientSocket, this);
                    clients.add(handler); // Thêm vào danh sách Client

                    // Chạy thread cho Client
                    new Thread(handler).start();
                }
            } catch (IOException ioe) {
                if (isRunning) this.log("Server Error: " + ioe.getMessage());
            }
        }).start();
    }

    // Gửi yêu cầu giám sát tới một client cụ thể
    public void requestMonitor(int clientIndex, String folderPath) { // clientIndex là chỉ số của Client trong danh sách Client
        if (clientIndex >= 0 && clientIndex < clients.size()) {
            ClientHandler client = clients.get(clientIndex); // Lấy client
            client.sendMonitorRequest(folderPath); // Gửi yêu cầu giám sát
            this.log("Sent monitor request to: " + client.getClientName()); // Log ra GUI
        }
    }

    // ClientHandler gọi hàm này khi client bị disconnect
    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
        this.log("Client disconnected: " + client.getClientName());
        notifyClientListChanged();
    }

    // Hàm log kết quả ra màn hình
    public void log(String msg) {
        if (callback != null) callback.onLog(msg);
    }

    // Hàm thông báo cho GUI là đã có thay đổi lên Clients list
    public void notifyClientListChanged() {
        if (callback != null) callback.onClientListUpdated();
    }

    // Hàm thông báo cho GUI là đã nhận msg
    public void notifyMessageReceived(Message msg) {
        if (callback != null) callback.onMessageReceived(msg);
    }

    // Clients list getter
    public List<ClientHandler> getClients() {
        return clients;
    }
}