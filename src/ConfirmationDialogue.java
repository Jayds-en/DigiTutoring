import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

//JAYDEN MAREE
public class ConfirmationDialogue extends JFrame {

	private JPanel contentPane;
	private JLabel lblWarning;
	
	public ConfirmationDialogue() {// this is used in transitions from currentFrame to nextFrame declared in Main.

		//creates the ConfirmationDialogue screen properties
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 268);
		setLocationRelativeTo(null);
		
		//contentPane properties
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		//creates the dynamically changing label which informs the user about the action they are going to perform. 
		lblWarning = new JLabel("");
		lblWarning.setHorizontalAlignment(SwingConstants.CENTER);
		lblWarning.setForeground(SystemColor.menu);
		lblWarning.setBounds(27, 11, 383, 176);
		contentPane.add(lblWarning);
		
		//creates the button that confirms the action.
		JButton btnOkay = new JButton("Okay");
		btnOkay.setBackground(SystemColor.menu);
		btnOkay.setBounds(256, 198, 89, 23);
		btnOkay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(Main.nextFrame == null){ //'null' symbolises that the user needs to be deleted.
					try {
						Main.storage.deleteUser(Main.username);
					} catch (SQLException | IOException e) {
						e.printStackTrace();
					}
					Main.master.setMasterFrame(MasterFrame.getLogIn());//when user gets deleted: LogInGUI is displayed
				}
				else if(Main.nextFrame.equals(MasterFrame.getStudProfScrn())&&Main.currentFrame.equals(MasterFrame.getStudProfScrn())){
					//this runs if Students press the 'btnApply' button in their profile screen.
					try {
						MasterFrame.getStudProfScrn().update();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					Main.master.setMasterFrame(Main.nextFrame);
				}
				else if(Main.nextFrame.equals(MasterFrame.getAdmnProfScrn())&&Main.currentFrame.equals(MasterFrame.getAdmnProfScrn())){
					//this runs if Admins press the 'btnApply' button in their profile screen.
					try {
						MasterFrame.getAdmnProfScrn().update();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					Main.master.setMasterFrame(Main.nextFrame);
				}
				else if(Main.nextFrame.equals(MasterFrame.getTutProfScrn())&&Main.currentFrame.equals(MasterFrame.getTutProfScrn())){
					//this runs if Tutors press the 'btnApply' button in their profile screen.
					try {
						MasterFrame.getTutProfScrn().update();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					Main.master.setMasterFrame(Main.nextFrame);
				}
//				else if(Main.nextFrame.equals(MasterFrame.getTutScrn())&&Main.currentFrame.equals(MasterFrame.getTutScrn())){
//					
//					try {
//						MasterFrame.getTutProfScrn().update();
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//					Main.master.setMasterFrame(Main.nextFrame);
//				}
				else if(Main.nextFrame.equals(MasterFrame.getAdmnScrn())&&Main.currentFrame.equals(MasterFrame.getAdmnScrn())){
					try {
						MasterFrame.getAdmnScrn().deleteUser();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					Main.master.setMasterFrame(Main.nextFrame);
				}
				else{
					Main.master.setMasterFrame(Main.nextFrame);
				}
			}
		});
		contentPane.add(btnOkay);
		
		//creates the button that cancels the action.
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBackground(SystemColor.menu);
		btnCancel.setBounds(88, 198, 89, 23);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.master.setMasterFrame(Main.currentFrame);
			}
		});
		contentPane.add(btnCancel);
		
	}
	
	//creates the method that allows the 'lblWarning' JLabel to be dynamically changed.
	public void setCDLabel(String cDlabel){
		this.lblWarning.setText("<html><p><center>"+cDlabel+"</center></p></html>");
	}


}
