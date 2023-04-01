import java.awt.Font;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

//JAYDEN MAREE
public class TutorGUI extends JFrame {

	private JLabel lblPictureDisplay;
	private JLabel lblViewProfile;
	private JLabel lblGrade;
	private JLabel lblGradeNumber;
	private JLabel lblAge;
	private JLabel lblAgeNumber;
	private JLabel lblCellphone;
	private JLabel lblCNumber;
	private JLabel lblSubjects;
	private JLabel lblSurname;
	private JLabel lblName;
	private JLabel lblSurnameText;
	private JLabel lblNameText;
	private JPanel contentPane;
	private JPanel pnlTutorStudents;
	private JPanel pnlRequestingStudents;
	private JPanel pnlDisplay;
	private DefaultListModel currStudModel;
	private DefaultListModel reqStudModel;
	private DefaultListModel subjectModel;
	private JList subjectList;
	private JList listUserStudents;
	private JList listRequestingStudents;
	private JScrollPane scrollPaneTimetable;
	private JScrollPane scrollPaneSL;
	private JButton btnRemove;
	private JButton btnAccept;
	private String[] tableColumnNames;
	private String filePath;
	private Object[][] tableData;
	private DefaultTableModel tableModel;
	private JTable timetable;
	private ImageIcon pfp;
	private Scanner scanner;
	private JTabbedPane tabbedPane;
	

	public TutorGUI() {// The GUI used by Tutors.

		//creates the tutor screen properties.
		setTitle("Peer Tutor Digital");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 648, 253);
		setLocationRelativeTo(null);// this centers the JFrame.
		
		//Instantiates default list models.
		currStudModel = new DefaultListModel();
		reqStudModel = new DefaultListModel();
		subjectModel = new DefaultListModel();
		
