package testable.stories.protagonists;

public class FriendlyProtagonist implements Protagonist {

    private final Protagonist friend;

    public FriendlyProtagonist(final Protagonist friend) {
        this.friend = friend;
    }

    @Override
    public String toString() {
        return "FriendlyProtagonist(" + friend.getClass().getSimpleName() + ")";
    }
}
