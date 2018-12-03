package testable.stories.plots;

import io.fries.ioc.annotations.Identified;
import io.fries.ioc.annotations.Register;

@Register
public class PredictablePlot implements Plot {

    private final String outcome;

    public PredictablePlot(@Identified("plot.outcome") final String outcome) {
        this.outcome = outcome;
    }

    @Override
    public String toString() {
        return "PredictablePlot('" + outcome + "')";
    }
}
