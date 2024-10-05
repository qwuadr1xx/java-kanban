import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Long> subtaskIdList;

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, long id) {
        super(name, description, id);
        subtaskIdList = new ArrayList<>();
    }

    public void addIdToList(long id) {
        subtaskIdList.add(id);
    }

    public void removeIdFromList(long id) {
        subtaskIdList.remove(id);
    }

    public void clearList() {
        subtaskIdList.clear();
    }

    public List<Long> getIdList() {
        return subtaskIdList;
    }
}
