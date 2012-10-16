
public class ElevatorRequest {

	// The floor to go to.
	private Floor _floorTo;
	
	/**
	 * Constructor.
	 * @param floorTo
	 * @param type
	 */
	public ElevatorRequest(Floor floorTo){
		// Initialize.
		_floorTo = floorTo;
	}
	
	/**
	 * Getter for the floor to.
	 * @return
	 */
	public Floor getFloorTo(){
		return _floorTo;
	}
	
}
