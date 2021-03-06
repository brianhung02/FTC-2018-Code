/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;



import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

/**
 * This file provides basic Telop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Mecanum", group="Pushbot")
//@Disabled
public class Mecanum extends OpMode {

    /* Declare OpMode members. */
    HardwarePushbot_Rachel robot       = new HardwarePushbot_Rachel(); // use the class created to define a Pushbot's hardware
                                                         // could also use HardwarePushbotMatrix class.

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        //get the values from the gamepads
        // Use gamepad 2 left and right stuck to drive robot
        double Speed = -gamepad2.left_stick_y;
        double Turn = gamepad2.left_stick_x;
        double Strafe = -gamepad2.right_stick_x;
        double MAX_SPEED = 1.0;
        holonomic(Speed, Turn, Strafe, MAX_SPEED );
    }

    public void holonomic(double Speed, double Turn, double Strafe, double MAX_SPEED) {

//      Left Front = +Speed + Turn - Strafe      Right Front = +Speed - Turn + Strafe
//      Left Rear  = +Speed + Turn + Strafe      Right Rear  = +Speed - Turn - Strafe

        double Magnitude = Math.abs(Speed) + Math.abs(Turn) + Math.abs(Strafe);
        Magnitude = (Magnitude > 1) ? Magnitude : 1; //Set scaling to keep -1,+1 range


        robot.leftFrontMotor.setPower(Range.scale((scaleInput(Speed) + scaleInput(Turn) - scaleInput(Strafe)),
                -Magnitude, +Magnitude, -MAX_SPEED, +MAX_SPEED));
        if (robot.leftRearMotor != null) {
            robot.leftRearMotor.setPower(Range.scale((scaleInput(Speed) + scaleInput(Turn) + scaleInput(Strafe)),
                    -Magnitude, +Magnitude, -MAX_SPEED, +MAX_SPEED));
        }
        robot.rightFrontMotor.setPower(Range.scale((scaleInput(Speed) - scaleInput(Turn) + scaleInput(Strafe)),
                -Magnitude, +Magnitude, -MAX_SPEED, +MAX_SPEED));
        if (robot.rightRearMotor != null) {
            robot.rightRearMotor.setPower(Range.scale((scaleInput(Speed) - scaleInput(Turn) - scaleInput(Strafe)),
                    -Magnitude, +Magnitude, -MAX_SPEED, +MAX_SPEED));
        }


        if (gamepad2.a){
            robot.leftClawServo.setPosition(1.0);
            robot.rightClawServo.setPosition(0.0);
        }else if (gamepad2.y){
            robot.leftClawServo.setPosition(0.0);
            robot.rightClawServo.setPosition(1.0);
        }

        if (gamepad1.b){
            robot.jewelArmServo.setPosition(0);
        }else if (gamepad1.x){
            robot.jewelArmServo.setPosition(1);
        }

        if (gamepad1.right_trigger>0.2){
            robot.armMotor.setPower(1);

        }else if (gamepad1.left_trigger>0.2){
            robot.armMotor.setPower(-1);
        }

        if(gamepad1.left_bumper){
            robot.relicTurn.setPosition(1);
        }else if(gamepad1.right_bumper){
            robot.relicTurn.setPosition(0);
        }

        if (gamepad2.right_trigger>0.2){
            robot.sliderMotor.setPower(1);

        }else if (gamepad2.left_trigger>0.2){
            robot.sliderMotor.setPower(-1);
        }

        if (gamepad1.a){
            robot.relicGrab.setPosition(1);

        }else if (gamepad1.y){
            robot.relicGrab.setPosition(0);
        }
    }
    double scaleInput(double dVal){
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

    @Override
    public void stop() {
    }
}
