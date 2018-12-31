import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a group of checkers in the game-board
 */
public class Group implements Cloneable{

    private GroupType type;
    private List<Coordinates2D> members = new ArrayList<>(4);

    /**
     * Initializes a group with members and certain type
     * @param first First member of the group.
     * @param second Second member of the group.
     * @param type Type members of the group are arranged
     */
    public Group(Coordinates2D first, Coordinates2D second, GroupType type) {
        members.add(first);
        members.add(second);
        this.type = type;
    }

    /**
     * Gets members of the group.
     * @return members of the group.
     */
    public List<Coordinates2D> getMembers() {
        return members;
    }

    /**
     * Gets type of the group.
     * @return type of the group.
     */
    public GroupType getType() {
        return type;
    }

    /**
     * Checks if group has certain member.
     * @param member Member that has to be searched for.
     * @return true if given member is part of the group.
     */
    public boolean hasMember(Coordinates2D member) {
        int col = member.getColumn();
        int row = member.getRow();
        for (Coordinates2D coordinate : members) {
            if (col == coordinate.getColumn() && row == coordinate.getRow()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a member to the group if it has less than four members.
     * @param member Member to add.
     */
    public void addMember(Coordinates2D member) {
        if (members.size() < Board.CONNECT) {
            members.add(member);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group clone() {
        Group copy;
        try {
            copy = (Group) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Error(ex);
        }

        for (int i = 0; i < members.size(); i++) {
            copy.members.set(i, members.get(i).clone());
        }

        return copy;
    }
}
