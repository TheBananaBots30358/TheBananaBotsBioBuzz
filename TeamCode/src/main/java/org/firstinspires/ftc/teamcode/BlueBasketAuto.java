package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Blue Basket Auto", group = "Blue")
public class BlueBasketAuto extends TwoStageBasketAuto {
    public BlueBasketAuto() {
        super(false); // false = Blue Alliance
    }
}