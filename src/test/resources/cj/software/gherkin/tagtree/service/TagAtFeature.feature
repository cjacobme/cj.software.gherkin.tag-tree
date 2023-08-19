@FeatureTag
Feature: tag at a feature

  Scenario: untagged scenario in tagged feature
    When the tags are analyzed
    Then this feature has 0 tags

  @TagTag
  Scenario: tagged scenario in tagged feature
    When the tags are analyzed
    Then this feature has 1 tags