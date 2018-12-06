import java.util.LinkedList;
import java.util.List;

public class Group {

    private static final int MAX_SIZE = 4;
    private List<Coordinates2D> members = new LinkedList<>();

    public Group() {

    }

    public Group(Coordinates2D first) {

    }

    public List<Coordinates2D> getMembers() {
        return members;
    }

    public boolean isInGroup(Checker c) {
        for (Coordinates2D member : members) {
            if (member.equals(c)) {
                return true;
            }
        }
        return false;
    }

    public void addMember(Coordinates2D c) {
        members.add(c);
    }
}
