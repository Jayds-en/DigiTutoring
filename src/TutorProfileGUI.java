import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import com.toedter.calendar.JDateChooser;
import javax.swing.SwingConstants;

//JAYDEN MAREE
public class TutorProfileGUI extends JFrame {

	private JPanel contentPane;
	private JLabel lblDeleteAccount;
	private JLabel lblError;
	private JLabel lblViewUsers;
	private JLabel lblName;
	private JLabel lblSurname;
	private JLabel lblDateOfBirth;
	private JLabel lblGrade;
	private JLabel lblSubject;
	private JLabel lblCellphone;
	private JLabel lblTimetable;
	private JLabel lblPictureDisplay;
	private JLabel lblProfilePicture;
	private JButton btnRemove;
	private JButton btnUndo;
	private JButton btnApply;
	private JButton btnAdd;
	private JButton btnBrowse;
	private JTextField nameField;
	private JTextField surnameField;
	private JTextField subjectField;
	private JTextField cellphoneField;
	private JScrollPane scrollPaneT;
	private JScrollPane scrollPaneSL;
	private String[] tableColumnNames;
	private String[] subjectArray;
	private String filePath = "";
	private JComboBox comboBoxGrade;
	private JDateChooser dateChooser;
	private JList subjectList;	
	private JTable timetable;
	private ImageIcon pfp;
	private DefaultListModel subjectModel;
	private JFileChooser chooser;
	private FileNameExtensionFilter filter;
	private Object[][] tableData;
	private DefaultTableModel tableModel;
	private Scanner scanner;
	
	
	

	public TutorProfileGUI() {//The GUI used by Tutors to complete their profile.
		
		//creates the TutorProfileScreen properties
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 675);
		setLocationRelativeTo(null);// this centers the JFrame.
		
