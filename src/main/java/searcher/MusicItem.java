package searcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class MusicItem {
	
	private File musicFile;
	private List<File> duplicates = new ArrayList<File>();
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
	
	public List<File> getDuplicates() {
		return duplicates;
	}

	public void setDuplicates(List<File> duplicates) {
		this.duplicates = duplicates;
	}
	
	public void addDupe(File duplicateFile) {
		duplicates.add(duplicateFile);
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
	
}
