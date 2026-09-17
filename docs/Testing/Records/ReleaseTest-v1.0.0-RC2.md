# Holfuy Upgrader Release Test Record

**App Release Name:** 1.0.0

**Test Data Tag:** test-data-v2

**Date Tested:** 2026-09-xx

**Tester:** John R. Wolfe

**Android Studio Version:** Android Studio Panda 4 | 2025.3.4 Patch 1

---

# Test Environment

## Test Devices

- After installing app to be tested, turn off USB Debugging and disable Developer Mode

| Device                | Android Version | Result | Notes |
| --------------------- | --------------- | ------ | ----- |
| Samsung Galaxy S21    | 15              |        |       |
| Samsung Galaxy S24    | 16              |        |       |
| Samsung Galaxy Tab A7 | 12              |        |       |
| Air3                  | 8.1             |        |       |

---

## Test Equipment

| Item                   | Notes |
| ---------------------- | ----- |
| Holfuy weather station |       |
| USB OTG adapter        |       |
| Firmware image         |       |

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
| TC-016    |        |       |
| TC-017    |        |       |
| TC-018    |        |       |
| TC-019    |        |       |
| TC-020    |        |       |
| TC-021    |  Pass  |       |
| TC-022    |  Pass  |       |
| TC-023    |        |       |
| TC-024    |  Pass  |       |
| TC-025    |        |       |
| TC-026    |        |       |

"Hist" indicates a historical test case that was not executed for this release.

---

# Compatibility Summary

| Android Version | Status | Notes |
| --------------- | ------ | ----- |
| 8.1             |        |       |
| 12              |        |       |
| 15              |        |       |
| 16              |        |       |

---

# Issues Found

- #20 - Resolved, test restarted.
- #22 - Resolved, test restarted.
- #23 - Resolution exists in a branch and will be included in a future release.

---

# Release Decision

Approved for release

Approved with known issues:

- #23

Release blocked

---

# Notes

Discovered #20 and #22 while running tests requiring manifest-override on the Air3.  
Restarted the test after fixing each issue.

