
public class FloorRequest {

	// Request types.
	public static final int UP			= -1;
	public static final int DOWN		= 1;
	
	// The floor.
	private Floor _floorFrom;
	// Type.
	private int _type;
	
	/**
	 * Constructor.
	 * @param floor
	 */
	public FloorRequest(Floor floor, int type){
		// Initialize.
		_floorFrom = floor;
		_type = type;
	}
	
	/**
	 * Getter for the floor from.
	 * @return
	 */
	public Floor getFloorFrom(){
		return _floorFrom;
	}
	
}
