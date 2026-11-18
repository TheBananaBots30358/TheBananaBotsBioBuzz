package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Red Basket Auto", group = "Red")
public class RedBasketAuto extends TwoStageBasketAuto {
    public RedBasketAuto() {
        super(true); // true = Red Alliance
    }
}