		//content pane properties
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		//creates the label used to view the user profile.
		lblViewProfile = new JLabel("View your profile");
		lblViewProfile.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblViewProfile.setForeground(SystemColor.menu);
		lblViewProfile.setBounds(499, 11, 110, 20);
		lblViewProfile.addMouseListener(new MouseAdapter() {//Allows tutor users to change to the viewing screen.
			@Override
			public void mouseEntered(MouseEvent arg0) {//when the mouse enters the text field.
				Font font = lblViewProfile.getFont();
				Map attributes = font.getAttributes();//Had to google this
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);// and this.
				lblViewProfile.setFont(font.deriveFont(attributes));
			}	
			@Override
			public void mouseClicked(MouseEvent arg0) {//when the mouse clicks on the text field
					Main.master.setMasterFrame(MasterFrame.getTutProfScrn());
					Main.currentFrame = MasterFrame.getTutProfScrn();
			}	
			@Override
			public void mouseExited(MouseEvent arg0){//When the mouse leaves text field.
				lblViewProfile.setFont(new Font("Tahoma", Font.ITALIC, 13));
			}
		});
		contentPane.add(lblViewProfile);
		
		/*
		 * one possible way to improve the program is to give Students timetables, but that functionality was removed from the program after consideration.
		 * 
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
						false, false, false, false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		};
		
		timetable = new JTable();
		timetable.setRowSelectionAllowed(false);
		timetable.setCellSelectionEnabled(true);
		timetable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		timetable.setModel(tableModel);
		
		//creates the scrollPane that surround the timetable to allow users to see the timetable column headings.
		scrollPaneTimetable = new JScrollPane();
		scrollPaneTimetable.setBounds(329, 179, 293, 106);
		scrollPaneTimetable.setViewportView(timetable);
		contentPane.add(scrollPaneTimetable);
		*/
		
		//creates the tabbed pane that hosts JPanels
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(23, 28, 230, 144);
		contentPane.add(tabbedPane);
		
		//creates the JPanel that hosts the list 'listRequestingStudents'
		pnlRequestingStudents = new JPanel();
		pnlRequestingStudents.setLayout(null);
		tabbedPane.addTab("Requests", null, pnlRequestingStudents, null);
		
		//creates the JPanel that hosts the list 'listUserStudents'
		pnlTutorStudents = new JPanel();
		pnlTutorStudents.setLayout(null);
		tabbedPane.addTab("Your Students", null, pnlTutorStudents, null);

		//creates the JList that hosts the names of students requesting to be tutored.
		listRequestingStudents = new JList();
		listRequestingStudents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listRequestingStudents.setBounds(0, 0, 225, 116);
		listRequestingStudents.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				boolean studentSelected = !(listRequestingStudents.getSelectedValue() == null)|| !(listUserStudents.getSelectedValue()==null);
				if(studentSelected){
					String usrName = listRequestingStudents.getSelectedValue().toString();
					try {
						setPanelDisplay(usrName);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		pnlRequestingStudents.add(listRequestingStudents);
		
		//creates the JList that hosts the names of students already accepted for tutoring by the user.
		listUserStudents = new JList();
		listUserStudents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listUserStudents.setBounds(0, 0, 225, 116);
		listUserStudents.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				boolean studentSelected = !(listRequestingStudents.getSelectedValue() == null)|| !(listUserStudents.getSelectedValue()==null);
				if(studentSelected){
					String usrName = listUserStudents.getSelectedValue().toString();
					try {
						setPanelDisplay(usrName);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		pnlTutorStudents.add(listUserStudents);
		
		//creates the JPanel that hosts all the information of the selected user.
		pnlDisplay = new JPanel();
		pnlDisplay.setBounds(263, 49, 359, 123);
		pnlDisplay.setLayout(null);
		contentPane.add(pnlDisplay);
		
		//creates a label used to display a preview of the user's profile picture.
		lblPictureDisplay = new JLabel("Profile Picture");
		lblPictureDisplay.setBounds(0, 0, 105, 123);
		lblPictureDisplay.setForeground(SystemColor.activeCaption);
		lblPictureDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		lblPictureDisplay.setBackground(SystemColor.menu);
		lblPictureDisplay.setOpaque(true);
		pnlDisplay.add(lblPictureDisplay);
		
		//creates the label to identify the user's grade.
		lblGrade = new JLabel("Grade:");
		lblGrade.setBounds(115, 76, 46, 14);
		pnlDisplay.add(lblGrade);
		
		//creates the label to store the user's grade.
		lblGradeNumber = new JLabel("");
		lblGradeNumber.setHorizontalAlignment(SwingConstants.TRAILING);
		lblGradeNumber.setBounds(193, 76, 61, 14);
		pnlDisplay.add(lblGradeNumber);
		
		//creates the label to identify the user's age.
		lblAge = new JLabel("Age:");
		lblAge.setBounds(115, 54, 30, 14);
		pnlDisplay.add(lblAge);
		
		//creates the label to store the user's age.
		lblAgeNumber = new JLabel("");
		lblAgeNumber.setHorizontalAlignment(SwingConstants.TRAILING);
		lblAgeNumber.setBounds(193, 54, 61, 14);
		pnlDisplay.add(lblAgeNumber);
		
		//creates the label to identify the user's cellphone number.
		lblCellphone = new JLabel("Cellphone:");
		lblCellphone.setBounds(115, 98, 61, 14);
		pnlDisplay.add(lblCellphone);
		
		//creates the label to store the student's cellphone number.
		lblCNumber = new JLabel("");
		lblCNumber.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCNumber.setBounds(169, 98, 85, 14);
		pnlDisplay.add(lblCNumber);
		
		//instantiates the JList used to store the subjects of the selected user.
		subjectList = new JList();
		
		//creates the label used as a header in the JScrollPane 'scrollPaneSL'.
		lblSubjects = new JLabel("Subjects:");
		
		//creates the JScrollPane used to store the selected user's list of subjects.
		scrollPaneSL = new JScrollPane();
		scrollPaneSL.setBounds(264, 11, 85, 101);
		scrollPaneSL.setViewportView(subjectList);
		scrollPaneSL.setColumnHeaderView(lblSubjects);
		pnlDisplay.add(scrollPaneSL);
		
		//creates the label to identify the user's surname.
		lblSurname = new JLabel("Surname:");
		lblSurname.setBounds(115, 32, 61, 14);
		pnlDisplay.add(lblSurname);
		
		//creates the label to identify the user's first name.
		lblName = new JLabel("Name:");
		lblName.setBounds(115, 12, 46, 14);
		pnlDisplay.add(lblName);
		
		//creates the label to store the user's surname.
		lblSurnameText = new JLabel("");
		lblSurnameText.setHorizontalAlignment(SwingConstants.TRAILING);
		lblSurnameText.setBounds(169, 32, 85, 14);
		pnlDisplay.add(lblSurnameText);
		
		//creates the label to store the user's first name.
		lblNameText = new JLabel("");
		lblNameText.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNameText.setBounds(159, 12, 95, 14);
		pnlDisplay.add(lblNameText);
		
		//creates the button used to accept requesting students from the JList 'listRequestingStudents'
		btnAccept = new JButton("Accept");
		btnAccept.setBackground(SystemColor.menu);
		btnAccept.setBounds(422, 183, 89, 23);
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				boolean studentSelected = !(listRequestingStudents.getSelectedValue() == null);
				
				if(studentSelected){
					String selectedStudent = listRequestingStudents.getSelectedValue().toString();
					try{
						Main.storage.updatePRRequest(Main.username,selectedStudent,false);
						setTutorGUI();
					}catch(SQLException k){k.printStackTrace();}
				}	
				
			}
		});
		contentPane.add(btnAccept);
		
		//creates the button used to remove students from either JList 'listRequestingStudent' or 'listUserStudent'
		btnRemove = new JButton("Remove");
		btnRemove.setBackground(SystemColor.menu);
		btnRemove.setBounds(520, 183, 89, 23);
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				boolean studentSelected = !(listRequestingStudents.getSelectedValue() == null)|| !(listUserStudents.getSelectedValue()==null);
				
				if(studentSelected){
					
					String studentName = new String();
					
					if(!(listRequestingStudents.getSelectedValue() == null)){						
						studentName = listRequestingStudents.getSelectedValue().toString();
						
					}else if (!(listUserStudents.getSelectedValue()==null)){						
						studentName = listUserStudents.getSelectedValue().toString();
					}	
					try {
						Main.storage.deletePeerRelation(Main.username,studentName);
						setTutorGUI();
					} catch (SQLException k) {
						k.printStackTrace();
					}
					
				}	
			}
		});
		contentPane.add(btnRemove);	
	}

	//creates the method used to update JLists 'listRequestingStudents' and 'listUserStudents'
	//based on 'PAT Database' fields in tblPeer.
	public void setTutorGUI() throws SQLException{
		
		currStudModel.clear();		
		listUserStudents.setModel(currStudModel);
		scanner = new Scanner(Main.storage.getUserStudents(Main.username));
		scanner.useDelimiter(",");
		while(scanner.hasNext()){
			currStudModel.addElement(scanner.next());	
		}
		
		reqStudModel.clear();		
		listRequestingStudents.setModel(reqStudModel);
		scanner = new Scanner(Main.storage.getRequestingStudents(Main.username));
		scanner.useDelimiter(",");
		while(scanner.hasNext()){
			reqStudModel.addElement(scanner.next());	
		}
	
		listRequestingStudents.setModel(reqStudModel);
		listUserStudents.setModel(currStudModel);
	
	}

	//creates the method to update pnlDisplay based on a user selected in the 2 Student JLists
	public void setPanelDisplay(String usrName) throws SQLException{// input username of the student to display
		
		lblAgeNumber.setText(Main.storage.getAge(usrName));
		lblGradeNumber.setText(Main.storage.getGrade(usrName));
		lblSurnameText.setText(Main.storage.getSurname(usrName));
		lblNameText.setText(Main.storage.getName(usrName));
		lblCNumber.setText(Main.storage.getCellphone(usrName));

		subjectModel.clear();		
		scanner = new Scanner(Main.storage.getSubjects(usrName));
		scanner.useDelimiter(",");
		while(scanner.hasNext()){
			subjectModel.addElement(scanner.next());	
		}
		subjectList.setModel(subjectModel);
		
		lblPictureDisplay.setVerticalAlignment(SwingConstants.TOP);
		lblPictureDisplay.setHorizontalAlignment(SwingConstants.LEFT);
		filePath = Main.storage.getPicture(usrName);
		pfp = new ImageIcon(filePath);
		pfp.setImage(pfp.getImage().getScaledInstance(105, 123,Image.SCALE_SMOOTH));
		lblPictureDisplay.setIcon(pfp);
		
		//students no longer have timetables.
//		if(Main.storage.getTimetableID(usrName)>0){
//			for (int row = 0; row <  5; row++) {
//				for (int column = 1; column < 4; column++) {
//					timetable.setValueAt(Main.storage.getTableData(usrName,row,column), row, column); 
//				}
//			}
//		}	
	}		
}












