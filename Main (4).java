import java.util.Scanner;
import java.io.*;
import java.util.Arrays;
class Main {
	public static void main(String[] args) throws IOException{
        
		int numPassOrUser;
		String username;

		//Initializing all the scanners and PrintWriters
		Scanner input = new Scanner (System.in);
		PrintWriter userName = new PrintWriter(new FileWriter(new File ("Username.txt"), true));
		PrintWriter passWord = new PrintWriter(new FileWriter(new File ("Password.txt"), true));
		Scanner userRead;
		Scanner passRead = new Scanner(new File ("Password.txt"));
		PrintWriter masterShows = new PrintWriter(new FileWriter(new File ("Shows.txt"), true));
		Scanner showsRead = new Scanner(new File("Shows.txt"));

		numPassOrUser = numLines(passRead);

		//Fills in the array that contains all the usernames and passwords
		String [] numUsername = new String [numPassOrUser];
		String [] numPass = new String [numPassOrUser];
		userRead = new Scanner(new File("Username.txt"));
		passRead = new Scanner(new File("Password.txt"));
		for (int i = 0; userRead.hasNextLine(); i++) {
			numUsername[i] = userRead.nextLine();
			numPass[i] = passRead.nextLine();
		}

		//Starting intro of the program
		while (true) {
			System.out.println("Welcome to ShowTime fellow user! We hope you have a pleasant time with this app!");
			System.out.println("\nDo you want to 'login' or 'create' a new account?");
			System.out.println();
			String userInput = input.nextLine();
			while (!userInput.equalsIgnoreCase("Create") && !userInput.equalsIgnoreCase("Login")){
				System.out.println("Please enter <Login> or <Create>:  ");
				userInput = input.nextLine();
			}
			if (userInput.equalsIgnoreCase("Create")) {
				creation(userName, passWord, input);
				System.out.println();
				System.out.println("If you want to login into the account just created, stop the program and re-run it. If not, then continue to login into an already existing account.");
				System.out.println();
			}
			else if (userInput.equalsIgnoreCase("Login")) {
				username = login(input, numUsername, numPass);
				break;
			}
		}
        System.out.println();
		System.out.println("Welcome to ShowTime " + username + "! Have a fun time using this app!");
		//Initializing the scanners and PrintWriters for the user File
		File userFile = new File(username + ".txt");
		PrintWriter pw = new PrintWriter(new FileWriter(userFile, true));
		Scanner userShows = new Scanner(userFile);
		if (!userFile.createNewFile() && !userFile.exists()) {
			System.out.println("Your file is not found!");
		}
		while (true) {
			System.out.println("What would you like to do? \n\nAdd (add a show to your list) \n\nEdit (edit a show within your list) \n\nRandom (Have us provide a random show for you) \n\nRank (Have us rank the shows within the database) \n\nExit (Leave the app)");
			System.out.println();
			String userResponse = input.nextLine();
			if (userResponse.equalsIgnoreCase("Add")) {
				addShow(username, pw, masterShows, showsRead, userFile, userShows);
			}
			else if (userResponse.equalsIgnoreCase("Edit")) {
				if (userFile.length() != 0) {
					editShows(username, masterShows, showsRead, userFile);
				}
				else {
					System.out.println("There is no show within your account to edit.");
				}
			}
			else if (userResponse.equalsIgnoreCase("Random")) {
				random(username, pw, showsRead,userFile);
			}
			else if (userResponse.equalsIgnoreCase("Rank")) {
				ranking(showsRead, userFile);
			}
			else if (userResponse.equalsIgnoreCase("Exit")) {
				System.out.println();
				System.out.println("Thank you for spending your time with us " + username + "! We hope you come back!");
				System.out.println();
				System.out.println("Existing the program...");
				break;
			}
			else {
				System.out.println("That is not one of our options! Please enter your desired action!");
			}
		}
		
		userRead.close();
		passRead.close();
		input.close();
		showsRead.close();
		pw.close();
		userShows.close();
		masterShows.close();
	}

