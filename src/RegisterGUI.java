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
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//JAYDEN MAREE
public class RegisterGUI extends JFrame {

	private JPanel contentPane;
	private JLabel lblLogo;
	private JLabel lblHeader;
	private JLabel lblFaction;
	private JLabel lblLogIn;
	private JLabel lblUsername;
	private JLabel lblPassword_1;
	private JLabel lblPassword_2;
	private JLabel lblRetype;
	private JLabel lblError;
	private JPasswordField passwordField_1;
	private JPasswordField passwordField_2;
	private JTextField usernameField;
	private JButton btnRegister;
	private Icon logo;
	private JComboBox comboBoxFaction;

	public RegisterGUI() {//The GUI used to register a new user.

		//creates the Registration screen properties
		setTitle("Peer Tutor Digital");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 247, 420);
		setLocationRelativeTo(null);// this centers the JFrame.
		
		//content pane properties
		contentPane = new JPanel();			
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setLayout(null);
		setContentPane(contentPane);

		//creates the Registration screen's 'display' picture.
		logo = new ImageIcon (getClass().getResource("LOGO.png"));
		lblLogo = new JLabel(logo);
		lblLogo.setBounds(10, 11, 210, 111);
		contentPane.add(lblLogo);		
	
		//creates the header label that gives the user instructions.
		lblHeader = new JLabel("Enter your registration details.");
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblHeader.setForeground(new Color(230, 230, 250));
		lblHeader.setBounds(20, 133, 185, 20);
		contentPane.add(lblHeader);
		
		//creates the combobox to select the type of faction.
		comboBoxFaction = new JComboBox();
		comboBoxFaction.setModel(new DefaultComboBoxModel(new String[] {"Student", "Tutor"}));
		comboBoxFaction.setBackground(SystemColor.menu);
		comboBoxFaction.setBounds(108, 170, 113, 20);
		contentPane.add(comboBoxFaction);
		
		//creates the label to identify the factions stored in the combobox.
		lblFaction = new JLabel("Faction:");
		lblFaction.setForeground(new Color(230, 230, 250));
		lblFaction.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFaction.setBounds(20, 170, 68, 20);
		contentPane.add(lblFaction);
		
		//creates the text field to input the username
		usernameField = new JTextField();
		usernameField.setBounds(108, 200, 113, 20);
		usernameField.addMouseListener(new MouseAdapter(){//makes the field white and clears errors.
			@Override
			public void mouseClicked(MouseEvent arg0) {//had to google mouse action listeners initially
				usernameField.setBackground(new Color(255,255,255));
				lblError.setText("");
			}	
		});
		contentPane.add(usernameField);			

		//creates the label to identify the usernameField.
		lblUsername = new JLabel("Username:");
		lblUsername.setForeground(new Color(230, 230, 250));
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblUsername.setBounds(20, 200, 68, 20);
		contentPane.add(lblUsername);

		//creates the field for entering a password.
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(108, 230, 113, 20);
		passwordField_1.addMouseListener(new MouseAdapter(){//makes both password fields white and clears errors.
			@Override
			public void mouseClicked(MouseEvent arg0) {
				passwordField_1.setBackground(new Color(255,255,255));
				passwordField_2.setBackground(new Color(255,255,255));
				lblError.setText(""); 
			}	
		});
		contentPane.add(passwordField_1);
		
		//creates the field for retyping the password for verification.
		passwordField_2 = new JPasswordField();
		passwordField_2.setBounds(108, 260, 113, 20);
		passwordField_2.addMouseListener(new MouseAdapter(){//makes both password fields white and clears errors.
			@Override
			public void mouseClicked(MouseEvent arg0) {
				passwordField_1.setBackground(new Color(255,255,255));
				passwordField_2.setBackground(new Color(255,255,255));
				lblError.setText("");
			}	
		});
		contentPane.add(passwordField_2);

		//creates the label to instruct users to retype their passwords.
		lblRetype = new JLabel("Retype");
		lblRetype.setForeground(new Color(230, 230, 250));
		lblRetype.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRetype.setBounds(20, 256, 68, 20);
		contentPane.add(lblRetype);
		
