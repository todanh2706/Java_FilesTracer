/**
 * Lớp interface cho ServerController
 * @author Danh, To Huu
 */

package com.danh.server.core;

import com.danh.common.model.Message;

public interface IServerCallback {
    void onLog(String log); // Xử lý thông báo quan trọng
    void onClientListUpdated(); // Xử lý các kết nối của  Client (kết nối/ngắt)
    void onMessageReceived(Message message); // Gọi khi nhận được Message từ Client
}