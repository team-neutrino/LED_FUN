// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel;

public class LEDSubsystem extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */

  private AddressableLED m_led = new AddressableLED(0);
  private AddressableLEDBuffer m_ledBuffer = new AddressableLEDBuffer(19);
  private XboxController m_driverxboxController;
  private DigitalInput m_beambreak = new DigitalInput(1);
  private CANSparkMax m_motor = new CANSparkMax()

  private int m_movement = 0;
  private int m_movement2 = 0;

  /**
   * Example command factory method.
   *
   * @return a command
   */
  public Command exampleMethodCommand() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          /* one-time action goes here */
        });
  }

  public LEDSubsystem(CommandXboxController p_XboxDriverController) {
    m_driverxboxController = p_XboxDriverController.getHID();
    m_led.setLength(m_ledBuffer.getLength());
    m_led.setData(m_ledBuffer);
    m_led.start();
    for (var i = 0; i < (m_ledBuffer.getLength()); i++) {
      m_ledBuffer.setHSV(i, 0, 255, 255);
    }
    m_led.setData(m_ledBuffer);
    System.out.println("ON");
  }

  /**
   * An example method querying a boolean state of the subsystem (for example, a
   * digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  public void setLEDLights() {
    // This sets the each LED of the string to make it rainbow
    for (var i = 0; i < (m_ledBuffer.getLength()); i++) {
      m_ledBuffer.setHSV(i, (i * m_ledBuffer.getLength() + m_movement) + 1000000, 255, 255);
    }
    System.out.println(m_movement);
    m_led.setData(m_ledBuffer);
    if (m_movement <= -900000 || m_movement >= 900000) {
      m_movement = 0;
    }
  }

  public void moveLEDs() {
    // Moves the LEDs left with left bumper and right with right bumper
    if (m_driverxboxController.getLeftBumper() == true) {
      m_movement += m_ledBuffer.getLength();
    } else if (m_driverxboxController.getRightBumper() == true) {
      m_movement -= m_ledBuffer.getLength();
    }
  }

  @Override
  public void periodic() {
    // This makes the LED go slower if the beambreak is not broken
    if (m_beambreak.get() == false) {
      m_movement2 = 0;
    }
    if (m_movement2 % 2 == 0) {
      moveLEDs();
      setLEDLights();
    }
    if (m_movement2 >= 2) {
      m_movement2 = 0;
    }
    m_movement2 += 1;
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
