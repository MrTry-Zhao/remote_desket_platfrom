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

public class UserBookManageComponent extends Box {
    final int WIDTH = 850;
    final int HEIGHT = 600;

    JFrame jf = null;
    private JTable table;
    private Vector<String> titles;
    private Vector<Vector> tableData;
    private List<TaskVo>  quarryTableData;
    private DefaultTableModel tableModel;

    public UserBookManageComponent(JFrame jf) {
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

                    }
                }).setVisible(true);
            }
        });

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //����һ���Ի������û�����ͼ�����Ϣ
                new UserAddBookDialog(jf, "��������", true, new ActionDoneListener() {
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
                new UserUpdateBookDialog(table,tableData,tableModel,jf, "�޸�����", true, new ActionDoneListener() {
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
        quarryTableData = new Vector<>();

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


    public UserBookManageComponent(int axis) {
        super(axis);
    }

    //��������
    public void requestData(){
        //��ȡ�û���¼��
        Integer taskID = 2022005553;

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
            java.util.List<Object> row = new ArrayList<Object>();
            row.add(vo.getTaskId());
            row.add(vo.getUserStudentNumber());
            row.add(vo.getTaskContent());
            row.add(vo.getTaskStartTime());
            row.add(vo.getTaskEndTime());
            Vector<Object> rowData = new Vector<>();
            rowData.addAll(row);
            tableData.add(rowData);
        }

        // ���±��ģ��
        tableModel.fireTableDataChanged();
    }


}

