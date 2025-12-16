# Remote Directory Monitor (Hệ thống Giám sát Thư mục Từ xa)

![Java](https://img.shields.io/badge/Language-Java-orange)
![GUI](https://img.shields.io/badge/GUI-Java_Swing-blue)
![Network](https://img.shields.io/badge/Tech-Socket_TCP-green)

Đồ án môn học Lập trình mạng (Socket Programming).
Ứng dụng cho phép Server giám sát sự thay đổi của một thư mục cụ thể nằm trên máy Client theo thời gian thực.

## Tính năng chính

### Phía Server (Người giám sát)

-   **Dashboard quản lý:** Xem danh sách các Client đang kết nối (Online/Offline).
-   **Thiết lập giám sát:** Gửi yêu cầu giám sát một đường dẫn thư mục cụ thể tới Client.
-   **Nhật ký thời gian thực (Real-time Logs):** Nhận và hiển thị thông báo ngay lập tức khi thư mục đích có sự thay đổi:
    -   Tạo mới (File/Folder created)
    -   Xóa (File/Folder deleted)
    -   Chỉnh sửa (File modified)
-   **Đa luồng (Multi-threading):** Hỗ trợ giám sát nhiều Client cùng lúc mà không bị treo giao diện.

### Phía Client (Máy bị giám sát)

-   **Kết nối:** Thiết lập kết nối tới Server qua IP và Port.
-   **Background Service:** Chạy ngầm tiến trình lắng nghe thay đổi file (sử dụng `java.nio.file.WatchService`).
-   **Báo cáo:** Tự động gửi gói tin (Packet) chứa thông tin thay đổi về Server.

## Công nghệ sử dụng

-   **Ngôn ngữ:** Java (JDK 17+)
-   **Giao diện (GUI):** Java Swing (JFrame, JPanel).
-   **Giao tiếp mạng:** Java Socket (TCP/IP).
-   **Xử lý File:** Java NIO (Non-blocking I/O) cho hiệu suất cao khi giám sát file.
-   **Kiến trúc:** Mô hình Client-Server, MVC (Model-View-Controller).

## Cấu trúc dự án

Dự án được tổ chức theo cấu trúc module hóa để dễ bảo trì:

```text
src/
├── common/           # Các thành phần dùng chung (Protocol, Constants)
├── client/           # Mã nguồn phía Client
│   ├── view/         # Giao diện Client
│   └── service/      # Logic xử lý Socket & WatchService
└── server/           # Mã nguồn phía Server
    ├── view/         # Giao diện Server
    └── controller/   # Quản lý luồng Client (ClientHandler)
```
