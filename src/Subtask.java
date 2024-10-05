public class Subtask extends Task {
    private long epicId;

    public Subtask(String name, String description, long epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(long id, Epic epic) {
        super(id);
        epic.addIdToList(id);
    }

    public long getEpicId() {
        return epicId;
    }

    public void changeSubtaskCondition(TaskCondition taskCondition) {
        this.taskCondition = taskCondition;

    }
/* В общем, я хочу сделать обновление состояний эпика через обновление состояния сабтаска. Т.е. при каждом обновлении
сабтаска, будет обновляться состояние эпика. Но как мне кажется, я как-то криво иду. Такое ощущение, что решение простое
и я не вижу его. Слишком муторно брать с TaskManager сам эпик, с класса эпиков лист id сабтасков, а потом еще и возвращать
также, глянь пожалуйста что я вообще еще понакалякал, особенно конструкторы) спасибо
 */
    public void updateEpicCondition() {

    }
}
