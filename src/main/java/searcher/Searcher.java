package searcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class is used to find all the duplicate musicFiles in the search folder
 * hierarchy.
 * 
 * @author joefischer
 *
 */
public class Searcher {

	private List<MusicItem> searchList;
	private List<MusicItem> hasDupesList;
	private String[] extensionsNames = { "ape", "tta", "mp3", "aac", "ogg", "flac", "alac", "wav", "aiff", "dsd", "pcm",
			"wma" };
	private DefaultListModel<MusicItem> hasDupesModel = new DefaultListModel<MusicItem>();

	

	/**
	 * Searches the searchFolder and nested folders recursively for musicFiles. A
	 * list ofMusicItems for all music files in the folder hierarchy is created. For
	 * each item in the list, a search is made again through the list for items with
	 * similar filenames. As duplicate MusicItems are found they are added to the
	 * dupeList for that MusicItem. When a MusicItem being searched has one or more
	 * duplicates, it is added to the hasDupesList.
	 *
	 * @param searchFolder
	 * 
	 * @return The hasDupesList that is rendered in the top half of the application
	 *         window.
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
	 * 
	 * @param searchFolder
	 * @return A List containing all the MusicItems nested under the searchFolder.
	 */
	private List<MusicItem> buildList(File searchFolder) {
		File[] folderFiles;
		List<MusicItem> searchList = new ArrayList<MusicItem>();
		if (searchFolder.exists() && searchFolder.isDirectory()) {
			folderFiles = searchFolder.listFiles();
			for (File file : folderFiles) {
				if (file.isDirectory()) {
					searchList.addAll(buildList(file));
				} else {
					List<String> extensions = Arrays.asList(this.extensionsNames);
					String[] tokens = file.getName().split("\\.");
					if (tokens.length == 2) {

						if (extensions.contains(tokens[1])) {
							MusicItem item = new MusicItem();
							item.setMusicFile(file);
							item.setTrackName(parseForTrackName(item));
							searchList.add(item);
						}
					}
				}
			}
		}
		return searchList;
	}

	/**
	 * Called on each MusicItem. A search is made through all MusicItems for
	 * filenames that are similar
	 * 
	 * @param item MusicItem object
	 */
	private void dupeSearch(MusicItem item) {
		for (MusicItem mItem : searchList) {
			if (mItem.getTrackName().equals(item.getTrackName())
					&& !(mItem.getMusicItemFileName().matches(".*Track.*"))) {
				// don't addDupe for same music file
				if (!(item.equals(mItem))) {
					item.addDupe(mItem.getMusicFile());
					item.setHasDupes(true);
					if (!(hasDupesList.contains(item))) {
						hasDupesList.add(item);
					}
				}
			}
		}
	}

	/**
	 * Parses a MusicItems fully qualified filename to form a search String that is
	 * used to find similar file names the search String is a sub-string of the
	 * filename that contains just enough text to identify the song. <br>
	 * For example:<br>
	 * 
	 */
	private String parseForTrackName(MusicItem item) {

		String fileName = item.getMusicItemFileName();
		String[] tokens = fileName.split("\\.");
		String trackName = "";
		if (tokens.length == 2) {
			String track = tokens[0];
			if (track.matches(".*Track.*")) {
				trackName = track;
			} else {
				/*
				 *  remove track number
				 */
				//If name starts with 2 digits then a hyphen and 2 more digits followed by non-digit characters
				if (track.matches("[0-9][0-9]\\-[0-9][0-9].*")) {
					trackName = track.replaceFirst("[0-9][0-9]\\-[0-9][0-9]- ", "");
				}
				//If name starts with a digit character followed by non digit 
				else if (track.matches("[0-9][^0-9].*")) {
					trackName = track.replaceFirst("[0-9]", "");
				}
				//If name starts with 2 digit characters followed by non digit 
				else if (track.matches("[0-9][0-9][^0-9].*")) {
					trackName = track.replaceFirst("[0-9][0-9]\\- ", "");
				}
				//If name starts with 3 digit characters followed by non digit 
				else if (track.matches("[0-9][0-9][0-9][^0-9].*")) {
					trackName = track.replaceFirst("[0-9][0-9][0-9]\\-", "");
				}
				
				/*
				 * If Track number is in middle of string. 
				 * THis happens when the album and or artist name is 
				 * the tracks prefix
				 */
				if (track.matches("[^0-9].*")) {
					//The name starts with non-digit characters has a single digit character followed by non-digit characters
					if (track.matches("[^0-9]*[0-9][^0-9].*")) {
						trackName = track.replaceFirst("[0-9]", "");
					}
					//The name starts with non-digit characters has 2 digit characters followed by non-digit characters
					else if (track.matches("[^0-9]*[0-9][0-9][^0-9].*")) {
						trackName = track.replaceFirst("[0-9][0-9]", "");
					}
					//The name starts with non-digit characters has 3 digit characters followed by non-digit characters
					else if (track.matches("[^0-9]*[0-9][0-9][0-9][^0-9].*")) {
						trackName = track.replaceFirst("[0-9][0-9][0-9]", "");
					}
					//Does not contain a track number
					else if (!(track.matches(".*[0-9].+.*"))) {
						trackName = track;
					}
				}
				
			}
			trackName = trackName.trim();
			return trackName;
		} else {
			trackName = fileName;
			return trackName;
		}

	}

	public DefaultListModel<MusicItem> getHasDupesModel() {
		return hasDupesModel;
	}

}
