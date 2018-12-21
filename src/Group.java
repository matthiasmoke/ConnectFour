import java.util.LinkedList;
import java.util.List;

/**
 * Represents a group of checkers in the game-board
 */
public class Group {

    private static final int MAX_SIZE = 4;
    private GroupType type;
    private List<Coordinates2D> members = new LinkedList<>();

    public Group(Coordinates2D first, Coordinates2D second, GroupType type) {
        members.add(first);
        members.add(second);
        this.type = type;
    }

    public List<Coordinates2D> getMembers() {
        return members;
    }

    public GroupType getType() {
        return type;
    }

    public boolean hasMember(Checker c) {
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
