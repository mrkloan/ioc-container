package testable.stories;

import io.fries.ioc.annotations.Identified;
import io.fries.ioc.annotations.Register;
import testable.stories.plots.Plot;
import testable.stories.protagonists.Protagonist;

@Register
public class FantasyStory implements Story {

    private final Plot plot;
    private final Protagonist protagonist;

    FantasyStory(
            @Identified("PredictablePlot") final Plot plot,
            @Identified("knights.perceval") final Protagonist protagonist
    ) {
        this.plot = plot;
        this.protagonist = protagonist;
    }

    @Override
    public String toString() {
        return "FantasyStory(" + plot + ", " + protagonist + ")";
    }
}
