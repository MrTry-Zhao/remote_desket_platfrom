package tyut.selab.desktop.ui.todolist.utils;

import java.sql.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;

public class AlarmClock01 {
    private Timer timer;

    public AlarmClock01() {
        timer = new Timer();
    }

    public void start() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                checkDeadlines();
            }
        }, 0, 1000); //ÿ���Ӽ��һ�ν�ֹ�����Ƿ���
    }

    public void stop() {
        timer.cancel();
    }

    private void checkDeadlines() {
        try {
            //���ӵ����ݿ�
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/desket_platfrom", "root", "159753");

            //��ѯ����δ���ڵĽ�ֹ����
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_tasks_list WHERE task_end_time > NOW()");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                //��ȡ��ֹ���ں�������Ϣ
                Date deadline = rs.getDate("task_end_time");
                String message = rs.getString("task_content");

                //�����ǰʱ�䳬����ֹ���ڣ��򵯳����ѿ�
                if (new Date().after(deadline)) {
                    JOptionPane.showMessageDialog(null, message+"ʱ�䵽������");
                }
            }

            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
