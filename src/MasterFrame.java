
import javax.swing.JFrame;

//JAYDEN MAREE
public class MasterFrame extends JFrame {
	
	private static LogInGUI logIn = new LogInGUI();
	private static RegisterGUI register = new RegisterGUI();	
	private static TutorProfileGUI tutProfScrn = new TutorProfileGUI();
	private static StudentProfileGUI studProfScrn = new StudentProfileGUI();
	private static AdminProfileGUI admnProfScrn = new AdminProfileGUI();
	private static TutorGUI tutScreen = new TutorGUI();
	private static StudentGUI studScrn = new StudentGUI();
	private static AdminGUI admnScrn = new AdminGUI();
	private static ConfirmationDialogue cD = new ConfirmationDialogue();
	
	private static JFrame mFrame = new JFrame();
	
	
	//default constructor is never used.
	public MasterFrame (){
		
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	//creates the constructor used in the Main class.
	//The MasterFrame object is the parent JFrame that has swappable children contentPanes.
	public MasterFrame(JFrame frame)  {
		
		mFrame = frame;
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.setBounds(frame.getBounds());
		setContentPane(frame.getContentPane());	
		
	}
	
	//creates the 'set' method usesd to change contentPane view.
	public void setMasterFrame(JFrame frame){
		
		mFrame = frame;
		//this.setLocationRelativeTo(masterFrame); how can i keep it in the same spot?
		this.setBounds(frame.getBounds());
		this.setContentPane(frame.getContentPane());
		this.setVisible(true); 
		
	}

	public static LogInGUI getLogIn() {
		return logIn;
	}
	public static RegisterGUI getRegister() {
		return register;
	}
	public static TutorProfileGUI getTutProfScrn() {
		return tutProfScrn;
	}
	public static StudentProfileGUI getStudProfScrn() {
		return studProfScrn;
	}
	public static AdminProfileGUI getAdmnProfScrn() {
		return admnProfScrn;
	}
	public static TutorGUI getTutScrn() {
		return tutScreen;
	}
	public static StudentGUI getStudScrn() {
		return studScrn;
	}
	public static AdminGUI getAdmnScrn() {
		return admnScrn;
	}

	public static ConfirmationDialogue getCD() {
		return cD;
	}


}
