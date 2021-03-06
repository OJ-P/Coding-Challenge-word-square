import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException; 

	public class Run_word_square { 
//================================================================================================//
		private static TrieNode TrieNode = new TrieNode();
//================================================================================================//		
		public static void main(String[] args) throws IOException {
			
			// Call function to load main menu.
			load_menu();	
		}
//================================================================================================//		
		public static Boolean validate_input(String input) {
			
			// Set input to lower case and strip all white spaces from input.
			input = input.toLowerCase();
			input = input.replaceAll("\\s+","");
			
			// Create variable set to length of input.
			int inputLength = input.length();

			// Create char array from input string.
			char[] inputArray = input.toCharArray();

			// first value has to be number and not zero.
			if (Character.isDigit(inputArray[0]) == false || inputArray[0] == 0) {
				System.out.println("invalid input. First character must be a number and not zero.");
				return false;
			}
			
			// Input length has equal the value of first number squared.
			int numValue = Character.getNumericValue(inputArray[0]);
			if ((inputLength - 1) != numValue * numValue) {
				System.out.println("invalid input. You must enter the correct number of characters.");
				return false;
			}
			
			// All values in array other than the first have to be letters.
			for (int i = 1; i < inputLength; i++) {
				if (Character.isLetter(inputArray[i]) == false) {
					System.out.println("invalid input. All characters after inital number must be letters.");
					return false;
				}
			}
			// If input is valid return function.
			return true;
		}
//================================================================================================//
		public static HashMap<String, Integer> create_dictionary() throws IOException  {
			
			HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
			
			// Open Buffered reader to read dictionary file.
			try(BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"))) {
				
				// Populate Hash map with words and word lengths.
				String fileLine;
			    while ((fileLine = reader.readLine()) != null) {
			    	dictionary.put(fileLine, fileLine.length());
			    }
			} return dictionary;
		}
//================================================================================================//
		public static void create_wordsquare(String validInput) throws IOException {
			
			// Create array from input and grab number value for word length.
			char[] inputArray = validInput.toCharArray();
			int numValue = Character.getNumericValue(inputArray[0]);
			
			// Create array list to fill with all the valid-length words from dictionary.
			ArrayList<String> validWords = new ArrayList<String>();
			
			// Creates dictionary file.
			HashMap<String, Integer> dictionary = create_dictionary();
			
			// Populate the array List with words of same length as specified integer (numValue).
			for (String i : dictionary.keySet()) {
				if (dictionary.get(i) == numValue) {
					validWords.add(i);
				}
			}
			
			// Create array list to fill with all the usable words from the letters provided and dictionary.
			ArrayList<String> usableWords = new ArrayList<String>();
			
			// Create ArrayList from "inputArray" to use during population of ArrayList "usableWords" (as elements can be easily removed).
			ArrayList<Character> inputArrayList = new ArrayList<Character>();

			// Counter to keep track of used letters.
			int count;
			
			// Populate array of usable words:
			// iterates through list of all valid words.
			for (int d = 0; d < validWords.size(); d++) {
				
				// populate array with available letters to create usable words from.
				inputArrayList.clear();
				populateArray(inputArrayList, numValue, inputArray);
				
				// Set counter to zero.
				count = 0;
				
				// iterates through number of characters in each valid word.
				for (int e = 0; e < (numValue); e++) {		
					
					// sets variable "letter" to the current character in the current valid word.
					char letter = validWords.get(d).charAt(e);			         
			        
					// iterates through list of valid letters against the current character of the current valid word.
					for (int f = 0; f < (numValue * numValue); f++) {
			        	
						// If the letter from the valid word matches the letter in the array iterate count, set that element to null and break out of loop.						
						if (inputArrayList.get(f) != null) {
							if (inputArrayList.get(f) == letter) {		   			    
								count++;
								inputArrayList.set(f, null);
								break;								
					        } 		
						}																	
			        }
					
					// On last letter of word check if it is a valid usable word and if so add it to the array.
					if (e == (numValue - 1)) {
		        		if (count == numValue) {		        			
		        			usableWords.add(validWords.get(d));
		        			inputArrayList.clear();
		        		} 
		        	}		
				}
			}
			
			// Re-populate the character array to get rid of any remaining null values and create a char array with its contents.
			char[] charArray = new char[inputArrayList.size()];
			inputArrayList.clear();
			populateArray(inputArrayList, numValue, inputArray);
			for (int i = 0; i  < inputArrayList.size(); i++) {
				charArray[i] = inputArrayList.get(i);
			}
			
			// Set sorted array to a string (alphabetical order).
			Arrays.sort(charArray);
			String characters = new String(charArray);
			
			// Set usable words to regular array to send to word square method.
			String[] wordsForSquare = usableWords.toArray(new String[usableWords.size()]);
			
			// Call method to create all possible words squares with the usable words passed to it.
			List<List<String>> wordSquares = TrieNode.wordSquares(wordsForSquare);
			
			// Variables to hold words and count.
			String square = "";
			count = 0;
			
			// Iterate through the list of possible word squares.
			for (int s = 0; s  < wordSquares.size(); s++) {
				
				// Iterate through each word in the current square.
				for (int w = 0; w  < numValue; w++) {
				
					// Build string of all words from current square.
					square = square + wordSquares.get(s).get(w);
					
					// If all the words from current square are in the string.
					if (square.length() == (numValue * numValue)) {
						
						// Create sorted string to compare to sorted character string.
						char[] squareArray = square.toCharArray();
						Arrays.sort(squareArray);
						square = new String(squareArray);
						
						// If all letters specified by user are used and both alphabetically sorted strings match it is a valid word square.
						if (characters.equals(square) == true) {
							System.out.println("Valid word square:");
							System.out.println("");
							for (int x = 0; x  < numValue; x++) {
								System.out.println(wordSquares.get(s).get(x));
							}
						}
						
						// Reset String for the next word square.
						square = "";
					}
					
				}
			}	
	    } 		
//================================================================================================//		
		public static ArrayList<Character> populateArray(ArrayList<Character> arrayToPopulate, int numValue, char[] inputArray) {
			
			// Function to populate array list with allowed characters.
			for (int c = 1; c <= (numValue * numValue); c++) {
				arrayToPopulate.add(inputArray[c]);
			} return arrayToPopulate; 
		}
//================================================================================================//		
		public static void load_menu() throws IOException {

			// Prints out the main menu options.
			int select = 0;
			Scanner scan = new Scanner(System.in);   		   				
			while (select != 3) {
				 
				
				System.out.println("Main Menu:");
				System.out.println("=====================");
				System.out.println("1. Instructions");
				System.out.println("2. Create word square");
				System.out.println("3. Quit");
				System.out.println("=====================");
				System.out.println("Enter choice to proceed:");
				
				select = scan.nextInt();
				scan.nextLine(); 
				
				switch (select) {
				    case 1:			  
				    	System.out.println("This program creates a word square. To do this you must provide the program with specific input.");
				    	System.out.println("You must provide an integer, which specifies how many rows and columns the square will have. Next");
				    	System.out.println("this must be followed by a string of letters. The total amount of letters must be the same as the");
				    	System.out.println("integer squared. Example input:");
				    	System.out.println("");
				    	System.out.println("4 aaccdeeeemmnnnoo");
				    	System.out.println("");
				    	System.out.println("Press Enter to continue");
				    	System.in.read();				
				    	break;
				    case 2:			
				    	// Get user input to create word square.
						System.out.println("Enter value to create word square...");
						String userInput = scan.nextLine(); 
						
						// Validate user input until true.
						while (validate_input(userInput) == false) {
							System.out.println("Enter value to create word square...");
							userInput = scan.nextLine();
						}
						
						// Sanitise input.
						userInput = userInput.toLowerCase();
						userInput = userInput.replaceAll("\\s+","");
						
						// Call function to create the word square based on input given.
				    	create_wordsquare(userInput);  
						
						System.out.println("");
						System.out.println("Press Enter to continue");
				    	System.in.read();		 
				    	break;
				    case 3:
				        System.exit(1);
				    default:
				        System.out.println("Enter valid Option.");
			    }  
			} scan.close();
		}
//================================================================================================//
	}
	
	
	