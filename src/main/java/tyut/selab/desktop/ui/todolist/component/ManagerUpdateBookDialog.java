package tyut.selab.desktop.ui.todolist.component;



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
import java.util.Date;
import java.util.Map;
import java.util.Vector;

public class ManagerUpdateBookDialog extends JDialog {
    final int WIDTH = 400;
    final int HEIGHT = 300;
    private String id;
    private ActionDoneListener listener;
    private Map<String,Object> map;

    private JTextField nameField;
    private JTextField stockField;
    private JTextField authorField;
    private JTextField priceField;
    private JTextArea descArea;
    JFrame jf = null;


    public ManagerUpdateBookDialog(JTable table, Vector<Vector> tableData, DefaultTableModel tableModel, JFrame jf, String title, boolean isModel, ActionDoneListener listener, String id){
        super(jf,title,isModel);
        this.listener  = listener;
        this.id=id;
        //��װ��ͼ
        this.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);

        Box vBox = Box.createVerticalBox();

//        //��װ������
//        Box nameBox = Box.createHorizontalBox();
//        JLabel nameLable = new JLabel("�����ţ�");
//        nameField = new JTextField(15);
//
//        nameBox.add(nameLable);
//        nameBox.add(Box.createHorizontalStrut(20));
//        nameBox.add(nameField);

        //��װ�û�ѧ��
        Box stockBox = Box.createHorizontalBox();
        JLabel stockLable = new JLabel("�û�ѧ�ţ�");
        stockField = new JTextField(15);

        stockBox.add(stockLable);
        stockBox.add(Box.createHorizontalStrut(20));
        stockBox.add(stockField);

        //��װ��ʼ����
//        Box authorBox = Box.createHorizontalBox();
//        JLabel authorLable = new JLabel("��ʼ���ڣ�");
//        authorField = new JTextField(15);
//
//        authorBox.add(authorLable);
//        authorBox.add(Box.createHorizontalStrut(20));
//        authorBox.add(authorField);

        //��װ��ֹ����
        Box priceBox = Box.createHorizontalBox();
        JLabel priceLable = new JLabel("��ֹ���ڣ�");
         priceField = new JTextField(15);
        CalendarPanel p = new CalendarPanel(priceField, "yyyy-MM-dd");
        p.initCalendarPanel();
        add(p);
        add(priceField);

        priceBox.add(priceLable);
        priceBox.add(Box.createHorizontalStrut(20));
        priceBox.add(priceField);


        //��װ�������
        Box descBox = Box.createHorizontalBox();
        JLabel descLable = new JLabel("������ܣ�");
        descArea = new JTextArea(3,15);

        descBox.add(descLable);
        descBox.add(Box.createHorizontalStrut(20));
        descBox.add(new JScrollPane(descArea));

        //��װ��ť
        Box btnBox = Box.createHorizontalBox();
        JButton updateBtn = new JButton("�޸�");
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //��ȡ��ǰ���ѡ�е�id
                int selectedRow = table.getSelectedRow();//�����ѡ�е���Ŀ���򷵻���Ŀ���кţ����û��ѡ�У���ô����-1

                //��ȡ�û��޸ĺ�������������������
                Integer taskID = Integer.valueOf(tableModel.getValueAt(selectedRow, 0).toString());
                Integer userStudentNumber = Integer.valueOf(stockField.getText().trim());
                String taskST = null;
                String taskET = priceField.getText().trim();
                String taskContent = descArea.getText().trim();

                SimpleDateFormat taskStartTimeFormat=new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat taskEndTimeFormat=new SimpleDateFormat("yyyy-MM-dd");

                Date taskStartTime = null;
                Date taskEndTime = null;
                try {
                    taskStartTime = new Date();
                    taskEndTime = taskEndTimeFormat.parse(taskET);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }

                TaskVo taskVo = new TaskVo(taskID,userStudentNumber,taskContent,taskStartTime,taskEndTime);
                TaskController taskController = new TaskController();
                try {
                    taskController.updateTask(taskVo);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                JOptionPane.showMessageDialog(jf,"�޸ĳɹ�");
                dispose();
                listener.done(null);
            }
        });
        //TODO �����޸ĵ���Ϊ
        btnBox.add(updateBtn);

//        vBox.add(Box.createVerticalStrut(20));
//        vBox.add(nameBox);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(stockBox);
//        vBox.add(Box.createVerticalStrut(15));
//        vBox.add(authorBox);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(priceBox);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(descBox);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(btnBox);

        //Ϊ�������м�࣬��vBox����װһ��ˮƽ��Box����Ӽ��
        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(20));

        this.add(hBox);
        //��������
        requestData();
    }

    //��������
    public void requestData(){

    }

}
