package callete.api.services.impl.simulator;

import callete.api.services.gpio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

/**
 * A Swing based simulator for visualizing GPIO states and
 * the status of the music playback.
 */
public class Simulator {

  private static Simulator instance;
  private JPanel pinPanel;
  private JPanel inputPanel;

  public static Simulator getInstance() {
    if(instance == null) {
      instance = new Simulator();
    }
    return instance;
  }

  public void show() {
    JFrame frame = new JFrame();
    frame.setTitle("Simulator");
    frame.setSize(500, 400);

    //center window
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(20, dim.height/2-frame.getSize().height/2);
    frame.setLayout(new BorderLayout(5, 5));

    // on close window the close method is called
    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        System.exit(0);
      }
    });

    JPanel center = new JPanel();
    center.setLayout(new GridLayout(3,1));
    frame.add(center, BorderLayout.CENTER);

    pinPanel = new JPanel(new FlowLayout());
    pinPanel.setBorder(BorderFactory.createTitledBorder("Pins"));
    center.add(pinPanel);

    inputPanel = new JPanel(new FlowLayout());
    inputPanel.setBorder(BorderFactory.createTitledBorder("Inputs"));
    center.add(inputPanel);

    JPanel displayPanel = new JPanel(new FlowLayout());
    displayPanel.setBorder(BorderFactory.createTitledBorder("LCD"));
    center.add(displayPanel);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    JButton close = new JButton("Close");
    close.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    buttonPanel.add(close);

    frame.add(buttonPanel, BorderLayout.SOUTH);

    frame.setVisible(true);
  }

  public void addPin(DigitalOutputPin pin, boolean high) {
    JPanel pinWrapper = new JPanel(new GridLayout(2,1));
    pinWrapper.add(new JLabel("Pin " + pin.getPin()));
    final JSwitchBox jSwitchBox = new JSwitchBox("high", "low");
    jSwitchBox.setHigh(high);
    pin.addPinStateChangeListener(new PinStateChangeListener() {
      @Override
      public void pinStateChanged(PinStateChangeEvent event) {
        jSwitchBox.setHigh(event.getState().equals(PinState.HIGH));
      }
    });
    pinWrapper.add(jSwitchBox);
    pinPanel.add(pinWrapper);
  }

  /**
   * Adds a JButton for PushButton
   */
  public void addPushButton(PushButton pushButton) {
    final SimulatorPushButton simulatorPushButton = (SimulatorPushButton)pushButton;
    final JButton button = new JButton(pushButton.getName());
    inputPanel.add(button);
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        simulatorPushButton.push();
      }
    });
  }

  public void addRotaryEncoder(final SimulatorRotaryEncoder simulatorRotaryEncoder) {
    final JButton right = new JButton(simulatorRotaryEncoder.getName() + " >");
    inputPanel.add(right);
    right.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        simulatorRotaryEncoder.right();
      }
    });

    final JButton left = new JButton("< " + simulatorRotaryEncoder.getName());
    inputPanel.add(left);
    left.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        simulatorRotaryEncoder.left();
      }
    });

  }
}
