# Limelight Vision System (`LimelightSystem`)

This class handles AprilTag detection for BioBuzz using the Limelight 3A. It finds hive tags, filters by alliance color, checks if a hive is upright or tipped over, and gives `X`, `Y`, `Z`, and `Yaw` coordinates for Auto and TeleOp.

## FiducialResult & FiducialId
* **Fiducial:** Just another word for an AprilTag.
* **`FiducialResult`:** All the tracking info the Limelight collects for a single tag in view. If the camera sees multiple tags at once, it gives us a list of `FiducialResult` objects.
* **`FiducialId`:** The actual ID number on that tag (Red tags are `30–37` and Blue tags are `38–45`).

## Pipelines
A pipeline is a saved vision setup on the Limelight (configured in the Limelight web dashboard at `http://limelight.local:5801`). 

We only use **Pipeline 0 (`PIPELINE_ALL_TAGS`)**, which tracks all 16 BioBuzz AprilTags (`30–45`) at the same time. Instead of switching between separate Red and Blue pipelines on the camera during a match, we leave it on Pipeline 0 and filter out opponent tags directly in Java using the `Alliance` setting (`RED`, `BLUE`, or `ALL`). This avoids any delay from pipeline switching.

## How Tipped vs. Upright Works
To tell if a hive is knocked over, we look at the tag's roll angle (`rollDeg`) from the camera's perspective (`getTargetPoseCameraSpace()`), which works like turning a steering wheel from `-180°` to `+180°`:

* **Upright (`isUpright = true`, `isTipped = false`):** The tag is straight up, so `rollDeg` is close to `0°` (anything tilted **less than 90°**).
* **Tipped (`isTipped = true`, `isUpright = false`):** The hive flipped upside-down, so `rollDeg` is close to `+180°` or `-180°` (anything tilted **90° or more**).

`isUpright` and `isTipped` are always exact opposites—we included both so Auto and TeleOp can use whichever one reads cleaner in their `if` statements.

## How to Use It in Auto & TeleOp

```java
// 1. Initialize and Start (in init())
// Pass in Alliance.RED or Alliance.BLUE to ignore opponent tags (use Alliance.ALL for desk testing)
LimelightSystem vision = new LimelightSystem(hardwareMap, LimelightSystem.Alliance.RED);
vision.start();

// 2. Read Hive Data (in your loop)
LimelightSystem.HiveVisionData hive = vision.getHiveData();

if (hive.detected) {
    int id = hive.tagId;              // Tag ID (30-45)
    String color = hive.hiveColor;    // "RED" or "BLUE"
    boolean tipped = hive.isTipped;   // true if upside-down (>= 90 deg)

    // Robot position relative to the tag (inches & degrees)
    double strafeX = hive.xInches;    // Left/Right offset
    double heightY = hive.yInches;    // Up/Down offset
    double distZ   = hive.zInches;    // Straight-line distance
    double yaw     = hive.yawDegrees; // Angle offset (0 deg = lined up straight)

    if (hive.isTipped) {
        // Run tipped hive routine
    } else {
        // Run upright hive routine
    }
}

// 3. Telemetry & Stop
vision.addTelemetry(telemetry, hive);
telemetry.update();

// Call at the end of the OpMode:
vision.stop();
