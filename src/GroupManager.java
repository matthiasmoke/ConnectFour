import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages groups for a ConnectFour game
 */
public class GroupManager implements Cloneable {

    private List<Group> groupsOfPlayer1 = new ArrayList<>();
    private List<Group> groupsOfPlayer2 = new ArrayList<>();
    private Player[] players = new Player[2];

    /**
     * Creates a new GroupManager for two players.
     *
     * @param player1 Player one.
     * @param player2 Player two.
     */
    public GroupManager(Player player1, Player player2) {
        players[0] = player1;
        players[1] = player2;
    }

    /**
     * Checks if the neighbours of a checker are in a group with certain type.
     *
     * @param checker Checker that neighbours belong to.
     * @param neighbours Neighbours of the given checker.
     * @param type Group type to check for.
     */
    public void check(Checker checker, List<Coordinates2D> neighbours,
                      GroupType type) {

        if (neighbours.size() > 0) {

            // Determine to whom the given checker belongs to and calls
            // checkGroups() with fitting parameters
            if (checker.getOwner().equals(players[0])) {
                checkGroups(checker, neighbours, type, groupsOfPlayer1);
            } else {
                checkGroups(checker, neighbours, type, groupsOfPlayer2);
            }
        }
    }

    /**
     * Checks if a machine win is possible.
     *
     * @return True if machine is able to win.
     */
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

    /**
     * Checks if the neighbours of a checker are in a group with certain type.
     *
     * @param checker Checker that neighbours belong to.
     * @param neighbours Neighbours of the checker.
     * @param type Type of group to check for.
     * @param allGroups Existing groups of certain player.
     */
    private void checkGroups(Checker checker, List<Coordinates2D> neighbours,
                             GroupType type, List<Group> allGroups) {
        //get all groups with certain group-type
        List<Group> groups =
                allGroups.stream()
                .filter(g -> g.getType() == type).collect(Collectors.toList());

        if (groups.size() > 0) {
            boolean noGroup = true;

            for (Group currGroup : groups) {

                int iterator = 0;
                boolean iterate = true;
                while (iterator < neighbours.size() && iterate) {

                    //is one of the neighbours in a group?
                    if (currGroup.hasMember(neighbours.get(iterator))) {

                        //add all to the existing group
                        neighbours.add(checker.getPosition());
                        currGroup.addMembers(neighbours);

                        // detect winner and save witness
                        if (currGroup.getMembers().size() == 4) {
                            Player winner = checker.getOwner();
                            //winner.setWinner(true);
                            //winner.setWitness(currGroup.getMembers());
                        }

                        iterate = false; // stop iterating
                        noGroup = false; // group found
                    } else {
                        iterator++;
                    }
                }
            }

            // if neighbours are not in a group, a new one is created
            if (noGroup) {
                neighbours.add(checker.getPosition());
                allGroups.add(new Group(neighbours, type));
            }

        } else {
            neighbours.add(checker.getPosition());
            allGroups.add(new Group(neighbours, type));
        }
    }

    /**
     * Count number of groups with certain member size.
     *
     * @param memberSize Size of groups to count.
     * @param groups Groups of a certain player to count.
     * @return Number of groups that were count.
     */
    private int countGroups(int memberSize, List<Group> groups) {
        if (groups.size() > 0) {
            return (int) groups.stream()
                    .filter(g -> g.getMembers().size() == memberSize)
                    .count();
        } else {
            return 0;
        }
    }

    /**
     * Deep copies a group list.
     *
     * @param list List to clone.
     * @return Clone of the list.
     */
    private List<Group> deepCopyGroupList(List<Group> list) {
        List<Group> clone = new ArrayList<>();

        for (Group group : list) {
            clone.add(group.clone());
        }
        return clone;
    }

    /**
     * Get the list with groups of machine.
     *
     * @return List with machine groups.
     */
    private List<Group> getMachineGroups() {
        if (players[0].isMachine()) {
            return groupsOfPlayer1;
        } else {
            return groupsOfPlayer2;
        }
    }

    /**
     * Get the list with groups of human.
     *
     * @return List with human groups.
     */
    private List<Group> getHumanGroups() {
        if (!players[0].isMachine()) {
            return groupsOfPlayer1;
        } else {
            return groupsOfPlayer2;
        }
    }
}
