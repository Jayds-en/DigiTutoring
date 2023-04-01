import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.toedter.calendar.JDateChooser;

//JAYDEN MAREE
public class AdminProfileGUI extends JFrame {

	private JPanel contentPane;
	private JLabel lblDeleteAccount;
	private JLabel lblError;
	private JLabel lblViewUsers;
	private JLabel lblCreateNewAdmin;
	private JLabel lblName;
	private JLabel lblSurname;
	private JLabel lblDateOfBirth;
	private JLabel lblGrade;
	private JLabel lblCellphone;
	private JButton btnUndo;
	private JButton btnApply;
	private JTextField nameField;
	private JTextField surnameField;
	private JTextField cellphoneField;
	private JComboBox comboBoxGrade;
	private JDateChooser dateChooser;

	public AdminProfileGUI() {//The GUI used by Admins to complete their profile.
		
		//creates the TutorProfileScreen properties
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 304);
		setLocationRelativeTo(null);// this centers the JFrame.
		
		//content pane properties
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		lblCreateNewAdmin = new JLabel("Register a new Admin.");
		lblCreateNewAdmin.setForeground(SystemColor.menu);
		lblCreateNewAdmin.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblCreateNewAdmin.setBounds(291, 3, 130, 25);
		lblCreateNewAdmin.addMouseListener(new MouseAdapter() {//Allows user to change to Admin screen.
			@Override
			public void mouseEntered(MouseEvent arg0) {//when the mouse enters the text field.
				Font font = lblCreateNewAdmin.getFont();
				Map attributes = font.getAttributes();//Had to google this
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);// and this.
				lblCreateNewAdmin.setFont(font.deriveFont(attributes));
			}	
			@Override
			public void mouseClicked(MouseEvent arg0) {//when the mouse clicks on the text field
				JComboBox AdminRegistration =	MasterFrame.getRegister().getComboBox();// can i put all the code below: here?
				AdminRegistration.setModel(new DefaultComboBoxModel(new String[] {"Student", "Tutor", "Admin"}));
				MasterFrame.getRegister().setComboBox(AdminRegistration);
				MasterFrame.getCD().setCDLabel("Performing this action causes you to log out of your account.<br><br>"
						+ "Are you sure you want to continue?");
				Main.nextFrame = MasterFrame.getRegister();
				Main.master.setMasterFrame(MasterFrame.getCD());
			}	
			@Override
			public void mouseExited(MouseEvent arg0){//When the mouse leaves text field.
				lblCreateNewAdmin.setFont(new Font("Tahoma", Font.ITALIC, 13));
			}
		});
		contentPane.add(lblCreateNewAdmin);
		
		//The label used to open the 'AdminGUI' screen.
		lblViewUsers = new JLabel("View Users");
		lblViewUsers.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblViewUsers.setForeground(SystemColor.menu);
		lblViewUsers.setBounds(10, 5, 81, 20);
		lblViewUsers.addMouseListener(new MouseAdapter() {//Allows admin users to change to the viewing screen.
			@Override
			public void mouseEntered(MouseEvent arg0) {//when the mouse enters the text field.
				Font font = lblViewUsers.getFont();
				Map attributes = font.getAttributes();//Had to google this
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);// and this.
				lblViewUsers.setFont(font.deriveFont(attributes));
			}	
			@Override
			public void mouseClicked(MouseEvent arg0) {//when the mouse clicks on the text field
				try {
					MasterFrame.getAdmnScrn().setAdmin();
				} catch (Exception e) {
					e.printStackTrace();
				}
				Main.master.setMasterFrame(MasterFrame.getAdmnScrn());
				Main.currentFrame = MasterFrame.getAdmnScrn();
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
				
		//creates the text field used to store the user's cell phone number.
		cellphoneField = new JTextField();
		cellphoneField.setBounds(301, 171, 120, 20);
		cellphoneField.setColumns(10);
		contentPane.add(cellphoneField);
		
		//creates the label to identify the cellphoneField.
		lblCellphone = new JLabel("Cellphone number :");
		lblCellphone.setForeground(new Color(230, 230, 250));
		lblCellphone.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCellphone.setBounds(33, 174, 110, 14);
		contentPane.add(lblCellphone);
		
		//creates a label to be edited in order to display the error of the user's action.
		lblError = new JLabel();
		lblError.setVerticalAlignment(SwingConstants.TOP);
		lblError.setForeground(new Color(230, 230, 250));
		lblError.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblError.setBounds(10, 202, 214, 25);
		contentPane.add(lblError);
		
		//creates the button used to validate fields and upload them into the database.
		btnApply = new JButton("Apply");
		btnApply.setBackground(SystemColor.menu);
		btnApply.setBounds(332, 236, 89, 23);
		btnApply.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			    if (e.getKeyCode()==KeyEvent.VK_ENTER){
			    	try {
			    		Main.btnAdmnApplyPressed(nameField.getText(), surnameField.getText(), dateChooser, comboBoxGrade.getSelectedItem().toString(), cellphoneField.getText());
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
			    }
			}
		});
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Main.btnAdmnApplyPressed(nameField.getText(), surnameField.getText(), dateChooser, comboBoxGrade.getSelectedItem().toString(), cellphoneField.getText());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(btnApply);		
		
		//creates the button used to undo any field changes.
		btnUndo = new JButton("Undo");
		btnUndo.setBackground(SystemColor.menu);
		btnUndo.setBounds(233, 236, 89, 23);
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					setAdmn(Main.username);
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
		lblDeleteAccount.setBounds(10, 237, 120, 20);
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
							"Performing this action causes a deletion of your account.<br>"
							+ "Please Register a new Admin account before you delete your own.<br><br>"
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
	
	//creates the method used to load this user's profile details into the database.
	public void update() throws SQLException{
		
		//Since UCanAccess requires dates to be queried in order MM/dd/yyy, thus this does not work: 
		//String date = dateChooser.getDate().toGMTString().substring(0, 11);
		String date = (dateChooser.getDate().getMonth()+1)+"/"+dateChooser.getDate().getDate()+"/"+(dateChooser.getDate().getYear() +1900) ;
		Main.storage.updateAdmin(Main.username,nameField.getText(), surnameField.getText(), date, comboBoxGrade.getSelectedItem().toString(), cellphoneField.getText());
	}
	//creates the method that uploads database information into the user's profile GUI.
	public void setAdmn(String usrName) throws Exception{
		
		if (Main.storage.isPopulated(usrName)){
			nameField.setText(Main.storage.getName(usrName));
			surnameField.setText(Main.storage.getSurname(usrName));
			comboBoxGrade.setSelectedItem(Main.storage.getGrade(usrName));
			cellphoneField.setText(Main.storage.getCellphone(usrName));
			
	        String databaseDate = (Main.storage.getBDay(usrName).substring(0, 11));
	        DateFormat dF = new SimpleDateFormat("yyyy-MM-dd"); //learnt online
	        // Convert from String to Date
	        Date date = dF.parse(databaseDate);
			dateChooser.setDate(date);
			
		}else{
			MasterFrame.getAdmnProfScrn().setLblError("Please finish entering the form.");
		}	
	}
	
	//creates the setter that allows users to see errors they have made.
	public void setLblError(String lbllError) {
		this.lblError.setText("<html><p>"+lbllError+"</p></html>"); 
	}

}
	
	





