package gui;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import javax.swing.JToolBar;

import searcher.MusicItem;
import searcher.Searcher;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;

import java.io.File;
import java.util.List;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JMenu;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;

public class AppWindow {

	private JFrame frame;
	private final JPanel panel = new JPanel();
	private File searchFolder = null;


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
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Select Search Folder");
		fileMenu.add(mntmNewMenuItem);
		
		JPanel controlArea = new JPanel(new GridLayout(2,0));
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		
		JLabel searchFolderName = new JLabel("Not Selected");
		controlArea.add(toolBar);
		controlArea.add(searchFolderName);
		frame.getContentPane().add(controlArea, BorderLayout.NORTH);
		
		//Build Status line		
		JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel statusLabel = new JLabel("Status: ");
		statusPanel.add(statusLabel);
		
		JLabel status = new JLabel("");
		statusPanel.add(status);
		
		
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
		    if(option == JFileChooser.APPROVE_OPTION){
		       searchFolder = fileChooser.getSelectedFile();
		       searchFolderName.setText("Folder Selected: " + searchFolder.getPath());
		    }else{
		    	searchFolderName.setText("Select Search Root canceled");
		    }
		 });
		
		mntmNewMenuItem.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int option = fileChooser.showOpenDialog(frame);
			if(option == JFileChooser.APPROVE_OPTION){
				searchFolder = fileChooser.getSelectedFile();
				searchFolderName.setText("Folder Selected: " + searchFolder.getPath());
			}else{
				searchFolderName.setText("Select Search Root canceled");
			}
		});
		
		searchButton.addActionListener(e -> {
			Searcher searcher = new Searcher();
			if(searchFolder != null) {
				searcher.doSearch(searchFolder);
				DefaultListModel model = searcher.getHasDupesModel();
				searchList.setModel(searcher.getHasDupesModel());
				searchedPane.setViewportView(searchList);
				status.setText(Integer.toString(model.getSize())+" Duplicates found");
			}
		});
		
		
		searchList.addListSelectionListener(e -> {
			MusicItem selected = searchList.getSelectedValue();
			DefaultListModel<File> dupes = selected.getDuplicatesModel();
			dupeList.setModel(dupes);
			dupePane.setViewportView(dupeList);
		});
	}
}
