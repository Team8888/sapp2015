/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class encoderTest extends OpMode {

    /*
    we want to be able to move the wheel and see the number of clicks on the screen.
     */

    DcMotorController.DeviceMode devMode;
    DcMotorController wheelController;
    DcMotor motorRight;
    DcMotor motorLeft;

    int currentPositionRight;
    int currentPositionLeft;


    autoWithEncoders ourbot;

    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
        motorRight = hardwareMap.dcMotor.get("motor2");
        motorLeft = hardwareMap.dcMotor.get("motor1");

        wheelController = hardwareMap.dcMotorController.get("motorc");

        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);




    }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void loop() {
     if(wheelController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.WRITE_ONLY) {
         motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
         motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
     }
        wheelController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);

        if (wheelController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY)

            currentPositionRight = motorRight.getCurrentPosition();
            currentPositionLeft = motorLeft.getCurrentPosition();

            telemetry.addData("left clicks: ", currentPositionLeft);
            telemetry.addData("Right clicks: " , currentPositionRight);

    }
}
