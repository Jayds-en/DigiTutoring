import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JTable;

//JAYDEN MAREE
public class Database {

	// variables
		private Connection connection = null;
		private Statement statement = null;
		private ResultSet resultSet = null;
		private String msAccDB = "DigiDatabase.accdb";
		private String dbURL = "jdbc:ucanaccess://" + msAccDB;
		private String driver ="net.ucanaccess.jdbc.UcanaccessDriver";
		private int timetableIDAutoNumber,subjectsIDAutoNumber;
		
		public Database(){ // this gets the connection
			
			// Step 1: Loading or registering Oracle JDBC driver class
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException cnfex) {
				System.out.println("Problem in loading or registring MS Access JDBC driver");
				cnfex.printStackTrace();
			}
			
			//Step2: Opening database connection/connect to database
			try{		
				
				//Step2.A: Create and get connection using DriverManager class
				connection = DriverManager.getConnection(dbURL);
				//Step2/B: Creating jdbc statement
				statement = connection.createStatement();
				
				//these resultSets instantiate the autoNumbers which act as primary key's in the 'PAT Database'.
				resultSet = statement.executeQuery("SELECT MAX(TimetableID)FROM tblTimetable;");
				resultSet.next();
				timetableIDAutoNumber = resultSet.getInt(1)+1;
				resultSet = statement.executeQuery("SELECT MAX(SubjectID)FROM tblSubjects;");
				resultSet.next();
				subjectsIDAutoNumber = resultSet.getInt(1)+1;
				resultSet = null;
				
			}catch(SQLException sqlex){
				sqlex.printStackTrace();}
		}
		
		//used in registration only
		public void insertUser (String uName, String pWord, String fction) throws SQLException{
			if((!uName.isEmpty()&&!pWord.isEmpty()&&!fction.isEmpty())){
				statement.executeUpdate("INSERT INTO tblUSER(Username,Password,Faction) VALUES('"+uName+"','"+pWord+"','"+fction+"');");
			}	
			if(fction.equals("Tutor")){
				statement.executeUpdate("UPDATE tblUSER SET NeedsAdminApproval = true WHERE Username = '"+uName+"';");
			}
		}
		//deletes any user and all their associated tables.
		public void deleteUser(String usrName) throws SQLException, IOException{
				
			if (getFaction(usrName).equals("Student")&& isPopulated(usrName)){
				resultSet = statement.executeQuery("SELECT PicturePath FROM tblUser WHERE Username = '"+usrName+"';");
				resultSet.next();
				Path picture = Paths.get(resultSet.getString(1));
				System.out.println(picture.toString());
				java.nio.file.Files.delete(picture);
				
				//timetables have been removed for students
//				resultSet = statement.executeQuery("SELECT TimetableID FROM tblUser WHERE Username = '"+usrName+"';");
//				resultSet.next();
//				int timetableID = resultSet.getInt(1);
//				statement.executeUpdate("DELETE * FROM tblTimetable WHERE TimetableID = '"+timetableID+"';");
				
				deleteUserSubjectList(usrName);
				
				resultSet = statement.executeQuery("SELECT COUNT('"+usrName+"') FROM tblPeer");
				if(resultSet.next()&& resultSet.getInt(1)>0){
					statement.executeUpdate("DELETE * FROM tblPeer WHERE Username = '"+usrName+")';");
				}

				statement.executeUpdate("DELETE * FROM tblUser WHERE Username = '"+usrName+")';");
				
			}else if (getFaction(usrName).equals("Tutor")&& isPopulated(usrName)){
				
				resultSet = statement.executeQuery("SELECT PicturePath FROM tblUser WHERE Username = '"+usrName+"';");
				resultSet.next();
				Path picture = Paths.get(resultSet.getString(1));
				System.out.println(picture.toString());
				java.nio.file.Files.delete(picture);
				
				resultSet = statement.executeQuery("SELECT TimetableID FROM tblUser WHERE Username = '"+usrName+"';");
				resultSet.next();
				int timetableID = resultSet.getInt(1);
				statement.executeUpdate("DELETE * FROM tblTimetable WHERE TimetableID = '"+timetableID+"';");
				
				deleteUserSubjectList(usrName);
				
				resultSet = statement.executeQuery("SELECT COUNT('"+usrName+"') FROM tblPeer");
				if(resultSet.next()&& resultSet.getInt(1)>0){
					statement.executeUpdate("DELETE * FROM tblPeer WHERE Username = '"+usrName+")';");
				}
				statement.executeUpdate("DELETE * FROM tblUser WHERE Username = '"+usrName+"';");
				
			}else {
				//this 'else' statement will delete admin users regardless of database status 'isPopulated'
				//this 'else' statement will also delete any unfinished registrations.
				statement.executeUpdate("DELETE * FROM tblUser WHERE Username = '"+usrName+"';");
			}
		}
		
		public void updateTutor (String username,String name,String surname,String bDate,String grade,String[] subjectArray,String cphone,String filePath,JTable timetable,int timetableID) throws SQLException
		{
			statement.execute("UPDATE tblUser SET Name = '"+ name +"' WHERE Username = '"+username+"';");
			statement.execute("UPDATE tblUser SET Surname = '"+ surname +"' WHERE Username = '"+username+"';");			
			statement.execute("UPDATE tblUser SET DateofBirth = #"+ bDate +"# WHERE Username = '"+username+"';");
			statement.execute("UPDATE tblUser SET Grade = '"+ grade +"' WHERE Username = '"+username+"';");
			statement.execute("UPDATE tblUser SET Cellphone = '"+ cphone +"' WHERE Username = '"+username+"';");
			statement.execute("UPDATE tblUser SET PicturePath = '"+ filePath +"' WHERE Username = '"+username+"';");
			for (int i = 0; i < timetable.getRowCount(); i++) {
				for (int j = 1; j < timetable.getColumnCount(); j++) {
					statement.executeUpdate("UPDATE tblTimetable\nSET ["+timetable.getColumnName(j)+"] = '"+ timetable.getValueAt(i, j) +"' WHERE WeekDay = '"+timetable.getValueAt(i, 0)+"' AND TimetableID = '"+timetableID+"';");
					//System.out.println("UPDATE tblTimetable\nSET ["+timetable.getColumnName(j)+"] = '"+ timetable.getValueAt(i, j) +"' WHERE WeekDay = '"+timetable.getValueAt(i, 0)+"' AND TimetableID = '"+timetableID+"';");
				}
			}
			deleteUserSubjectList(username);
			insertUserSubjectList(username,subjectArray);
			
		}
		public void updateAdmin (String username,String name,String surname,String bDate,String grade,String cphone) throws SQLException
		{
			statement.execute("UPDATE tblUser SET Name = '"+ name +"' WHERE Username = '"+username+"';");
			statement.execute("UPDATE tblUser SET Surname = '"+ surname +"' WHERE Username = '"+username+"';");			
			statement.execute("UPDATE tblUser SET DateofBirth = #"+ bDate +"# WHERE Username = '"+username+"';");
			statement.execute("UPDATE tblUser SET Grade = '"+ grade +"' WHERE Username = '"+username+"';");
			statement.execute("UPDATE tblUser SET Cellphone = '"+ cphone +"' WHERE Username = '"+username+"';");
		}
		public void updateStudent (String username,String name,String surname,String bDate,String grade,String[] subjectArray, String cphone, String filePath) throws SQLException
		{	
			statement.execute("UPDATE tblUser SET Name = '"+ name +"' WHERE Username = '"+username+"';");
			statement.execute("UPDATE tblUser SET Surname = '"+ surname +"' WHERE Username = '"+username+"';");			
			statement.execute("UPDATE tblUser SET DateofBirth = #"+ bDate +"# WHERE Username = '"+username+"';");
			statement.execute("UPDATE tblUser SET Grade = '"+ grade +"' WHERE Username = '"+username+"';");
			statement.execute("UPDATE tblUser SET Cellphone = '"+ cphone +"' WHERE Username = '"+username+"';");
			statement.execute("UPDATE tblUser SET PicturePath = '"+ filePath +"' WHERE Username = '"+username+"';");
			deleteUserSubjectList(username);
			insertUserSubjectList(username,subjectArray);
		}

		public void insertUserTable(String username,JTable timetable) throws SQLException{
			String [] weekDay = {"Monday","Tuesday","Wednesday","Thursday","Friday"};
			for (int i = 0; i < timetable.getRowCount(); i++) {
				statement.executeUpdate("INSERT INTO tblTimetable(TimetableID,WeekDay) Values ('"+timetableIDAutoNumber+"','"+weekDay[i]+"');");
			}
			statement.executeUpdate("UPDATE tblUser\nSET TimetableID = '"+ timetableIDAutoNumber +"' WHERE Username = '"+username+"';");
			timetableIDAutoNumber++;
		}
		public void updateUserTable(String username,JTable timetable) throws SQLException{
			int timetableID = getTimetableID(username);
			for (int i = 0; i < timetable.getRowCount(); i++) {
				for (int j = 1; j < timetable.getColumnCount(); j++) {
					statement.executeUpdate("UPDATE tblTimetable\nSET ["+timetable.getColumnName(j)+"] = '"+ timetable.getValueAt(i, j) +"' WHERE WeekDay = '"+timetable.getValueAt(i, 0)+"' AND TimetableID = '"+timetableID+"';");
					//System.out.println("UPDATE tblTimetable\nSET ["+timetable.getColumnName(j)+"] = '"+ timetable.getValueAt(i, j) +"' WHERE WeekDay = '"+timetable.getValueAt(i, 0)+"' AND TimetableID = '"+timetableID+"';");
				}
			}
		}
		public boolean getTableData(String usrName,int row ,int column) throws SQLException {
			resultSet = statement.executeQuery("SELECT TimetableID FROM tblUser WHERE Username = '"+usrName+"';");
			resultSet.next();
			int timetableID = resultSet.getInt(1);// gets the timetable ID for the user.
			
			String [] weekDay = {"Monday","Tuesday","Wednesday","Thursday","Friday"};
			String [] timeSlot = {"WeekDay", "2:00 - 3:00", "3:00 - 4:00", "4:00 - 5:00"};
			resultSet = statement.executeQuery("SELECT ["+timeSlot[column]+"] FROM tblTimetable WHERE WeekDay = '"+weekDay[row]+"' AND TimetableID ='"+timetableID+"';");
			resultSet.next();
			return resultSet.getBoolean(1);// the selected values for each row and column and timetableID
		}
		
		public void insertUserSubjectList(String username, String[] subjectArray) throws SQLException{
			for (int i = 0; i < subjectArray.length; i++) {
				statement.executeUpdate("INSERT INTO tblSubjects(SubjectID,Subject) Values ('"+subjectsIDAutoNumber+"','"+subjectArray[i]+"');");
			}
			statement.executeUpdate("UPDATE tblUser SET SubjectID = '"+ subjectsIDAutoNumber +"' WHERE Username = '"+username+"';");
			subjectsIDAutoNumber++;
		}
		public void deleteUserSubjectList(String username) throws SQLException{
			resultSet = statement.executeQuery("SELECT SubjectID FROM tblUser WHERE Username = '"+username+"';");
			resultSet.next();
			int subjectID = resultSet.getInt(1);
			statement.executeUpdate("DELETE * FROM tblSubjects WHERE SubjectID = '"+subjectID+"';");
		}
		
		public void approveTutor(String username)throws SQLException{
			statement.executeUpdate("UPDATE tblUSER SET NeedsAdminApproval = false WHERE Username = '"+username+"';");
		}
		public void insertPeerRelation(String tutorUsername, String studentUsername, boolean newValue) throws SQLException {
			statement.executeUpdate("INSERT INTO tblPeer (IsRequestingTutoring,TutorUsername,StudentUsername) VALUES ('"+newValue+"','"+tutorUsername+"','"+studentUsername+"');");
		}
		public void updatePRRequest(String tutorUsername,String studentUsername, boolean newValue) throws SQLException{
			statement.executeUpdate("UPDATE tblPeer SET IsRequestingTutoring = '"+newValue+"' WHERE StudentUsername ='"+studentUsername+"' AND TutorUsername ='"+tutorUsername+"';");
		}
		public void deletePeerRelation(String tutorUsername, String studentUsername) throws SQLException {
			statement.executeUpdate("DELETE * FROM tblPeer WHERE TutorUsername = '"+tutorUsername+"' AND StudentUsername = '"+studentUsername+"';");
		}
		
		public String getPassword(String usrName) throws SQLException{
			resultSet = statement.executeQuery("SELECT Password FROM tblUser WHERE Username = '" + usrName +"';");
			resultSet.next();
			return resultSet.getString(1);
		}
		public String getFaction(String usrName) throws SQLException{
			resultSet = statement.executeQuery("SELECT Faction FROM tblUser WHERE Username = '" + usrName +"';");
			resultSet.next();
			return resultSet.getString(1);
		}
		public String getName(String usrName) throws SQLException{
			resultSet = statement.executeQuery("SELECT Name FROM tblUser WHERE Username = '" + usrName +"';");
			resultSet.next();
			return resultSet.getString(1);		
		}
		public String getSurname(String usrName) throws SQLException{
			resultSet = statement.executeQuery("SELECT Surname FROM tblUser WHERE Username = '" + usrName +"';");
			resultSet.next();
			return resultSet.getString(1);		
		}
		public String getBDay(String usrName) throws SQLException{
			resultSet = statement.executeQuery("SELECT DateofBirth FROM tblUser WHERE Username = '" + usrName +"';");
			resultSet.next();
			return resultSet.getString(1);		
		}
		public String getGrade(String usrName) throws SQLException{
			resultSet = statement.executeQuery("SELECT Grade FROM tblUser WHERE Username = '" + usrName +"';");
			resultSet.next();
			return resultSet.getString(1);		
		}
		public String getCellphone(String usrName) throws SQLException{
			resultSet = statement.executeQuery("SELECT Cellphone FROM tblUser WHERE Username = '" + usrName +"';");
			resultSet.next();
			return resultSet.getString(1);		
		}
		public String getPicture(String usrName) throws SQLException{
			resultSet = statement.executeQuery("SELECT PicturePath FROM tblUser WHERE Username = '" + usrName +"';");
			resultSet.next();
			return resultSet.getString(1);
		}
		public String getSubjects(String usrName) throws SQLException{
			resultSet = statement.executeQuery("SELECT SubjectID FROM tblUser WHERE Username = '" + usrName +"';");
			resultSet.next();
			resultSet = statement.executeQuery("SELECT Subject FROM tblSubjects WHERE SubjectID = '" + resultSet.getInt(1) +"';");
			String temp = "";
			while(resultSet.next()){
				temp += resultSet.getString(1)+",";
			}
			return temp;		
		}
		public String getAge(String usrName) throws SQLException{//this is only returning age based on year
			resultSet = statement.executeQuery("SELECT YEAR(NOW()) - YEAR(DateOfBirth)FROM tblUser WHERE Username = '"+usrName+"'");
			resultSet.next();
			return resultSet.getString(1);
		}
		
		public String getRequestingStudents(String username) throws SQLException {
			resultSet = statement.executeQuery("SELECT StudentUsername FROM tblPeer WHERE TutorUsername = '" + username +"' AND IsRequestingTutoring = true ;");
			String temp = "";
			while(resultSet.next()){
				temp += resultSet.getString(1)+",";
			}
			return temp;	
		}
		public String getUserStudents(String username) throws SQLException {
			resultSet = statement.executeQuery("SELECT StudentUsername FROM tblPeer WHERE TutorUsername = '" + username +"' AND IsRequestingTutoring = false;");
			String temp = "";
			while(resultSet.next()){
				temp += resultSet.getString(1)+",";
			}
			return temp;			
		}
		public String getAllStudents() throws SQLException {
			resultSet = statement.executeQuery("SELECT Username FROM tblUser WHERE Faction = 'Student';");
			String temp = "";
			while(resultSet.next()){
				temp += resultSet.getString(1)+",";
			}
			return temp;			
		}

		public String getProspectiveTutors() throws SQLException {
			resultSet = statement.executeQuery("SELECT Username FROM tblUser WHERE Faction = 'Tutor' AND NeedsAdminApproval = true;");
			String temp = "";
			while(resultSet.next()){
				temp += resultSet.getString(1)+",";
			}
			return temp;			
		}
		public String getUserTutors(String username) throws SQLException {
			resultSet = statement.executeQuery("SELECT TutorUsername FROM tblPeer WHERE StudentUsername = '" + username +"' AND IsRequestingTutoring = false;");
			String temp = "";
			while(resultSet.next()){
				temp += resultSet.getString(1)+",";
			}
			return temp;	
		}
		public String getAllTutors() throws SQLException {
			resultSet = statement.executeQuery("SELECT Username FROM tblUser WHERE Faction = 'Tutor' AND NeedsAdminApproval = false;");
			String temp = "";
			while(resultSet.next()){
				temp += resultSet.getString(1)+",";
			}
			return temp;			
		}

		public int getTimetableID(String usrName) throws SQLException{
			resultSet = statement.executeQuery("SELECT TimetableID FROM tblUser WHERE Username = '" + usrName +"';");
			resultSet.next();
			return resultSet.getInt(1);
		}
		public int getSubjectID(String usrName) throws SQLException{
			resultSet = statement.executeQuery("SELECT SubjectID FROM tblUser WHERE Username = '" + usrName +"';");
			resultSet.next();
			return resultSet.getInt(1);
		}
		public int countUsername (String usrName) throws SQLException{
			resultSet = statement.executeQuery("SELECT COUNT(Username) FROM tblUser WHERE Username = '" + usrName + "';");
			resultSet.next();
			 return resultSet.getInt(1);
		}
		public boolean isPopulated(String usrName)throws SQLException{
			resultSet = statement.executeQuery("SELECT Name FROM tblUser WHERE Username = '"+usrName+"';");
			resultSet.next();
				if (resultSet.getObject(1)== null){
					return false;
				}else return true;
		}
		public boolean isPaired(String tutorUsername, String studentUsername) throws SQLException {
			resultSet = statement.executeQuery("SELECT TutorUsername,StudentUsername FROM tblPeer WHERE TutorUsername = '" + tutorUsername +"' AND StudentUsername = '" + studentUsername +"';");
			resultSet.next();
			try{
				if (resultSet.getObject(1)== null){
					return false;
				}else return true;
			}catch(SQLException k){return false;}
		}
		
		//this closes the database connection, is runned in Main's main ShutDownHook
		public void closeConnection(){
			//Step3: Closing database connection
			try{
				if(null!= connection){
					//cleanup resources, once after processing
					resultSet.close();
					statement.close();
					//and then finally close connection
					connection.close();}
				
			}catch(SQLException sqlex) {
				sqlex.printStackTrace();
				}
		}
		
	
	
	
	
		
		


}
