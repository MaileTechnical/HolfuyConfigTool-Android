# Holfuy Upgrader Release Test Record

**App Release Name:** 1.0.0

**Test Data Tag:** test-data-v2

**Date Tested:** 2026-09-11 through 2026-09-17

**Tester:** John R. Wolfe

**Android Studio Version:** Android Studio Panda 4 | 2025.3.4 Patch 1

---

# Test Environment

## Test Devices

- After installing app to be tested, turn off USB Debugging and disable Developer Mode

| Device                | Android Version | Result | Notes |
| --------------------- | --------------- | ------ | ----- |
| Samsung Galaxy S21    | 15              | Pass   | All test cases executed on primary test device. |
| Samsung Galaxy S24    | 16              | Pass   | All compatibility test cases executed. |
| Samsung Galaxy Tab A7 | 12              | Pass   | All compatibility test cases executed. |
| Air3                  | 8.1             | Pass   | All compatibility test cases executed.  #20, #22, #23 discovered. |

---

## Test Equipment

| Item                   | Notes |
| ---------------------- | ----- |
| Holfuy weather station | ID: 1380 |
| USB OTG adapter        | Not used since all test devices have native OTG capability. |
| Firmware image         | V10.06, V11.04, SIM_LTE_latest |


---

# Test Results

| Test Case | Result | Notes |
| --------- | ------ | ----- |
| TC-001    |  Pass  |       |
| TC-002    |  Pass  |       |
| TC-003    |  Pass  |       |
| TC-004    |  Pass  |       |
| TC-005    |  Pass  |       |
| TC-006    |  Pass  |       |
| TC-007    |  Pass  |       |
| TC-008    |  Pass  |       |
| TC-009    |  Fail  | Passed on all devices except Air3, where it fails only when Back is tapped.  See #23. |
| TC-010    |  Hist  |       |
| TC-011    |  Hist  |       |
| TC-012    |  Pass  |       |
| TC-013    |  Pass  |       |
| TC-014    |  Pass  |       |
| TC-015    |  Pass  |       |
| TC-016    |  Pass  |       |
| TC-017    |  Pass  |       |
| TC-018    |  Pass  |       |
| TC-019    |  Pass  |       |
| TC-020    |  Pass  |       |
| TC-021    |  Pass  |       |
| TC-022    |  Pass  |       |
| TC-023    |  Pass  |       |
| TC-024    |  Pass  |       |
| TC-025    |  Pass  |       |
| TC-026    |  Pass  |       |

"Hist" indicates a historical test case that was not executed for this release.

---

# Issues Found

- [#20](https://github.com/MaileTechnical/HolfuyConfigTool-Android/issues/20) - Resolved, test restarted.
- [#22](https://github.com/MaileTechnical/HolfuyConfigTool-Android/issues/22) - Resolved, test restarted.
- [#23](https://github.com/MaileTechnical/HolfuyConfigTool-Android/issues/23) - Resolution exists in a [branch](https://github.com/johnrwolfe/HolfuyConfigTool-Android/tree/issue/23-Air3_TC-009_invalid_update_status) and will be included in a future release.

---

# Release Decision

Approved for release with known issues:

- [#23](https://github.com/MaileTechnical/HolfuyConfigTool-Android/issues/23)

---

# Notes

Discovered #20 and #22 while running tests requiring manifest-override on the Air3.  
Restarted the test after fixing each #20 and #22.
Discovered #23 while running TC-009 on the Air3 and determined it was not a release-blocking issue.

