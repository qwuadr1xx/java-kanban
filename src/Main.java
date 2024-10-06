public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.addTask(new Task("task1", "desctask1"));
        taskManager.addTask(new Task("task2", "deasktask2"));
        System.out.println(taskManager.getListOfTasks());
        taskManager.addEpic(new Epic("epic1", "descepic1"));
        taskManager.addEpic(new Epic("epic2", "descepic2"));
        System.out.println(taskManager.getListOfEpics());
        taskManager.addSubtask(new Subtask("sub1", "decssub1", 3));
        taskManager.addSubtask(new Subtask("sub2", "decssub2", 4));
        System.out.println(taskManager.getListOfTasks());
        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfSubtasks());
    }
}
