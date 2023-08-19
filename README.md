# Gherkin-Tree
## Creates a tree of @Tags of Gherkin files

The Gherkin language contains **scenarios** and **features**. Each scenario can have
0 to many features. Scenarios and features can be marked with a tag. If a tag is assigned
to a feature, it is also assigned to all scenarios of that feature.

The purpose of this project is to create a new relationship between tags and its
associated scenarios, no matter where the tag is placed at.

One key class of this project is the `GherkinTreeParser` and its method `parse(String, InputStream)`.
This parser parses a feature file and returns an object graph of

```tree
├── ParsedFeature
│   ├── name: String
│   ├── tags: 0..N Strings
│   ├── scenario: 0..N of ParsedScenario
│   │   ├── name: String
│   │   ├── tags
```

Have a look at the classes 
[ParsedFeature.java](src/main/java/cj/software/gherkin/tagtree/entity/ParsedFeature.java) and
[ParsedScenario.java](src/main/java/cj/software/gherkin/tagtree/entity/ParsedScenario.java)

The other key feature of this project is the class `Converter` and its method `parse`. It takes a 
`Collection<ParsedFeature>` and returns a `SortedMap<String, SortedSet<Coordinate>>`, where `Coordinate`
simply is the tuple of feature and scenario name. With the help of this map, one can simply inspect
all scenarios that are reachable by any tag.