		//creates the label to identify the first password field.
		lblPassword_1 = new JLabel("Password:");
		lblPassword_1.setForeground(new Color(230, 230, 250));
		lblPassword_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPassword_1.setBounds(20, 230, 68, 20);
		contentPane.add(lblPassword_1);

		//creates the label to identify the retyped password field.
		lblPassword_2 = new JLabel("Password:");
		lblPassword_2.setForeground(new Color(230, 230, 250));
		lblPassword_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPassword_2.setBounds(20, 266, 68, 20);
		contentPane.add(lblPassword_2);

		//creates a label to be edited dynamically in order to display the error of the user's action.
		lblError = new JLabel();
		lblError.setForeground(new Color(230, 230, 250));
		lblError.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblError.setBounds(20, 297, 201, 39);
		contentPane.add(lblError);
		
		//creates the Register button.
		btnRegister = new JButton("Register");
		btnRegister.setBackground(SystemColor.menu);
		btnRegister.setBounds(132, 347, 89, 23);
		btnRegister.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {//Runs the method 'btnRegisterPressed' in the Main class.
			    if (e.getKeyCode()==KeyEvent.VK_ENTER){//if 'Enter' key is pressed.
			    	try {
			    		Main.btnRegisterPressed(usernameField.getText(), passwordField_1.getPassword(),passwordField_2.getPassword(),comboBoxFaction.getSelectedItem().toString());
					} catch (Exception k) {
						k.printStackTrace();
					}
			    }
			}
		});
		btnRegister.addActionListener(new ActionListener() {//Runs the method 'btnRegisterPressed' in the Main class.
			public void actionPerformed(ActionEvent arg0) {////if the button is pressed.
					try {
						Main.btnRegisterPressed(usernameField.getText(), passwordField_1.getPassword(),passwordField_2.getPassword(),comboBoxFaction.getSelectedItem().toString());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
		});
		contentPane.add(btnRegister);
		
		//creates the link to the registration screen.
		lblLogIn = new JLabel("Already have an account?");
		lblLogIn.addMouseListener(new MouseAdapter() {//Allows user to change to LogIn screen.
			@Override
			public void mouseEntered(MouseEvent arg0) {//when the mouse enters the text field.
				Font font = lblLogIn.getFont();
				Map attributes = font.getAttributes();
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
				lblLogIn.setFont(font.deriveFont(attributes));
			}				
			public void mouseExited(MouseEvent arg0){//when the mouse exits the text field.
				lblLogIn.setFont(new Font("Tahoma", Font.ITALIC, 9));
			}
			public void mouseClicked(MouseEvent arg0) {//when the mouse clicks on the text field.
					Main.master.setMasterFrame(MasterFrame.getLogIn());
			}
		});
		lblLogIn.setFont(new Font("Tahoma", Font.ITALIC, 9));
		lblLogIn.setForeground(new Color(230, 230, 250));
		lblLogIn.setBounds(10, 350, 112, 20);
		contentPane.add(lblLogIn);
		
	}

	//these Getters are used in the method 'btnRegisterPressed' in Main.
	public JTextField getUsernameField() {
		return usernameField;
	}
	public JPasswordField getPasswordField_1() {// do not change the return because it is used to change the field colour.
		return passwordField_1;
	}
	public JPasswordField getPasswordField_2() {// do not change the return because it is used to change the field colour.
		return passwordField_2;
	}
	
	//these Setters are used in the method 'btnRegisterPressed' in Main.
	public void setErrorField(JTextField field){
		field.setBackground(new Color(255, 99, 71));
	}
	public void setErrorField(JPasswordField field){
		field.setBackground(new Color(255, 99, 71));
	}
	public void setLblError(String lbllError) {
		this.lblError.setText("<html><p>"+lbllError+"</p></html>"); 
	}

	//these JComboBox getters and setters are used when registering a new Admin in the 'AdminProfileGUI'
	public JComboBox getComboBox() {
		return comboBoxFaction;
	}
	public void setComboBox(JComboBox comboBox) {
		this.comboBoxFaction = comboBox;
	}
	
	
		
		
}
