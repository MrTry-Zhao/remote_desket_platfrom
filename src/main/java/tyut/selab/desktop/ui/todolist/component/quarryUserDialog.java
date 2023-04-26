package tyut.selab.desktop.ui.todolist.component;

import tyut.selab.desktop.moudle.todolist.controller.impl.TaskController;
import tyut.selab.desktop.moudle.todolist.domain.vo.TaskVo;
import tyut.selab.desktop.ui.todolist.listener.ActionDoneListener;
import tyut.selab.desktop.ui.todolist.utils.ScreenUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class quarryUserDialog extends JDialog {
    final int WIDTH = 400;
    final int HEIGHT = 300;
    private ActionDoneListener listener;

    public quarryUserDialog(JTable table, Vector<Vector> tableData, DefaultTableModel tableModel, JFrame jf, String title, boolean isModel, ActionDoneListener listener) {
        super(jf, title, isModel);
        this.listener = listener;
        //��װ��ͼ
        this.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);

        Box vBox = Box.createVerticalBox();

        //��װ�û����
        Box stockBox = Box.createHorizontalBox();
        JLabel stockLable = new JLabel("�û���ţ�");
        JTextField stockField = new JTextField(15);

        stockBox.add(stockLable);
        stockBox.add(Box.createHorizontalStrut(20));
        stockBox.add(stockField);


        //��װ��ť
        Box btnBox = Box.createHorizontalBox();
        JButton quarryBtn = new JButton("��ѯ");
        quarryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //��ȡ�û���¼��
                Integer taskID = Integer.parseInt(stockField.getText().trim());

                //��ѯ����
                TaskController taskController = new TaskController();

                List<TaskVo> data;
                try {
                    data = taskController.queryAllTask(taskID);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchFieldException ex) {
                    throw new RuntimeException(ex);
                } catch (InstantiationException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }

                tableData.clear();
                for (TaskVo vo: data) {
                    Vector<Object> rowData = new Vector<>();
                    rowData.add(vo);
                    tableData.add(rowData);
                }

                // ���±��ģ��
                tableModel.fireTableDataChanged();

                JOptionPane.showMessageDialog(jf, "��ѯ�ɹ�");

                dispose();
                listener.done(null);
            }
        });

        btnBox.add(quarryBtn);

        vBox.add(Box.createVerticalStrut(20));
        vBox.add(stockField);

        vBox.add(Box.createVerticalStrut(15));
        vBox.add(btnBox);


        //Ϊ�������м�࣬��vBox����װһ��ˮƽ��Box����Ӽ��
        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(20));

        this.add(hBox);
    }

    //    public void requestData() {
//        // �������ݿ�
//        String url = "jdbc:mysql:///desket_platfrom";
//        String username = "root";
//        String password = "159753";
//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection(url, username, password);
//            // ��ѯ�������
//            String sql = "SELECT task_id, user_student_number, task_content, task_start_time, task_end_time FROM user_tasks_list";
//            Statement stmt = null;
//            ResultSet rs = null;
//            try {
//                stmt = conn.createStatement();
//                rs = stmt.executeQuery(sql);
//                // ����ѯ����洢��һ���б���
//                List<List<Object>> data = new ArrayList<>();
//                while (rs.next()) {
//                    java.util.List<Object> row = new ArrayList<Object>();
//                    row.add(rs.getInt("task_id"));
//                    row.add(rs.getString("user_student_number"));
//                    row.add(rs.getString("task_content"));
//                    row.add(rs.getDate("task_start_time"));
//                    row.add(rs.getDate("task_end_time"));
//                    data.add(row);
//                }
//                tableData.clear();
//                // ������ѯ�������ÿ��������ӵ�tableData��
//                for (java.util.List<Object> row : data) {
//                    Vector<Object> rowData = new Vector<>();
//                    rowData.addAll(row);
//                    tableData.add(rowData);
//                }
//
//
//                // ���±��ģ��
//                tableModel.fireTableDataChanged();
//            } finally {
//                // �ر�ResultSet��Statement
//                if (rs != null) {
//                    try {
//                        rs.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//                if (stmt != null) {
//                    try {
//                        stmt.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        } finally {
//            // �ر�����
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
//    }
    public void quarryRequestData() {

    }

}

