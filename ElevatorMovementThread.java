
public class ElevatorMovementThread extends Thread {

	// Reference to elevator.
	private Elevator _elevator;
	
	/**
	 * Constructor.
	 * @param elevator
	 */
	public ElevatorMovementThread(Elevator elevator){
		// Initialize.
		_elevator = elevator;
	}
	
	/**
	 * Run.
	 */
	public void run(){
		
		// Endless loop.
		while(true){
			
			// Variables.
			Floor floorAt = _elevator.getFloorAt();
			Floor floorTo = _elevator.getFloorDestination();
			// Door.
			ElevatorDoor elevatorDoor = _elevator.getElevatorDoor();
			int elevatorDoorStatus = elevatorDoor.getStatus();
			
			// Make sure we have destination.
			if(floorTo != null){
				// If the door is open, close it before we begin moving.
				if(elevatorDoorStatus == ElevatorDoor.OPEN){
					elevatorDoor.close();
				// Only move if closed.
				} else if(elevatorDoorStatus == ElevatorDoor.CLOSED){
					// Compare floors.
					int floorCompare = floorAt.compareTo(floorTo);
					int y = _elevator.getLocation().y + (_elevator.getElevatorDef().moveSpeed * floorCompare);
					// Move the elevator.
					_elevator.moveTo(y);
				}
			}
			
			// Attempt to serve the next request.
			_elevator.serveNextRequest();
			
			try {
				sleep(_elevator.getElevatorDef().moveAnimationMilliseconds);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
}
