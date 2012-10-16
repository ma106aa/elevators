
public class ElevatorDoorAnimationThread extends Thread {

	// Reference to elevator door.
	private ElevatorDoor _elevatorDoor;
	
	/**
	 * Constructor.
	 * @param elevatorDoor
	 */
	public ElevatorDoorAnimationThread(ElevatorDoor elevatorDoor){
		// Initialize.
		_elevatorDoor = elevatorDoor;
	}
	
	/**
	 * Run.
	 */
	public void run(){
		
		// Endless loop.
		while(true){
			
			// Extra calculations.
			int centerX = _elevatorDoor.getElevator().getElevatorDef().width / 2;
			// Variables.
			int x = centerX - (_elevatorDoor.getClosingRectangle().width / 2);
			int y = _elevatorDoor.getClosingRectangle().y; // Should not be edited (opening left to right).
			int width = _elevatorDoor.getClosingRectangle().width;
			int height = _elevatorDoor.getClosingRectangle().height; // Should not be edited (opening left to right).
			// Determine the status multiplier (since CLOSED and OPEN can't both be 0, they are made -2, and 2 respectively).
			int statusMultiplier = Math.abs(_elevatorDoor.getStatus()) == 2 ? 0 : _elevatorDoor.getStatus();
			// Determine what we are doing, opening or closing?
			width += statusMultiplier * _elevatorDoor.getElevator().getElevatorDef().doorSpeed;
			// Update.
			_elevatorDoor.updateClosingRectangle(x, y, width, height);
			
			try {
				sleep(_elevatorDoor.getElevator().getElevatorDef().doorAnimationMilliseconds);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
}
