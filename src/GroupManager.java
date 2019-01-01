import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupManager implements Cloneable {

    private List<Group> groupsOfPlayer1 = new ArrayList<>();
    private List<Group> groupsOfPlayer2 = new ArrayList<>();
    private Player[] players = new Player[2];

    public GroupManager(Player player1, Player player2) {
        players[0] = player1;
        players[1] = player2;
    }

    public void check(Checker current, List<Coordinates2D> neighbours,
                      GroupType type) {

        if (neighbours.size() > 0) {
            if (current.getOwner().equals(players[0])) {
                checkGroups(current, neighbours, type, groupsOfPlayer1);
            } else {
                checkGroups(current, neighbours, type, groupsOfPlayer2);
            }
        }
    }

    public boolean isBotWinPossible() {
        for (Group group : getMachineGroups()) {
            if (group.getMembers().size() == Board.CONNECT) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates Q value by using the formula given in the task-specification.
     *
     * @return Calculated P value for groups.
     */
    public int calculateValue() {

        // List with groups of human player.
        List<Group> groupsHuman = getHumanGroups();

        // List with groups of bot.
        List<Group> groupsMachine = getMachineGroups();
        int result = 0;

        result += 50 + countGroups(2, groupsMachine);
        result += 4 * countGroups(3, groupsMachine);
        result += 5000 * countGroups(4, groupsMachine);

        result -= countGroups(2, groupsHuman);
        result -= 4 * countGroups(3, groupsHuman);
        result -= 500000 * countGroups(4, groupsHuman);

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupManager clone() {
        GroupManager copy;
        try {
            copy = (GroupManager) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Error(ex);
        }

        copy.groupsOfPlayer1 = deepCopyGroupList(groupsOfPlayer1);
        copy.groupsOfPlayer2 = deepCopyGroupList(groupsOfPlayer2);

        return copy;
    }

    private void checkGroups(Checker checker, List<Coordinates2D> neighbours,
                             GroupType type, List<Group> allGroups) {
        //get all groups with certain group-type
        List<Group> groups =
                allGroups.stream()
                .filter(g -> g.getType() == type).collect(Collectors.toList());

        if (groups.size() > 0) {
            for (Coordinates2D neighbour : neighbours) {
                boolean noGroup = true;
                for (Group currGroup : groups) {
                    //is neighbour already in a group?
                    if (currGroup.hasMember(neighbour)
                            && !currGroup.hasMember(checker.getPosition())) {

                        currGroup.addMember(checker.getPosition());

                        // detect winner and save witness
                        if (currGroup.getMembers().size() == 4) {
                            Player winner = checker.getOwner();
                            winner.setWinner(true);
                            winner.setWitness(currGroup.getMembers());
                        }
                        noGroup = false;
                    }
                }
                // if neighbour is not in a group, a new one is created
                if (noGroup) {
                    allGroups.add(new Group(checker.getPosition(),
                            neighbour, type));
                }
            }
        } else {
            allGroups.add(createNewGroup(checker, neighbours, type));
        }
    }

    private Group createNewGroup(Checker checker,
                                 List<Coordinates2D> neighbours,
                                 GroupType type) {

        Group newGroup = new Group(checker.getPosition(),
                neighbours.get(0), type);

        if (neighbours.size() > 1) {
            for (int i = 1; i < neighbours.size(); i++) {
                newGroup.addMember(neighbours.get(i));
            }
        }
        return newGroup;
    }

    private int countGroups(int memberSize, List<Group> groups) {
        if (groups.size() > 0) {
            return (int) groups.stream()
                    .filter(g -> g.getMembers().size() == memberSize)
                    .count();
        } else {
            return 0;
        }
    }

    private List<Group> deepCopyGroupList(List<Group> list) {
        List<Group> clone = new ArrayList<>();

        for (Group group : list) {
            clone.add(group.clone());
        }

        return clone;
    }

    private List<Group> getMachineGroups() {
        if (players[0].isMachine()) {
            return groupsOfPlayer1;
        }

        if (players[1].isMachine()) {
            return groupsOfPlayer2;
        }

        return null;
    }

    private List<Group> getHumanGroups() {
        if (!players[0].isMachine()) {
            return groupsOfPlayer1;
        } else {
            return groupsOfPlayer2;
        }
    }
}
