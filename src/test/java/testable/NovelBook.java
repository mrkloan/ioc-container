package testable;

import io.fries.ioc.annotations.Identified;
import io.fries.ioc.annotations.Register;
import testable.stories.Story;

@Register
public class NovelBook implements Book {

    private final Story story;

    private NovelBook(@Identified("FantasyStory") final Story story) {
        this.story = story;
    }

    @Override
    public String toString() {
        return "NovelBook(" + story + ")";
    }
}
