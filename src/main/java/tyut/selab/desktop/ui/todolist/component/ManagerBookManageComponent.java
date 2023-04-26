package tyut.selab.desktop.ui.todolist.component;


import tyut.selab.desktop.moudle.todolist.controller.impl.TaskController;
import tyut.selab.desktop.moudle.todolist.domain.vo.TaskVo;
import tyut.selab.desktop.ui.todolist.listener.ActionDoneListener;
import tyut.selab.desktop.ui.todolist.utils.AlarmClock01;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class ManagerBookManageComponent extends Box {
    final int WIDTH = 850;
    final int HEIGHT = 600;

    JFrame jf = null;
    private JTable table;
    private Vector<String> titles;
    private Vector<Vector> tableData;
    private DefaultTableModel tableModel;

    public ManagerBookManageComponent(JFrame jf) {
        //��ֱ����
        super(BoxLayout.Y_AXIS);
        //��װ��ͼ
        this.jf = jf;
        JPanel btnPanel = new JPanel();
        Color color = new Color(203, 220, 217);
        btnPanel.setBackground(color);
        btnPanel.setMaximumSize(new Dimension(WIDTH, 80));
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton addBtn = new JButton("���");
        JButton updateBtn = new JButton("�޸�");
        JButton deleteBtn = new JButton("ɾ��");
        JButton queryBtn = new JButton("��ѯ");
        AlarmClock01 alarmClock01 = new AlarmClock01();
        alarmClock01.start();

        queryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new quarryUserDialog(table,tableData,tableModel,jf, "��ѯ����", true, new ActionDoneListener() {
                    @Override
                    public void done(Object result) {
                        requestData();
                    }
                }).setVisible(true);
            }
        });

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //����һ���Ի������û�����ͼ�����Ϣ
                new AddBookDialog(jf, "��������", true, new ActionDoneListener() {
                    @Override
                    public void done(Object result) {
                        requestData();
                    }
                }).setVisible(true);
            }
        });

        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //��ȡ��ǰ���ѡ�е�id
                int selectedRow = table.getSelectedRow();//�����ѡ�е���Ŀ���򷵻���Ŀ���кţ����û��ѡ�У���ô����-1

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(jf, "��ѡ��Ҫ�޸ĵ���Ŀ��");
                    return;
                }

                String id = tableModel.getValueAt(selectedRow, 0).toString();

                //����һ���Ի������û��޸�
                new ManagerUpdateBookDialog(table,tableData,tableModel,jf, "�޸�����", true, new ActionDoneListener() {
                    @Override
                    public void done(Object result) {
                        requestData();
                    }
                }, id).setVisible(true);
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //��ȡѡ�е���Ŀ
                int selectedRow = table.getSelectedRow();//�����ѡ�е���Ŀ���򷵻���Ŀ���кţ����û��ѡ�У���ô����-1

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(jf, "��ѡ��Ҫɾ������Ŀ��");
                    return;
                }

                //��ֹ�����
                int result = JOptionPane.showConfirmDialog(jf, "ȷ��Ҫɾ��ѡ�е���Ŀ��", "ȷ��ɾ��", JOptionPane.YES_NO_OPTION);
                if (result != JOptionPane.YES_OPTION) {
                    return;
                }

                //��ȡ��ѡ�е�����
                Integer taskID = Integer.valueOf(tableModel.getValueAt(selectedRow, 0).toString());
                Integer userStudentNumber = Integer.valueOf(tableModel.getValueAt(selectedRow, 1).toString());
                String taskContent = tableModel.getValueAt(selectedRow, 2).toString();
                String taskST = tableModel.getValueAt(selectedRow, 3).toString();
                String taskET = tableModel.getValueAt(selectedRow, 4).toString();

                SimpleDateFormat taskStartTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat taskEndTimeFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date taskStartTime = null;
                Date taskEndTime = null;
                try {
                    taskStartTime = taskStartTimeFormat.parse(taskST);
                    taskEndTime = taskEndTimeFormat.parse(taskET);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }

                //ɾ������
                TaskVo taskVo = new TaskVo(taskID, userStudentNumber, taskContent, taskStartTime, taskEndTime);
                TaskController taskController = new TaskController();
                try {
                    taskController.deleteTask(taskVo);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                requestData();

            }
        });

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);

        this.add(btnPanel);

        //��װ���
        String[] ts = {"������", "�û�ѧ��", "�������", "��ʼ����", "��ֹ����"};
        titles = new Vector<>();
        titles.addAll(Arrays.asList(ts));

        tableData = new Vector<>();

        tableModel = new DefaultTableModel(tableData, titles);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //����ֻ��ѡ��һ��
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane);
        requestData();

    }

    //��������
    public void requestData() {
        // �������ݿ�
        String url = "jdbc:mysql:///desket_platfrom";
        String username = "root";
        String password = "159753";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            // ��ѯ�������
            String sql = "SELECT task_id, user_student_number, task_content, task_start_time, task_end_time FROM user_tasks_list";
            Statement stmt = null;
            ResultSet rs = null;
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                // ����ѯ����洢��һ���б���
                List<List<Object>> data = new ArrayList<>();
                while (rs.next()) {
                    java.util.List<Object> row = new ArrayList<Object>();
                    row.add(rs.getInt("task_id"));
                    row.add(rs.getString("user_student_number"));
                    row.add(rs.getString("task_content"));
                    row.add(rs.getDate("task_start_time"));
                    row.add(rs.getDate("task_end_time"));
                    data.add(row);
                }
                tableData.clear();
                // ������ѯ�������ÿ��������ӵ�tableData��
                for (java.util.List<Object> row : data) {
                    Vector<Object> rowData = new Vector<>();
                    rowData.addAll(row);
                    tableData.add(rowData);
                }


                // ���±��ģ��
                tableModel.fireTableDataChanged();
            } finally {
                // �ر�ResultSet��Statement
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // �ر�����
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}