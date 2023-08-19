package cj.software.gherkin.tagtree.entity;

public class CoordinateBuilder extends Coordinate.Builder{
    public CoordinateBuilder() {
        super.withFeature("sample feature").withScenario("sample scenario in sample feature");
    }
}
