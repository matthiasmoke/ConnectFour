import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupManager {

    private List<Group> groupsPlayer1 = new LinkedList<>();
    private List<Group> groupsPlayer2 = new LinkedList<>();
    Player[] players = new Player[2];

    public GroupManager(Player player1, Player player2) {
        players[0] = player1;
        players[1] = player2;
    }

    public void check(Checker current, List<Checker> neighbours,
                      GroupType type) {
        if (current.getOwner().equals(players[0])) {
            checkGroups(current, neighbours, type, groupsPlayer1);
        } else {
            checkGroups(current, neighbours, type, groupsPlayer2);
        }
    }

    private void checkGroups(Checker checker, List<Checker> neighbours,
                              GroupType type, List<Group> allGroups) {
        //get all groups with certain group-type
        List<Group> groups =
                allGroups.stream()
                .filter(g -> g.getType() == type).collect(Collectors.toList());

        for (Checker neighbour : neighbours) {
            boolean noGroup = true;
            for (Group currGroup : groups) {
                //is neighbour already in a group?
                if (currGroup.hasMember(neighbour)
                        && !currGroup.hasMember(checker)) {

                    currGroup.addMember(checker.getPosition());
                    noGroup = false;
                }
            }
            // if neighbour is not in a group, a new one is created
            if (noGroup) {
                allGroups.add(new Group(checker.getPosition(),
                        neighbour.getPosition(), type));
            }
        }
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

    public int calculateGroupValue() {
        return 0;
    }
}
