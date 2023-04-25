package tyut.selab.desktop.ui.todolist.component;


import javafx.concurrent.Task;
import tyut.selab.desktop.moudle.todolist.controller.impl.TaskController;
import tyut.selab.desktop.moudle.todolist.domain.vo.TaskVo;
import tyut.selab.desktop.ui.todolist.listener.ActionDoneListener;
import tyut.selab.desktop.ui.todolist.utils.ScreenUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AddBookDialog extends JDialog {
    private JTable table;

    private DefaultTableModel tableModel;
    final int WIDTH = 400;
    final int HEIGHT = 300;

    private ActionDoneListener listener;

    public AddBookDialog(JFrame jf, String title, boolean isModel, ActionDoneListener listener) {
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

        //��װͼ����
//        Box stockBox = Box.createHorizontalBox();
//        JLabel stockLable = new JLabel("�û���ţ�");
//        JTextField stockField = new JTextField(15);
//
//        stockBox.add(stockLable);
//        stockBox.add(Box.createHorizontalStrut(20));
//        stockBox.add(stockField);

        //��װ������
        Box descBox = Box.createHorizontalBox();
        JLabel descLable = new JLabel("������ܣ�");
        JTextArea descArea = new JTextArea(3, 15);

        descBox.add(descLable);
        descBox.add(Box.createHorizontalStrut(20));
        descBox.add(descArea);
//
//        //��װ��ʼ����
//        Box authorBox = Box.createHorizontalBox();
//        JLabel authorLable = new JLabel("��ʼ���ڣ�");
//        JTextField authorField = new JTextField(15);
//
//        authorBox.add(authorLable);
//        authorBox.add(Box.createHorizontalStrut(20));
//        authorBox.add(authorField);

        //��װ��ֹ����
        Box priceBox = Box.createHorizontalBox();
        JLabel priceLable = new JLabel("��ֹ���ڣ�");
        JTextField priceField = new JTextField(15);

        priceBox.add(priceLable);
        priceBox.add(Box.createHorizontalStrut(20));
        priceBox.add(priceField);


        //��װ��ť
        Box btnBox = Box.createHorizontalBox();
        JButton addBtn = new JButton("���");
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //��ȡ�û���¼��
                Integer taskID = null;
                Integer userStudentNumber = 2022005553;
                String taskST = null;
                String taskET = priceField.getText().trim();
                String taskContent = descArea.getText().trim();
                //StringתDate
                SimpleDateFormat taskStartTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat taskEndTimeFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date taskStartTime = null;
                Date taskEndTime = null;
                try {
                    taskStartTime = new Date();
                    taskEndTime = taskEndTimeFormat.parse(taskET);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }

                //�������
                TaskController taskController = new TaskController();
                TaskVo taskVo = new TaskVo(taskID, userStudentNumber, taskContent, taskStartTime, taskEndTime);
                try {
                    taskController.insertTask(taskVo);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(jf, "��ӳɹ�");

                dispose();
                listener.done(null);
            }
        });


        btnBox.add(addBtn);

//        vBox.add(Box.createVerticalStrut(20));
//        vBox.add(nameBox);
//        vBox.add(Box.createVerticalStrut(15));
//        vBox.add(stockBox);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(descBox);
//        vBox.add(Box.createVerticalStrut(15));
//        vBox.add(authorBox);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(priceBox);

        vBox.add(Box.createVerticalStrut(15));
        vBox.add(btnBox);

        //Ϊ�������м�࣬��vBox����װһ��ˮƽ��Box����Ӽ��
        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(20));

        this.add(hBox);

    }

    public void requestData(TaskVo taskVo) {
        Vector<String> titles;
        Vector<Vector> tableData;
        titles = new Vector<>();
        tableData = new Vector<>();


        //�������
        Vector<Object> rowData = new Vector<>();
        rowData.add(taskVo.getTaskId());
        rowData.add(taskVo.getUserStudentNumber());
        rowData.add(taskVo.getTaskContent());
        rowData.add(taskVo.MyToString(taskVo.getTaskStartTime()));
        rowData.add(taskVo.MyToString(taskVo.getTaskEndTime()));
        tableData.add(rowData);
        tableModel = new DefaultTableModel(tableData, titles);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableModel.fireTableDataChanged();


        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane);


    }

}
