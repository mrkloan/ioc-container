package testable;

import io.fries.ioc.annotations.Configuration;
import io.fries.ioc.annotations.Register;

@Configuration
public class TestableApplication {

    @Register("plot.outcome")
    String plotOutcome() {
        return "Outcome";
    }

    @Register
    String otherOutcome() {
        return "Other outcome";
    }

    String nonRegisteredOutcome() {
        return "Meh.";
    }
}
