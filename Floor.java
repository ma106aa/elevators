import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Floor extends JPanel implements Comparable {

	// Definition.
	private FloorDef _def;
	// Reference to building.
	private Building _building;
	// Level of the floor.
	private int _level;
	// Up/Down buttons.
	private JButton _upButton;
	private JButton _downButton;
	// Requests.
	private FloorRequest _upRequest;
	private FloorRequest _downRequest;

	/**
	 * Constructor.
	 * @param def
	 * @param building
	 * @param level
	 */
	public Floor(FloorDef def, Building building, int level){
		// Base constructor.
		super();
		// Initialize.
		_def = def;
		_building = building;
		_level = level;
		// Initialize the floor.
		initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize(){
		// Set layout.
		setLayout(new BorderLayout());
		// Setup the size.
		setSize(_building.getBuildingDef().width, _def.height);
		// Position the floor.
		setLocation(0, _level * _def.height);
		// Set the color.
		if(_level % 2 == 0){
			setBackground(_def.primaryColor);
		} else {
			setBackground(_def.secondaryColor);
		}
		// Create up/down buttons.
		createRequestButtons();
	}

	/**
	 * Create the floor request buttons.
	 */
	private void createRequestButtons(){
		// Create the buttons (if needed).
		if(_level > 0){
			_upButton = new JButton("U (0)");
		}
		if(_level < _building.getBuildingDef().numFloors - 1){
			_downButton = new JButton("D (0)");
		}
		// Panel that holds both buttons.
		JPanel panel = new JPanel();
		panel.setBackground(_def.tertiaryColor);
		//panel.setLayout(new GridLayout(2, 1));
		panel.setLayout(new BorderLayout());
		// Add listeners
		if(_upButton != null){
			_upButton.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						addRequestUp();
					}
				}
			);
			// Display.
			panel.add(_upButton, BorderLayout.NORTH);
		}
		if(_downButton != null){
			_downButton.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						addRequestDown();
					}
				}
			);
			// Display.
			panel.add(_downButton, BorderLayout.SOUTH);
		}
		// Add floor label.
		JLabel levelLabel = new JLabel("" + _level);
		panel.add(levelLabel, BorderLayout.CENTER);
		// Display the panel.
		add(panel, BorderLayout.WEST);
	}
	
	/**
	 * Add an elevator request to go UP, relative to this floor.
	 */
	private void addRequestUp(){
		// Add if one already doesn't exist (we don't need more than one).
		if(_upRequest == null){
			// Update request.
			_upRequest = new FloorRequest(this, FloorRequest.UP);
			// Update label.
			_upButton.setText("U (1)");
			// Send request.
			_building.getElevatorSystem().addRequest(_upRequest);
		}
	}
	
	/**
	 * Add an elevator request to go DOWN, relative to this floor.
	 */
	private void addRequestDown(){
		// Add if one already doesn't exist.
		if(_downRequest == null){
			// Create and update request.
			_downRequest = new FloorRequest(this, FloorRequest.DOWN);
			// Update label.
			_downButton.setText("D (1)");
			// Send request.
			_building.getElevatorSystem().addRequest(_downRequest);
		}
	}
	
	/**
	 * Compare to specified floor.
	 * @return 
	 */
	public int compareTo(Object object) {
		// Default.
		int compare = 0;
		// Cast to floor.
		Floor floor = (Floor) object;
		// Compare levels.
		if(getLevel() < floor.getLevel()){
			compare = 1;
		} else if(getLevel() > floor.getLevel()){
			compare = -1;
		}
		// Return comparison.
		return compare;
	}
	
	/**
	 * Getter for the floor level.
	 * @return
	 */
	public int getLevel(){
		return _level;
	}
	
	/**
	 * Clear the request going up.
	 */
	public void clearGoingUpRequest(){
		// Update label. For reason, this button goes null sometimes.
		if(_upButton != null){
			_upButton.setText("U (0)");
		}
		// Delete from elevator system.
		_building.getElevatorSystem().removeRequest(_upRequest);
		// Clear reference.
		_upRequest = null;
	}
	
	/**
	 * Clear the request going down.
	 */
	public void clearGoingDownRequest(){
		// Update label. For reason, this button goes null sometimes.
		if(_downButton != null){
			_downButton.setText("D (0)");
		}
		// Delete from elevator system.
		_building.getElevatorSystem().removeRequest(_downRequest);
		// Clear reference.
		_downRequest = null;
	}
	
	/**
	 * Determine if the floor has an up request.
	 * @return
	 */
	public boolean hasUpRequest(){
		return _upRequest != null;
	}
	
	/**
	 * Determine if the floor has a down request.
	 * @return
	 */
	public boolean hasDownRequest(){
		return _downRequest != null;
	}

}