	//Method Header
	//Purpose: To allow users of the program to create their own accounts.
	//Pre: A printwriter for usernames, a printwriter for passswords, a scanner for the file with usernames, a scanner for the file with passwords, a scanner for user input, an array containing the existing usernames, an array containing the existing passwords, and an int variable containing the number of passwords/usernames in the program
	//Post: none
	public static void creation (PrintWriter userName, PrintWriter passWord, Scanner input) throws IOException {
		System.out.println("\nWelcome to the registration of ShowTime! Here, you will be able to create an account to expereince this wonderful app!");
		Scanner passRead = new Scanner(new File("Password.txt"));
		int numPassOrUser = numLines(passRead);
		passRead = new Scanner(new File("Password.txt"));
		String [] numUsername = new String [numPassOrUser];
		String [] numPass = new String [numPassOrUser];
		Scanner userRead = new Scanner(new File("Username.txt"));
		passRead = new Scanner(new File("Password.txt"));
		
		for (int i = 0; userRead.hasNextLine(); i++) {
			numUsername[i] = userRead.nextLine();
			numPass[i] = passRead.nextLine();
		}
		
		System.out.println("Please enter a desired username: ");
		String username = input.nextLine();

		//Check for duplicate
		while (duplicateUser(username, numUsername)) {
			System.out.println("This username already exists within the app. Please choose another username.");
			username = input.nextLine();
		}
		//Checks for size
		while(username.length()<5) {
			System.out.println("Have at least 5 characters within your username. Pick a new username!");
			username = input.nextLine();
		}

		while (true) {
			//Checks for special characters
			if (!specialChar(username)) {
				System.out.println("Have at least one special character in your username. Pick a new username!");
				username = input.nextLine();
			}
			else {
				break;
			}
		}
		userName.println(username);
		numPassOrUser++;
		userName.flush();
		
		System.out.println("Please enter a desired password: ");
		String password = input.nextLine();

		//Check duplicate
		while (duplicateUser(password, numPass)) {
			System.out.println("This password already exists within the app. Please choose another password.");
			password = input.next();
		}
		//Check size
		while(password.length()<5) {
			System.out.println("Have at least 5 characters within your password. Pick a new password!");
			password = input.nextLine();
		}

		while (true) {
			//Check special char
			if (!specialChar(password)) {
				System.out.println("Have at least one special character in your password. Pick a new password!");
				password = input.nextLine();
			}
			else {
				break;
			}
		}
		passWord.println(password);
		passWord.flush();

		System.out.println("\nYou have finally created an account within this database!");
		System.out.println("\n");

		userName.close();
		passWord.close();
		passRead.close();	
	}

	//Method Header
	//Purpose: To allow users to enter the app using the account that they created.
	//Pre: A scanner for user input, an array containing the existing usernames, and an array containing the existing passwords.
	//Post: Returns the username that the user used in order to be used within the main method.
	public static String login (Scanner input, String [] numUsername, String [] numPass)  {
			System.out.println("\nEnter your username: ");
			String username = input.nextLine().trim();
			while (!duplicateUser(username,numUsername)) {
				System.out.println("This username does not exist within the app!");
				System.out.println("Enter your username: ");
				username = input.nextLine();
			}
			System.out.println("\nEnter your password: ");
			String password = input.nextLine().trim();
		    int line = 0;
		    for (int i = 0; i<numPass.length;i++) {
				if (numUsername[i].equals(username)) {
					line = i;
					break;
				}
			}
			while (!numPass[line].equals(password)) {
				System.out.println("This password does not match with your inputed username!");
				System.out.println("Enter your password: ");
				password = input.nextLine();
			}
			System.out.println("Login completed!");
			return username;
	}

