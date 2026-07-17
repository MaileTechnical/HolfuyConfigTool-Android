# Coverage Analysis

This document explains the rationale behind the Holfuy Upgrader manual test
suite.

Rather than attempting to exercise every possible sequence of user actions, the
suite identifies behavioral variations that may occur during each supported
workflow and demonstrates that each distinct observable behavior is verified by
at least one test case.

Behaviorally equivalent situations are intentionally represented by a single
canonical test.

---

# Scope

This analysis applies to:

- Workflow: WF-001
- Application Session: Either, unless otherwise specified.

Additional workflows and application session states should be analyzed separately
as the application evolves.

---

# Behavioral Variations

Each behavioral variation is analyzed independently.

Matrix entries use the following notation.

| Entry | Meaning |
|-------|---------|
| TC-nnn | Canonical test exercising this behavior |
| =TC-nnn | Behavior equivalent to TC-nnn |
| N | Not meaningful |
| TBD | Analysis not yet complete |

---

## Unexpected Station Loss

Representative actions:

- Disconnect USB cable.
- Power off weather station.
- Allow bootloader timeout to expire.

Behavioral property:

The weather station unexpectedly becomes unavailable while the firmware update workflow
is in progress.

| Interruption Point | IP-1 | IP-2 | IP-3 | IP-4 | IP-5 | IP-6 | IP-7 |
|--------------------|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| Coverage | =TC-012 | =TC-012 | =TC-012 | =TC-012 | =TC-012 | TC-006 | N |

**Rationale**

Prior to firmware programming, all causes of unexpected station loss produce
the same observable application behavior. Android reports USB device removal,
the application transitions to the disconnected state, and the workflow may be
restarted after the station is restored.

Firmware programming is analyzed separately because interruption during flash
programming has unique consequences.

---

## Android Lifecycle

Representative actions:

- Rotate device.
- Home / Resume.
- Recents / Resume.
- Lock / Unlock.
- Screen timeout.
- Back.

| Interruption Point | IP-1 | IP-2 | IP-3 | IP-4 | IP-5 | IP-6 | IP-7 |
|--------------------|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| Coverage | =TC-009 | TC-008 | =TC-009 | TC-013 | =TC-009 | TC-009 | =TC-009 |

**Rationale**

Interruption points are grouped according to which component owns the active user interface.
When Holfuy Upgrader owns the UI (IPs 1, 3, 5, 6, and 7), Android lifecycle events are handled
uniformly by the application and are therefore covered by TC-009.

Waiting for USB permission (IP-2) and interacting with the Android document picker (IP-4) involve
externally managed UI with distinct lifecycle and navigation behavior, requiring separate canonical tests.

---

## Firmware Selection

### Cancel Firmware Selection

| Interruption Point | IP-1 | IP-2 | IP-3 | IP-4 | IP-5 | IP-6 | IP-7 |
|--------------------|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| Coverage | N | N | N | TC-004 | N | N | N |

---

### Replace Firmware Selection

| Interruption Point | IP-1 | IP-2 | IP-3 | IP-4 | IP-5 | IP-6 | IP-7 |
|--------------------|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| Coverage | N | N | N | N | TC-005 | N | N |

---

## USB Permission

| Interruption Point | IP-1 | IP-2 | IP-3 | IP-4 | IP-5 | IP-6 | IP-7 |
|--------------------|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| Coverage | N | TC-002 | N | N | N | N | N |

---

## Unsupported USB Device

| Interruption Point | IP-1 | IP-2 | IP-3 | IP-4 | IP-5 | IP-6 | IP-7 |
|--------------------|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| Coverage | TC-007 | N | N | N | N | N | N |