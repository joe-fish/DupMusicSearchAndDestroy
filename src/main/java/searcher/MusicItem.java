package searcher;

import java.io.File;
import javax.swing.DefaultListModel;


public class MusicItem {
	
	private File musicFile;
//	private List<File> duplicates = new ArrayList<File>();
	private DefaultListModel<File> duplicatesModel = new DefaultListModel<File>();
	
	private boolean hasDupes = false;
	private String songName = "";

	public void setMusicFile(File musicFile){
		this.musicFile = musicFile;
	}
	
	public File getMusicFile(){
		return musicFile;
	}
	
	public String getMusicItemFileName(){
		return musicFile.getName();
	}
	
	public DefaultListModel<File> getDuplicatesModel() {
		return duplicatesModel;
	}

//	public void setDuplicates(List<File> duplicates) {
//		this.duplicates = duplicates;
//		duplicates.forEach(item -> duplicatesModel.addElement(item));
//	}
	
	public void addDupe(File duplicateFile) {
		duplicatesModel.addElement(duplicateFile);
	}

	public boolean getHasDupes() {
		return hasDupes;
	}

	public void setHasDupes(boolean hasDupes) {
		this.hasDupes = hasDupes;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}
	
	public String toString() {
		return musicFile.getPath();
	}
	
}