	//Method Header *A new method not mentioned within the design phase
	//Purpose: To check if the new username and password that the user creates includes at least one special character
	//Pre: A String variable that witholds the information that the user inputs for either their password or username
	//Post: Returns either a true or a false
	public static boolean specialChar (String info) {
		String special = "!@#$%^&*()+_-=/|";
		char [] characterSpecial = info.toCharArray();

		for (int i = 0; i < characterSpecial.length; i++) {
			if (special.indexOf(characterSpecial[i]) > -1) {
				return true;
			}
		}
		return false;
	}

	//Method Header *A new method not mentioned within the design phase
	//Purpose: To check the text file with usernames to see if a dupplicate username (or account) already exists
	//Pre: A String variable for user input and a string array that contains the usernames
	//Post: (boolean) A true or a false
	public static boolean duplicateUser (String userInput, String [] numUsername) {
		for (int i = 0; i<numUsername.length; i++) {
			if (userInput.equals(numUsername[i])) {
				return true;
			}
		}
		return false;
	}

	//Method Header *A new method not mentioned within the design phase
	//Purpose: To check if the show exists within the array.
	//Pre A String variable for user Input, and a String 2-D array 
	//Post: (boolean) true or false
	public static boolean showExist (String userInput, String [][] list) {
		for (int i = 0; i<list.length;i++) {
			if (userInput.equals(list[i][0])) {
				return true;
			}
		}
		return false;
	}

	//Method Header *A new method not mentioned within the design phase
	//Purpose: Reads the lines within a given scanner of file or text file. 
	//Pre A scanner for a file or a text file
	//Post: returns an int variable called numLines
	public static int numLines (Scanner file) {
		int numLines = 0;
		while(file.hasNextLine()) {
			numLines++;
			file.nextLine();
		}
		return numLines;
	}

	//Method Header *A new method not mentioned within the design phase
	//Purpose: Reads everything within the text file or file and puts them within a desired 2-D array
	//Pre String variable for the file name, int variable for the number of lines in the text file or file, and number of the columns within the file or text file
	//Post: returns the 2-D array with information
	public static String [][] readFilesExtractMaster (String fileName, int numShowLines, int showColumns) throws FileNotFoundException {
		Scanner userShows = new Scanner(new File(fileName));
		String masterlist [][] = new String [numShowLines][showColumns];

		for (int i = 0; i<numShowLines;i++) {
			String shows = userShows.nextLine();
			String readShows [] = shows.split(" - ");
			for (int j = 0; j<showColumns; j++) {
				masterlist[i][j] = (readShows[j]);
			}
		}
		userShows.close();
		return masterlist;
	}

	//Method Header
	////Purpose: Allows the user to edit a show that exists within their account or add a show to their list of shows that only exists within the database but not the user’s account.
	//Pre: String variable for the username, PrintWriter for the shows within the user's account, PrintWriter for the master list of shows, Scanner for the textfile with the shows, and file for the specific user, Scanner for the user's textfile
	//Post: None

