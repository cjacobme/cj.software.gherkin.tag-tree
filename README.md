# Gherkin-Tree
## Creates a tree of @Tags of Gherkin files

The Gherkin language contains **scenarios** and **features**. Each scenario can have
0 to many features. Scenarios and features can be marked with a tag. If a tag is assigned
to a feature, it is also assigned to all scenarios of that feature.

This project shall create a tree structure for selected tags.

If a tag is assigned to a one or more scenarios, these are the leaves of the tag node.
The node hierarchy would be `scenario tag` → `0..N scenario`. 

If a tag is assigned to one or more features, all scenarios of the selected features are
added to the feature's tag. The node hierarchy would be `feature tag` → `0..N Features`
→ `0..N scenario tags` → `0..N scenarios`.

Very important: each scenario may be contained in the tree only once.