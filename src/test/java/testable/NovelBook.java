package testable;

import testable.stories.Story;

public class NovelBook implements Book {

    private final Story story;

    private NovelBook(final Story story) {
        this.story = story;
    }

    @Override
    public String toString() {
        return "NovelBook(" + story + ")";
    }
}
