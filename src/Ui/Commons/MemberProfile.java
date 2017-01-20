package Ui.Commons;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import Controllers.Controller;
import Member.Member;
import java.awt.SystemColor;


public class MemberProfile extends JPanel {

	private static final long serialVersionUID = 1246643824073349692L;
	private JLabel userIcon;
	private JLabel lblLastname;
	private JLabel lblFirstname;
	private JPanel panel;
	private JPanel panel_1;

	public MemberProfile() {
		setBackground(Color.LIGHT_GRAY);
		setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		setSize(new Dimension(220, 80));
		setMinimumSize(new Dimension(220, 80));
		setMaximumSize(new Dimension(220, 80));
		
		panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setLayout(new BorderLayout(10, 10));
		
		userIcon = new JLabel("\uF2BE");
		userIcon.setForeground(new Color(30, 144, 255));
		panel.add(userIcon, BorderLayout.WEST);
		userIcon.setHorizontalAlignment(SwingConstants.CENTER);
		userIcon.setFont(new Font("FontAwesome", Font.PLAIN, 60));
		userIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent me) { userIcon.setForeground(Color.BLUE);}
			@Override
			public void mouseExited(MouseEvent me) {  userIcon.setForeground(new Color(30,144,255));}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(10))
		);
		
		panel_1 = new JPanel();
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(2, 1, 10, 10));
		
		lblLastname = new JLabel();
		lblLastname.setForeground(new Color(30, 144, 255));
		lblLastname.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_1.add(lblLastname);
		lblLastname.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblLastname.setHorizontalAlignment(SwingConstants.LEFT);
		
		lblFirstname = new JLabel();
		lblFirstname.setForeground(new Color(30, 144, 255));
		lblFirstname.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_1.add(lblFirstname);
		lblFirstname.setHorizontalAlignment(SwingConstants.LEFT);
		setLayout(groupLayout);
		
		updateProfile(Controller.getInstance().getCurrentMember());
		repaint();
	}
	
	public void updateProfile(Member p_member){
		if(p_member != null){
			lblLastname.setText(p_member.getLastname());
			lblFirstname.setText(p_member.getFirstname());
		}else{
			lblLastname.setText("UNDEFINED");
			lblFirstname.setText("UNDEFINED");
		}
	}
}
