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

        //��װͼ������
//        Box nameBox = Box.createHorizontalBox();
//        JLabel nameLable = new JLabel("�����ţ�");
//        JTextField nameField = new JTextField(15);
//
//        nameBox.add(nameLable);
//        nameBox.add(Box.createHorizontalStrut(20));
//        nameBox.add(nameField);

        //��װ�û����
        Box stockBox = Box.createHorizontalBox();
        JLabel stockLable = new JLabel("�û���ţ�");
        JTextField stockField = new JTextField(10);

        stockBox.add(stockLable);
        stockBox.add(Box.createHorizontalStrut(15));
        stockBox.add(stockField);

        //��װ������
//        Box descBox = Box.createHorizontalBox();
//        JLabel descLable = new JLabel("������ܣ�");
//        JTextArea descArea = new JTextArea(3, 15);
//
//        descBox.add(descLable);
//        descBox.add(Box.createHorizontalStrut(20));
//        descBox.add(descArea);
////
//        //��װ��ʼ����
//        Box authorBox = Box.createHorizontalBox();
//        JLabel authorLable = new JLabel("��ʼ���ڣ�");
//        JTextField authorField = new JTextField(15);
//
//        authorBox.add(authorLable);
//        authorBox.add(Box.createHorizontalStrut(20));
//        authorBox.add(authorField);

        //��װ��ֹ����
//        Box priceBox = Box.createHorizontalBox();
//        JLabel priceLable = new JLabel("��ֹ���ڣ�");
//        JTextField priceField = new JTextField(15);
//        CalendarPanel p = new CalendarPanel(priceField, "yyyy-MM-dd");
//        p.initCalendarPanel();
//        add(p);
//        add(priceField);
//
//        priceBox.add(priceLable);
//        priceBox.add(Box.createHorizontalStrut(20));
//        priceBox.add(priceField);



        //��װ��ť
        Box btnBox = Box.createHorizontalBox();
        JButton quarryBtn = new JButton("��ѯ");
        quarryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //��ȡ�û���¼��
                Integer userStudentNumber ;
                try {
                    userStudentNumber = Integer.parseInt(stockField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(jf, "��������ȷ��ʽ");
                    return; // ������ѯ����
                }
                //��ѯ����
                TaskController taskController = new TaskController();

                List<TaskVo> data;
                try {
                    data = taskController.queryAllTask(userStudentNumber);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchFieldException ex) {
                    throw new RuntimeException(ex);
                } catch (InstantiationException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
                if (data.isEmpty()) {
                    JOptionPane.showMessageDialog(jf, "���޴���");
                    return;
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

                JOptionPane.showMessageDialog(jf, "��ѯ�ɹ�");

                dispose();
                listener.done(null);
            }
        });

        btnBox.add(quarryBtn);

//        vBox.add(Box.createVerticalStrut(20));
//        vBox.add(nameBox);
        vBox.add(Box.createVerticalStrut(100));
        vBox.add(stockBox);
//        vBox.add(Box.createVerticalStrut(15));
//        vBox.add(descBox);
//        vBox.add(Box.createVerticalStrut(15));
//        vBox.add(authorBox);
//        vBox.add(Box.createVerticalStrut(15));
//        vBox.add(priceBox);

        vBox.add(Box.createVerticalStrut(100));
        vBox.add(btnBox);

        //Ϊ�������м�࣬��vBox����װһ��ˮƽ��Box����Ӽ��
        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(5));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(5));

        this.add(hBox);
    }


}

