package tage;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import javax.swing.border.*;

/**
 * This class provides a dialog box for specifying display settings.
 * It receives a GraphicsDevice object,
 * queries the device to obtain the supported display modes, and provides a GUI which
 * allows the user to select from the available options.
 * The class remembers the user-selected values and provides methods for
 * obtaining the selected values, including DisplayMode (width, height, bit-depth,
 * and refresh rate), and whether or not to use FSEM (Full Screen Exclusive Mode).
 * <P>
 * After creating a DisplaySettingsDialog, call showIt() to make the dialog visible.
 * The visible dialog is modal (meaning the invoking program cannot continue until the dialog
 * is dismissed).  Hitting either "OK" or "Cancel" on the dialog dismisses (hides) the
 * dialog, but does not delete it; the invoking code can make it visible again by calling
 * showIt() again.
 * <P>
 * Note that this class does <i>not</i> actually alter any display settings
 * or modes; it simply gives the invoking code a convenient graphical method
 * for allowing the user to indicate the desired settings.  The invoking
 * code is responsible for fetching the user's selections from the dialog
 * and using that information to actually alter any desired display settings.
 *
 * @author  John Clevenger
 *
 * @version  1.0  20060924
 * @version  1.1  20080922	Changed default to "fullscreen"
 *
 */

@SuppressWarnings("unchecked")
public class DisplaySettingsDialog extends JDialog implements ActionListener
{
 	private boolean useFullScreen = false;			//the current user FSEM selection
	private DisplayMode selectedDisplayMode = null;		//the current user DisplayMode selection
	private GraphicsDevice device ;				//the current default graphics device

  	private JRadioButton windowedModeRadioButton;
  	private JRadioButton fullScreenModeRadioButton;
  	private JComboBox displayModeComboBox ;
  	private JLabel currentResolutionLabel;

	/**
	 * Creates a DisplaySettingsDialog for the specified GraphicsDevice and
	 * allows the user to choose desired display mode values.
	 * Call showIt() in the created dialog to make it visible.
	 * The dialog is modal (must be dismissed once shown before the invoking program
	 * can continue).
	 * The created dialog retains its settings even when hidden;
	 * accessors are provided for the invoking code to determine
	 * the display values selected by the user.
	 */
	public DisplaySettingsDialog (GraphicsDevice theDevice)
	{
		setTitle("Choose Display Settings");
		setSize(450, 200);
		setLocation (200,200);
		setResizable(true);
		device = theDevice;
		doMyLayout();

		// make the dialog modal, so that 'show' will block until dialog is dismissed
		setModal( true );
	}

	private void doMyLayout()
	{
		setLayout (new BorderLayout());

		//add a top panel showing the currently active screen resolution
		JPanel topPanel = new JPanel();
			currentResolutionLabel = new JLabel("Current Resolution: unknown");
			topPanel.add(currentResolutionLabel);
		this.add(topPanel, "North");

		//add a bottom panel containing control buttons (OK, Cancel)
		JPanel buttonPanel = new JPanel();
		JButton newButton = new JButton("OK");
		newButton.setActionCommand( "OK" );
		newButton.addActionListener(this);
		buttonPanel.add(newButton);

		newButton = new JButton("Cancel");
		newButton.setActionCommand( "Cancel" );
		newButton.addActionListener(this);
		buttonPanel.add(newButton);
		this.add(buttonPanel, "South");

		//add a left panel with a radio button group for selecting full or windowed screen mode
		JPanel screenPanel = new JPanel ();
		screenPanel.setBorder (new TitledBorder("Screen Mode:  ") );
		Box screenButtonBox = new Box(BoxLayout.Y_AXIS);

		windowedModeRadioButton = new JRadioButton("Windowed",true);
		fullScreenModeRadioButton = new JRadioButton ("Full Screen",false);

		ButtonGroup screenModeButtonGroup = new ButtonGroup();
		screenModeButtonGroup.add (windowedModeRadioButton);
		screenModeButtonGroup.add (fullScreenModeRadioButton);
		screenButtonBox.add (windowedModeRadioButton);
		screenButtonBox.add (fullScreenModeRadioButton);

		screenPanel.add(screenButtonBox);

		this.add(screenPanel, "West");

		//add a rightside panel containing a drop-down list of available display modes
		JPanel displayModesPanel = new JPanel (new BorderLayout());
		displayModesPanel.setBorder (new TitledBorder("New Resolution:  ") );

		//get the display modes supported by the device
		DisplayMode [] modes = device.getDisplayModes();

		//build a set of strings each describing one mode
		Vector displayModeList = getDisplayModeList(modes);

		//add a combo box containing the available modes
		displayModeComboBox = new JComboBox(displayModeList);
		displayModesPanel.add (displayModeComboBox);

		this.add(displayModesPanel, "East");
	}

	private Vector<String> getDisplayModeList(DisplayMode [] modes)
	{
		Vector<String> displayList = new Vector<String>();

		for (DisplayMode m : modes)
		{
			int width = m.getWidth();
			int height = m.getHeight();
			int depth = m.getBitDepth();
			int rate = m.getRefreshRate();

			//create a composite display string for the JComboBox.
			// Note that the format is important;
			// the "OK" button parsing code for the JComboBox selected-string expects
			// the width/height to be separated by a single 'x', commas between the
			// width/height, depth, and rate fields, and that the depth and rate numeric
			// values are terminated with a '-'.
			String s = "" + width + "x" + height + ", " + depth + "-bit color, "
							+ rate + "-Hz refresh rate" ;
			displayList.addElement (s);
		}

		return displayList ;
	}

