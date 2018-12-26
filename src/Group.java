import java.util.LinkedList;
import java.util.List;

/**
 * Represents a group of checkers in the game-board
 */
public class Group implements Cloneable{

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

    public boolean hasMember(Coordinates2D pos) {
        for (Coordinates2D member : members) {
            if (member.equals(pos)) {
                return true;
            }
        }
        return false;
    }

    public void addMember(Coordinates2D pos) {
        if (members.size() < Board.CONNECT) {
            members.add(pos);
        }
    }

    @Override
    public Group clone() {
        Group copy;
        try {
            copy = (Group) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Error(ex);
        }

        for (Coordinates2D member : members) {
            copy.addMember(member.clone());
        }

        return copy;
    }
}
