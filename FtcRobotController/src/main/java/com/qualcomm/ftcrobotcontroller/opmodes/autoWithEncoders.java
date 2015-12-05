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
public class autoWithEncoders extends OpMode {
    /*
    first, we will reset the encoders and set the target position.
    Then, We will turn on the motor and have them go foward for a certaint number of ticks.
    Then, the motors will stop for a for a cople of seconds.
    then, we will reset the encoders again.
    Then, we will turn right or left(depending on which allliance we are on).
    then, we will go strait again.

     */


    DcMotorController.DeviceMode devMode;
    DcMotorController wheelController;
    DcMotor motorRight;
    DcMotor motorLeft;

    DcMotor oneToUse;

    int state = 0;
    int line1;
    int currentPositionRight;
    int currentPositionLeft;
    int currentOneToUse;
    int Turning;
    boolean redOrBlue = false;//true means red and false means blue.
    int line2;





    public autoWithEncoders(boolean isRed){
        redOrBlue = isRed;
    }


    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {

        motorRight = hardwareMap.dcMotor.get("motor2");
        motorLeft = hardwareMap.dcMotor.get("motor1");


        motorRight.setDirection(DcMotor.Direction.REVERSE);


        wheelController = hardwareMap.dcMotorController.get("motorc");
        devMode = DcMotorController.DeviceMode.WRITE_ONLY;
        if (redOrBlue = true) {
            oneToUse = motorLeft;
        }
        else {
            oneToUse = motorRight;
        }

    }


    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void loop() {

        switch (state) {
            case 0:
                motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                state++;
                break;
            case 1:
                motorRight.setTargetPosition(setMyTarget(currentPositionRight, line1));
                motorLeft.setTargetPosition(setMyTarget(currentPositionLeft, line1));
                motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                motorRight.setPower(1.0);
                motorLeft.setPower(1.0);
                wheelController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
                state++;
                break;
            case 2:

                if (wheelController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY) {

                    currentPositionRight = motorRight.getCurrentPosition();
                    currentPositionLeft = motorLeft.getCurrentPosition();
                    if (currentPositionRight >= setMyTarget(currentPositionRight, line1) && currentPositionLeft >= setMyTarget(currentPositionRight, line1)) {
                        currentPositionLeft=motorLeft.getCurrentPosition();
                        currentPositionRight=motorRight.getCurrentPosition();
                        currentOneToUse=oneToUse.getCurrentPosition();
                        wheelController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
                        state++;

                    }
                }
                break;
            case 3:
                if (wheelController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.WRITE_ONLY) {
                    motorRight.setPower(0);
                    motorLeft.setPower(0);
                    state++;
                }
                break;
            case 4:


                oneToUse.setTargetPosition(setMyTarget(currentOneToUse, Turning));
                oneToUse.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                wheelController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
                oneToUse.setPower(1.0);
                state++;
                break;
            case 5:
                if ((wheelController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY)) {
                    currentPositionLeft = oneToUse.getCurrentPosition();
                    if (currentPositionLeft >= Turning) {
                        currentPositionLeft=motorLeft.getCurrentPosition();
                        currentPositionRight=motorRight.getCurrentPosition();
                        wheelController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
                        state++;
                    }
                }
                break;
            case 6:
                if (wheelController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.WRITE_ONLY) {
                    oneToUse.setPower(0);
                    state++;
                }
                break;
            case 7:
                motorRight.setTargetPosition(setMyTarget(currentPositionRight, line2));
                motorLeft.setTargetPosition(setMyTarget(currentPositionLeft, line2));
                motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                motorRight.setPower(1.0);
                motorLeft.setPower(1.0);
                wheelController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
                state++;
                break;
            case 8:
                if (wheelController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY) {
                    currentPositionRight = motorRight.getCurrentPosition();
                    currentPositionLeft = motorLeft.getCurrentPosition();
                    if (currentPositionRight >= line2 && currentPositionLeft >= line2) {

                        wheelController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
                        state++;
                    }
                }
                break;

            case 9:
                if (wheelController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY) {
                    motorRight.setPower(0);
                    motorLeft.setPower(0);
                    state++;
                }
                break;


            default:
                break;
        }


}


    private int setMyTarget(int current, int dist){
        int clickNum=dist*27;
        int total=clickNum+current;
        return total;
    }

}