import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

//JAYDEN MAREE
public class StudentGUI extends JFrame {

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
	private JPanel pnlDisplay;
	private JPanel pnlAllTutors;
	private JPanel pnlStudentTutors;
	private DefaultListModel currTutModel;
	private DefaultListModel allTutModel;
	private DefaultListModel subjectModel;
	private JList subjectList;
	private JList listAllTutors;
	private JList listUserTutors;
	private JScrollPane scrollPaneTimetable;
	private JScrollPane scrollPaneSL;
	private JButton btnRemove;
	private JButton btnRequestTutor;
	private String[] tableColumnNames;
	private String filePath;
	private Object[][] tableData;
	private DefaultTableModel tableModel;
	private JTable timetable;
	private ImageIcon pfp;
	private Scanner scanner;
	private JTabbedPane tabbedPane;
	
	public StudentGUI() {// The GUI used by Students.

		//creates the student screen properties
		setTitle("Peer Tutor Digital");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 648, 366);
		setLocationRelativeTo(null);// this centers the JFrame.
		
		//content pane properties
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		//Instantiates default list models
		currTutModel = new DefaultListModel();
		allTutModel = new DefaultListModel();
		subjectModel = new DefaultListModel();
		
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
					Main.master.setMasterFrame(MasterFrame.getStudProfScrn());
					Main.currentFrame = MasterFrame.getStudProfScrn();
			}	
			@Override
			public void mouseExited(MouseEvent arg0){//When the mouse leaves text field.
				lblViewProfile.setFont(new Font("Tahoma", Font.ITALIC, 13));
			}
		});
		contentPane.add(lblViewProfile);
		
		//creates the table used to display tutoring times..
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
						false,false,false,false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		};
		timetable = new JTable();
		timetable.setRowSelectionAllowed(false);
		timetable.setCellSelectionEnabled(true);
		timetable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		/*
		 * I removed this function because it complicates the functionality of the program more than it benefits it.
		 * It serves as a idea of further improvement.
		 * 
		 * timetable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {//when the mouse clicks on the text field
				boolean studentSelected = !(listAllTutors.getSelectedValue() == null);// this also means it only happens if selections from one list take place.
				if(studentSelected){
					try {
						if(timetable.getSelectedColumn()>0){
							if(Main.storage.getTableData(listAllTutors.getSelectedValue().toString(), timetable.getSelectedRow(), timetable.getSelectedColumn()) == false){
								if(timetable.getValueAt(timetable.getSelectedRow(), timetable.getSelectedColumn()).equals(Boolean.FALSE)){
									
									timetable.setValueAt(true, timetable.getSelectedRow(), timetable.getSelectedColumn());
									for (int row = 0; row <  5; row++) {
										for (int column = 1; column < 4; column++) {
											if(Main.storage.getTableData(listAllTutors.getSelectedValue().toString(),row,column) == true)
												timetable.setValueAt(false, row, column);
										}
									}
									if(Main.storage.getTimetableID(Main.username)>0){
										Main.storage.updateUserTable(Main.username, timetable);
									}else{
										Main.storage.insertUserTable(Main.username, timetable);
										Main.storage.updateUserTable(Main.username, timetable);
									}
									setPanelDisplay(listAllTutors.getSelectedValue().toString());
								}else{
									timetable.setValueAt(false, timetable.getSelectedRow(), timetable.getSelectedColumn());
									for (int row = 0; row <  5; row++) {
										for (int column = 1; column < 4; column++) {
											if(Main.storage.getTableData(listAllTutors.getSelectedValue().toString(),row,column) == true)
												timetable.setValueAt(false, row, column);
										}
									}
									Main.storage.updateUserTable(Main.username, timetable);
									setPanelDisplay(listAllTutors.getSelectedValue().toString());
								}
							}
						}	
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}	
			}	
		});	*/
		timetable.setModel(tableModel);
		
		//creates the scrollPane that surround the timetable to allow users to see the timetable column headings.
		scrollPaneTimetable = new JScrollPane();
		scrollPaneTimetable.setBounds(263, 179, 359, 106);
		scrollPaneTimetable.setViewportView(timetable);
		contentPane.add(scrollPaneTimetable);
		
		//creates the tabbed pane that hosts JPanels
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(23, 28, 230, 258);
		contentPane.add(tabbedPane);
		
		//creates the JPanel that hosts the list 'listAllTutors'
		pnlAllTutors = new JPanel();
		pnlAllTutors.setLayout(null);
		tabbedPane.addTab("All Tutors", null, pnlAllTutors, null);
		
		//creates the JPanel that hosts the list 'listUserTutors'
		pnlStudentTutors = new JPanel();
		pnlStudentTutors.setLayout(null);
		tabbedPane.addTab("Your Tutors", null, pnlStudentTutors, null);
		
		//creates the JList that hosts the names of tutors with admin approval, available for tutoring.
		listAllTutors = new JList();
		listAllTutors.setBounds(0, 0, 225, 230);
		listAllTutors.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				boolean studentSelected = !(listAllTutors.getSelectedValue() == null);
				if(studentSelected){
					String usrName = listAllTutors.getSelectedValue().toString();//fix	
					try {
						setPanelDisplay(usrName);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		pnlAllTutors.add(listAllTutors);
	
		//creates the JList that hosts the names of tutors that have accepted the user for tutoring.
		listUserTutors = new JList();
		listUserTutors.setBounds(0, 0, 225, 230);
		listUserTutors.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				boolean studentSelected = !(listUserTutors.getSelectedValue()==null);
				if(studentSelected){
					String usrName = listUserTutors.getSelectedValue().toString();
					try {
						setPanelDisplay(usrName);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		pnlStudentTutors.add(listUserTutors);
		
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

		//creates the button used to request tutoring from tutors on the JList 'listAllTutors'
		btnRequestTutor = new JButton("Request");
		btnRequestTutor.setBackground(SystemColor.menu);
		btnRequestTutor.setBounds(520, 296, 89, 23);
		btnRequestTutor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean studentSelected = !(listAllTutors.getSelectedValue() == null);
				if(studentSelected){
					try{
						if(!Main.storage.isPaired(listAllTutors.getSelectedValue().toString(),Main.username)){	
							Main.storage.insertPeerRelation(listAllTutors.getSelectedValue().toString(),Main.username,true);
							
						}else{
							Main.storage.updatePRRequest(listAllTutors.getSelectedValue().toString(), Main.username, true);
						}
						
						setPanelDisplay(listAllTutors.getSelectedValue().toString());
						setStudentGUI();
					}catch(SQLException k){k.printStackTrace();}
				}	
			}
		});
		contentPane.add(btnRequestTutor);
		
		//creates the button used to remove students the JList 'listUserTutors'.
		btnRemove = new JButton("Remove");
		btnRemove.setBackground(SystemColor.menu);
		btnRemove.setBounds(421, 296, 89, 23);
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean studentSelected = !(listUserTutors.getSelectedValue()==null);
				if(studentSelected){
					try {
						Main.storage.deletePeerRelation(listUserTutors.getSelectedValue().toString(),Main.username);
						setStudentGUI();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}	
			}
		});
		contentPane.add(btnRemove);	
	}
	
	//creates the method used to update JLists 'listAllTutors' and 'listUserTutors'
	//based on 'PAT Database' fields in tblPeer.
	public void setStudentGUI() throws SQLException{
		
		currTutModel.clear();		
		listUserTutors.setModel(currTutModel);
		scanner = new Scanner(Main.storage.getUserTutors(Main.username));
		scanner.useDelimiter(",");
		while(scanner.hasNext()){
			currTutModel.addElement(scanner.next());	
		}
		
		allTutModel.clear();		
		listAllTutors.setModel(allTutModel);
		scanner = new Scanner(Main.storage.getAllTutors());
		scanner.useDelimiter(",");
		while(scanner.hasNext()){
			allTutModel.addElement(scanner.next());	
		}
	
		listAllTutors.setModel(allTutModel);
		listUserTutors.setModel(currTutModel);
		
	}

	//creates the method to update pnlDisplay based on a user selected in the 2 Tutor JLists
	public void setPanelDisplay(String usrName) throws SQLException{
		
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
		
		// this code was decided to be removed along with the code in timetable mouseListener
		/*if(Main.storage.getTimetableID(Main.username)>0){
			for (int row = 0; row <  5; row++) {
				for (int column = 1; column < 4; column++) {
					timetable.setValueAt(false, row, column);
					if(Main.storage.getTableData(usrName,row,column) == true)
						timetable.setValueAt(true, row, column);
					if(Main.storage.getTableData(Main.username,row,column) == true)
						timetable.setValueAt(true, row, column);
				}
			}
		}else{*/
			for (int row = 0; row <  5; row++) {
				for (int column = 1; column < 4; column++) {
					timetable.setValueAt(false, row, column);
					if(Main.storage.getTableData(usrName,row,column) == true)
						timetable.setValueAt(true, row, column);
				}
			}
//		}
	}

}











