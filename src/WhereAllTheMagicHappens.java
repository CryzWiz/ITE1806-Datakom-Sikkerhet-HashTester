import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.codec.digest.DigestUtils;
// Originally I had 2 files. One for verifying hashes, and one for making them
// But I moved it all to one file, and instead will add one file for GUI control
// and one for commandline control. And let the user deside what they want to use 
// for themself.

public class WhereAllTheMagicHappens {
	
	private static String[] temp_hit_on_file;													// holder for the hits on stored hashes we might get
	public static ArrayList<String> returnStringForGui = new ArrayList<>(); 					// Return values for the GUI
	private final static boolean GUI = true; 													// GUI mode
	private final static boolean CMD = false; 													// commandline mode
	private static String HashFile;																// Holder for the hashfile	
	
	// Set mode here
	public static boolean mode = GUI;															// If you change this to CMD the program will use sysout, and not 
																								// store values to returnStringForGUI
	
	
	public static void SetMode(boolean wichMode){												// Method for changing between GUI and CMD mode  = Output form
		mode = wichMode;
	}
	public static boolean getMode(){
		return mode;
	}
	public static void getFilesAndGenerateHash(File directory){      							// Recursively search for files to hash
		File entry;                                 											// A reference to an entry in the directory
		String entryName;                           											// The full path name of a file /Not used
		
		ArrayList<File> files = new ArrayList<>(); 												// Array to hold the files
		
		if(directory.isDirectory()){															// If it is a directory, do the things below
			if(mode){ // If GUI are the mode Write this to the return array to the GUI
				returnStringForGui.add("Starting search of directory "
						+ directory.getAbsolutePath());
			}
			else { // Else use sysout
				System.out.println("Starting search of directory " 	
						+ directory.getAbsolutePath());
			}
			String contents[] = directory.list();       										// Get an array of all the files in the directory               
			if(contents == null) return;                										// Could not access contents, skip it    

			for(int i=0; i<contents.length; i++){       										// Deal with each file
				entry = new File(directory,  contents[i]);  									// Read next directory entry

				if(contents[i].charAt(0) == '.')        										// Skip the . and .. directories
					continue;

				if (entry.isDirectory()){               										// Is it a directory
					getFilesAndGenerateHash(entry);    											// Yes, enter and search it
				} 
				else {                                											// its a file, add it to the files list
					if(entry.isFile())
						files.add(entry);

				}
			}
			writeHashToFile(makehash(files));													// Lets generate some hash and write it to our hash file
		}
		else if(directory.isFile()){															// If directory is a file
			if(mode){ // If GUI are the mode Write this to the return array to the GUI
				returnStringForGui.add("Starting the check on file "
						+ directory.getAbsolutePath());
			}
			else { // else use sysout
				System.out.println("Starting the check on file " 
						+ directory.getAbsolutePath());
			}
			files.add(directory);																// Add it to the files list
			writeHashToFile(makehash(files));													// and make a hash, and write it to the hash file
		}
		else {
			if(mode){ // If GUI are the mode write this to the return array to the GUI
				returnStringForGui.add("Could not determin if this is a file/directory or if the file even exist");
				returnStringForGui.add("Remember to add the file extention if it is a file.");
			}
			else { // else use sysout
				System.out.println("Could not determin if this is a file/directory or if the file even exist");
				System.out.println("Remember to add the file extention if it is a file.");
			}
			
		}
		if(mode){ // If GUI are the mode write this to the return array to the GUI
			returnStringForGui.add("Done with " + directory.getName() + "\n");
		}
		
	}
	public static void getFilesAndVerifyHash(File directory){      								// Recursively search for files to hash
		File entry;                                 			   								// A reference to an entry in the directory
		String entryName;                           			   								// The full path name of a file /Not used
		
		ArrayList<File> files = new ArrayList<>();												// Array to hold the files
		
		if(directory.isDirectory()){															// If it is a directory
			if(mode){ // If GUI are the mode write this to the return array to the GUI
				returnStringForGui.add("Starting search of directory " 
						+ directory.getAbsolutePath());
			}
			else { // Else use sysout
				System.out.println("Starting search of directory " 
						+ directory.getAbsolutePath());
			}
			
			String contents[] = directory.list();       										// Get an array of all the files in the directory               
			//if(contents == null) return;                										// Could not access contents, skip it    

			for(int i=0; i<contents.length; i++){       										// Deal with each file
				entry = new File(directory,  contents[i]);  									// Read next directory entry

				if(contents[i].charAt(0) == '.')        										// Skip the . and .. directories
					continue;

				if (entry.isDirectory()){               										// Is it a directory
					getFilesAndVerifyHash(entry);       										// Yes, enter and search it
				} 
				else {                                											// its a file, add it to the files list
					if(entry.isFile())
						files.add(entry);

				}
			}
			verifyThatHashIsValid(files);														// Lets send the files to a method that can verify our hashes
		}
		else if(directory.isFile()){
			if(mode){ // If GUI are the mode write this to the return array to the GUI
				returnStringForGui.add("Starting the check on file " 
						+ directory.getAbsolutePath());
			}
			else { // Else use sysout
				System.out.println("Starting the check on file " 
						+ directory.getAbsolutePath());
			}
			files.add(directory);																// Add it to the files list
			verifyThatHashIsValid(files);														// And send the one file to be verified
		}
		else {
			
			if(mode){ // If GUI are the mode write this to the return array to the GUI
				returnStringForGui.add("Could not determin if this is a file/directory or if the file even exist");
				returnStringForGui.add("Remember to add the file extention if it is a file.");
			}
			else { // Else use sysout
				System.out.println("Could not determin if this is a file/directory or if the file even exist");
				System.out.println("Remember to add the file extention if it is a file.");
			}
		}
		if(mode){ // If GUI are the mode write this to the return array to the GUI
			returnStringForGui.add("Done with " + directory.getName() + "\n");
		}
	}
	private static void verifyThatHashIsValid(ArrayList<File> files) {
		File toCheck;																			// File to check
		String newHashGen;																		// new hash value to check against
		String[] toVerifyInString;																// The file and hash to verify
		String holder;																			// Just a temp holder for the temp_hit_on_file
		for(int i = 0; i < files.size(); i++){ 													// For every file we have
			toCheck = files.get(i);																// Set the toCheck to the next file
			newHashGen = makeSingle(toCheck);													// Generate a new hash from the file we have
			if(checkIfFileIsOnTheList(toCheck)){												// If the file exist in our hash file
				toVerifyInString = temp_hit_on_file; 											// Get the value stored
				if(toVerifyInString[1].compareTo(newHashGen) == 0){ 							// Compare the stored value with the new value
					if(mode){ // If GUI are the mode write this to the return array to the GUI
						returnStringForGui.add("The file " + files.get(i).getName() + " is verified!");
					}
					else { // Else use sysout
						System.out.println("The file " + files.get(i) + " is verified!"); // If they match
					}
					
				}
				else{
					if(mode){ // If GUI are the mode write this to the return array to the GUI
						returnStringForGui.add("The file " + files.get(i).getName() + " is NOT verified!");
					}
					else { // Else use sysout
						System.out.println("The file " + files.get(i) + " is NOT verified!"); // If they don't match
					}
					
				}
				
			}
			else{
				if(mode){ // If GUI are the mode write this to the return array to the GUI
					returnStringForGui.add("The file " + files.get(i).getName() + " does not have a hash stored. \n"
							+ "Make one by using the hash button.");
				}
				else{ // Else use sysout
					System.out.println("The file " + files.get(i).getName() + " does not have a hash stored. \n"
							+ "Make one by using the hash command.");
				}
						
			}
		}		
	}
	
