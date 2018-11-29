package testable.stories.plots;

public class PredictablePlot implements Plot {

    private final String outcome;

    public PredictablePlot(final String outcome) {
        this.outcome = outcome;
    }

    @Override
    public String toString() {
        return "PredictablePlot('" + outcome + "')";
    }
}
