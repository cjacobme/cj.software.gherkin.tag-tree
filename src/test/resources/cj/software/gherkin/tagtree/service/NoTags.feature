Feature: no tags

  Scenario: scenario without a tag
    When the tags are analyzed
    Then this feature has 0 tags
