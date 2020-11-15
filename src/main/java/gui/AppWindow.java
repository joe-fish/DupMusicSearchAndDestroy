package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import searcher.MusicItem;
import searcher.Searcher;

/**
 * Builds the application window and adds listeners to perform operations.
 * 
 * @author joefischer
 *
 */
public class AppWindow {

	private JFrame frame;
	private final JPanel panel = new JPanel();
	private File searchFolder = null;
	private File dupeListSelected = null;
	private static JLabel status;
	private String appVersion = "1.0";

	/**
	 * Launch the application. Program entry point
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				AppWindow window = new AppWindow();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Creates the application window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public AppWindow() {
		initialize();
	}

	/**
	 * Define and Initialize the contents of the frame and adds the
	 * Listeners
	 */
	private void initialize() {
		frame = new JFrame("Dupe Music Search and Destroy");
		frame.setBounds(100, 100, 1200, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JMenuBar topMenuBar = new JMenuBar();
		frame.setJMenuBar(topMenuBar);

		JMenu fileMenu = new JMenu("File");
		topMenuBar.add(fileMenu);

		JMenuItem selectSearchFolderMenuItem = new JMenuItem("Select Search Folder");
		fileMenu.add(selectSearchFolderMenuItem);

		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);

		JMenu helpMenu = new JMenu("Help");
		topMenuBar.add(helpMenu);

		JMenuItem helpMenuItem = new JMenuItem("Help");
		helpMenu.add(helpMenuItem);

		JMenuItem versionMenuItem = new JMenuItem("Version");
		helpMenu.add(versionMenuItem);

		JPanel controlArea = new JPanel(new GridLayout(2, 0));
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);

		JLabel searchFolderName = new JLabel("Not Selected");
		controlArea.add(toolBar);
		controlArea.add(searchFolderName);
		frame.getContentPane().add(controlArea, BorderLayout.NORTH);

		// Build Status line
		JPanel statusPanel = new JPanel(new GridBagLayout());
		GridBagConstraints statusConstraint = new GridBagConstraints();
		statusConstraint.fill = GridBagConstraints.HORIZONTAL;
		statusConstraint.gridheight = 1;
		statusConstraint.gridx = 0;
		statusConstraint.gridy = 0;
		JLabel statusLabel = new JLabel("Status: ");
		statusPanel.add(statusLabel, statusConstraint);
		statusConstraint.gridx = 1;
		statusConstraint.gridy = 0;
		statusConstraint.weightx = 0.2;
		statusConstraint.weighty = 0.2;
		status = new JLabel("");
		statusPanel.add(status, statusConstraint);
		frame.getContentPane().add(statusPanel, BorderLayout.SOUTH);

		JButton selectSearchRootButton = new JButton("Select Search Root");
		toolBar.add(selectSearchRootButton);

