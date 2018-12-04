package testable.stories.protagonists;

import io.fries.ioc.annotations.Register;

@Register("knights.karadoc")
public class HeroicProtagonist implements Protagonist {

    @Override
    public String toString() {
        return "HeroicProtagonist";
    }
}
