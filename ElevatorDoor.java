import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;


public class ElevatorDoor extends JPanel {

	// Status of the door.
	public static final int OPENING 		= 1;
	public static final int CLOSING 		= -1;
	public static final int OPEN 			= 2;
	public static final int CLOSED 			= -2;
	
	// Reference to the elevator.
	private Elevator _elevator;
	// Animation thread.
	private ElevatorDoorAnimationThread _doorAnimation;
	// Closing Rectangle.
	private Rectangle _closingRectangle;
	// Status.
	private int _status = CLOSED; // Closed by default.
	
	/**
	 * Constructor.
	 * @param elevator
	 */
	public ElevatorDoor(Elevator elevator){
		
		// Base constructor.
		super();
		// Initialize.
		_elevator = elevator;
		// Initialize.
		initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize(){
		// Set the layout.
		setLayout(new FlowLayout());
		// Set size.
		setSize(_elevator.getElevatorDef().width, 100);
		// Set the color.
		setBackground(_elevator.getElevatorDef().secondaryColor);
		// Initialize the closing rectangle.
		_closingRectangle = new Rectangle(0, 0, 0, _elevator.getBuilding().getBuildingDef().floorDef.height);
		// Create animation thread.
		_doorAnimation = new ElevatorDoorAnimationThread(this);
		// Start the thread. Logic inside here takes care of the opening/closing/doing nothing.
		_doorAnimation.start();
	}
	
	/**
	 * Paint the component.
	 */
	public void paintComponent(Graphics g){
		// Draw the door (closing box).
		g.setColor(Color.BLACK);
		g.fillRect(_closingRectangle.x, _closingRectangle.y, _closingRectangle.width, _closingRectangle.height);
	}
	
	/**
	 * Called to start opening the door.
	 */
	public void open(){
		// Only do if not already doing one.
		if(_status == CLOSED){
			// Set the status.
			_status = OPENING;
		}
	}
	
	/**
	 * Called to start closing the door.
	 */
	public void close(){
		// Only do if not already doing one.
		if(_status == OPEN){
			// Set the status.
			_status = CLOSING;
		}
	}
	
	/**
	 * Update the closing rectangle.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void updateClosingRectangle(int x, int y, int width, int height){
		// Update the rectangle.
		_closingRectangle.setLocation(x, y);
		_closingRectangle.setSize(width, height);
		// Determine if we have finished opening or closing.
		if(width == 0){
			finishedClosing();
		} else if(width == _elevator.getElevatorDef().width){
			finishedOpening();
		}
		// Now repaint.
		_elevator.getBuilding().repaint();
	}
	
	/**
	 * Called when the door has finished opening.
	 */
	private void finishedOpening(){
		// Set status.
		_status = OPEN;
	}
	
	/**
	 * Called when the door has finished closing.
	 */
	private void finishedClosing(){
		// Set status.
		_status = CLOSED;
	}
	
	/**
	 * Getter for the closing rectangle.
	 * @return
	 */
	public Rectangle getClosingRectangle(){
		return _closingRectangle;
	}
	
	/**
	 * Getter for the status.
	 * @return
	 */
	public int getStatus(){
		return _status;
	}
	
	/**
	 * Getter for the reference to the elevator.
	 * @return
	 */
	public Elevator getElevator(){
		return _elevator;
	}
	
}