		JButton searchButton = new JButton("Search");
		toolBar.add(searchButton);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);

		JScrollPane searchedPane = new JScrollPane();
		splitPane.setTopComponent(searchedPane);

		JScrollPane dupePane = new JScrollPane();
		splitPane.setBottomComponent(dupePane);
		dupePane.setViewportView(panel);

		JList<MusicItem> searchList = new JList<MusicItem>();
		searchedPane.setViewportView(searchList);

		JList<File> dupeList = new JList<File>();
		searchedPane.setViewportView(dupeList);

		/*
		 * Add Listeners
		 */

		// Listener for the Select Search folder button
		selectSearchRootButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int option = fileChooser.showOpenDialog(frame);
			if (option == JFileChooser.APPROVE_OPTION) {
				searchFolder = fileChooser.getSelectedFile();
				searchFolderName.setText("Folder Selected: " + searchFolder.getPath());
				clearDisplayedFiles(searchedPane, dupePane, searchList, dupeList);
			} else {
				searchFolderName.setText("Select Search Root canceled");
			}
		});

		// Listener for the Select Search folder menu item
		selectSearchFolderMenuItem.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int option = fileChooser.showOpenDialog(frame);
			if (option == JFileChooser.APPROVE_OPTION) {
				searchFolder = fileChooser.getSelectedFile();
				searchFolderName.setText("Folder Selected: " + searchFolder.getPath());
				clearDisplayedFiles(searchedPane, dupePane, searchList, dupeList);
			} else {
				searchFolderName.setText("Select Search Root canceled");
			}
		});

		// Listener for Search Biutton
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				performSearch(searchedPane, dupePane, searchList, dupeList);
			}
		});

		// SelectionListener for the list of files that have duplicates
		searchList.addListSelectionListener(e -> {
			MusicItem selected = searchList.getSelectedValue();
			if (selected != null) {
				DefaultListModel<File> dupes = selected.getDuplicatesModel();
				dupeList.setModel(dupes);
				dupePane.setViewportView(dupeList);
				status.setText("Selected: " + selected.getMusicItemFileName());
			}
		});

		// SelectionListener for duplicate files list
		dupeList.addListSelectionListener(e -> {
			dupeListSelected = dupeList.getSelectedValue();
			if (dupeListSelected != null) {
				status.setText("Selected for Delete: " + dupeListSelected.getPath());

			}
		});

		// Listener for delete key
		dupeList.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_DELETE:
				case KeyEvent.VK_BACK_SPACE:
					int a = JOptionPane.showConfirmDialog(frame, "Do you want to delete: " + dupeListSelected.getName(),
							"Confirm", JOptionPane.YES_NO_OPTION);
					if (a == JOptionPane.YES_OPTION) {
						dupeListSelected.delete();
						String deletedFile = dupeListSelected.getPath();
						performSearch(searchedPane, dupePane, searchList, dupeList);
						status.setText("Deleted: " + deletedFile);
					} else {
						// do nothing
					}
					break;
				}
			}
		});

		//Add Help menu item listener
		helpMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HelpDialog help = new HelpDialog(frame);
				help.showHelp();
			}
		});
		
		//Add Version Menu item listener
		versionMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = "Version: " + appVersion;
				JOptionPane.showMessageDialog(frame, message, "Version Info", JOptionPane.PLAIN_MESSAGE);
			}
		});
	
	} // END OF initialize

	



	/**
	 * Performs a search for duplicate music files.
	 * 
	 * @param searchedPane The ScrollPane that displays files that have duplicates
	 * @param dupePane     The ScrollPane that displays the duplicate files
	 * @param searchList   The JList that renders musicItems that have duplicates
	 * @param dupeList     The JList that renders the duplicates
	 */
	private void performSearch(JScrollPane searchedPane, JScrollPane dupePane, JList<MusicItem> searchList,
			JList<File> dupeList) {
		Searcher searcher = new Searcher();
		if (searchFolder != null) {

			// Set status to searching in case search take a while
			status.setText("Searching");

			// do the search and update the searchPane with te results
			searcher.doSearch(searchFolder);
			DefaultListModel<MusicItem> model = searcher.getHasDupesModel();
			searchList.setModel(model);
			searchedPane.setViewportView(searchList);

			// Clear any previous selections
			dupeList.clearSelection();
			searchList.clearSelection();

			/*
			 * Reset the dupeList model to clear display of files that may have been
			 * previously deleted
			 */
			DefaultListModel<File> duplicatesModel = new DefaultListModel<File>();
			dupeList.setModel(duplicatesModel);
			dupePane.setViewportView(dupeList);

			// Update status to show the number of duplicates found
			status.setText(Integer.toString(model.getSize()) + " files with duplicates found");
			frame.revalidate();
		}
	}
	
	private void clearDisplayedFiles(JScrollPane searchedPane, JScrollPane dupePane, JList<MusicItem> searchList,
			JList<File> dupeList) {
		
		DefaultListModel<MusicItem> model = new DefaultListModel<MusicItem>();
		searchList.setModel(model);
		searchedPane.setViewportView(searchList);
		
		DefaultListModel<File> duplicatesModel = new DefaultListModel<File>();
		dupeList.setModel(duplicatesModel);
		dupePane.setViewportView(dupeList);
		status.setText("");
		
	}
}
