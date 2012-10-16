import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;


public class ElevatorSystem {

	// Reference to the building.
	private Building _building;
	// Queue of elevator requests.
	private ArrayBlockingQueue<FloorRequest> _floorRequests = new ArrayBlockingQueue<FloorRequest>(20);
	
	/**
	 * Constructor.
	 * @param building
	 */
	public ElevatorSystem(Building building){
		// Initialize.
		_building = building;
	}
	
	/**
	 * Add a new floor request.
	 * @param request
	 */
	public void addRequest(FloorRequest request){
		// Add to queue.
		_floorRequests.offer(request);
	}
	
	/**
	 * Remove an existing floor request.
	 * @param request
	 */
	public void removeRequest(FloorRequest request){
		// Remove from queue.
		_floorRequests.remove(request);
	}
	
	/**
	 * Getter for the floor requests.
	 * @return
	 */
	public ArrayBlockingQueue<FloorRequest> getFloorRequests(){
		return _floorRequests;
	}
	
}
