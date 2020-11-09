package gui;

import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DeleteDialog {

	public void displayDialog(JFrame frame, File deleteFile) {
//		JDialog dialog = new JDialog(frame, "Confirm Delete!");

//		JOptionPane optionPane = new JOptionPane(
//			    "The only way to close this dialog is by\n"
//			    + "pressing one of the following buttons.\n"
//			    + "Do you understand?",
//			    JOptionPane.QUESTION_MESSAGE,
//			    JOptionPane.YES_NO_OPTION);
//		dialog.add(optionPane);

		int a = JOptionPane.showConfirmDialog(frame, "Do you want to delete: ");
		if (a == JOptionPane.YES_OPTION) {
			deleteFile.delete();
			// frame.setDefaultCloseOperation(frame.EX);
			// DO_NOTHING_ON_CLOSE
		} else {

		}
	}
}
