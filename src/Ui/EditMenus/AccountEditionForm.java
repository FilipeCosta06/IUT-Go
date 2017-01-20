package Ui.EditMenus;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import Controllers.Controller;
import Ui.Commons.LogBar;
import Ui.Commons.MemberProfile;
import Utils.Mood;
import Utils.Util;

@SuppressWarnings("serial")
public class AccountEditionForm extends JPanel implements ActionListener{

	private MemberProfile panelProfile;
	
	private JLabel lblNickname;
	private JTextField fieldNickname;
	
	private JLabel lblAddress;
	private JTextField fieldAddress;
	
	private JLabel lblFormation;
	private JTextField fieldFormation;
	
	private JLabel lblMood;
	
	private JCheckBox checkHappy;
	private JCheckBox checkCalm;
	private JCheckBox checkSad;
	private JCheckBox checkParty;
	private ButtonGroup checkGroup;
	
	private JButton btnConfirm;
	private JLabel lblQuit;
	private JButton btnQuit;
	
	private Mood moodSelected;
	private JButton btnEraseMood;
		
	public AccountEditionForm() {
		setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		setLayout(null);
		
		btnQuit = new JButton("");
		btnQuit.setFocusPainted(false);
		btnQuit.setContentAreaFilled(false);
		btnQuit.setBorderPainted(false);
		btnQuit.setBorder(null);
		btnQuit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnQuit.setBounds(412, 10, 28, 28);
		btnQuit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent me) { lblQuit.setForeground(Color.BLUE); }
			@Override
			public void mouseExited(MouseEvent me) { lblQuit.setForeground(Color.BLACK); }
		});
		add(btnQuit);
		
		lblQuit = new JLabel("\uF00D");
		lblQuit.setFont(new Font("FontAwesome", Font.PLAIN, 26));
		lblQuit.setBounds(413, 11, 37, 27);
		add(lblQuit);
		
		lblNickname = new JLabel("Nickname");
		lblNickname.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 18));
		lblNickname.setBounds(10, 92, 54, 22);
		add(lblNickname);
		
		fieldNickname = new JTextField();
		fieldNickname.setHorizontalAlignment(SwingConstants.CENTER);
		fieldNickname.setText(Controller.getInstance().getCurrentMember().getNickname());
		fieldNickname.setBounds(76, 92, 110, 20);
		add(fieldNickname);
		fieldNickname.setColumns(10);
		
		lblMood = new JLabel("Mood");
		lblMood.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 18));
		lblMood.setBounds(223, 96, 46, 14);
		add(lblMood);
		
		lblAddress = new JLabel("Address");
		lblAddress.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 18));
		lblAddress.setBounds(10, 130, 54, 14);
		add(lblAddress);
		
		fieldAddress = new JTextField();
		fieldAddress.setHorizontalAlignment(SwingConstants.CENTER);
		fieldAddress.setText(Controller.getInstance().getCurrentMember().getAddress().toString());
		fieldAddress.setBounds(76, 128, 110, 20);
		add(fieldAddress);
		fieldAddress.setColumns(10);
		
		lblFormation = new JLabel("Formation");
		lblFormation.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 18));
		lblFormation.setBounds(223, 131, 60, 14);
		add(lblFormation);
		
		fieldFormation = new JTextField();
		fieldFormation.setHorizontalAlignment(SwingConstants.CENTER);
		fieldFormation.setText(Controller.getInstance().getCurrentMember().getFormation().toString());
		fieldFormation.setBounds(293, 128, 128, 20);
		add(fieldFormation);
		fieldFormation.setColumns(10);
		
		btnConfirm = new JButton("Confirm");
		btnConfirm.setBackground(Color.BLACK);
		btnConfirm.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		btnConfirm.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.PLAIN, 14));
		btnConfirm.setIcon(new ImageIcon(AccountEditionForm.class.getResource("/Resources/102.png")));
		btnConfirm.setBounds(164, 170, 105, 23);
		btnConfirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnConfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nickname = fieldNickname.getText();
				String formation = fieldFormation.getText();
				String address = fieldAddress.getText();
				Mood m = moodSelected;
				Controller.getInstance().fillInformationsForMember(nickname, formation, m, address);
				LogBar.getInstance().showPositiveFeedback("You've just updated your personal informations.");
				btnQuit.doClick();
			}
		});
		add(btnConfirm);
		
		checkHappy = new JCheckBox("");
		checkHappy.setToolTipText("Happy");
		checkHappy.setIcon(new ImageIcon(AccountEditionForm.class.getResource("/Resources/_smiley_happy_u.png")));
		checkHappy.setSelectedIcon(new ImageIcon(AccountEditionForm.class.getResource("/Resources/_smiley_happy.png")));
		checkHappy.setBounds(264, 88, 37, 32);
		add(checkHappy);
		
		checkCalm = new JCheckBox("");
		checkCalm.setToolTipText("Calm");
		checkCalm.setSelectedIcon(new ImageIcon(AccountEditionForm.class.getResource("/Resources/_smiley_calm.png")));
		checkCalm.setIcon(new ImageIcon(AccountEditionForm.class.getResource("/Resources/_smiley_calm_u.png")));
		checkCalm.setBounds(302, 88, 37, 32);
		add(checkCalm);
		
		checkSad = new JCheckBox("");
		checkSad.setToolTipText("Sad");
		checkSad.setSelectedIcon(new ImageIcon(AccountEditionForm.class.getResource("/Resources/_smiley_sad.png")));
		checkSad.setIcon(new ImageIcon(AccountEditionForm.class.getResource("/Resources/_smiley_sad_u.png")));
		checkSad.setBounds(340, 88, 37, 32);
		add(checkSad);
		
		checkParty = new JCheckBox("");
		checkParty.setToolTipText("Party");
		checkParty.setIcon(new ImageIcon(AccountEditionForm.class.getResource("/Resources/_smiley_party_u.png")));
		checkParty.setSelectedIcon(new ImageIcon(AccountEditionForm.class.getResource("/Resources/_smiley_party.png")));
		checkParty.setBounds(378, 88, 37, 32);
		add(checkParty);
		
		checkGroup = new ButtonGroup();
		checkGroup.add(checkHappy); checkHappy.addActionListener(this);
		checkGroup.add(checkCalm); checkCalm.addActionListener(this);
		checkGroup.add(checkSad); checkSad.addActionListener(this);
		checkGroup.add(checkParty); checkParty.addActionListener(this);
		
		btnEraseMood = new JButton();
		btnEraseMood.setFocusPainted(false);
		btnEraseMood.setContentAreaFilled(false);
		btnEraseMood.setBorder(null);
		btnEraseMood.setIcon(new ImageIcon(AccountEditionForm.class.getResource("/Resources/101.png")));
		btnEraseMood.setBounds(416, 89, 24, 28);
		btnEraseMood.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				checkGroup.clearSelection();
				moodSelected = Mood.NO_SPECIAL_MOOD;
			}
		});
		add(btnEraseMood);
		
		this.panelProfile = new MemberProfile();
		panelProfile.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.BLACK));
		panelProfile.setBounds(1, 1, 438, 80);
		add(this.panelProfile);
	}

	public JButton getBtnQuit() { return this.btnQuit; }

	@Override
	public void actionPerformed(ActionEvent e) {
		JCheckBox src = (JCheckBox)e.getSource();
		moodSelected = Util.decodeMood(src.getToolTipText());
	}
}