	public static void addShow (String username, PrintWriter pw, PrintWriter masterShows, Scanner showsRead, File userFile, Scanner userShows) throws IOException {
		Scanner input = new Scanner (System.in);
		System.out.println();
		System.out.println("Here are the available shows!");
		System.out.println("------------------------------------------------------------------------------------------------------------");
		//filling in the master show list array
		showsRead = new Scanner(new File("Shows.txt"));
		int numShowLines = numLines(showsRead);
		int showColumns = 5;
		String fileName = "Shows.txt";
		String list [][] = readFilesExtractMaster(fileName, numShowLines, showColumns);
		showsRead = new Scanner(new File("Shows.txt"));
		
		for (int i = 0; i<numShowLines;i++) {
			for (int j = 0; j<2; j++) {
				System.out.format("%-30s", list[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		//filling in the user show list array
		int numUserShowLines = numLines(userShows);
		int showColumns2 = 4;
		String fileName2 = username + ".txt";
		String userlist [][] = readFilesExtractMaster(fileName2, numUserShowLines, showColumns2);
		userShows = new Scanner(userFile);
		
		System.out.println("Do you want to add a show within a database (database) or add a show that is not within the database (notdatabase)?");
		String userResponse = input.nextLine();
		while (!userResponse.equalsIgnoreCase("database") && !userResponse.equalsIgnoreCase("notdatabase")) {
			System.out.println("Please enter a valid response!");
			userResponse = input.nextLine();
		}
		if (userResponse.equalsIgnoreCase("database")) {
			while (true) {
				System.out.println("What is the show you want to add? (CASE SENSITIVE) ");	
				String userShow = input.nextLine();
					
				if (showExist(userShow,list)) {
				    if (!showExist(userShow,userlist)) {
						System.out.println(userShow + " has been added to your account!");
						System.out.println();
						for (int i = 1; i<numShowLines;i++) {
							if (list[i][0].equals(userShow)) {
								for (int j = 0; j<4; j++) {
									list[i][3] = "0";
									pw.print(list[i][j] + " - ");
								}
								pw.println();
							}
						}
						pw.flush();
						break;
					}
					else {
						System.out.println("This show already exists within your account! Try Again");
					}
				}
				else {
					System.out.println("This show does not exist within the database! Try Again");
				}
			}
		}
		else if (userResponse.equalsIgnoreCase("notdatabase")) {
			while (true) {
				System.out.println("What is the new show you want to add?");
				String userNewShow = input.nextLine();

				if (!showExist(userNewShow,list)) {
					System.out.println("How many total episodes does this show have?");
					String eps = input.nextLine();
					pw.print(userNewShow + " - " + eps + " - " + "0" + " - " + "0" + " - ");
					pw.println();
					pw.flush();
					masterShows.println(userNewShow + " - " + eps + " - " + "0" + " - " + "0" + " - " + "0" + "");
					masterShows.flush();
					System.out.println();
					System.out.println(userNewShow + " has been added to your account!");
					System.out.println(userNewShow + " has been added to the master list!");
					System.out.println();
					break;
				}
				else {
					System.out.println("This show already exists within the database! Try Again");
				}
			} 
		}
		showsRead.close();
		userShows.close();
	}

	//Method Header
	//Purpose: Gives the user an opportunity to edit the shows within their list
	//Pre: String variable for username, PrintWriter for the master show list, Scanner for the master show list, and the userFile list
	//Post: none

	public static void editShows (String username, PrintWriter masterShows, Scanner showsRead, File userFile) throws IOException {
		Scanner input = new Scanner(System.in);
		System.out.println();
		System.out.println("Here are the shows within your account!");
		System.out.println("------------------------------------------------------");
		Scanner userShows = new Scanner(userFile);
		int numUserShowLines = numLines(userShows);
		int showColumns2 = 4;
		String fileName2 = username + ".txt";
		String userlist [][] = readFilesExtractMaster(fileName2, numUserShowLines, showColumns2);
		userShows = new Scanner(userFile);
		for (int i = 0; i<numUserShowLines;i++) {
			for (int j = 0; j<4; j++) {
				System.out.format(userlist[i][j] + " - ");
			}
			System.out.println();
		}
		showsRead = new Scanner(new File("Shows.txt"));
		int numShowLines = numLines(showsRead);
		int showColumns = 5;
		String fileName = "Shows.txt";
		String list [][] = readFilesExtractMaster(fileName, numShowLines, showColumns);
		showsRead = new Scanner(new File("Shows.txt"));
		
		System.out.println();
		PrintWriter pw;
		task:
		while (true) {
			System.out.println("What show within your account do you want to edit?");
			String userResponse = input.nextLine();
			boolean showExists = false;
			for (int i = 0; i<numUserShowLines; i++) {
				if (userlist[i][0].equals(userResponse)) {
					showExists = true;
					if (userlist[i][1].equals(userlist[i][2])) {
						System.out.println("This show already has been completed! Try Again!");
						continue task; 
					}
					String epsLeft = String.valueOf(Integer.parseInt(userlist[i][1]) - Integer.parseInt(userlist[i][2]));
					System.out.println("\nNOTE: You only have " + epsLeft + " episodes left before completing it!");
					System.out.println("\nHow many episodes have you watched so far?");
					int epsWatched = input.nextInt();
					userlist[i][2] = String.valueOf(epsWatched + Integer.parseInt(userlist[i][2]));
					System.out.println();
					System.out.println("You have now updated " + userlist[i][0] + "!");
					if (Integer.parseInt(userlist[i][2]) == Integer.parseInt(userlist[i][1])) {
						System.out.println("\nYou are now elligible to provide a rating for " + userlist[i][0] + "!");
						System.out.println("\nFrom a range of 10 - 20 (10 being the worst, 15 being ok, and 20 being AMAZING!), what would you like to rate the show?");
						String userRating = input.next();
						System.out.println("\n" + userlist[i][0]+ " has been rated!");
						System.out.println();
						userlist[i][3] = userRating;
						for (int i2 = 1; i2<numShowLines;i2++) {
							if (list[i2][0].equals(userResponse)) {
								list[i2][4] = String.valueOf(Integer.parseInt(list[i2][4]) + 1);
								list[i2][3] = String.valueOf(Integer.parseInt(list[i2][3]) + Integer.parseInt(userRating));
							}
						}
					}
					break;
				}
			}
			if (!showExists) {
				System.out.println("This show does not exist within your account! Please try again!");
				continue task;
			}
			else {
				pw = new PrintWriter(new FileWriter(userFile));
				for (int i = 0; i<numUserShowLines;i++) {
					for (int j = 0; j<4; j++) {
						pw.print(userlist[i][j] + " - ");
					}
					pw.println();
				}
				pw.close();
				masterShows = new PrintWriter(new FileWriter("Shows.txt"));
				for (int i = 0; i<numShowLines;i++) {
					for (int j = 0; j<5; j++) {
						masterShows.print(list[i][j] + " - ");
					}
					masterShows.println();
				}
				masterShows.flush();
				masterShows.close();
				break;
			}
		}
		userShows.close();
		showsRead.close();
	}

	//Method Header
	//Purpose: The program provides a random show for the user and give the user the option to add the show to their account. 
	//Pre: String variable for the username, PrintWriter for the userFile, Scanner for the master show list, and userFile itself
	//Post: none

	public static void random (String username, PrintWriter pw, Scanner showsRead, File userFile) throws IOException{
		Scanner input = new Scanner (System.in);
		
		Scanner userShows = new Scanner(userFile);
		int numUserShowLines = numLines(userShows);
		int showColumns2 = 4;
		String fileName2 = username + ".txt";
		String userlist [][] = readFilesExtractMaster(fileName2, numUserShowLines, showColumns2);
		userShows = new Scanner(userFile);
		
		showsRead = new Scanner(new File("Shows.txt"));
		int numShowLines = numLines(showsRead);
		int showColumns = 5;
		String fileName = "Shows.txt";
		String list [][] = readFilesExtractMaster(fileName, numShowLines, showColumns);
		showsRead = new Scanner(new File("Shows.txt"));
		
		pw = new PrintWriter(new FileWriter(userFile,true));
		task2:
		while (true) {
			System.out.println("\nProviding a random show for you...");
			System.out.println("\nHere is the randomly selected show!");
			//makes random number from the number of indexes or lines
			int randomIndex = (int)(Math.random()*numShowLines);
			System.out.println(list[randomIndex][0]);
			System.out.println();
			for (int i = 0; i<numShowLines; i++) {
				System.out.println("Would you like to add this show to your account or no?");
				String userResponse = input.nextLine();
				while (!userResponse.equalsIgnoreCase("yes") && !userResponse.equalsIgnoreCase("no")) {
					System.out.println("Please enter a valid response!");
					userResponse = input.nextLine();
				}
				if (userResponse.equalsIgnoreCase("yes")) {
					if (showExist(list[randomIndex][0],userlist)) {
						System.out.println(list[randomIndex][0] + " has already been added to your account! ");
						continue task2;
					}
					System.out.println(list[randomIndex][0] + " has been added to your account!");
					System.out.println();
					for (int i2 = 1; i2<numShowLines; i2++) {
						if (list[i2][0].equals(list[randomIndex][0])) {
							for (int j = 0; j<4; j++) {
								list[i2][3] = "0";
								pw.print(list[i2][j] + " - ");
							}
							pw.println();
							break;
						}
					}
					pw.flush();
					pw.close();
					break;
				}
				//case if when the user does not want to add the show provided
				else if (userResponse.equalsIgnoreCase("no")) {
					System.out.println("Do you want another random show?");
					String userResponse2 = input.nextLine();
					while (!userResponse2.equalsIgnoreCase("yes") && !userResponse2.equalsIgnoreCase("no")) {
						System.out.println("Please enter a valid response!");
						userResponse2 = input.nextLine();
					}
					if (userResponse2.equalsIgnoreCase("yes")) {
						continue task2;
					}
					else {
						break task2;
					}
				}
			}
			break;
		}
		showsRead.close();
		pw.close();
	}

    //Method Header
    //Purpose: The database will provide a ranking of its shows from highest to lowest based on number of people watched and their ratings
    //Pre: Scanner for the shows within master show list, the user's file
    //Post: none

	public static void ranking (Scanner showsRead, File userFile) throws IOException{
		System.out.println("\nHere are shows currently within this database from best to worst (based on rating)!");
		System.out.println("-----------------------------------------------------------------------------------------");
		showsRead = new Scanner(new File("Shows.txt"));
		int numShowLines = numLines(showsRead);
		int showColumns = 5;
		String fileName = "Shows.txt";
		String list [][] = readFilesExtractMaster(fileName, numShowLines, showColumns);
		showsRead = new Scanner(new File("Shows.txt"));
		
		String ratings [] = new String [numShowLines];
		String names [] = new String [numShowLines];
		
		for (int i = 0; i<numShowLines; i++) {
			ratings[i] = list[i][3];
			names[i] = list[i][0];
		}
		//ranks show within database based on rating
		for (int i = 0; i<ratings.length; i++) {
			for (int j = 0; j<ratings.length - i  - 1; j++) {
				if (ratings[j].compareTo(ratings[j + 1]) < 0) {
					String ratings2 = ratings[j+1];
					ratings[j+1] = ratings[j];
					ratings[j] = ratings2;
					String names2 = names[j+1];
					names[j+1] = names[j];
					names[j] = names2;
				}
			}
		}
		int number = 1;
		for (int i = 1; i<ratings.length;i++) {
			System.out.println(number + "." + " " + names[i] + " (Rating: " + ratings[i] + ")");
			number++;
		}
		System.out.println("\n\nHere are shows currently within this database from best to worst (based on the number of people watched)!");
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		String numWatched [] = new String [numShowLines];
		String names2 [] = new String [numShowLines];
		for (int i = 0; i<numShowLines; i++) {
			numWatched[i] = list[i][4];
			names2[i] = list[i][0];
		}
		//ranks show within database based on num of people watched
		for (int i = 0; i<numWatched.length; i++) {
			for (int j = 0; j<numWatched.length - i  - 1; j++) {
				if (numWatched[j].compareTo(numWatched[j + 1]) < 0) {
					String numWatched2 = numWatched[j+1];
					numWatched[j+1] = numWatched[j];
					numWatched[j] = numWatched2;
					String names22 = names2[j+1];
					names2[j+1] = names2[j];
					names2[j] = names22;
				}
			}
		}
		int number1 = 1;
		for (int i = 1; i<numWatched.length;i++) {
			System.out.println(number1 + "." + " " + names2[i] + " (Number of People Watched: " + numWatched[i] + ")");
			number1++;
		}
		System.out.println();
		System.out.println();
		showsRead.close();
	}
}