	public static String makeSingle(File file){ 												// Lets make a hash to compare against to verify
		 String apache_sha256= ""; 																// Holder for the sha_256 hash
		 FileInputStream in;																	// Our file reader 
		try {
			in = new FileInputStream(file); 													// Read the file we are checking
			apache_sha256 = DigestUtils.sha256Hex(in);											// Generate a hash from the file
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		 
		 return apache_sha256; 																	// Return the new hash
	}
	
	public static ArrayList<String> makehash(ArrayList<File> files){
		 String apache_sha256="";																// The hash string
		 File fileToHash;																		// The file to make a hash from
		 ArrayList<String> returnValues = new ArrayList<>();									// The return string with the filename and the hash value
		 for(int i = 0; i < files.size(); i++){													// For every file
			 fileToHash = files.get(i);															// Point fileToHash to the next file in the Array
			 try {
				 FileInputStream in = new FileInputStream(fileToHash);							// Read the file to hash
				 apache_sha256 = DigestUtils.sha256Hex(in);										// Generate a hash value based on the file and content
			 }
			 catch (FileNotFoundException ex) {
				 if(mode){ // If GUI are the mode write this to the return array to the GUI
					 returnStringForGui.add("File not found");
				 }
				 else { // Else use sysout
					 System.out.println("File not found");
				 }
				 
			 }
			 catch (IOException ex) {
				 if(mode){ // If GUI are the mode write this to the return array to the GUI
					 returnStringForGui.add("Something went wrong...");
				 }
				 else{ // Else use sysout
					 System.out.println("Something went wrong...");
				 }
				 
			 }
			 
			 if(checkIfFileIsOnTheList(fileToHash) == false){										// If the file is not already in the hash-value file
				 returnValues.add(fileToHash + " " + apache_sha256);								// Set return value to be "filename space hash-value"
				 
			 }
			 else {
				 if(mode){ // If GUI are the mode write this to the return array to the GUI
					 returnStringForGui.add("The file " + fileToHash.getName() + " is already hashed");
				 }
				 else{ // Else use sysout
					 System.out.println("The file " + fileToHash + " is already hashed"); 			// If the file is already in the hash value file
				 }
				 
			 }
		 	}
		 return returnValues; 																		// Return the filename(file path) and the hash value 
	}
	
	
	private static boolean checkIfFileIsOnTheList(File fileToCheck){
		boolean isPresent = false;																	// Starting with false - The file is not in the list
		//File hashfile = new File(HashFile);														
		String toCheck = fileToCheck.getAbsolutePath(); 											// Get the absolute path to the file
		String[] line;																				// Holder to hold each line as we go forward
		Scanner scan;																				// Scanner to read the hash file
		try {
			scan = new Scanner(new File(HashFile));													// Read the hash file and make
			while(scan.hasNextLine()){																// While we still have lines left in the hash file
				line = scan.nextLine().split(" ");													// Get the next line, and split it when we find a space
				if(toCheck.contains(line[0])){														// If the hash file already have this file
					temp_hit_on_file = line;														// Set temp_hit_on_file to this filename and hash value
					isPresent = true;																// Set isPresent to true
				}
			}
			scan.close();																			// Close the scanner
		} catch (FileNotFoundException e) {
			
			//System.out.println("File holding hash values not found - Making theHashList.txt and continuing");
		}
		return isPresent;																			// Return true or false depending on we found the file to be present already
	}
	
	public static void writeHashToFile(ArrayList<String> filesAndHash) {		
		try(FileWriter fw = new FileWriter(HashFile, true); 										// Start our file writer
			    BufferedWriter bw = new BufferedWriter(fw);											// Start our buffered writer
				
				PrintWriter out = new PrintWriter(bw)){												// Finally we start out printer
					for(int i = 0; i < filesAndHash.size(); i++){									// Then, for every file
					out.println(filesAndHash.get(i));												// print it to the hash file
						if(mode){ // If GUI are the mode write this to the return array to the GUI
							returnStringForGui.add(filesAndHash.get(i) + " is hashed");
						}
						else{ // Else use sysout
							System.out.println(filesAndHash.get(i) + " is hashed");
						}
					}
				} 
		catch (IOException e) {
			if(mode){ // If GUI are the mode write this to the return array to the GUI
				returnStringForGui.add("Something went wrong....");
			}
			else{ // Else use sysout
				System.out.println("Something went wrong...");
			}
					
		}
		
	}
	public static ArrayList<String> getAnswerForGUIVer(File fileToWorkWith, String hash) { 	// method for filling the textfield in the GUI
		SetMode(GUI);
		setHashFile(hash);
		getFilesAndVerifyHash(fileToWorkWith);												// run the program - Verifying the files we can find
		return returnStringForGui;															// Return the results from the program
	}
	private static void setHashFile(String hash) { 											// Method for setting the hashfile value
		if(hash.isEmpty()){																	// If no value is added
			returnStringForGui.add("No hash file is supplied.\n"							// We only make the file if we are trying to make hashes
					+ "Making the file TheHashList.txt\n"									// Else we check if we have a TheHashList.txt file
					+ "ONLY IF YOU ARE MAKING HASHES - ELSE WE TRY TO FIND THAT FILE");		// to verify against
			HashFile = "TheHashList.txt";													// Set the value to TheHashList.txt	
		}
		else HashFile = hash;																// Else set it to the filename given by the user
		
	}
	public static void clearGUIResponse() { 												// Method for clearing the array containing the answers
		returnStringForGui.clear();															// Clear the array
	}
	public static ArrayList<String> getAnswerForGUIhash(File fileToWorkWith, String hash) { // method for filling the textfield in the GUI
		SetMode(GUI);
		setHashFile(hash);
		getFilesAndGenerateHash(fileToWorkWith);											// run the program - make hash for all the files we find
		return returnStringForGui;															// Return the results from the program
	}
	public static void getVer(File fileToWorkWith, String hash){							// Method for verifying hash in the cmd
		SetMode(CMD);																		// Set response mode to CMD
		setHashFile(hash);																	// Set the file holding the hashes
		getFilesAndVerifyHash(fileToWorkWith);												// Verify hashes from the dir/file we are working with
	}
	public static void getHash(File fileToWorkWith, String hash){							// Method for hashing in cmd
		SetMode(CMD);																		// Set response to cmd mode
		setHashFile(hash);																	// File to hold the hashes
		getFilesAndGenerateHash(fileToWorkWith);											// File to store the hashes in
	}	
}
