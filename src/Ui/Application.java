package Ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Controllers.Controller;
import Controllers.MapController;
import Member.Member;
import Ui.Commons.LogBar;
import Ui.EditMenus.AccountEditionForm;
import Ui.EditMenus.FriendAdditionForm;
import Ui.Forms.TitleBarForms;
import Ui.Forms.TravelCreationForm;
import Ui.Markers.MemberPinMarker;
import Ui.SearchMenus.Menu;
import Utils.Mood;
import Utils.Util;
import fr.unice.iut.info.methodo.maps.Coordinate;

public class Application extends JFrame {

	private static final long serialVersionUID = 773127820785648597L;

	private MapInterfaceTree m_mapViewer;
	private Menu m_menu;
	private AccountEditionForm m_editionPanel;
	private FriendAdditionForm m_additionPanel;

	private JLabel lblFocusOnMember;
	private JButton btnFocusCurrentLocation;

	private JLabel lblEditAccount;
	private JButton btnEditAccount;
	private JLabel lblAddFriend;
	private JButton btnAddFriend;

	private Point coords;

	private LogBar feedBar;
	private JLabel lblUnzoom;
	private JButton btnUnzoom;
	private JLabel lblHome;
	private JButton btnHome;

	private double m_radiusChosen = 0.0;
	private boolean eyeShown;
	private JButton btnAddTravel;
	private JLabel lblAddTravel;

	public Application() {
		super("Iut Go");
		initialize();
	}

	private void initialize() {
		setSize(1000, 675);
		setUndecorated(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("/Resources/icone_iutgo.png")));
		getContentPane().setLayout(new BorderLayout());
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		setResizable(false);