	/**
	 * Makes the dialog visible and waits for the user to dismiss it
	 * by hitting either "OK" or "Cancel".  Hitting "OK" causes the
	 * values shown in the dialog GUI to be saved in the dialog.  Note however
	 * that the dialog does not actually install any values into the underlying
	 * machine; the invoking application is responsible for reading the dialog
	 * values and installing them if desired.
	 */
	public void showIt()
	{
		//update the radiobuttons to match the current state
		if (useFullScreen)
		{
			fullScreenModeRadioButton.doClick();
		}
		else
		{
			windowedModeRadioButton.doClick();
		}

		//update the label showing the current device resolution
		DisplayMode curMode = device.getDisplayMode();
		int width = curMode.getWidth();
		int height = curMode.getHeight();
		int depth = curMode.getBitDepth();
		int refreshRate = curMode.getRefreshRate();

		currentResolutionLabel.setText("Current Resolution:  " + width + "x" + height
						+ ", " + depth + "-bits, " + refreshRate + "-Hz " );

		//set the combo box so the current actual resolution is selected by default
		displayModeComboBox.setSelectedIndex(getComboBoxIndexOf(curMode));

		this.setVisible(true);
	}

	/**
	 * Returns a copy of the currently selected DisplayMode, or
	 * null if no display mode has been selected.
	 */
	public DisplayMode getSelectedDisplayMode()
	{
		DisplayMode copy = null;

		if (selectedDisplayMode != null)
		{
			copy = new DisplayMode (selectedDisplayMode.getWidth(),
						selectedDisplayMode.getHeight(),
						selectedDisplayMode.getBitDepth(),
						selectedDisplayMode.getRefreshRate());
		}
		return copy;
	}

	/**
	 * Returns a boolean indicating whether or not the user has specified that
	 * Full Screen Exclusive Mode (FSEM) should be used.
	 */
	public boolean isFullScreenModeSelected()
	{
		return useFullScreen ;
	}

	/**
	 * Searches the Display Mode strings in the Display Mode ComboBox for a match
	 * with the specified DisplayMode.  If a match is found, returns the index
	 * in the ComboBox of the matching item; otherwise, returns zero.
	 */
	private int getComboBoxIndexOf(DisplayMode curMode)
	{
		int numItems = displayModeComboBox.getItemCount();

		boolean found = false;
		int nextItemNum = 0;
		String itemString ;

		while (!found && nextItemNum < numItems)
		{
			itemString = (String) displayModeComboBox.getItemAt(nextItemNum);

			if (match(itemString, curMode))
			{
				found = true;
			}
			else
			{
				nextItemNum ++;
			}
		}

		if (found)
		{
			return nextItemNum;
		}
		else
		{
			return 0 ;
		}
	}

	/**
	 * Returns true if the display mode defined by the specified String matches
	 * the specified DisplayMode object values; false otherwise.
	 */
	private boolean match (String modeString, DisplayMode dispMode)
	{
		if (toDisplayMode(modeString).equals(dispMode))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Parses the given modeString to extract the width, height, depth, and refresh rate
	 * of the mode defined by the string, and returns a DisplayMode object containing
	 * those values.
	 */
	private DisplayMode toDisplayMode(String modeString)
	{
		//split the given string into its comma-separated composite parts
		String [] parts = modeString.split(",");
		String widthAndHeightString = parts[0].trim();
		String depthString = parts[1].trim();
		String refreshRateString = parts[2].trim();

		//split the width-and-height string into its 'x'-separated parts
		String [] dimensions = widthAndHeightString.split("x");
		String widthString = dimensions[0].trim();
		String heightString = dimensions[1].trim();

		//split the depth and refresh-rate strings at the '-' which trails
		// the numeric value
		String [] depthParts = depthString.split("-");
		String [] rateParts = refreshRateString.split("-");

		//extract the numeric components of the depth and refresh rate strings
		depthString = depthParts[0];
		refreshRateString = rateParts[0];

		//create and return a DisplayMode object describing the selected mode
		DisplayMode mode = new DisplayMode (Integer.valueOf(widthString),
							Integer.valueOf(heightString),
							Integer.valueOf(depthString),
							Integer.valueOf(refreshRateString) );
		return mode ;
	}


	/**
	 * Respond to Action Events from Dialog buttons (OK, Cancel).
	 * An "OK" event causes the current GUI values to be saved in the dialog;
	 * "Cancel" results in no changes to the dialog.
	 * In either event the dialog is hidden afterwards.
	 */
	public void actionPerformed(ActionEvent e)
  	{
		if (e.getActionCommand() == "OK")
		{
			//fetch the string defining the mode selected by the user
			String selectedModeString = (String) displayModeComboBox.getSelectedItem();

			//create and save a DisplayMode object describing the selected mode
			selectedDisplayMode = toDisplayMode (selectedModeString);

			//update the local flags indicating user-selected states
			useFullScreen = fullScreenModeRadioButton.isSelected();

			//System.out.println ("OK pressed");
		}

      		else if (e.getActionCommand() == "Cancel")
      		{
			//System.out.println ("Cancel pressed");
  		}

      		setVisible(false);
  	}
}