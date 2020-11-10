package gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Displays help information for the application
 * @author joefischer
 *
 */
public class HelpDialog {
	
	private JFrame parent = null;
	private String message = "";
	private String helpFileName = "helpFile.txt";

	public HelpDialog(JFrame frame) {
		parent = frame;
		
	}
	
	public void showHelp() {
		String message = readHelpFile();
		JOptionPane.showMessageDialog(parent, message, "Application Help", JOptionPane.PLAIN_MESSAGE);
	}
	
	
	
	/**
	 * Reads the help text in from the help file
	 * @return help file contents as a string
	 */
	private String readHelpFile(){
		String fileName = helpFileName;
        ClassLoader classLoader = getClass().getClassLoader();
        String content = "";
        File file = new File(classLoader.getResource(fileName).getFile());
        try {
			content = new String(Files.readAllBytes(file.toPath()));
			//System.out.println("Help file found. File =  "+file.getName());
		} catch (IOException e) {
			System.out.println("Error reading help file. ");
			e.printStackTrace();
		}
        return content;
	}
}