		m_menu = new Menu();
		m_menu.setPreferredSize(new Dimension(190, 600));
		m_menu.setBorder(new MatteBorder(0, 1, 1, 1, (Color) new Color(0, 0, 0)));
		m_menu.getRadiusSlider().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				m_radiusChosen = ((double) m_menu.getRadiusSlider().getValue()) / 1000f;
			}
		});
		m_radiusChosen = ((double) m_menu.getRadiusSlider().getValue()) / 1000f;
		m_menu.getRelationMenu().getCheckbox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean visible = ((JCheckBox) e.getSource()).isSelected();
				MapController.getInstance().showRelationMembers(
						(String) m_menu.getRelationMenu().getComboBox().getSelectedItem(), visible);
			}
		});
		m_menu.getInterestMenu().getCheckbox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean visible = ((JCheckBox) e.getSource()).isSelected();
				String nameFilter = m_menu.getInterestMenu().getNameFilter().getText();
				float note = Float.parseFloat(m_menu.getInterestMenu().getRater().getRating().toString());
				MapController.getInstance().showPointOfInterest(m_radiusChosen, nameFilter, note, visible);
			}
		});
		m_menu.getInterestMenu().getNameFilter().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nameFilter = m_menu.getInterestMenu().getNameFilter().getText();
				float note = Float.parseFloat(m_menu.getInterestMenu().getRater().getRating().toString());
				MapController.getInstance().showPointOfInterest(m_radiusChosen, nameFilter, note, true);
			}
		});
		m_menu.getFormationMenu().getCheckbox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean visible = ((JCheckBox) e.getSource()).isSelected();
				String nameFilter = m_menu.getFormationMenu().getField().getText();
				MapController.getInstance().showFormationMembers(m_radiusChosen, nameFilter, visible);
			}
		});
		m_menu.getFormationMenu().getField().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nameFilter = m_menu.getFormationMenu().getField().getText();
				MapController.getInstance().showFormationMembers(m_radiusChosen, nameFilter, true);
			}
		});
		m_menu.getCarpoolingMenu().getCheckbox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean visible = ((JCheckBox) e.getSource()).isSelected();
				String start = m_menu.getCarpoolingMenu().getStart().getText();
				String end = m_menu.getCarpoolingMenu().getEnd().getText();
				boolean happy = m_menu.getCarpoolingMenu().getM_happyMood().isSelected();
				boolean sad = m_menu.getCarpoolingMenu().getM_sadMood().isSelected();
				boolean calm = m_menu.getCarpoolingMenu().getM_calmMood().isSelected();
				boolean party = m_menu.getCarpoolingMenu().getM_partyMood().isSelected();
				boolean none = (!happy && !sad && !calm && !party);
				if (happy || none)
					MapController.getInstance().showCarpools(m_radiusChosen, start, end, Mood.HAPPY, visible);
				if (sad || none)
					MapController.getInstance().showCarpools(m_radiusChosen, start, end, Mood.SAD, visible);
				if (calm || none)
					MapController.getInstance().showCarpools(m_radiusChosen, start, end, Mood.CALM, visible);
				if (party || none)
					MapController.getInstance().showCarpools(m_radiusChosen, start, end, Mood.PARTY, visible);
			}
		});

		TitleBarForms titleBarForms = new TitleBarForms();
		titleBarForms.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				coords = e.getPoint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				coords = null;
			}
		});
		titleBarForms.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				Point p = e.getLocationOnScreen();
				Application.this.setLocation((int) (p.getX() - coords.getX()), (int) (p.getY() - coords.getY()));
			}
		});
		titleBarForms.getBtnClose().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnHome.doClick();
			}
		});
		titleBarForms.getBtnMinus().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Application.this.setState(ICONIFIED);
			}
		});

		getRootPane().registerKeyboardAction(e -> {
			btnHome.doClick();
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

		getContentPane().add(titleBarForms, BorderLayout.NORTH);

		m_mapViewer = new MapInterfaceTree("Go");
		m_mapViewer.getViewer().setBorder(new MatteBorder(0, 0, 1, 1, (Color) new Color(0, 0, 0)));

		getContentPane().add(m_menu, BorderLayout.WEST);
		getContentPane().add(m_mapViewer, BorderLayout.CENTER);

		btnFocusCurrentLocation = new JButton("");
		btnFocusCurrentLocation.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnFocusCurrentLocation.setToolTipText("Display current location");
		btnFocusCurrentLocation.setOpaque(false);
		btnFocusCurrentLocation.setFocusPainted(false);
		btnFocusCurrentLocation.setContentAreaFilled(false);
		btnFocusCurrentLocation.setBorder(null);
		btnFocusCurrentLocation.setBounds(10, 565, 32, 34);
		btnFocusCurrentLocation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MapController.getInstance().showAndFitOnCurrentPosition();
			}
		});
		btnFocusCurrentLocation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent mv) {
				lblFocusOnMember.setIcon(Util.darken(lblFocusOnMember.getIcon()));
			}

			@Override
			public void mouseExited(MouseEvent mv) {
				lblFocusOnMember.setIcon(Util.brighten(lblFocusOnMember.getIcon()));
			}
		});

		btnEditAccount = new JButton("");
		btnEditAccount.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnEditAccount.setToolTipText("Edit account");
		btnEditAccount.setContentAreaFilled(false);
		btnEditAccount.setOpaque(false);
		btnEditAccount.setBorder(null);
		btnEditAccount.setBorderPainted(false);
		btnEditAccount.setFocusPainted(false);
		btnEditAccount.setBounds(763, 9, 32, 32);
		btnEditAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lblEditAccount.setVisible(false);
				btnEditAccount.setVisible(false);
				new Timer(0, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						m_editionPanel.setLocation(m_editionPanel.getX() - 1, m_editionPanel.getY());
						if (m_editionPanel.getX() < getWidth() - 650)
							((Timer) e.getSource()).stop();
					}
				}).start();
			}
		});
		btnEditAccount.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent mv) {
				lblEditAccount.setIcon(Util.darken(lblEditAccount.getIcon()));
			}

			@Override
			public void mouseExited(MouseEvent mv) {
				lblEditAccount.setIcon(Util.brighten(lblEditAccount.getIcon()));
			}
		});

		btnAddFriend = new JButton("");
		btnAddFriend.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnAddFriend.setToolTipText("Add relation");
		btnAddFriend.setOpaque(false);
		btnAddFriend.setFocusPainted(false);
		btnAddFriend.setContentAreaFilled(false);
		btnAddFriend.setBorder(null);
		btnAddFriend.setBorderPainted(false);
		btnAddFriend.setBounds(763, 52, 32, 32);
		btnAddFriend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnAddFriend.setVisible(false);
				lblAddFriend.setVisible(false);
				new Timer(5, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						m_additionPanel.setLocation(m_additionPanel.getX(), m_additionPanel.getY() + 1);
						if (m_additionPanel.getY() > 15)
							((Timer) e.getSource()).stop();
					}
				}).start();
			}
		});
		btnAddFriend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent me) {
				lblAddFriend.setIcon(Util.darken(lblAddFriend.getIcon()));
			}

			@Override
			public void mouseExited(MouseEvent me) {
				lblAddFriend.setIcon(Util.brighten(lblAddFriend.getIcon()));
			}
		});

		btnUnzoom = new JButton("");
		btnUnzoom.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnUnzoom.setToolTipText("Unzoom");
		btnUnzoom.setBorderPainted(false);
		btnUnzoom.setBorder(null);
		btnUnzoom.setContentAreaFilled(false);
		btnUnzoom.setFocusPainted(false);
		btnUnzoom.setBounds(763, 565, 32, 32);
		btnUnzoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LogBar.getInstance().showCommonFeedBack("You've juste unzoomed the map.");
				m_mapViewer.getViewer().setZoom(2);
			}
		});
		btnUnzoom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent me) {
				lblUnzoom.setIcon(Util.darken(lblUnzoom.getIcon()));
			}

			@Override
			public void mouseExited(MouseEvent me) {
				lblUnzoom.setIcon(Util.brighten(lblUnzoom.getIcon()));
			}
		});

		btnHome = new JButton("");
		btnHome.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnHome.setToolTipText("Disconnect");
		btnHome.setFocusPainted(false);
		btnHome.setContentAreaFilled(false);
		btnHome.setBorder(null);
		btnHome.setBorderPainted(false);
		btnHome.setBounds(52, 565, 32, 32);
		btnHome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getInstance().disconnectUser();
			}
		});
		btnHome.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent me) {
				lblHome.setIcon(Util.darken(lblHome.getIcon()));
			}

			@Override
			public void mouseExited(MouseEvent me) {
				lblHome.setIcon(Util.brighten(lblHome.getIcon()));
			}
		});
		m_mapViewer.getViewer().add(btnHome);
		m_mapViewer.getViewer().add(btnUnzoom);

		m_mapViewer.getViewer().add(btnAddFriend);

		m_mapViewer.getViewer().add(btnEditAccount);
		m_mapViewer.getViewer().add(btnFocusCurrentLocation);

		lblFocusOnMember = new JLabel("");
		lblFocusOnMember.setIcon(new ImageIcon(Application.class.getResource("/Resources/icone_currLocation.png")));
		lblFocusOnMember.setFont(new Font("FontAwesome", Font.PLAIN, 19));
		lblFocusOnMember.setBounds(10, 565, 32, 34);
		m_mapViewer.getViewer().add(lblFocusOnMember);

		lblEditAccount = new JLabel("");
		lblEditAccount.setIcon(new ImageIcon(Application.class.getResource("/Resources/icone_editAccount.png")));
		lblEditAccount.setBounds(763, 9, 32, 32);
		m_mapViewer.getViewer().add(lblEditAccount);

		m_editionPanel = new AccountEditionForm();
		m_editionPanel.setLocation(m_mapViewer.getX() + this.getWidth(), m_mapViewer.getY());
		m_editionPanel.setSize(445, 200);
		m_editionPanel.getBtnQuit().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Timer(0, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						m_editionPanel.setLocation(m_editionPanel.getX() + 1, m_editionPanel.getY());
						if (m_editionPanel.getX() > getWidth()) {
							lblEditAccount.setVisible(true);
							btnEditAccount.setVisible(true);
							lblEditAccount.setVisible(true);
							((Timer) e.getSource()).stop();
						}
					}
				}).start();
			}
		});
		m_mapViewer.getViewer().add(m_editionPanel);

		lblAddFriend = new JLabel("");
		lblAddFriend.setIcon(new ImageIcon(Application.class.getResource("/Resources/icone_addFriend.png")));
		lblAddFriend.setBounds(763, 52, 32, 32);
		m_mapViewer.getViewer().add(lblAddFriend);

		m_additionPanel = new FriendAdditionForm();
		m_additionPanel.setBounds(m_mapViewer.getX() + this.getWidth() / 5, -80, 340, 65);
		m_additionPanel.getBtnReduce().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Timer(2, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						m_additionPanel.setLocation(m_additionPanel.getX(), m_additionPanel.getY() - 1);
						if (m_additionPanel.getY() == -80) {
							((Timer) e.getSource()).stop();
							btnAddFriend.setVisible(true);
							lblAddFriend.setVisible(true);
						}
					}
				}).start();
			}
		});
		m_mapViewer.getViewer().add(m_additionPanel);

		feedBar = LogBar.getInstance();
		feedBar.setBounds(0, 610, 350, 25);
		m_mapViewer.getViewer().add(feedBar);

		lblUnzoom = new JLabel("");
		lblUnzoom.setIcon(new ImageIcon(Application.class.getResource("/Resources/icone_maxUnzoom.png")));
		lblUnzoom.setBounds(763, 565, 32, 32);
		m_mapViewer.getViewer().add(lblUnzoom);

		lblHome = new JLabel("");
		lblHome.setIcon(new ImageIcon(Application.class.getResource("/Resources/icone_disconnect.png")));
		lblHome.setBounds(52, 565, 32, 32);
		m_mapViewer.getViewer().add(lblHome);

		btnAddTravel = new JButton("");
		btnAddTravel.setToolTipText("Unzoom");
		btnAddTravel.setFocusPainted(false);
		btnAddTravel.setContentAreaFilled(false);
		btnAddTravel.setBorderPainted(false);
		btnAddTravel.setBorder(null);
		btnAddTravel.setBounds(721, 565, 32, 32);
		btnAddTravel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TravelCreationForm d = new TravelCreationForm();
				d.setVisible(true);
				d.getBtnAdd().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (!d.getStart().getText().isEmpty() && !d.getEnd().getText().isEmpty()) {
							Member m = Controller.getInstance().getCurrentMember();
							Coordinate c = m.getLastPosition().getMyCoordinate().toOSMCoordinate();
							Controller.getInstance().addPointOfInterest(m, (int)d.getSeat().getValue(), d.getStart().getText(), d.getEnd().getText(), c);
							d.dispose();
						}
					}
				});
				d.getBtnMin().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						d.dispose();
					}
				});
			}
		});
		m_mapViewer.getViewer().add(btnAddTravel);

		lblAddTravel = new JLabel("");
		lblAddTravel.setIcon(new ImageIcon(Application.class.getResource("/Resources/icone_travel.png")));
		lblAddTravel.setBounds(721, 565, 32, 32);
		m_mapViewer.getViewer().add(lblAddTravel);
		
		JButton btnEye = new JButton("");
		btnEye.setToolTipText("Hide/Show your location");
		btnEye.setIcon(new ImageIcon(Application.class.getResource("/Resources/icone_eye_visible.png")));
		btnEye.setFocusPainted(false);
		btnEye.setContentAreaFilled(false);
		btnEye.setBorder(null);
		btnEye.setBorderPainted(false);
		btnEye.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnEye.setBounds(721, 565, 32, 32);
		eyeShown = true;
		btnEye.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MapController.getInstance().getMarkerCollection().setMemberMarker(
						new MemberPinMarker("You", Controller.getInstance().getCurrentMember().getLastPosition().getMyCoordinate().toOSMCoordinate()));
				if(eyeShown)  {
					btnEye.setIcon(new ImageIcon(Application.class.getResource("/Resources/icone_eye_invisible.png")));
					MapController.getInstance().getMarkerCollection().getCurrentMember().setVisible(false);
				}
				else {
					btnEye.setIcon(new ImageIcon(Application.class.getResource("/Resources/icone_eye_visible.png")));
					MapController.getInstance().getMarkerCollection().getCurrentMember().setVisible(true);
				}
				eyeShown = !eyeShown;
				m_mapViewer.getViewer().updateUI();
			}
		});
		btnEye.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent me) { btnEye.setIcon(Util.darken(btnEye.getIcon())); }
			@Override
			public void mouseExited(MouseEvent me) { btnEye.setIcon(Util.brighten(btnEye.getIcon())); }
		});
		m_mapViewer.getViewer().add(btnEye);
	}

	public Menu getMenu() {
		return m_menu;
	}
}