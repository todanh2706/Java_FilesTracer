package com.danh.server.view;

import com.danh.common.model.Message;
import com.danh.server.core.ClientHandler;
import com.danh.server.core.IServerCallback;
import com.danh.server.core.ServerController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ServerFrame extends JFrame implements IServerCallback {
    private JTextArea txtLog;
    private JTable tblClients;
    private DefaultTableModel tableModel;
    private JTextField txtFolderPath;
    private JButton btnMonitor;
    private ServerController serverController;

    public ServerFrame() {
        super("Server Monitor - Remote Directory Watcher");
        setupUI();

        // Khởi tạo controller và start server
        serverController = new ServerController(this);
        serverController.startServer();
    }

    private void setupUI() {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel phía trên (khung nhập và nút bấm)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtFolderPath = new JTextField(30);
        btnMonitor = new JButton("Monitor this Folder");

        // Thêm các thành phần vào top panel
        topPanel.add(new JLabel("Folder Path on Client: "));
        topPanel.add(txtFolderPath);
        topPanel.add(btnMonitor);

        // Thêm top panel vào Frame
        add(topPanel, BorderLayout.NORTH);

        // Panel ở giữa (danh sách Client và log)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // Bảng danh sách Client
        String[] columns = {"Client Name", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        tblClients = new JTable(tableModel);
        JScrollPane scrollClients = new JScrollPane(tblClients);
        scrollClients.setBorder(BorderFactory.createTitledBorder("Connected Clients"));

        // Log
        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Real-time Logs"));

        // Thêm vào split pane và đặt kích cỡ chia
        splitPane.setLeftComponent(scrollClients);
        splitPane.setRightComponent(scrollLog);
        splitPane.setDividerLocation(250);

        // Thêm split pane vào Frame
        add(splitPane, BorderLayout.CENTER);

        // Thêm sự kiện cho nút
        btnMonitor.addActionListener(e -> {
            int selectedRow = tblClients.getSelectedRow(); // Lấy Client được chọn
            String path = txtFolderPath.getText().trim(); // Lấy đường dẫn vừa nhập

            // Xử lý ngoại lệ
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a client from the table!");
                return;
            }
            if (path.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a folder path!");
                return;
            }

            // Gọi Controller để gửi lệnh
            serverController.requestMonitor(selectedRow, path);
        });
    }

    // Ghi đè các hàm của lớp IServerCallback
    @Override
    public void onLog(String log) {
        SwingUtilities.invokeLater(() -> {
            txtLog.append("[SYSTEM]: " + log + "\n");
            txtLog.setCaretPosition(txtLog.getDocument().getLength()); // Tự cuộn xuống dưới
        });
    }

    @Override
    public void onClientListUpdated() {
        SwingUtilities.invokeLater(() -> {
            // Xóa hết bảng cũ
            tableModel.setRowCount(0);

            // Lấy danh sách mới nhất từ Controller đổ vào bảng
            for (ClientHandler client : serverController.getClients()) {
                tableModel.addRow(new Object[]{client.getClientName(), "Connected"});
            }
        });
    }

    @Override
    public void onMessageReceived(Message message) {
        SwingUtilities.invokeLater(() -> {
            txtLog.append("--------------------------------------------------\n");
            txtLog.append("FROM: " + message.getSender() + "\n");
            txtLog.append("CONTENT: " + message.getContent() + "\n");
        });
    }
}
