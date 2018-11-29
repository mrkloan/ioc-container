package testable.stories;

import testable.stories.plots.Plot;
import testable.stories.protagonists.Protagonist;

public class FantasyStory implements Story {

    private final Plot plot;
    private final Protagonist protagonist;

    FantasyStory(final Plot plot, final Protagonist protagonist) {
        this.plot = plot;
        this.protagonist = protagonist;
    }

    @Override
    public String toString() {
        return "FantasyStory(" + plot + ", " + protagonist + ")";
    }
}
