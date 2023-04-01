import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.JList;
import java.awt.CardLayout;

//JAYDEN MAREE
public class AdminGUI extends JFrame {

	private JLabel lblPictureDisplay;
	private JLabel lblViewProfile;
	private JLabel lblGrade;
	private JLabel lblGradeNumber;
	private JLabel lblAge;
	private JLabel lblAgeNumber;
	private JLabel lblCellphone;
	private JLabel lblCNumber;
	private JLabel lblSubjects;
	private JLabel lblViewTutors;
	private JLabel lblViewStudents;
	private JLabel lblTutors;
	private JLabel lblStudents;
	private JLabel lblSurname;
	private JLabel lblName;
	private JLabel lblSurnameText;
	private JLabel lblNameText;
	private DefaultListModel subjectModel;
	private DefaultListModel allStudModel;
	private DefaultListModel reqTutModel;
	private DefaultListModel allTutModel;
	private JScrollPane scrollPaneTimetable;
	private JScrollPane scrollPaneSL;
	private JScrollPane scrollPaneTutors;
	private JScrollPane scrollPaneStudents;
	private JPanel contentPane;
	private JPanel pnlDisplay;
	private JPanel pnlRequestingTutors;
	private JPanel pnlUsers;
	private JList subjectList;
	private JList listAllTutors;
	private JList listReqTutors;
	private JList listAllStudents;
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
	private CardLayout pnlUserCardLayout;
	
