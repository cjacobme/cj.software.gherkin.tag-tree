Feature: Tags only at a scenario

  @OnlyAtScenario
  Scenario: the one with tags
    When the tags are analyzed
    Then this feature has 1 tags