import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;


public class Building extends JFrame {

	// Layers.
	private static final int LAYER_ELEVATORS 	= 0;
	private static final int LAYER_FLOORS 		= 1;
	
	// Definition.
	private BuildingDef _def;
	// Floors.
	private Floor[] _floors;
	// Elevators.
	private Elevator[] _elevators;
	// Elevator system.
	private ElevatorSystem _elevatorSystem;
	// Layers.
	private JLayeredPane _layers = new JLayeredPane();
	
	/**
	 * Constructor.
	 * @param def
	 */
	public Building(BuildingDef def){
		// Base constructor.
		super("Building");
		// Initialize.
		_def = def;
		// Setup the frame.
		initializeFrame();
		// Construct the building.
		constructBuilding();
	}
	
	/**
	 * Initialize the frame.
	 */
	private void initializeFrame(){
		// Setup the layers.
		add(_layers);
		// Setup the JFrame.
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(_def.width, _def.numFloors * _def.floorDef.height + 50); // Add frame title bar offset.
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Construct the building (create the floors & elevators).
	 */
	private void constructBuilding(){
		// Create elevator system.
		_elevatorSystem = new ElevatorSystem(this);
		// Build the floors.
		_floors = new Floor[_def.numFloors];
		// Iterate through floors.
		for(int i=0; i<_def.numFloors; i++){
			// Create the floor.
			Floor floor = new Floor(_def.floorDef, this, i);
			// Add to array.
			_floors[i] = floor;
			// Display.
			_layers.add(floor, LAYER_FLOORS);
		}
		// Build the elevators.
		_elevators = new Elevator[_def.numElevators];
		// Iterate through elevators.
		for(int i=0; i<_def.numElevators; i++){
			// Create the elevator.
			Elevator elevator = new Elevator(_def.elevatorDef, this, i);
			// Add to array.
			_elevators[i] = elevator;
			// Display.
			_layers.add(elevator, LAYER_ELEVATORS);
		}
	}
	
	/**
	 * Return the floor at the specified level.
	 * @param level
	 * @return
	 */
	public Floor getFloorAtLevel(int level){
		// Make sure level is in bounds.
		return level < 0 || level > _def.numFloors - 1 ? null : _floors[level];
	}
	
	/**
	 * Return the floor that has the specified id.
	 * @param id
	 * @return
	 */
	public Elevator getElevatorById(int id){
		// Make sure it is in bounds.
		return id < 0 || id > _def.numElevators - 1 ? null : _elevators[id];
	}
	
	/**
	 * Getter for the building definition.
	 * @return
	 */
	public BuildingDef getBuildingDef(){
		return _def;
	}
	
	/**
	 * Getter for the elevator system.
	 * @return
	 */
	public ElevatorSystem getElevatorSystem(){
		return _elevatorSystem;
	}
	
}
