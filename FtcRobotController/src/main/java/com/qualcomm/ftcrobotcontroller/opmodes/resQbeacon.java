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
//

////
package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.util.Range;//

/**
 * Autonomous Mode
 * detect blue light and move towards it
 * pixy camera, shows when sees blue, x coordinate of largest block
 * turn so x block in center
 * drive forward
 * keep centered as driving, adjust center
 * go to touch sensor
 */
public class resQbeacon extends OpMode {

    DcMotorController.DeviceMode devMode;
    DcMotorController wheelController;
    DcMotor motorRight;
    DcMotor motorLeft;
    DigitalChannel colorTrue;
    AnalogInput pixyX;


    int numOpLoops = 1;

    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {

        motorRight = hardwareMap.dcMotor.get("motor2");
        motorLeft = hardwareMap.dcMotor.get("motor1");
        colorTrue = hardwareMap.digitalChannel.get("PixyDigital");
        pixyX = hardwareMap.analogInput.get("PixyAnalog");
        colorTrue.setMode(DigitalChannelController.Mode.INPUT);

        motorRight.setDirection(DcMotor.Direction.REVERSE);

        double Rval = 0.0;
        double Lval = 0.0;

        wheelController = hardwareMap.dcMotorController.get("motorc");
        devMode = DcMotorController.DeviceMode.WRITE_ONLY;


    }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void loop() {

        double Rval = 0.0;
        double Lval = 0.0;

        if (colorTrue.getState()) {
            telemetry.addData("seesColor", "Color Detected");
            telemetry.addData("colorX", "X=" + pixyX.getValue());

            if (pixyX.getValue() > 297 && pixyX.getValue() < 397) {
                Rval = 0.005;
                Lval = 0.005;
                telemetry.addData("dir", "straight");
            } else if (pixyX.getValue() < 297) {
                Lval = -0.005;
                Rval = 0.005;
                telemetry.addData("dir", "right");
            } else {
                Lval = 0.005;
                Rval = -0.005;
                telemetry.addData("dir", "left");
            }

            motorLeft.setPower(Lval);
            motorRight.setPower(Rval);
        }


       /*x val=297-397
       if not then turn REALLY SLOWLY
       if >397 turn LEFT
       if <297 turn RIGHT
       while x val > 297 && x val < 397 move forward
*/
    }
}
