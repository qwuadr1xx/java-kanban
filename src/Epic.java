import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Long> subtaskIdList;

    public Epic(String name, String description) {
        super(name, description);
        subtaskIdList = new ArrayList<>();
    }

    public Epic(long id) {
        super(id);
    }

    public void addIdToList(long id) {
        subtaskIdList.add(id);
    }

    public void setIdList(List<Long> subtaskIdList) {
        this.subtaskIdList = subtaskIdList;
    }

    public List<Long> getIdList() {
        return subtaskIdList;
    }
}
