package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;

public class AppWindow {

	private JFrame frame;
	private final JPanel panel = new JPanel();
	private File searchFolder = null;
	private File dupeListSelected = null;
	private static JLabel status;
	private String appVersion = "1.0";

	/**
	 * Launch the application.
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
	 * Create the application.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public AppWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Dupe Music Search and Destroy");
		frame.setBounds(100, 100, 450, 300);
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
			} );
		fileMenu.add(exitMenuItem);
		
		
		JMenu helpMenu = new JMenu("Help");
		topMenuBar.add(helpMenu);
		
		JMenuItem helpMenuItem = new JMenuItem("Help");
		helpMenuItem.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				HelpDialog help = new HelpDialog(frame);
				help.showHelp();
			} 
		} );
		helpMenu.add(helpMenuItem);

		JMenuItem versionMenuItem = new JMenuItem("Version");
		versionMenuItem.addActionListener(new ActionListener() { 
			

			public void actionPerformed(ActionEvent e) { 
					String message = "Version: "+appVersion;
					JOptionPane.showMessageDialog(frame, message, "Version Info", JOptionPane.PLAIN_MESSAGE);
			} 
		});
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
		GridBagConstraints statusLabelConstraint = new GridBagConstraints();
		statusLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
		statusLabelConstraint.gridheight = 1;
		statusLabelConstraint.gridx = 0;
		statusLabelConstraint.gridy = 0;
		JLabel statusLabel = new JLabel("Status: ");
		GridBagConstraints statusConstraint = new GridBagConstraints();
		statusPanel.add(statusLabel, statusConstraint);
		statusConstraint.gridx = 1;
		statusConstraint.gridy = 0;
		statusConstraint.weightx = 0.2;
		statusConstraint.weighty = 0.2;
		status = new JLabel("");
		statusPanel.add(status, statusLabelConstraint);
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

		selectSearchRootButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int option = fileChooser.showOpenDialog(frame);
			if (option == JFileChooser.APPROVE_OPTION) {
				searchFolder = fileChooser.getSelectedFile();
				searchFolderName.setText("Folder Selected: " + searchFolder.getPath());
			} else {
				searchFolderName.setText("Select Search Root canceled");
			}
		});

		selectSearchFolderMenuItem.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int option = fileChooser.showOpenDialog(frame);
			if (option == JFileChooser.APPROVE_OPTION) {
				searchFolder = fileChooser.getSelectedFile();
				searchFolderName.setText("Folder Selected: " + searchFolder.getPath());
			} else {
				searchFolderName.setText("Select Search Root canceled");
			}
		});

		searchButton.addActionListener(e -> {
			Searcher searcher = new Searcher();
			if (searchFolder != null) {
				searcher.doSearch(searchFolder);
				DefaultListModel<MusicItem> model = searcher.getHasDupesModel();
				searchList.setModel(searcher.getHasDupesModel());
				searchedPane.setViewportView(searchList);
				status.setText(Integer.toString(model.getSize()) + " Duplicates found");
			}
		});

		searchList.addListSelectionListener(e -> {
			MusicItem selected = searchList.getSelectedValue();
			if (selected != null) {
				DefaultListModel<File> dupes = selected.getDuplicatesModel();
				dupeList.setModel(dupes);
				dupePane.setViewportView(dupeList);
			}
		});

		dupeList.addListSelectionListener(e -> {
			dupeListSelected = dupeList.getSelectedValue();
			if (dupeListSelected != null) {
				status.setText("Selected: " + dupeListSelected.getPath());

			}
		});

		
		dupeList.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_DELETE:
				case KeyEvent.VK_BACK_SPACE:
					int a = JOptionPane.showConfirmDialog(frame, "Do you want to delete: " + dupeListSelected.getName(),
							"Confirm", JOptionPane.YES_NO_OPTION);
					if (a == JOptionPane.YES_OPTION) {
						dupeListSelected.delete();
						status.setText("Deleted: " + dupeListSelected.getPath());
					} else {
						// do nothing
					}
					break;
				}
			}
		});
	}
	
//	private class SwingAction extends AbstractAction {
//		public SwingAction() {
//			putValue(NAME, "SwingAction");
//			putValue(SHORT_DESCRIPTION, "Some short description");
//		}
//		public void actionPerformed(ActionEvent e) {
//		}
//	}
	
}