		//content pane properties
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		//The label used to open the 'TutorGUI' screen.
		lblViewUsers = new JLabel("View Students");
		lblViewUsers.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblViewUsers.setForeground(SystemColor.menu);
		lblViewUsers.setBounds(10, 5, 89, 20);
		lblViewUsers.addMouseListener(new MouseAdapter() {//Allows tutor users to change to the viewing screen.
			@Override
			public void mouseEntered(MouseEvent arg0) {//when the mouse enters the text field.
				Font font = lblViewUsers.getFont();
				Map attributes = font.getAttributes();//Had to google this
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);// and this.
				lblViewUsers.setFont(font.deriveFont(attributes));
			}	
			@Override
			public void mouseClicked(MouseEvent arg0) {//when the mouse clicks on the text field
				if(!Main.isEmpty){
					try {
						Main.master.getTutScrn().setTutorGUI();
					} catch (Exception e) {
						e.printStackTrace();
					}
					Main.master.setMasterFrame(MasterFrame.getTutScrn());
					Main.currentFrame = MasterFrame.getTutScrn();
				}else{
					MasterFrame.getTutProfScrn().setLblError("Please finish entering the form.");
				}
			}	
			@Override
			public void mouseExited(MouseEvent arg0){//When the mouse leaves text field.
				lblViewUsers.setFont(new Font("Tahoma", Font.ITALIC, 13));
			}
		});
		contentPane.add(lblViewUsers);
		
		//creates the text field that stores the user's name.
		nameField = new JTextField();
		nameField.setBounds(301, 47, 120, 20);
		nameField.setColumns(10);
		contentPane.add(nameField);
		
		//creates the label to identify the name field.
		lblName = new JLabel("Name :");
		lblName.setBounds(33, 50, 46, 14);
		lblName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblName.setForeground(new Color(230, 230, 250));
		contentPane.add(lblName);
		
		//creates the text field that stores the user's surname.
		surnameField = new JTextField();
		surnameField.setColumns(10);
		surnameField.setBounds(301, 78, 120, 20);
		contentPane.add(surnameField);
		
		//creates the label to identify the surname field.
		lblSurname = new JLabel("Surname :");
		lblSurname.setForeground(new Color(230, 230, 250));
		lblSurname.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSurname.setBounds(33, 81, 58, 14);
		contentPane.add(lblSurname);
		
		//this jar file was downloaded.
		//this creates the field to enter a date of birth.
		dateChooser = new JDateChooser();		
		dateChooser.setBounds(301, 109, 120, 20);
		contentPane.add(dateChooser);
		
		//creates the label to identify the date of birth field.
		lblDateOfBirth = new JLabel("Date of Birth :");
		lblDateOfBirth.setForeground(new Color(230, 230, 250));
		lblDateOfBirth.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDateOfBirth.setBounds(33, 112, 82, 14);
		contentPane.add(lblDateOfBirth);
		
		//creates the combo box used to select a grade.
		comboBoxGrade = new JComboBox();
		comboBoxGrade.setFont(new Font("Tahoma", Font.PLAIN, 10));
		comboBoxGrade.setModel(new DefaultComboBoxModel(new String[] {"(Select your grade)", "12", "11", "10", "9", "8"}));
		comboBoxGrade.setSelectedIndex(0);
		comboBoxGrade.setBounds(301, 140, 120, 20);
		contentPane.add(comboBoxGrade);
		
		//creates the label to identify that the combo box stores grades.
		lblGrade = new JLabel("Grade :");
		lblGrade.setForeground(new Color(230, 230, 250));
		lblGrade.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblGrade.setBounds(33, 143, 46, 14);
		contentPane.add(lblGrade);
		
		//creates the text field used to store the input text for subjects.
		subjectField = new JTextField();
		subjectField.setBounds(301, 172, 120, 20);
		subjectField.setColumns(10);
		subjectField.addKeyListener(new KeyAdapter() { 
			@Override
			public void keyPressed(KeyEvent e) {
			    if (e.getKeyCode()==KeyEvent.VK_ENTER){
			    		subjectModel.addElement(subjectField.getText());
						subjectList.setModel(subjectModel);
			    }
			}
		});
		contentPane.add(subjectField);
		
		//creates the label to identify the subject field.
		lblSubject = new JLabel("<html><p>Subjects you would like to tutor :</p></html>");//sourced online
		lblSubject.setVerticalAlignment(SwingConstants.TOP);
		lblSubject.setForeground(new Color(230, 230, 250));
		lblSubject.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSubject.setBounds(32, 175, 164, 28);
		contentPane.add(lblSubject);
		
		//creates the pane to stores the entered list of subjects.
		subjectList = new JList();
		subjectList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			    if (e.getKeyCode()==KeyEvent.VK_ENTER){
			    	Object selected = subjectList.getSelectedValue();
					subjectModel.removeElement(selected);
			    	}
				}
			});
		
		//creates the scrollPane that surrounds the subjectList to allow users to scroll the list.
		scrollPaneSL = new JScrollPane();
		scrollPaneSL.setBounds(301, 204, 120, 40);
		scrollPaneSL.setViewportView(subjectList);
		contentPane.add(scrollPaneSL);
		
		//creates a default list model that helps the subjectList function.
		subjectModel = new DefaultListModel();
		
		//creates the button to transfer subjects from the subjectField to the subjectList.
		btnAdd = new JButton("ADD");
		btnAdd.setBackground(SystemColor.menu);
		btnAdd.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 8));
		btnAdd.setToolTipText("add item to list");
		btnAdd.addKeyListener(new KeyAdapter() { 
			@Override
			public void keyPressed(KeyEvent e) {
			    if (e.getKeyCode()==KeyEvent.VK_ENTER){
			    		subjectModel.addElement(subjectField.getText());
						subjectList.setModel(subjectModel);
			    }
			}
		});btnAdd.setBounds(225, 173, 66, 20);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				subjectModel.addElement(subjectField.getText());
				subjectList.setModel(subjectModel);
			}
		});
		contentPane.add(btnAdd);
			
		//creates the button to remove items from the subjectList.
		btnRemove = new JButton("Remove");
		btnRemove.setBackground(SystemColor.menu);
		btnRemove.setToolTipText("add item to list");		
		btnRemove.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 8));
		btnRemove.setBounds(225, 224, 68, 20);
		btnRemove.addKeyListener(new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
		    if (e.getKeyCode()==KeyEvent.VK_ENTER){
		    		subjectModel.addElement(subjectField.getText());
					subjectList.setModel(subjectModel);
		    }
		}
	});
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object selected = subjectList.getSelectedValue();
				subjectModel.removeElement(selected);
			}
		});
		contentPane.add(btnRemove);
		
		//creates the text field used to store the user's cellphone number.
		cellphoneField = new JTextField();
		cellphoneField.setBounds(301, 258, 120, 20);
		cellphoneField.setColumns(10);
		contentPane.add(cellphoneField);
		
		//creates the label to identify the cellphoneField.
		lblCellphone = new JLabel("Cellphone number :");
		lblCellphone.setForeground(new Color(230, 230, 250));
		lblCellphone.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCellphone.setBounds(33, 261, 110, 14);
		contentPane.add(lblCellphone);
		
		//this is code copied from an online source.
		//creates the button that allows users to upload images.
		btnBrowse = new JButton("Browse");
		btnBrowse.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnBrowse.setBackground(SystemColor.menu);
		btnBrowse.setBounds(346, 428, 75, 23);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				    chooser = new JFileChooser();//creates a new JFileChooser each time the action is performed.
				    filter = new FileNameExtensionFilter(//sets the file type filter
				        "PNG,JPG & GIF Images", "png", "jpg", "gif");
				    chooser.setAcceptAllFileFilterUsed(false);
				    chooser.setFileFilter(filter);
				    Component parent = null ;
				    int returnVal = chooser.showOpenDialog(parent);
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				    	filePath = chooser.getSelectedFile().getAbsolutePath();
				    	changeImage(filePath);
				    }	
			}
		});
		contentPane.add(btnBrowse);
		
		//creates a label to be edited in order to display the error of the user's action.
		lblError = new JLabel();
		lblError.setForeground(new Color(230, 230, 250));
		lblError.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblError.setBounds(10, 576, 214, 25);
		contentPane.add(lblError);
		
		//creates a label used to display a preview of the user's profile picture.
		lblPictureDisplay = new JLabel("Profile Picture");
		lblPictureDisplay.setForeground(SystemColor.activeCaption);
		lblPictureDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		lblPictureDisplay.setBackground(SystemColor.menu);
		lblPictureDisplay.setBounds(301, 285, 120, 140);
		lblPictureDisplay.setOpaque(true);
		contentPane.add(lblPictureDisplay);
		
		//creates the label used to identify the JFileChooser.
		lblProfilePicture = new JLabel("Profile Picture :");
		lblProfilePicture.setForeground(new Color(230, 230, 250));
		lblProfilePicture.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblProfilePicture.setBounds(33, 331, 110, 14);
		contentPane.add(lblProfilePicture);
		

		
		//creates the table used by users to input their tutoring times.
		tableData = new Object[][]{
			{"Monday", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
			{"Tuesday", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
			{"Wednesday", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
			{"Thursday", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
			{"Friday", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
		};
		
		tableColumnNames = new String[]{"Week day", "2:00 - 3:00", "3:00 - 4:00", "4:00 - 5:00"};
		
		tableModel = new DefaultTableModel(tableData,tableColumnNames){
				Class[] columnTypes = new Class[] {
						Object.class, Boolean.class, Boolean.class, Boolean.class
					};
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
					boolean[] columnEditables = new boolean[] {
						false, true, true, true
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		};
		timetable = new JTable();
		timetable.setRowSelectionAllowed(false);
		timetable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		timetable.setModel(tableModel);
		
		//creates the scrollPane that surround the timetable to allow users to see the timetable column headings.
		scrollPaneT = new JScrollPane();
		scrollPaneT.setBounds(160, 462, 261, 103);
		scrollPaneT.setViewportView(timetable);
		contentPane.add(scrollPaneT);
				
		//creates the label that identifies the timetable.
		lblTimetable = new JLabel("Timetable :");
		lblTimetable.setForeground(new Color(230, 230, 250));
		lblTimetable.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTimetable.setBounds(30, 469, 76, 14);
		contentPane.add(lblTimetable);
		
		//creates the button used to validate fields and upload them into the database.
		btnApply = new JButton("Apply");
		btnApply.setBackground(SystemColor.menu);
		btnApply.setBounds(332, 602, 89, 23);
		btnApply.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			    if (e.getKeyCode()==KeyEvent.VK_ENTER){
			   		try {
			   			subjectArray = new String[subjectModel.getSize()]; 
						for (int i = 0; i < subjectArray.length; i++) {
							subjectArray[i] = subjectModel.getElementAt(i).toString();
						}	
						Main.btnTutApplyPressed(nameField.getText(), surnameField.getText(), dateChooser, comboBoxGrade.getSelectedItem().toString(), subjectArray, cellphoneField.getText(),filePath,timetable);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
			    }
			}
		});
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					subjectArray = new String[subjectModel.getSize()]; 
					for (int i = 0; i < subjectArray.length; i++) {
						subjectArray[i] = subjectModel.getElementAt(i).toString();
					}	
					Main.btnTutApplyPressed(nameField.getText(), surnameField.getText(), dateChooser, comboBoxGrade.getSelectedItem().toString(), subjectArray, cellphoneField.getText(),filePath,timetable);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(btnApply);		
			
		//creates the button used to undo any field changes.
		btnUndo = new JButton("Undo");
		btnUndo.setBackground(SystemColor.menu);
		btnUndo.setBounds(233, 602, 89, 23);
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					setTutor(Main.username);
				} catch ( Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(btnUndo);
		
		//creates the label used to delete the account
		lblDeleteAccount = new JLabel("Delete your account ?");
		lblDeleteAccount.setForeground(SystemColor.menu);
		lblDeleteAccount.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblDeleteAccount.setBounds(10, 606, 120, 20);
		lblDeleteAccount.addMouseListener(new MouseAdapter() {//Allows tutor users to change to the viewing screen.
			@Override
			public void mouseEntered(MouseEvent arg0) {//when the mouse enters the text field.
				Font font = lblDeleteAccount.getFont();
				Map attributes = font.getAttributes();//Had to google this
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);// and this.
				lblDeleteAccount.setFont(font.deriveFont(attributes));
			}	
			@Override
			public void mouseClicked(MouseEvent arg0) {//when the mouse clicks on the text field
					MasterFrame.getCD().setCDLabel(
							"Performing this action causes a deletion of your account.<br><br>"
							+ "Are you sure you want to continue?");
					Main.nextFrame = null;
					Main.master.setMasterFrame(MasterFrame.getCD());
			}	
			@Override
			public void mouseExited(MouseEvent arg0){//When the mouse leaves text field.
				lblDeleteAccount.setFont(new Font("Tahoma", Font.ITALIC, 11));
			}
		});
		contentPane.add(lblDeleteAccount);
		
	}

	//creates the method used to load this user's profile details into the database and to save their profile pic.
	public void update() throws SQLException{
		
		//Since UCanAccess requires dates to be queried in order MM/dd/yyy, thus this does not work: 
		//String date = dateChooser.getDate().toGMTString().substring(0, 11);
		String date = (dateChooser.getDate().getMonth()+1)+"/"+dateChooser.getDate().getDate()+"/"+(dateChooser.getDate().getYear() +1900) ;
		
		boolean hasTimetable = Main.storage.getTimetableID(Main.username) > 0;
		if(!hasTimetable){
			Main.storage.insertUserTable(Main.username, timetable);
		}
		
		Path source = Paths.get(filePath);
		//all Users have unique usernames therefore we can have unique image names.
		Path destination = Paths.get("ProfilePictures//"+Main.username+".jpg");//converts all images to .jpg
	    CopyOption [] x = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.COPY_ATTRIBUTES};
	    try {
			java.nio.file.Files.copy(source, destination, x);//saves the image to the project folder.
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Main.storage.updateTutor(Main.username,nameField.getText(), surnameField.getText(), date,//next line
				comboBoxGrade.getSelectedItem().toString(), subjectArray, cellphoneField.getText(),destination.toString(),timetable,Main.storage.getTimetableID(Main.username));
		
	}
	
	//creates the method that dynamically updates the ImageIcon and displays when a new image is chosen.
	public void changeImage(String filePath) {
		
		lblPictureDisplay.setVerticalAlignment(SwingConstants.TOP);
		lblPictureDisplay.setHorizontalAlignment(SwingConstants.LEFT);

		pfp = new ImageIcon(filePath);
		pfp.setImage(pfp.getImage().getScaledInstance(120, 140,Image.SCALE_SMOOTH));
		lblPictureDisplay.setIcon(pfp);

	}
	
	//creates the method that uploads database information into the user's profile GUI.
	public void setTutor(String usrName) throws Exception{
		
		if (Main.storage.isPopulated(usrName)){
			nameField.setText(Main.storage.getName(usrName));
			surnameField.setText(Main.storage.getSurname(usrName));
			cellphoneField.setText(Main.storage.getCellphone(usrName));
			comboBoxGrade.setSelectedItem(Main.storage.getGrade(usrName));
			lblPictureDisplay.setVerticalAlignment(SwingConstants.TOP);
			lblPictureDisplay.setHorizontalAlignment(SwingConstants.LEFT);
			
	        String databaseDate = (Main.storage.getBDay(usrName).substring(0, 11));
	        DateFormat dF = new SimpleDateFormat("yyyy-MM-dd"); //learnt online
	        //parses from String to Date
	        Date date = dF.parse(databaseDate);
			dateChooser.setDate(date);
			
			subjectModel.clear();
			scanner = new Scanner(Main.storage.getSubjects(usrName));
			scanner.useDelimiter(",");
			while(scanner.hasNext()){
				subjectModel.addElement(scanner.next());	
			}
			subjectList.setModel(subjectModel);
			
			filePath = Main.storage.getPicture(usrName);
			pfp = new ImageIcon(filePath);
			pfp.setImage(pfp.getImage().getScaledInstance(120, 140,Image.SCALE_SMOOTH));
			lblPictureDisplay.setIcon(pfp);
			
			for (int row = 0; row <  5; row++) {
				for (int column = 1; column < 4; column++) {
					timetable.setValueAt(Main.storage.getTableData(usrName,row,column), row, column); 
				}
			}
			
		}else{
			MasterFrame.getTutProfScrn().setLblError("Please finish entering the form.");
		}
		
	}
	
	//creates the setter that allows users to see errors they have made.
	public void setLblError(String lbllError) {//HTML used here to get JLabel to wrap tight around text.
		this.lblError.setText("<html><p>"+lbllError+"</p></html>"); 
	}

}
