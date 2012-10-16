import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;


public class Elevator extends JPanel {

	// Definition.
	private ElevatorDef _def;
	// Reference to building.
	private Building _building;
	// ID.
	private int _positionId;
	// Mover thread.
	private ElevatorMovementThread _mover;
	// Door.
	private ElevatorDoor _elevatorDoor;
	// Reference to current floor at.
	private Floor _floorAt;
	// Reference to the floor destination.
	private Floor _floorDestination;
	// Queue of requests to be served.
	private LinkedList<ElevatorRequest> _elevatorRequests = new LinkedList<ElevatorRequest>();
	// Requests text.
	private JTextArea _requestsLog = new JTextArea();
	
	/**
	 * Constructor.
	 * @param def
	 * @param building
	 * @param id
	 */
	public Elevator(ElevatorDef def, Building building, int positionId){
		// Base constructor.
		super();
		// Initialize.
		_def = def;
		_building = building;
		_positionId = positionId;
		// Initialize the elevator.
		initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize(){
		// Set layout.
		setLayout(new BorderLayout());
		// Setup the size.
		setSize(_def.width, _building.getBuildingDef().floorDef.height);
		// Position the floor.
		setLocation(determinePosition());
		// Set the color.
		setBackground(_def.primaryColor);
		// Create request buttons.
		createRequestButtons();
		// Create door.
		createDoor();
		// Call initial moveTo (Just so floorAt, and perhaps other things can get updated).
		moveTo(getY());
		// Create the mover thread.
		_mover = new ElevatorMovementThread(this);
		_mover.start(); // Start and handle all logic in here.
		// Create the text area of requests.
		add(_requestsLog, BorderLayout.SOUTH);
	}
	
	/**
	 * Create the request buttons.
	 */
	private void createRequestButtons(){
		// Create panel.
		JPanel panelRequestButtons = new JPanel();
		panelRequestButtons.setBackground(_def.secondaryColor);
		// Determine grid layout variables.
		int dimension = (int) Math.ceil(Math.sqrt((double) _building.getBuildingDef().numFloors));
		// Set the grid layout.
		panelRequestButtons.setLayout(new GridLayout(dimension, dimension, 2, 2));
		// Create buttons inside elevator.
		for(int i=0; i<_building.getBuildingDef().numFloors; i++){
			// Create the button.
			JButton floorNumberButton = new JButton("" + i);
			// Add action listener.
			floorNumberButton.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						// Get the text in the button.
						String text = ((JButton) e.getSource()).getText();
						int floorNumber = Integer.parseInt(text);
						// Add a request.
						addRequest(new ElevatorRequest(_building.getFloorAtLevel(floorNumber)));
					}
				}
			);
			// Display.
			panelRequestButtons.add(floorNumberButton);
		}
		// Display panel.
		add(panelRequestButtons, BorderLayout.NORTH);
	}
	
	/**
	 * Create the door of the elevator.
	 */
	private void createDoor(){
		// Create the door.
		_elevatorDoor = new ElevatorDoor(this);
		// Add to display.
		add(_elevatorDoor, BorderLayout.CENTER);
	}
	
	/**
	 * Determine the position of where this elevator should go.
	 * All the elevators are aligned so any amount of elevators can fit.
	 * @return
	 */
	private Point determinePosition(){
		// Define the point.
		Point point = new Point(0, 0);
		// Determine new x position.
		int x = (_building.getBuildingDef().width / 2) - (_def.width / 2) + (_positionId * (_def.width + _def.spacing));
		// Move the point to the new x position.
		point.move(x, 0);
		// Return the point.
		return point;
	}
	
	/**
	 * Called to serve the next request.
	 * Only call if there is no destination.
	 */
	public void serveNextRequest(){
		// Make sure we already don't have a destination.
		if(_floorDestination != null)
			return;
		// Determine if we have any elevator requests remaining.
		if(_elevatorRequests.size() > 0){
			// Get the next elevator request.
			ElevatorRequest elevatorRequest = removeRequest();
			// If there is a request, go to it's floor.
			if(elevatorRequest != null){
				goToFloor(elevatorRequest.getFloorTo());
			}
		} else {
			// Since there are no elevator requests, check for floor requests.
			if(_building.getElevatorSystem().getFloorRequests().size() > 0){
				// Get the next floor request.
				FloorRequest floorRequest = _building.getElevatorSystem().getFloorRequests().poll();
				// If there is a floor request, go to it.
				if(floorRequest != null){
					goToFloor(floorRequest.getFloorFrom());
				}
			} else {
				// There are no elevator requests and no floor requests.
			}
		}
	}
	
	/**
	 * Add a new elevator request.
	 * @param elevatorRequest
	 */
	public void addRequest(ElevatorRequest elevatorRequest){
		// Default boolean.
		boolean isDuplicate = false;
		// Make sure there is no duplicate.
		Iterator<ElevatorRequest> iterator = _elevatorRequests.iterator();
		while(iterator.hasNext()){
			if(iterator.next().getFloorTo() == elevatorRequest.getFloorTo()){
				isDuplicate = true;
				break;
			}
		}
		// Add to list (if not duplicate).
		if(!isDuplicate){
			// Add the request.
			_elevatorRequests.add(elevatorRequest);
			// Update the log.
			updateLog();
		}
	}
	
	/**
	 * Remove the request.
	 * @param elevatorRequest
	 */
	private void removeRequest(ElevatorRequest elevatorRequest){
		// Remove from list.
		_elevatorRequests.remove(elevatorRequest);
		updateLog();
	}
	
	/**
	 * Remove the first elevator request.
	 */
	private ElevatorRequest removeRequest(){
		// Remove first.
		ElevatorRequest elevatorRequest = _elevatorRequests.poll();
		updateLog();
		// Return the request.
		return elevatorRequest;
	}
	
	/**
	 * Called to update the log.
	 */
	private void updateLog(){
		// Clear the text.
		_requestsLog.setText("");
		// Iterate through elevator requests.
		Iterator<ElevatorRequest> iterator = _elevatorRequests.iterator();
		while(iterator.hasNext()){
			_requestsLog.append(iterator.next().getFloorTo().getLevel() + ", ");
		}
	}
	
	/**
	 * Move the elevator to the specified y position.
	 * @param y
	 */
	public void moveTo(int y){
		// Move the elevator.
		move(getX(), y);
		// Update floor at.
		Floor floorAtNow = calculateFloorAt();
		// If we are at some floor, update our reference.
		if(floorAtNow != null){
			// Arrived at new floor.
			arrivedAtFloor(floorAtNow);
		}
	}
	
	/**
	 * Called when we have exactly reached a new floor.
	 * @param floorArrivedAt
	 */
	private void arrivedAtFloor(Floor floorArrivedAt){
		// Update reference.
		_floorAt = floorArrivedAt;
		// Default boolean.
		boolean stopAndOpen = false;
		// Do the efficiency checks if we are going to some destination.
		if(_floorDestination != null){
			// We can let off anyone that wants to get off at this floor,
			// even though they might not be first in the queue (but they are along the way).
			// Loop through requests.
			Iterator<ElevatorRequest> iterator = _elevatorRequests.iterator();
			while(iterator.hasNext()){
				// Get the next elevator request.
				ElevatorRequest elevatorRequest = iterator.next();
				// Check if the floor is this floor.
				if(elevatorRequest.getFloorTo() == floorArrivedAt){
					// Remove the request.
					removeRequest(elevatorRequest);
					// Set the boolean.
					stopAndOpen = true;
					// Exit out.
					break;
				}
			}
			// Pickup passengers, if there are any on this floor, that want to go
			// in the direction that we are currently going.
			// Determine the direction the elevator is going.
			int direction = getY() - _floorDestination.getY();
			// Going down.
			if(direction < 0){
				// Check if there are any that are going down.
				if(floorArrivedAt.hasDownRequest()){
					// Set boolean.
					stopAndOpen = true;
					// Extract all.
					floorArrivedAt.clearGoingDownRequest();
				}
			// Going up.
			} else if(direction > 0){
				// Check if there are any that are going up.
				if(floorArrivedAt.hasUpRequest()){
					// Set boolean.
					stopAndOpen = true;
					// Extract all.
					floorArrivedAt.clearGoingUpRequest();
				}
			// On same floor.
			} else {
				// Clear requests.
				floorArrivedAt.clearGoingDownRequest();
				floorArrivedAt.clearGoingUpRequest();
			}
		}
		// If reached destination.
		if(floorArrivedAt == _floorDestination){
			// Set boolean.
			stopAndOpen = true;
			// Clear destination.
			_floorDestination = null;
			// Update the requests log.
			updateLog();
		}
		// Stop and open.
		if(stopAndOpen){
			_elevatorDoor.open();
		}
	}
	
	/**
	 * Calculate the floor we are currently at in THIS instant of time.
	 * This calculation is made so that the floor that we are EXACTLY at is returned.
	 * If we are not EXACTLY on some floor, then null is returned.
	 * @return
	 */
	private Floor calculateFloorAt(){
		// Determine level we are at.
		int remainder = getY() % _building.getBuildingDef().floorDef.height;
		// There must not be a remainder.
		if(remainder == 0){
			// Determine exact level.
			int level = getY() / _building.getBuildingDef().floorDef.height;
			// Return the floor at that level.
			return _building.getFloorAtLevel(level);	
		} else {
			// We are in between floors.
			return null;
		}

	}
	
	/**
	 * Called to tell the elevator to go to the specified floor.
	 * @param floor
	 */
	public void goToFloor(Floor floor){
		// Set destination.
		_floorDestination = floor;
	}
	/**
	 * Go to the floor specified by level.
	 * @param level
	 */
	public void goToFloor(int level){
		// Go to floor at level.
		goToFloor(_building.getFloorAtLevel(level));
	}
	
	/**
	 * Getter for the elevator definition.
	 * @return
	 */
	public ElevatorDef getElevatorDef(){
		return _def;
	}
	
	/**
	 * Getter for the elevator door.
	 * @return
	 */
	public ElevatorDoor getElevatorDoor(){
		return _elevatorDoor;
	}
	
	/**
	 * Getter for the reference to the building.
	 * @return
	 */
	public Building getBuilding(){
		return _building;
	}
	
	/**
	 * Getter for the floor at.
	 * @return
	 */
	public Floor getFloorAt(){
		return _floorAt;
	}
	
	/**
	 * Getter for the floor destination.
	 * @return
	 */
	public Floor getFloorDestination(){
		return _floorDestination;
	}
	
}
