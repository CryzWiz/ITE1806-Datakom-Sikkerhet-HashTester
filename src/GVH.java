import java.io.File;

// Called GVH in short for "Generate or Verify Hash"
// Easy to use in the commandline - short! :)
public class GVH{
	public static void main(String args[]) {

		if(args.length == 3){												// If we have 2 arguments, we are good
			if(args[0].compareTo("ver") == 0){								// If arg1 == ver we run the verification
				File fileToWorkWith = new File(args[1]);					// Set filename to verify to be a file
				WhereAllTheMagicHappens.getVer(fileToWorkWith, args[2]);	// Pass the file to work with with the string hashfile name
			}
			else if(args[0].compareTo("hash") == 0){						// Else if arg1 == hash we run the hash
				File fileToWorkWith = new File(args[1]);					// Set filename to hash to be a file
				WhereAllTheMagicHappens.getHash(fileToWorkWith, args[2]);	// Pass the file to hash and with the string hashfile name
				
			}
			else{ // If we recived 3 arguments, but they where wrong
				System.out.println("To hash/ver hash use: [ver/hash] [file/dirToWorkWith] [FileThatHoldsHash/FileToHoldHash]");
			}
		}
		else{	// If we recived less than/or more than 3 arguments
			System.out.println("To hash/ver hash use: [ver/hash] [file/dirToWorkWith] [FileThatHoldsHash/FileToHoldHash]");
		}
		
		// Job done!
        System.out.println("Done");
        
    }
}
