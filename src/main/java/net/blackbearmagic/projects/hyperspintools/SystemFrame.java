package net.blackbearmagic.projects.hyperspintools;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class SystemFrame extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3887991122962611206L;
	JPanel pane = new JPanel();
	JButton processDirectoryCreate;
	JFileChooser chooser;
	JButton chooseDestination;
	JLabel destinationLabel;
	JButton chooseSource;
	JLabel sourceLabel;
	JTextField fileFilter;
	JLabel filterLabel;
	ButtonGroup actionGroup = new ButtonGroup();
	JMenuItem helpMenuItem;
	String choosertitle;
	int xStacker = 0;
	int yStacker = 0;

	public SystemFrame() {
		super("HyperSpin Directory Create");
		this.setBounds(100, 100, 500, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pane.setLayout(new BoxLayout(this.pane, BoxLayout.PAGE_AXIS));
		Container con = this.getContentPane(); // inherit main frame
		con.add(this.pane);
		try {
			SystemProperties.loadSettings();
		} catch (FileNotFoundException e) {
			Log.logMessage(e);
		}
		this.placeHelpMenu();
		this.placeSourceDisplayLabel();
		this.placeSourceChooseButton();
		this.placeDestinationDisplayLabel();
		this.placeDestinationChooseButton();
		this.placeFileFilterLabel();
		this.placeFileFilterTextField();
		this.placeActionButtons();
		this.placeDirCreateButton();

		this.setVisible(true); // make frame visible
	}

	private void addPanel(JPanel panel) {
		this.pane.add(panel);
	}

	private void placeHelpMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
				"This is the help menu");
		menuBar.add(menu);

		this.helpMenuItem = new JMenuItem("Help Contents", KeyEvent.VK_T);
		this.helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.ALT_MASK));
		this.helpMenuItem.getAccessibleContext().setAccessibleDescription(
				"This will show help text");
		this.helpMenuItem.addActionListener(this);
		menu.add(this.helpMenuItem);

		this.setJMenuBar(menuBar);
	}

	private void placeDestinationDisplayLabel() {
		JPanel panel = new JPanel();
		this.destinationLabel = new JLabel(SystemProperties.getProperties()
				.getProperty("output.directory"));
		this.destinationLabel.setLocation(this.xStacker, this.yStacker);
		this.destinationLabel.setSize(100, 30);
		this.destinationLabel.setHorizontalAlignment(0);
		this.destinationLabel.setForeground(Color.blue);
		panel.add(this.destinationLabel);
		this.addPanel(panel);
	}

	private void placeDestinationChooseButton() {
		JPanel panel = new JPanel();
		this.chooseDestination = new JButton("Choose Destination");
		this.chooseDestination.setLocation(this.xStacker, this.yStacker);
		this.chooseDestination.addActionListener(this);
		this.chooseDestination.setMnemonic('D');
		panel.add(this.chooseDestination);
		this.addPanel(panel);
	}

	private void placeSourceDisplayLabel() {
		JPanel panel = new JPanel();
		this.sourceLabel = new JLabel(SystemProperties.getProperties()
				.getProperty("source.directory"));
		this.sourceLabel.setLocation(this.xStacker, this.yStacker);
		this.sourceLabel.setSize(100, 30);
		this.sourceLabel.setHorizontalAlignment(0);
		this.sourceLabel.setForeground(Color.blue);
		panel.add(this.sourceLabel);
		this.addPanel(panel);
	}

	private void placeSourceChooseButton() {
		JPanel panel = new JPanel();
		this.chooseSource = new JButton("Choose Source");
		this.chooseSource.setLocation(this.xStacker, this.yStacker);
		this.chooseSource.addActionListener(this);
		this.chooseSource.setMnemonic('S');
		panel.add(this.chooseSource);
		this.addPanel(panel);
	}

	private void placeFileFilterTextField() {
		JPanel panel = new JPanel();
		this.fileFilter = new JTextField(20);
		this.fileFilter.setText(SystemProperties.getProperties().getProperty(
				"file.filter"));
		this.fileFilter.setLocation(this.xStacker, this.yStacker);
		this.fileFilter.setSize(100, 30);
		this.fileFilter.setHorizontalAlignment(0);
		panel.add(this.fileFilter);
		this.addPanel(panel);

	}

	private void placeFileFilterLabel() {
		JPanel panel = new JPanel();
		this.filterLabel = new JLabel(
				"File filter to apply to directory create search.");
		this.filterLabel.setLocation(this.xStacker, this.yStacker);
		this.filterLabel.setSize(100, 30);
		this.filterLabel.setHorizontalAlignment(0);
		this.filterLabel.setForeground(Color.black);
		panel.add(this.filterLabel);
		this.addPanel(panel);

	}

	private void placeActionButtons() {
		JPanel panel = new JPanel();
		JRadioButton createOnly = new JRadioButton(Action.CREATE.toString());
		createOnly.setMnemonic(KeyEvent.VK_C);
		createOnly.setActionCommand(Action.CREATE.toString());
		createOnly.setSelected(ExecuteAction.isActionSelected(Action.CREATE));
		panel.add(createOnly);
		JRadioButton moveOnly = new JRadioButton(Action.MOVE.toString());
		moveOnly.setMnemonic(KeyEvent.VK_F);
		moveOnly.setActionCommand(Action.MOVE.toString());
		moveOnly.setSelected(ExecuteAction.isActionSelected(Action.MOVE));
		panel.add(moveOnly);
		JRadioButton createAndMove = new JRadioButton(
				Action.CREATE_MOVE.toString());
		createAndMove.setMnemonic(KeyEvent.VK_B);
		createAndMove.setActionCommand(Action.CREATE_MOVE.toString());
		createAndMove.setSelected(ExecuteAction
				.isActionSelected(Action.CREATE_MOVE));
		panel.add(createAndMove);
		this.actionGroup.add(createOnly);
		this.actionGroup.add(moveOnly);
		this.actionGroup.add(createAndMove);
		this.addPanel(panel);
	}

	private void placeDirCreateButton() {
		JPanel panel = new JPanel();
		this.processDirectoryCreate = new JButton("Execute");
		this.processDirectoryCreate.setLocation(this.xStacker, this.yStacker);
		this.processDirectoryCreate.addActionListener(this); // register button
																// listener
		panel.add(this.processDirectoryCreate);
		this.processDirectoryCreate.requestFocus();
		this.addPanel(panel);
	}

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		Log.logMessage("source:" + source);
		if (source == this.processDirectoryCreate) {
			String runMessage = "";
			try {
				SystemProperties.getProperties().put("file.filter",
						this.fileFilter.getText());
				SystemProperties.getProperties().put("selected.action",
						this.actionGroup.getSelection().getActionCommand());
				SystemProperties.saveProperties();
				runMessage = ExecuteAction.run();
			} catch (Exception e) {
				Log.logMessage(e);
			}
			JOptionPane.showMessageDialog(null, runMessage,
					"Action Execution Results", JOptionPane.PLAIN_MESSAGE);
			this.setVisible(true); // show something
		} else if (source == this.chooseDestination) {
			{
				this.createFileChooser(SystemProperties.getProperties()
						.getProperty("output.directory"));
				if (this.chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
					File directory = this.chooser.getSelectedFile();
					if (directory.isFile()) {
						directory = directory.getParentFile();
					}
					SystemProperties.getProperties().put("output.directory",
							directory.getAbsolutePath());
					this.destinationLabel.setText(SystemProperties
							.getProperties().getProperty("output.directory"));
					SystemProperties.saveProperties();

				} else {
					Log.logMessage("No Selection ");
				}
			}
		} else if (source == this.chooseSource) {
			{
				this.createFileChooser(SystemProperties.getProperties()
						.getProperty("source.directory"));
				if (this.chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
					File directory = this.chooser.getSelectedFile();
					String filterPart = null;
					if (directory.isFile()) {
						filterPart = directory.getName();
						directory = directory.getParentFile();
					}
					SystemProperties.getProperties().put("source.directory",
							directory.getAbsolutePath());
					if (filterPart != null) {
						SystemProperties.getProperties().put("file.filter",
								filterPart);
						this.fileFilter.setText(SystemProperties
								.getProperties().getProperty("file.filter"));
					}
					this.sourceLabel.setText(SystemProperties.getProperties()
							.getProperty("source.directory"));
					SystemProperties.saveProperties();

				} else {
					Log.logMessage("No Selection ");
				}
			}
		} else if (source == this.helpMenuItem) {
			JOptionPane.showMessageDialog(this, Help.getHelpText());
		}

	}

	private void createFileChooser(String chooserCurrentDirectory) {
		this.chooser = new JFileChooser();
		this.chooser.setCurrentDirectory(new java.io.File(
				chooserCurrentDirectory));
		this.chooser.setDialogTitle(this.choosertitle);
		this.chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		//
		// disable the "All files" option.
		//
		this.chooser.setAcceptAllFileFilterUsed(false);
	}
}
