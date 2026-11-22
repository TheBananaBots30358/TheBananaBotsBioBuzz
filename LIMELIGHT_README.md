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

More detail information : 
1. What is rollDeg?
Java
double rollDeg = tag.getTargetPoseCameraSpace().getOrientation().getRoll();
In 3D space, a target has three rotation angles relative to your camera:

Yaw: Turning left or right (shaking your head "no").

Pitch: Tilting forward or backward (nodding your head "yes").

Roll: Rotating clockwise or counterclockwise around the camera's line of sight (tilting your ear toward your shoulder, or turning a steering wheel).

rollDeg measures that steering-wheel rotation of the AprilTag in degrees, ranging from -180° to +180°.

Upright tag: The top of the AprilTag points straight up, so rollDeg is close to 0° (for example, -5° or +12° if the floor or camera is slightly uneven).

Upside-down (tipped) tag: If the hive flips completely upside down, the tag rotates 180° like a steering wheel, so rollDeg becomes close to +180° or -180°.

2. How the isTipped and isUpright Formulas Work
Java
data.isTipped = Math.abs(rollDeg) >= 90.0;
data.isUpright = !data.isTipped;
Math.abs(rollDeg) strips away the plus or minus sign so you only care how far the tag rotated from upright—whether it tipped clockwise (+175°) or counterclockwise (-175°), both become 175.0.

>= 90.0 sets the halfway threshold:

Between 0° and 89.9°: The tag is tilted less than halfway, so isTipped is false and isUpright is true.

Between 90.0° and 180.0°: The tag has rotated past sideways toward upside-down, so isTipped becomes true and isUpright becomes false.

Quick Real-World Check for Your Robot
Make sure your Limelight is mounted right-side up! If your team mounted the Limelight upside-down on the robot (and didn't flip the image in the Limelight web interface), every upright hive will read ~180° and appear "tipped."

If a hive tips sideways (90°) instead of completely upside-down (180°): Depending on how BioBuzz hives physically rest when tipped, you might want to test a tipped hive on the field and check what rollDeg prints in telemetry. If a tipped hive sits at 75° or 80°, you can lower the threshold from 90.0 to something like 60.0 so it still registers as tipped.

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
