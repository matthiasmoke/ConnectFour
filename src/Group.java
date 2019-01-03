import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a group of checkers in the game-board
 */
public class Group implements Cloneable{

    private GroupType type;
    private List<Checker> members = new ArrayList<>(4);

    /**
     * Initializes a group with members and certain type.
     *
     * @param members Members of the group.
     * @param type Type members of the group are arranged.
     */
    public Group(Collection<Checker> members, GroupType type) {
        addMembers(members);
        this.type = type;
    }

    /**
     * Gets members of the group.
     *
     * @return members of the group.
     */
    public List<Checker> getMembers() {
        return members;
    }

    /**
     * Gets the owner of the group
     *
     * @return Owner of the group.
     */
    public Player getOwner() {
        return members.get(0).getOwner();
    }

    /**
     * Gets type of the group.
     *
     * @return type of the group.
     */
    public GroupType getType() {
        return type;
    }

    /**
     * Checks if group has certain member.
     *
     * @param member Member that has to be searched for.
     * @return true if given member is part of the group.
     */
    public boolean hasMember(Checker member) {
        int col = member.getPosition().getColumn();
        int row = member.getPosition().getRow();
        for (Checker checker : members) {
            if (col == checker.getPosition().getColumn()
                    && row == checker.getPosition().getRow()) {
                return true;
            }
        }
        return false;
    }

    public void addMembers(Collection<Checker> memberList) {
        for (Checker member : memberList) {
            // if group size smaller than 4 and member isnt already member
            if (members.size() < Board.CONNECT && !hasMember(member)) {
                members.add(member);
            }
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

        List<Checker> membersCopy =  new ArrayList<>(4);

        for (Checker member : members) {
            membersCopy.add(member);
        }

        copy.members = membersCopy;

        return copy;
    }
}
