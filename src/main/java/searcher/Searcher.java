package searcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;

/**
 * This class is used to find all the duplicate musicFiles in the search folder
 * hierarchy.  
 * @author joefischer
 *
 */
public class  Searcher {
	
	private List<MusicItem> searchList;
	private List<MusicItem> hasDupesList;
	private String[] extensionsNames = {"ape", "tta", "mp3", "aac", "ogg", "flac", 
			"alac", "wav", "aiff", "dsd", "pcm", "wma"};
	private DefaultListModel<MusicItem> hasDupesModel = new  DefaultListModel<MusicItem>();
	
	
	/**
	 * Searches the searchFolder and nested folders recursively for musicFiles.
	 * A list ofMusicItems for all music files in the folder hierarchy is created. For each item
	 * in the list, a search is made again through the list for items with similar filenames.
	 * As duplicate MusicItems are found they are added to the dupeList for that MusicItem. 
	 * When a MusicItem being searched has one or more duplicates, it is added to the hasDupesList.
	 *
	 * @param searchFolder
	 * 
	 * @return The hasDupesList that is rendered in the top half of the application window. 
	 */
	public void doSearch(File searchFolder) {
		
		this.searchList = buildList(searchFolder);
		this.hasDupesList = new ArrayList<MusicItem>();
		this.searchList.forEach(item -> dupeSearch(item));
		hasDupesList.forEach(item -> hasDupesModel.addElement(item));
		
	}
	
	/**
	 * Searches for music files in each of the folders in the searchFolder hierarchy
	 * and builds a list of MusicItems that represent each music file.
	 * @param searchFolder
	 * @return A List containing all the MusicItems nested under the searchFolder.
	 */
	private List<MusicItem> buildList(File searchFolder) {
		File[] folderFiles;
		List<MusicItem> searchList = new ArrayList<MusicItem>();
		if(searchFolder.exists() && searchFolder.isDirectory()) {
			folderFiles = searchFolder.listFiles();
			for(File file : folderFiles) {
				if (file.isDirectory()) {
					searchList.addAll(buildList(file));
				} 
				else {
					MusicItem item = new MusicItem();
					item.setMusicFile(file);
					item.setSongName(parseForSong(item));
					searchList.add(item);
				}
			}
		}
		return searchList;
	}
	

	
	/**
	 * Called on each MusicItem. A search is made through all
	 * MusicItems for filenames that are similar
	 * @param item MusicItem object
	 */
	private void dupeSearch(MusicItem item) {
		for(MusicItem mItem :searchList) {
			if(mItem.getMusicItemFileName().contains(item.getSongName())) {
				//don't addDupe for same music file
				if(!(item.equals(mItem))) {
					item.addDupe(mItem.getMusicFile());
					item.setHasDupes(true);
					hasDupesList.add(item);
				}
			}
		}
		
	}
	
	/**
	 * Parses a MusicItems fully qualified filename to form a search String that
	 * is used to find similar file names the search String is a sub-string of the 
	 * filename that contains just enough text to identify the song. 
	 * <br>For example:<br>
	 *   
	 */
	private String parseForSong(MusicItem item) {

		List<String> extensions = Arrays.asList(this.extensionsNames);
		String fileName = item.getMusicItemFileName();
		String[] parsed = fileName.split("\\.");
		String songName = "";
		if(parsed.length == 2) {
			if(extensions.contains(parsed[1])) {
				String[] alphaOnly = parsed[0].split("[0-9]");
				for (String token : alphaOnly) {
					songName = songName + token;
				}
				
			}
		}
		return songName;
	}

	public DefaultListModel<MusicItem> getHasDupesModel() {
		return hasDupesModel;
	}
	
}
