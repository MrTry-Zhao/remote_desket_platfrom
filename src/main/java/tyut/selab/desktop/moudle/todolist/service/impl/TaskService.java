package tyut.selab.desktop.moudle.todolist.service.impl;

import tyut.selab.desktop.moudle.todolist.dao.ITaskDao;
import tyut.selab.desktop.moudle.todolist.domain.Task;
import tyut.selab.desktop.moudle.todolist.domain.vo.TaskVo;
import tyut.selab.desktop.moudle.todolist.service.ITaskService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskService implements ITaskService {
    private ITaskDao taskDao;


    @Override
    public void taskWarnTime(TaskVo taskVo) {
    }

    @Override
    public List<TaskVo> queryAllTask(Integer studentNumber) {
        return transferVo(taskDao.queryAllTask(studentNumber));
    }

    @Override
    public int insertTask(TaskVo taskVo) {
        Task task = transferTask(taskVo);
        return taskDao.insertTask(task);
    }

    @Override
    public int updateTask(TaskVo taskVo) {
        return taskDao.updateTask(transferTask(taskVo),new Task(taskVo.getTaskId()));
    }

    @Override
    public int deleteTask(TaskVo taskVo) {
        Task task = transferTask(taskVo);
        return taskDao.deleteTask(task);
    }

    public List<TaskVo> transferVo(List<Task> task){
        ArrayList<TaskVo> taskVo = new ArrayList<>();
        Iterator<Task> ite = task.iterator();
        while(ite.hasNext()){
            taskVo.add(transferTaskVo(ite.next()));
        }
        return taskVo;
    }

    public Task transferTask(TaskVo taskVo){
        Task task = new Task(taskVo.getTaskId(), taskVo.getUserStudentNumber(), taskVo.getTaskContent(), taskVo.getTaskEndTime());
        return task;
    }

    public TaskVo transferTaskVo(Task task){
        TaskVo taskVo = new TaskVo(task.getTaskId(),task.getUserStudentNumber(),task.getTaskContent(),task.getTaskStartTime(),task.getTaskEndTime());
        return taskVo;
    }
}
