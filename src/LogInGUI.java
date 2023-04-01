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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//JAYDEN MAREE
public class LogInGUI extends JFrame {

	private JPanel contentPane;
	private JTextField usernameField;
	private Icon logo;
	private JLabel lblLogo,lblHeader,lblRegister,lblUsername,lblPassword_1,lblError;
	private JButton btnLogIn;
	private JPasswordField passwordField_1;


public LogInGUI() {
	
		//creates the Log In screen properties
		setTitle("Peer Tutor Digital");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 247, 340);
		setLocationRelativeTo(null);// this centers the JFrame.
		
		//content pane properties
		contentPane = new JPanel();			
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setLayout(null);
		setContentPane(contentPane);
				
		//creates the Log In screen's 'display' picture.
		logo = new ImageIcon (getClass().getResource("/LOGO.png"));
		lblLogo = new JLabel(logo);
		lblLogo.setBounds(10, 11, 210, 111);
		contentPane.add(lblLogo);		
		
		//creates the header label that gives the user instructions.
		lblHeader = new JLabel("Please enter your details below.");
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblHeader.setForeground(new Color(230, 230, 250));
		lblHeader.setBounds(20, 133, 185, 20);
		contentPane.add(lblHeader);
		
		//creates the text field to input the username
		usernameField = new JTextField();
		usernameField.setBounds(108, 170, 113, 20);
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
		lblUsername.setBounds(20, 170, 68, 20);
		contentPane.add(lblUsername);
					
		//creates the field for entering a password.
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(108, 200, 113, 20);
		passwordField_1.addMouseListener(new MouseAdapter(){//makes the field white and clears errors.
			@Override
			public void mouseClicked(MouseEvent arg0) {
				passwordField_1.setBackground(new Color(255,255,255));
				lblError.setText("");
			}	
		});
		contentPane.add(passwordField_1);
		
		//creates the label to identify the first password field.
		lblPassword_1 = new JLabel("Password:");
		lblPassword_1.setForeground(new Color(230, 230, 250));
		lblPassword_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPassword_1.setBounds(20, 200, 68, 20);
		contentPane.add(lblPassword_1);
		
		//creates a label to be edited in order to display the error of the user's action.
		lblError = new JLabel();
		lblError.setForeground(new Color(230, 230, 250));
		lblError.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblError.setBounds(20, 230, 201, 39);
		contentPane.add(lblError);
			
		//creates the log in button.
		btnLogIn = new JButton("Log In");
		btnLogIn.setBackground(SystemColor.menu);
		btnLogIn.setBounds(132, 260, 89, 23);
		btnLogIn.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			    if (e.getKeyCode()==KeyEvent.VK_ENTER){
			    	try {
						Main.btnLogInPressed(usernameField.getText(),passwordField_1.getPassword());
					} catch (Exception k) {
						k.printStackTrace();
					}
			    }
			}
		});
		btnLogIn.addActionListener(new ActionListener() {//runs the method in Main.
			public void actionPerformed(ActionEvent arg0) {
					try {
						Main.btnLogInPressed(usernameField.getText(),passwordField_1.getPassword());
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		});
		contentPane.add(btnLogIn);
		
		//creates the link to the registration screen.
		lblRegister = new JLabel("Don't have an account?");
		lblRegister.addMouseListener(new MouseAdapter() {//Allows user to change to Registration screen.
			@Override
			public void mouseEntered(MouseEvent arg0) {//when the mouse enters the text field.
				Font font = lblRegister.getFont();
				Map attributes = font.getAttributes();//Had to google this
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);// and this.
				lblRegister.setFont(font.deriveFont(attributes));
			}	
			@Override
			public void mouseClicked(MouseEvent arg0) {//when the mouse clicks on the text field
					Main.master.setMasterFrame(MasterFrame.getRegister());
			}	
			@Override
			public void mouseExited(MouseEvent arg0){//When the mouse leaves text field.
				lblRegister.setFont(new Font("Tahoma", Font.ITALIC, 9));
			}
		});
		lblRegister.setFont(new Font("Tahoma", Font.ITALIC, 9));
		lblRegister.setForeground(new Color(230, 230, 250));
		lblRegister.setBounds(10, 260, 112, 20);
		contentPane.add(lblRegister);
	
	}

	//used to return the fields in the Main class to allow them to be set to a red colour
	public JTextField getUsernameField() {
		return usernameField;
	}
	public JPasswordField getPasswordField_1() {
		return passwordField_1;
	}
	
	//used to set the fields to red to indicate errors made.
	public void setErrorField(JTextField field){
		field.setBackground(new Color(255, 99, 71));
	}
	public void setErrorField(JPasswordField field){
		field.setBackground(new Color(255, 99, 71));
	}

	//creates the setter that allows users to see errors they have made.
	public void setLblError(String lbllError) {
		this.lblError.setText("<html><p>"+lbllError+"</p></html>"); 
	}

}
