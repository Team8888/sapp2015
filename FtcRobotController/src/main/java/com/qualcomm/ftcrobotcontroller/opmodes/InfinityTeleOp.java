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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class InfinityTeleOp extends OpMode {

  // position of the claw servo
  double dispenserPosition;

  // amount to change the claw servo position by
  double dispensorDelta = 0.01;

  // position of the wrist servo
  double grabberPosition;

  // amount to change the wrist servo position by
  double grabberDelta = 0.01;

  DcMotorController.DeviceMode devMode;
  DcMotorController wheelController;
  DcMotor motorRight;
  DcMotor motorLeft;
  DcMotor collector;
  DcMotor elevator;
  TouchSensor elDown;

  Servo dispenser;
  Servo grabber;

  int height;
  boolean goingDown;


  int numOpLoops = 1;

  /*
   * Code to run when the op mode is first enabled goes here
   * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
   */
  @Override
  public void init() {

    motorRight = hardwareMap.dcMotor.get("motor2");
    motorLeft = hardwareMap.dcMotor.get("motor1");
    dispenser = hardwareMap.servo.get("dispenser"); // channel 1
    grabber = hardwareMap.servo.get("grabber"); // channel 2
    collector = hardwareMap.dcMotor.get("collector");
    elevator = hardwareMap.dcMotor.get("elevator");
    elDown = hardwareMap.touchSensor.get("eldown");

    wheelController = hardwareMap.dcMotorController.get("motors1");
    devMode = DcMotorController.DeviceMode.WRITE_ONLY;

    motorRight.setDirection(DcMotor.Direction.REVERSE);
    collector.setDirection(DcMotor.Direction.REVERSE);
    elevator.setDirection(DcMotor.Direction.REVERSE);
    //motorLeft.setDirection(DcMotor.Direction.REVERSE);

    // set the mode
    // Nxt devices start up in "write" mode by default, so no need to switch device modes here.
    motorLeft.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    motorRight.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    elevator.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

    dispenserPosition = 0.6;
    grabberPosition = 0.5;
  }

  /*
   * This method will be called repeatedly in a loop
   * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
   */
  @Override
  public void loop() {

    // The op mode should only use "write" methods (setPower, setChannelMode, etc) while in
    // WRITE_ONLY mode or SWITCHING_TO_WRITE_MODE
    if (allowedToWrite()) {
    /*
     * Gamepad 1
     *
     * Gamepad 1 controls the motors via the left stick, and it controls the wrist/claw via the a,b,
     * x, y buttons
     */

      // ----------------- Driving -------------------
      // throttle:  left_stick_y ranges from -1 to 1, where -1 is full up,  and 1 is full down
      // direction: left_stick_x ranges from -1 to 1, where -1 is full left and 1 is full right
      float throttle = -gamepad1.left_stick_y;
      float direction = gamepad1.left_stick_x;
      float right = throttle - direction;
      float left = throttle + direction;

      // clip the right/left values so that the values never exceed +/- 1
      right = Range.clip(right, -1, 1);
      left = Range.clip(left, -1, 1);

      // scale the joystick value to make it easier to control
      // the robot more precisely at slower speeds.
      right = (float)scaleInput(right);
      left =  (float)scaleInput(left);

      // write the values to the motors
      motorRight.setPower(right);
      motorLeft.setPower(left);

      //---------- Dispenser -----------------

      if (gamepad2.right_bumper)
        dispenserPosition = 0.65;
      else
        dispenserPosition = 0.2;
      dispenser.setPosition(dispenserPosition);

      //--------- Grabber ------------------
      if (gamepad1.left_bumper)
        grabberPosition = 0.6;
      else
        grabberPosition = 0.0;
      grabber.setPosition(grabberPosition);

      //telemetry.addData("dispenser", dispenserPosition);
      //telemetry.addData("grabber", grabberPosition);

      //----------- Collector ----------------

      if (gamepad2.left_bumper)
        collector.setPower(1.0);
      else
        collector.setPower(0.0);

      //----------- Elevator -------------
      telemetry.addData("eldown", elDown.isPressed());
      int newHeight = 0;

      if (gamepad2.x){				//"x" button
        newHeight = 6000;
      }

      if (gamepad2.a){		//"a" button
        newHeight=16300;
      }

      if (gamepad2.b){		//"b" button
        newHeight=18200;
      }

      if (gamepad2.y){ //"y" button
        if(!(elDown.isPressed())){
          goingDown=true;
        }
      }

      if(goingDown){
        elevator.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        elevator.setPower(-1.0);
      } else if(newHeight > 0){ //button has been pressed
        elevator.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        elevator.setTargetPosition(newHeight);
        if(newHeight > height)
          elevator.setPower(1.0);
        else
          elevator.setPower(-1.0);
      }


      if(elDown.isPressed() && goingDown){
        elevator.setPower(0);
        elevator.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        goingDown=false;
      }

     // elevator.setPower(Range.clip(gamepad2.left_stick_y,-1.0,1.0));
      // telemetry.addData("el position", elevator.getCurrentPosition());


      // update the position of the wrist
      /*
      if (gamepad1.a) {
        wristPosition -= wristDelta;
      }

      if (gamepad1.y) {
        wristPosition += wristDelta;
      }

      // update the position of the claw
      if (gamepad1.x) {
        clawPosition -= clawDelta;
      }

      if (gamepad1.b) {
        clawPosition += clawDelta;
      }

      // clip the position values so that they never exceed 0..1
      wristPosition = Range.clip(wristPosition, 0, 1);
      clawPosition = Range.clip(clawPosition, 0, 1);

      // write position values to the wrist and claw servo
      wrist.setPosition(wristPosition);
      claw.setPosition(clawPosition);
      */


      // To read any values from the NXT controllers, we need to switch into READ_ONLY mode.
      // It takes time for the hardware to switch, so you can't switch modes within one loop of the
      // op mode. Every 17th loop, this op mode switches to READ_ONLY mode, and gets the current power.
      //   if (numOpLoops % 17 == 0){
      // Note: If you are using the NxtDcMotorController, you need to switch into "read" mode
      // before doing a read, and into "write" mode before doing a write. This is because
      // the NxtDcMotorController is on the I2C interface, and can only do one at a time. If you are
      // using the USBDcMotorController, there is no need to switch, because USB can handle reads
      // and writes without changing modes. The NxtDcMotorControllers start up in "write" mode.
      // This method does nothing on USB devices, but is needed on Nxt devices.
      //     wheelController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
      //   }

      // Every 17 loops, switch to read mode so we can read data from the NXT device.
      // Only necessary on NXT devices.
      // if (wheelController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY) {

      // Update the reads after some loops, when the command has successfully propagated through.
      // telemetry.addData("Text", "free flow text");
      //  telemetry.addData("left motor", motorLeft.getPower());
      //  telemetry.addData("right motor", motorRight.getPower());
      //  telemetry.addData("RunMode: ", motorLeft.getChannelMode().toString());

      // Only needed on Nxt devices, but not on USB devices
      // wheelController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);

      // Reset the loop
      //  numOpLoops = 0;
      // }

      // Update the current devMode
      //  devMode = wheelController.getMotorControllerDeviceMode();
      //  numOpLoops++;
    }
  }

  // If the device is in either of these two modes, the op mode is allowed to write to the HW.
  private boolean allowedToWrite(){
    return (devMode == DcMotorController.DeviceMode.WRITE_ONLY);
  }

  /*
	 * This method scales the joystick input so for low joystick values, the
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
  double scaleInput(double dVal)  {
    double[] scaleArray = { 0.0, 0.02, 0.05, 0.09, 0.12, 0.15, 0.18, 0.21, 0.24,
            0.30, 0.33, 0.36, 0.40, 0.43, 0.47, 0.50 };

    // get the corresponding index for the scaleInput array.
    int index = (int) (dVal * 16.0);
    if (index < 0) {
      index = -index;
    }
    if (index > 15) {
      index = 15;
    }

    double dScale = 0.0;
    if (dVal < 0) {
      dScale = -scaleArray[index];
    } else {
      dScale = scaleArray[index];
    }

    return dScale;
  }
}
