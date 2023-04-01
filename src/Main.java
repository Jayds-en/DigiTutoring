import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JTable;

import com.toedter.calendar.JDateChooser;

//JAYDEN MAREE
//test png, jpg, gif
//Register register = (Register)new LogIn(); // what would this do?
//LogIn logIn = new Register(); what is the difference again?
public class Main {
	
	public static MasterFrame master = new MasterFrame();
	public static Database storage = new Database();
	public static String username = new String();
	public static boolean isEmpty,tooManySubjects;
	public static JFrame currentFrame ,nextFrame;
	// The 2 above JFrames were made to facilitate actions that need to occur in between frames.
	// Look in the MasterFrame class to see the arguements revolving around the 2 JFrames.
	
	
	//creates the method which starts the program
	public static void main(String[] args)  { 
		master.setMasterFrame(MasterFrame.getLogIn());
		Main.currentFrame = MasterFrame.getLogIn();//temp
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			// The shutdownhook makes sure that any non complete registrations are void.
			// It also closes the database connection. 
			// It was sourced online.
			public void run() {
				try {
					if(!storage.isPopulated(username)){
						storage.deleteUser(username);
					}
					storage.closeConnection();
				} catch (SQLException | IOException e) {
					storage.closeConnection();
					//e.printStackTrace();
					//expected to catch errors if system is closed during LogIn
				}
	        }
	    }, "Shutdown-thread"));
	}
	
	//checks the LogInGUI's fields for verification and changes the user's screen. 
	public static void btnLogInPressed(String uNameText, char[] pWord) throws Exception{
		
		boolean usernameFieldEmpty = uNameText.equals("");
		boolean passwordFieldEmpty = Arrays.equals(pWord,"".toCharArray());
		
		if (usernameFieldEmpty||passwordFieldEmpty){
			
			if (usernameFieldEmpty&&passwordFieldEmpty){
				MasterFrame.getLogIn().setLblError("Please enter your username and password."); 
			}else if (usernameFieldEmpty){
				MasterFrame.getLogIn().setLblError("Please enter your username."); 
			}else if (passwordFieldEmpty){
				MasterFrame.getLogIn().setLblError("Please enter your password"); 
			}	
			
		}else{
			
			boolean usernameValid = storage.countUsername(uNameText) == 1;
			try{
				String databasePassword = storage.getPassword(uNameText);
				boolean passwordCorrect = Arrays.equals(pWord,databasePassword.toCharArray());
				
				if (passwordCorrect&&usernameValid){
					
					Main.username = uNameText;
					isEmpty = false;// when do i use this?
					
					switch(storage.getFaction(uNameText)){// do not change the order of statements below
						
					case "Student" : 
						master.setMasterFrame(MasterFrame.getStudProfScrn());//changes screen
						currentFrame = MasterFrame.getStudProfScrn();
						MasterFrame.getStudProfScrn().setStudent(Main.username);//updates screen to show user info.
						break;
					case "Tutor" : 
						master.setMasterFrame(MasterFrame.getTutProfScrn());
						currentFrame = MasterFrame.getTutProfScrn();
						MasterFrame.getTutProfScrn().setTutor(Main.username);
						break;
					case "Admin" : 
						master.setMasterFrame(MasterFrame.getAdmnProfScrn());
						currentFrame = MasterFrame.getAdmnProfScrn();
						MasterFrame.getAdmnProfScrn().setAdmn(Main.username);
						break;
					default: 
						MasterFrame.getLogIn().setLblError("System Error:Faction not supported"); 
						break;
					}
					
				}else{
					MasterFrame.getLogIn().setLblError("Incorrect username or password.");
					MasterFrame.getLogIn().setErrorField(MasterFrame.getLogIn().getUsernameField());
					MasterFrame.getLogIn().setErrorField(MasterFrame.getLogIn().getPasswordField_1());
				}
			}
			catch(SQLException e){
				MasterFrame.getLogIn().setLblError("Incorrect username or password.");
				MasterFrame.getLogIn().setErrorField(MasterFrame.getLogIn().getUsernameField());
				MasterFrame.getLogIn().setErrorField(MasterFrame.getLogIn().getPasswordField_1());
			}
		}
	}
	//checks the RegisterGUI's fields for verification, changes the user's screen, calls method to insert user details into database.
	public static void btnRegisterPressed(String uNameText, char[] pWord_1, char[] pWord_2, String fction) throws SQLException{
		
		boolean usernameNotAvailable = storage.countUsername(uNameText) == 1;
		boolean usernameFieldEmpty = uNameText.equals("");
		boolean passwordsRetypedCorrectly = Arrays.equals(pWord_1,pWord_2);
		boolean passwordFieldsEmpty = Arrays.equals(pWord_1,"".toCharArray());
		// another way of checking if array empty is in studapplybuttonpressed method:
		// used here it would be pWord_1.length == 0;
		
		if (usernameFieldEmpty||passwordFieldsEmpty){
			
			if (usernameFieldEmpty&&passwordFieldsEmpty){
				MasterFrame.getRegister().setLblError("Please enter your username and password."); 
			}else if (usernameFieldEmpty){
				MasterFrame.getRegister().setLblError("Please enter your username."); 
			}else if (passwordFieldsEmpty){
				MasterFrame.getRegister().setLblError("Please enter your password"); 
			}	
			
		}else if (usernameNotAvailable){
			
			MasterFrame.getRegister().setLblError("Username already in use: please choose another. You do not have to enter your real name.");
			MasterFrame.getRegister().setErrorField(MasterFrame.getRegister().getUsernameField());
			
		} else if (passwordsRetypedCorrectly){
			
			storage.insertUser(uNameText, String.valueOf(pWord_1), fction);//insert into database.
			Main.username = uNameText;
			isEmpty = false;// when do i use this
			
			switch(fction){
			case "Student" : 
				master.setMasterFrame(MasterFrame.getStudProfScrn());
				currentFrame = MasterFrame.getStudProfScrn();
				break;
			case "Tutor" : 
				master.setMasterFrame(MasterFrame.getTutProfScrn());
				currentFrame = MasterFrame.getTutProfScrn();
				break;
			case "Admin" : 
				master.setMasterFrame(MasterFrame.getAdmnProfScrn());
				currentFrame = MasterFrame.getAdmnProfScrn();
				break;
			default: 
				MasterFrame.getRegister().setLblError("System Error:Faction not supported"); 
				break;
			}
			
		}else{
			
			MasterFrame.getRegister().setLblError("Passwords not entered correctly.");
			MasterFrame.getRegister().setErrorField(MasterFrame.getRegister().getPasswordField_1());
			MasterFrame.getRegister().setErrorField(MasterFrame.getRegister().getPasswordField_2());
			
		}
	}
	
	/*
	 *validates, verifies and calls method to update Student info into database.
	 */
	public static void btnStudApplyPressed(String name, String surname, JDateChooser dateChooser, String grade, String[] subjects, String cellphone,String filePath ) throws SQLException{
		
		isEmpty = name.equals("")||surname.equals("")||dateChooser.getDate() == null||grade.equals("")||subjects.length == 0||cellphone.equals("")|| filePath.equals("");
		if(isEmpty){

			if (name.equals("")){
				MasterFrame.getStudProfScrn().setLblError("Please enter your name.");
			}
			else if (surname.equals("")){
				MasterFrame.getStudProfScrn().setLblError("Please enter your surname.");
			}
			else if (dateChooser.getDate() == null ){
				MasterFrame.getStudProfScrn().setLblError("Please enter your birth date.");
			}
			else if (dateChooser.getDate().getYear()< 99 || dateChooser.getDate().getYear() > new Date().getYear()- 13 ){
				MasterFrame.getStudProfScrn().setLblError("Please enter a valid birth date.");
			}
			else if (grade.equals("(Select your grade)")){
				MasterFrame.getStudProfScrn().setLblError("Please enter your grade.");
			}			
			else if (subjects.length == 0){
				MasterFrame.getStudProfScrn().setLblError("Please enter your subjects.");
			}
			else if (subjects.length > 10){
				MasterFrame.getStudProfScrn().setLblError("Please enter 10 subjects or less");
			}
			else if (cellphone.equals("")){
				MasterFrame.getStudProfScrn().setLblError("Please enter your cellphone number.");
			}
			else if (!cellphone.matches("\\d+")){//learnt online
				MasterFrame.getStudProfScrn().setLblError("Please enter your cellphone number using only numbers 0-9.");
			}
			else if (cellphone.length() != 10){
				MasterFrame.getStudProfScrn().setLblError("Please enter a cellphone number of 10 digits.");
			}
			else if (filePath.equals("")){
				MasterFrame.getStudProfScrn().setLblError("Please submit your profile picture.");
			}
			
		}else if(storage.isPopulated(username)){
			
				MasterFrame.getCD().setCDLabel(
						"Performing this action causes you to overwrite your current account details.<br><br>"
						+ "Are you sure you want to continue?");
				Main.nextFrame = MasterFrame.getStudProfScrn();//nextFrame = currentFrame
				Main.master.setMasterFrame(MasterFrame.getCD());
				
		}else{
			MasterFrame.getStudProfScrn().update();
		}
	}
	/*
	 *validates, verifies and calls method to update Tutor info into database.
	 */
	public static void btnTutApplyPressed(String name, String surname, JDateChooser dateChooser, String grade, String[] subjects,String cellphone,String filePath,JTable timetable) throws SQLException{
		
		isEmpty = name.equals("")||surname.equals("")||dateChooser.getDate() == null||grade.equals("")||subjects.equals("")||cellphone.equals("")||filePath.equals("")||timetable.getSelectedRowCount() == 0;
		
		if(isEmpty){

			if (name.equals("")){
				MasterFrame.getTutProfScrn().setLblError("Please enter your name.");
			}
			else if (surname.equals("")){
				MasterFrame.getTutProfScrn().setLblError("Please enter your surname.");
			}
			else if (dateChooser.getDate() == null ){
				MasterFrame.getTutProfScrn().setLblError("Please enter your birth date.");
			}
			else if (dateChooser.getDate().getYear()< 99 || dateChooser.getDate().getYear() > new Date().getYear()- 13 ){
				MasterFrame.getTutProfScrn().setLblError("Please enter a valid birth date.");
			}
			else if (grade.equals("(Select your grade)")){
				MasterFrame.getTutProfScrn().setLblError("Please enter your grade.");
			}			
			else if (subjects.length == 0){
				MasterFrame.getTutProfScrn().setLblError("Please enter your subjects.");
			}
			else if (subjects.length > 10){
				MasterFrame.getTutProfScrn().setLblError("Please enter 10 subjects or less");
			}
			else if (cellphone.equals("")){
				MasterFrame.getTutProfScrn().setLblError("Please enter your cellphone number.");
			}
			else if (!cellphone.matches("\\d+")){//learnt online
				MasterFrame.getTutProfScrn().setLblError("Please enter your cellphone number using only numbers 0-9.");
			}
			else if (cellphone.length() != 10){
				MasterFrame.getTutProfScrn().setLblError("Please enter a cellphone number of 10 digits.");
			}
			else if (timetable.getSelectedRowCount() == 0){
				MasterFrame.getTutProfScrn().setLblError("Please enter your timetable.");
			}
			else if (filePath.equals("")){
				MasterFrame.getTutProfScrn().setLblError("Please submit your profile picture.");
			}
			
		}else {
			
			if(storage.isPopulated(username)){
			//	if(!(timetable.getSelectedRowCount() == 0)){
					MasterFrame.getCD().setCDLabel(
							"Performing this action causes you to overwrite your current account details.<br><br>"
							+ "Are you sure you want to continue?");
					Main.nextFrame = MasterFrame.getTutProfScrn();//nextFrame = currentFrame
					Main.master.setMasterFrame(MasterFrame.getCD());
			//	}
			}else{
				MasterFrame.getTutProfScrn().update();
			}
			
		}
	}
	/*
	 *validates, verifies and calls method to update Admin info into database.
	 */
	public static void btnAdmnApplyPressed(String name, String surname, JDateChooser dateChooser, String grade, String cellphone ) throws SQLException{
		
		isEmpty = name.equals("")||surname.equals("")||dateChooser.getDate() == null||grade.equals("")||cellphone.equals("");
		
		if(isEmpty){

			if (name.equals("")){
				MasterFrame.getAdmnProfScrn().setLblError("Please enter your name.");
			}
			else if (surname.equals("")){
				MasterFrame.getAdmnProfScrn().setLblError("Please enter your surname.");
			}
			else if (dateChooser.getDate() == null ){
				MasterFrame.getAdmnProfScrn().setLblError("Please enter your birth date.");
			}
			else if (dateChooser.getDate().getYear()< 99 || dateChooser.getDate().getYear() > new Date().getYear()- 13 ){
				MasterFrame.getStudProfScrn().setLblError("Please enter a valid birth date.");
			}
			else if (grade.equals("(Select your grade)")){
				MasterFrame.getAdmnProfScrn().setLblError("Please enter your grade.");
			}			
			else if (cellphone.equals("")){
				MasterFrame.getAdmnProfScrn().setLblError("Please enter your cellphone number.");
			}
			else if (!cellphone.matches("\\d+")){//learnt online
				MasterFrame.getStudProfScrn().setLblError("Please enter your cellphone number using only numbers 0-9.");
			}
			else if (cellphone.length() != 10){
				MasterFrame.getStudProfScrn().setLblError("Please enter a cellphone number of 10 digits.");
			}
			
		}else {
			
			if(storage.isPopulated(username)){
				
				MasterFrame.getCD().setCDLabel(
						"Performing this action causes you to overwrite your current account details.<br><br>"
						+ "Are you sure you want to continue?");
				Main.nextFrame = MasterFrame.getAdmnProfScrn();
				Main.master.setMasterFrame(MasterFrame.getCD());
				
			}else{
				MasterFrame.getAdmnProfScrn().update();
			}
			
		}
	}
}

