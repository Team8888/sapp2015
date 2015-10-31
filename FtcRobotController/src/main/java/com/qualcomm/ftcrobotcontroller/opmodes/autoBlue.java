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
public class autoBlue extends OpMode {

    DcMotorController.DeviceMode devMode;
    DcMotorController wheelController;
    DcMotor motorRight;
    DcMotor motorLeft;



    int numOpLoops = 1;
  boolean vertOnly = false;
  boolean crawlMode = false;
  float direction;
  float right;
  float left;
  double slowSpeed = 0.5;
  int state = 0;
  double forward1 =2;
  double turnLeft=forward1+2;
  double forward2=turnLeft+2;
  double turnRight=forward2+2;
  double forward3=turnRight+2;
  double turnLeft2=forward3+2;
  double forward4=turnLeft2+2;

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


  }

  /*
   * This method will be called repeatedly in a loop
   * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
   */
  @Override
  public void loop() {
    telemetry.addData("time run", getRuntime());

    switch (state){
      case 0:
        motorLeft.setPower(slowSpeed);
        motorRight.setPower(slowSpeed);
        if(getRuntime()==forward1)
          state++;
        break;
      case 1:
        motorRight.setPower(slowSpeed);
        motorLeft.setPower(0);
        if(getRuntime()==turnLeft)
          state++;
         break;
      case 2:
        motorLeft.setPower(slowSpeed);
        motorRight.setPower(slowSpeed);
        if(getRuntime()==forward2)
          state++;
        break;
      case 3:
        motorRight.setPower(0);
        motorLeft.setPower(slowSpeed);
        if(getRuntime()==turnRight)
          state++;
        break;
      case 4:
        motorLeft.setPower(slowSpeed);
        motorRight.setPower(slowSpeed);
        if(getRuntime()==forward3)
          state++;
        break;
      case 5:
        motorRight.setPower(slowSpeed);
        motorLeft.setPower(0);
        if(getRuntime()==turnLeft2)
          state++;
        break;
      case 6:
        motorLeft.setPower(slowSpeed);
        motorRight.setPower(slowSpeed);
        if(getRuntime()==forward4)
          state++;
        break;
      case 7:
        motorLeft.setPower(0);
        motorRight.setPower(0);
        break;
      default:
        break;
    }


  }

  // If the device is in either of these two modes, the op mode is allowed to write to the HW.
  private boolean allowedToWrite(){
    return (devMode == DcMotorController.DeviceMode.WRITE_ONLY);
  }
}