	public AdminGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 648, 366);
		setLocationRelativeTo(null);// this centers the JFrame.
		
		//Instantiates default list models
		allStudModel = new DefaultListModel();
		allTutModel = new DefaultListModel();
		reqTutModel = new DefaultListModel();
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
					try {
						MasterFrame.getAdmnProfScrn();
					} catch (Exception e) {
						e.printStackTrace();
					}
					Main.master.setMasterFrame(MasterFrame.getAdmnProfScrn());
					Main.currentFrame = MasterFrame.getAdmnProfScrn();
			}	
			@Override
			public void mouseExited(MouseEvent arg0){//When the mouse leaves text field.
				lblViewProfile.setFont(new Font("Tahoma", Font.ITALIC, 13));
			}
		});
		contentPane.add(lblViewProfile);
		
		//creates the table used to display tutoring times.
		timetable = new JTable();
		timetable.setRowSelectionAllowed(false);
		timetable.setCellSelectionEnabled(true);
		timetable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
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
		
		//creates the CardLayout used to differentiate between 'pnlUser' cards.
		pnlUserCardLayout =new CardLayout(0,0);
		
		//creates the JPanel that hosts the list 'listReqTutors'
		pnlRequestingTutors = new JPanel();
		pnlRequestingTutors.setLayout(null);
		tabbedPane.addTab("Requesting Tutors", null, pnlRequestingTutors, null);
		
		//creates the JPanel that hosts the JScrollPanes: 'scrollPaneStudents' and 'scrollPaneTutors'.
		pnlUsers = new JPanel();
		pnlUsers.setLayout(pnlUserCardLayout);
		tabbedPane.addTab("All Users", null, pnlUsers, null);
		
		//creates the JList that hosts the names of tutors without admin approval, requesting to become tutors.
		listReqTutors = new JList();
		listReqTutors.setBounds(0, 0, 225, 230);
		listReqTutors.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				listAllStudents.setSelectedIndex(-1);//deselects other lists.
				listAllTutors.setSelectedIndex(-1);
				boolean studentSelected = !(listReqTutors.getSelectedValue()==null);
				if(studentSelected){
					String usrName = listReqTutors.getSelectedValue().toString();
					try {
						setPanelDisplay(usrName);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		pnlRequestingTutors.add(listReqTutors);
		
		//creates the JList that hosts the names of tutors with admin approval, available for tutoring.
		listAllTutors = new JList();
		listAllTutors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listAllTutors.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {//updates pnlDisplay based on the user selected
				listAllStudents.setSelectedIndex(-1);//deselects other lists.
				listReqTutors.setSelectedIndex(-1);
				boolean studentSelected = !(listAllTutors.getSelectedValue()==null);
				if(studentSelected){
					String usrName = listAllTutors.getSelectedValue().toString();
					try {
						setPanelDisplay(usrName);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		//creates the JList that hosts the names of all Students in the 'PAT Database'.
		listAllStudents = new JList();
		listAllStudents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listAllStudents.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {//updates pnlDisplay based on the user selected
				listAllTutors.setSelectedIndex(-1);//deselects other lists.
				listReqTutors.setSelectedIndex(-1);
				boolean studentSelected = !(listAllStudents.getSelectedValue()==null);
				if(studentSelected){
					String usrName = listAllStudents.getSelectedValue().toString();
					try {
						setPanelDisplay(usrName);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		//instantiates the JLabels used as headers in the JScrollPanes 'scrollPaneStudents' and 'scrollPaneTutors'.
		lblTutors = new JLabel("Showing all Tutors :");
		lblStudents = new JLabel("Showing all Students :");

		//creates the JScrollPane that hosts the list 'listAllStudents'.
		scrollPaneStudents = new JScrollPane();
		scrollPaneStudents.setViewportView(listAllStudents);
		scrollPaneStudents.setColumnHeaderView(lblStudents);
		pnlUsers.add(scrollPaneStudents, "Students");
		
		//creates the JScrollPane that hosts the list 'listAllTutors'.
		scrollPaneTutors = new JScrollPane();
		scrollPaneTutors.setViewportView(listAllTutors);
		scrollPaneTutors.setColumnHeaderView(lblTutors);
		pnlUsers.add(scrollPaneTutors, "Tutors");

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

		//creates the label clicked to display the JScrollPane 'scrollPaneStudents'
		lblViewStudents = new JLabel("View Students");
		lblViewStudents.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblViewStudents.setForeground(SystemColor.menu);
		lblViewStudents.setBounds(164, 297, 89, 20);
		lblViewStudents.addMouseListener(new MouseAdapter() {//Allows student users to change to the viewing screen.
			@Override
			public void mouseEntered(MouseEvent arg0) {//when the mouse enters the text field.
				Font font = lblViewStudents.getFont();
				Map attributes = font.getAttributes();//Had to google this
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);// and this.
				lblViewStudents.setFont(font.deriveFont(attributes));
			}	
			@Override
			public void mouseClicked(MouseEvent arg0) {//when the mouse clicks on the text field
				pnlUserCardLayout.show(pnlUsers, "Students");
			}	
			@Override
			public void mouseExited(MouseEvent arg0){//When the mouse leaves text field.
				lblViewStudents.setFont(new Font("Tahoma", Font.ITALIC, 13));
			}
		});
		contentPane.add(lblViewStudents);
		
		//creates the label clicked to display the JScrollPane 'scrollPaneStudents'
		lblViewTutors = new JLabel("View Tutors");
		lblViewTutors.setForeground(SystemColor.menu);
		lblViewTutors.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblViewTutors.setBounds(77, 297, 77, 20);
		lblViewTutors.addMouseListener(new MouseAdapter() {//Allows tutor users to change to the viewing screen.
			@Override
			public void mouseEntered(MouseEvent arg0) {//when the mouse enters the text field.
				Font font = lblViewTutors.getFont();
				Map attributes = font.getAttributes();//Had to google this
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);// and this.
				lblViewTutors.setFont(font.deriveFont(attributes));
			}	
			@Override
			public void mouseClicked(MouseEvent arg0) {//when the mouse clicks on the text field
				pnlUserCardLayout.show(pnlUsers, "Tutors");
			}	
			@Override
			public void mouseExited(MouseEvent arg0){//When the mouse leaves text field.
				lblViewTutors.setFont(new Font("Tahoma", Font.ITALIC, 13));
			}
		});
		contentPane.add(lblViewTutors);
		
		//creates the button used to accept requesting students from the JList 'listRequestingStudents'
		btnAccept = new JButton("Accept");
		btnAccept.setBackground(SystemColor.menu);
		btnAccept.setBounds(422, 296, 89, 23);
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean studentSelected = !(listReqTutors.getSelectedValue() == null);
				if(studentSelected){
					try{
					Main.storage.approveTutor(listReqTutors.getSelectedValue().toString());
					setAdmin();
					}catch(SQLException k){k.printStackTrace();}
				}	
			}
		});
		contentPane.add(btnAccept);
		
		//creates the button used to remove any user from any JList.
		btnRemove = new JButton("Remove");
		btnRemove.setBackground(SystemColor.menu);
		btnRemove.setBounds(520, 296, 89, 23);
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MasterFrame.getCD().setCDLabel(
						"Performing this action causes you to delete a user's account details without their knowledge.<br><br>"
						+ "Make sure to get their consent before you continue.<br><br>"
						+ "Are you sure you want to continue?");
				Main.nextFrame = MasterFrame.getAdmnScrn();
				Main.master.setMasterFrame(MasterFrame.getCD());
			}
		});
		contentPane.add(btnRemove);
		
		
	}
	
	//creates the methods used to delete a user from the any of the JLists in the GUI.
	public void deleteUser() throws SQLException{
		
		try{
			if(!(listReqTutors.getSelectedValue() == null)){
				Main.storage.deleteUser(listReqTutors.getSelectedValue().toString());
			}
			if(!(listAllTutors.getSelectedValue() == null)){
				Main.storage.deleteUser(listAllTutors.getSelectedValue().toString());
			}
			if(!(listAllStudents.getSelectedValue() == null)){
				Main.storage.deleteUser(listAllStudents.getSelectedValue().toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		setAdmin();
	}
	
	//creates the method used to update JLists 'listAllTutors' and 'listUserTutors'
	//based on 'PAT Database' fields in tblPeer.
	public void setAdmin() throws SQLException{
		
		reqTutModel.clear();		
		listReqTutors.setModel(reqTutModel);
		scanner = new Scanner(Main.storage.getProspectiveTutors());
		scanner.useDelimiter(",");
		while(scanner.hasNext()){
			reqTutModel.addElement(scanner.next());	
		}
	
		allStudModel.clear();		
		listAllStudents.setModel(allStudModel);
		scanner = new Scanner(Main.storage.getAllStudents());
		scanner.useDelimiter(",");
		while(scanner.hasNext()){
			allStudModel.addElement(scanner.next());	
		}
		
		allTutModel.clear();		
		listAllTutors.setModel(allTutModel);
		scanner = new Scanner(Main.storage.getAllTutors());
		scanner.useDelimiter(",");
		while(scanner.hasNext()){
			allTutModel.addElement(scanner.next());	
		}
		
		listReqTutors.setModel(reqTutModel);
		listAllTutors.setModel(allTutModel);
		listAllStudents.setModel(allStudModel);
	
	}

	//creates the method to update pnlDisplay based on a user selected in the 2 Tutor JLists
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

		if(Main.storage.getFaction(usrName).equals("Tutor")){
			for (int row = 0; row <  5; row++) {
				for (int column = 1; column < 4; column++) {
					timetable.setValueAt(false, row, column);
					timetable.setValueAt(Main.storage.getTableData(usrName,row,column), row, column); 
				}
			}
		}else{
			for (int row = 0; row <  5; row++) {
				for (int column = 1; column < 4; column++) {
					timetable.setValueAt(false, row, column); 
				}
			}
		}
	}
}

















