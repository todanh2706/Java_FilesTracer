/**
 * Class giám sát thư mục
 * @author Danh, To Huu
 */

package com.danh.client.core;

import com.danh.common.model.FileEvent;
import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;

public class DirectoryWatcher implements Runnable {
    private String path; // Đường dẫn đầy đủ của dir cần theo dõi
    private WatchService watchService;
    private IClientCallback callback; // Client callback
    private boolean isRunning = false; // Trạng thái

    // Constructor
    public DirectoryWatcher(String path, IClientCallback callback) {
        this.path = path;
        this.callback = callback;
    }

    // Hàm dừng theo dõi
    public void stopWatcher() {
        isRunning = false;
        try {
            if (watchService != null) watchService.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // Hàm run
    @Override
    public void run() {
        try {
            // Khởi tạo WatchService
            watchService = FileSystems.getDefault().newWatchService();
            Path folder = Paths.get(path);

            // Đăng ký sự kiện muốn nghe (tạo, xoá, sửa)
            folder.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

            isRunning = true; // Đặt cờ

            // Lắng nghe
            while (isRunning) {
                WatchKey key;
                try {
                    // Chờ đến khi có sự kiện xảy ra
                    key = watchService.take();
                } catch (InterruptedException ie) {
                    return;
                } catch (ClosedWatchServiceException cwse) {
                    break; // Thoát nếu service bị đóng
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    // Lấy loại sự kiện
                    WatchEvent.Kind<?> kind = event.kind();

                    // Bỏ qua sự kiện OVERFLOW
                    if (kind == OVERFLOW) continue;

                    // Lấy tên của file bị đổi
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();

                    // Chuyển loại sự kiện của Java thành chuỗi dễ đọc
                    String typeStr = "";
                    if (kind == ENTRY_CREATE) typeStr = "CREATED";
                    else if (kind == ENTRY_DELETE) typeStr = "DELETED";
                    else if (kind == ENTRY_MODIFY) typeStr = "MODIFIED";

                    // Tạo object FileEvent và out
                    FileEvent fileEvent = new FileEvent(fileName.toString(), path, typeStr);
                    callback.onFileEvent(fileEvent);
                }

                // Reset key để tiếp tục nhận sự kiện sau
                boolean valid = key.reset();
                if (!valid) break; // Thư mục bị xóa thì dừng luôn
            }
        } catch (IOException ioe) {
            callback.onConnectionStatusChanged(false, "Error watching folder: " + ioe.getMessage());
        }
    }
}
