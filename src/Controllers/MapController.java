package Controllers;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.swing.border.MatteBorder;

import Interests.InterestPoint;
import Member.Member;
import Travels.Travel;
import Ui.Forms.InterestForm;
import Ui.InfoCards.InterestCard;
import Ui.InfoCards.UserCard;
import Ui.Markers.FormationPinMarker;
import Ui.Markers.InterestPinMarker;
import Ui.Markers.MemberPinMarker;
import Ui.Markers.PinMarker;
import Ui.Markers.RelationPinMarker;
import Ui.Markers.TravelPinMarker;
import Utils.MarkerCollection;
import Utils.Mood;
import Utils.MyCoordinate;
import fr.unice.iut.info.methodo.maps.Coordinate;
import fr.unice.iut.info.methodo.maps.JMapController;
import fr.unice.iut.info.methodo.maps.JMapViewer;
import fr.unice.iut.info.methodo.maps.interfaces.MapMarker;

public class MapController extends JMapController
		implements MouseListener, MouseMotionListener, MouseWheelListener, Observer {

	private static final int MOUSE_BUTTONS_MASK = MouseEvent.BUTTON3_DOWN_MASK | MouseEvent.BUTTON1_DOWN_MASK
			| MouseEvent.BUTTON2_DOWN_MASK;

	private static final int MAC_MOUSE_BUTTON1_MASK = MouseEvent.BUTTON1_DOWN_MASK;
	private int movementMouseButton = MouseEvent.BUTTON1;
	private int movementMouseButtonMask = MouseEvent.BUTTON1_DOWN_MASK;
	private int interestMouseButton = MouseEvent.BUTTON3;

	private Point lastDragPoint;

	private boolean isMoving;
	private boolean movementEnabled = true;
	private boolean wheelZoomEnabled = true;
	private boolean isEditing = false;
	
	private MarkerCollection m_listMarkers;
	private UserCard m_userCard;
	private InterestCard m_interestCard;

	// -----------------------------------------------------------------
	// SINGLETON -------------------------------------------------
	private static MapController m_instance;

	private MapController(JMapViewer map) {
		super(map);
		m_listMarkers = new MarkerCollection();
		m_listMarkers.addObserver(this);
		m_userCard = new UserCard();
		m_userCard.getBtnMinus().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_userCard.setVisible(false);
				map.remove(m_userCard);
			}
		});
		m_userCard.setVisible(false);

		m_interestCard = new InterestCard();
		m_interestCard.getBtnMinus().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_interestCard.setVisible(false);
				map.remove(m_interestCard);
			}
		});
		m_interestCard.setVisible(false);
	}

	public static MapController getInstance() {
		return m_instance;
	}

	public static void init(JMapViewer p_map) {
		m_instance = new MapController(p_map);
	}

	// -----------------------------------------------------------------
	// LISTENERS -------------------------------------------------
	@Override
	public void mouseDragged(MouseEvent e) {
		if (!movementEnabled || !isMoving || !canMoveMap())
			return;
		// Is only the selected mouse button pressed?
		if ((e.getModifiersEx() & MOUSE_BUTTONS_MASK) == movementMouseButtonMask
				|| (isPlatformOsx() && e.getModifiersEx() == MAC_MOUSE_BUTTON1_MASK)) {
			Point p = e.getPoint();
			if (lastDragPoint != null) {
				int diffx = lastDragPoint.x - p.x;
				int diffy = lastDragPoint.y - p.y;
				map.moveMap(diffx, diffy);
			}
			lastDragPoint = p;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == movementMouseButton) {
			PinMarker p = isOnMarker(e.getPoint());
			if (p != null) {
				Point position = map.getMapPosition(p.getCoordinate());
				if(p instanceof TravelPinMarker){
					//Afficher panneau travel
				}
				else if(p instanceof FormationPinMarker){
					position.setLocation(position.getX() - m_userCard.getWidth() / 2,
							position.getY() - m_userCard.getHeight() / 2);
					m_userCard.showMember(((RelationPinMarker) p).getMember(), position);
					m_userCard.setVisible(true);
					map.add(m_userCard);
				}
				else if(p instanceof FormationPinMarker){
					position.setLocation(position.getX() - m_userCard.getWidth() / 2,
							position.getY() - m_userCard.getHeight() / 2);
					m_userCard.showMember(((RelationPinMarker) p).getMember(), position);
					m_userCard.setVisible(true);
					map.add(m_userCard);
				}
				else if (p instanceof MemberPinMarker) {
					position.setLocation(position.getX() - m_userCard.getWidth() / 2,
							position.getY() - m_userCard.getHeight() / 2);
					m_userCard.showMember(((MemberPinMarker) p).getMember(), position);
					m_userCard.setVisible(true);
					map.add(m_userCard);
				} else if (p instanceof InterestPinMarker) {
					position.setLocation(position.getX() - m_interestCard.getWidth() / 2,
							position.getY() - m_interestCard.getHeight() / 2);
					m_interestCard.showInterestPoint(((InterestPinMarker) p).getInterestPoint(), position);
					m_interestCard.setVisible(true);
					map.add(m_interestCard);
				}
			}
		} else if (e.getButton() == interestMouseButton) {
			Coordinate c = (Coordinate) map.getPosition(e.getPoint());
			InterestForm d = new InterestForm();
			Point p = e.getLocationOnScreen();
			d.setLocation((int)p.getX()-d.getWidth()/2,(int) p.getY()-d.getHeight()/2);
			d.setVisible(true);
			isEditing = true;
			d.getAddBtn().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!d.getTxtDesc().getText().isEmpty() && !d.getTxtName().getText().isEmpty()) {
						Controller.getInstance().addPointOfInterest(d.getTxtName().getText(), d.getTxtDesc().getText(),
								c);
						isEditing = false;
						d.dispose();
					}
				}
			});
			d.getMinBtn().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					isEditing = false;
					d.dispose();
				}
			});
			
		}
		map.updateUI();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == movementMouseButton || (isPlatformOsx() && e.getModifiersEx() == MAC_MOUSE_BUTTON1_MASK)) {
			lastDragPoint = null;
			isMoving = true;
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == movementMouseButton || (isPlatformOsx() && e.getButton() == MouseEvent.BUTTON1)) {
			lastDragPoint = null;
			isMoving = false;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (wheelZoomEnabled && canMoveMap()) {
			int rotation = JMapViewer.zoomReverseWheel ? -e.getWheelRotation() : e.getWheelRotation();
			if (map.getZoom() - rotation >= 2) {
				map.setZoom(map.getZoom() - rotation, e.getPoint());
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		map.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		map.setBorder(null);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (isPlatformOsx()) {
			if (!movementEnabled || !isMoving)
				return;
			if (e.getModifiersEx() == MouseEvent.CTRL_DOWN_MASK) {
				Point p = e.getPoint();
				if (lastDragPoint != null) {
					int diffx = lastDragPoint.x - p.x;
					int diffy = lastDragPoint.y - p.y;
					map.moveMap(diffx, diffy);
				}
				lastDragPoint = p;
			}
		}
	}

	public static boolean isPlatformOsx() {
		String os = System.getProperty("os.name");
		return os != null && os.toLowerCase(Locale.ENGLISH).startsWith("mac os x");
	}

	// -----------------------------------------------------------------
	// METHODES ESSENTIELLES -------------------------------------------------

	public void showAndFitOnCurrentPosition() {
		Coordinate currLocation = Controller.getInstance().getCurrentMember().getLastPosition().getMyCoordinate()
				.toOSMCoordinate();
		MemberPinMarker markerCurrLocation = new MemberPinMarker("You", currLocation);
		m_listMarkers.setMemberMarker(markerCurrLocation);
		map.setDisplayPosition(new Coordinate(currLocation.getLat(), currLocation.getLon()), 18);
	}

	public void showRelationMembers(String kindOfRelation, boolean visible) {
		m_listMarkers.removeAllRelations();
		if (visible) {
			for (Member m : Controller.getInstance().getCurrentMember().getRelations(kindOfRelation)) {
				MyCoordinate mcMember = m.getLastPosition().getMyCoordinate();
				Coordinate osmcMember = new Coordinate(mcMember.getLat(), mcMember.getLon());
				RelationPinMarker relation = new RelationPinMarker(m.toString(), osmcMember, m);
				m_listMarkers.addRelation(relation);
			}
		}
	}
	
	public void showRelationMembersByBood(String kindOfRelation, Mood mood, boolean visible) {
		m_listMarkers.removeAllRelations();
		if (visible) {
			for (Member m : Controller.getInstance().getCurrentMember().getRelations(kindOfRelation)) {
				if(m.getMood().equals(mood)) {
					MyCoordinate mcMember = m.getLastPosition().getMyCoordinate();
					Coordinate osmcMember = new Coordinate(mcMember.getLat(), mcMember.getLon());
					RelationPinMarker relation = new RelationPinMarker(m.toString(), osmcMember, m);
					m_listMarkers.addRelation(relation);
				}
			}
		}
	}

	public void showPointOfInterest(double p_radius, String nameFilter, float minNote, boolean visible) {
		m_listMarkers.removeAllInterest();
		if (visible) {
			ArrayList<InterestPoint> pIs = Controller.getInstance().getInterestManager().getNearestPointOfInterest(
					Controller.getInstance().getCurrentMember().getLastPosition().getMyCoordinate(), p_radius,
					nameFilter, minNote);
			for (InterestPoint p : pIs) {
				InterestPinMarker relation = new InterestPinMarker(p.getName(), p.getMyCoordinate().toOSMCoordinate(), p);
				m_listMarkers.addInteret(relation);
			}
		}
	}
	
	public void showFormationMembers(double p_radius, String formation, boolean visible){
		m_listMarkers.removeAllFormation();
		if (visible) {
			for (Member m : Controller.getInstance().getMembers()) {
				if(m.equals(Controller.getInstance().getCurrentMember())) continue;
				if(m.getFormation().getFormationName().toLowerCase().contains(formation.toLowerCase())){
					FormationPinMarker relation = new FormationPinMarker(m.toString(), m.getLastPosition().getMyCoordinate().toOSMCoordinate(), m);
					m_listMarkers.addFormation(relation);
				}
			}
		}
	}
	
	public void showCarpools(double p_radius, String start, String end, Mood mood, boolean visible){
		MyCoordinate pos = Controller.getInstance().getCurrentMember().getLastPosition().getMyCoordinate();
		m_listMarkers.removeAllFormation();
		if (visible) {
			for (Travel t : Controller.getInstance().getTravelManager().searchTravelAround(pos, p_radius, start, end, mood)) {
				TravelPinMarker relation = new TravelPinMarker(t.toString(), t.getCoord().toOSMCoordinate(), t);
				m_listMarkers.addCarpool(relation);
			}
		}
	}

	public boolean canMoveMap() {
		return !(m_userCard.isVisible() || m_interestCard.isVisible() || isEditing);
	}

	public PinMarker isOnMarker(Point position) {
		if(m_listMarkers.getCurrentMember() != null && m_listMarkers.getCurrentMember().contains(position)){
			return m_listMarkers.getCurrentMember();
		}
		for (MapMarker m : m_listMarkers.getAllRelations()) {
			if (m instanceof PinMarker) {
				if (((PinMarker) m).contains(position))
					return (PinMarker) m;
			}
		}
		for (MapMarker m : m_listMarkers.getAllInterest()) {
			if (m instanceof PinMarker) {
				if (((PinMarker) m).contains(position))
					return (PinMarker) m;
			}
		}
		for (MapMarker m : m_listMarkers.getAllFormations()) {
			if (m instanceof PinMarker) {
				if (((PinMarker) m).contains(position))
					return (PinMarker) m;
			}
		}
		for (MapMarker m : m_listMarkers.getAllCarpools()) {
			if (m instanceof PinMarker) {
				if (((PinMarker) m).contains(position))
					return (PinMarker) m;
			}
		}
		return null;
	}
	
	public MarkerCollection getMarkerCollection() { return m_listMarkers; }

	// ------------------------------------------------------------------
	// PATTERN OBSERVER-OBSERVABLE
	// -----------------------------------------------
	@Override
	public void update(Observable o, Object arg) {
		map.removeAllMapMarkers();
		MarkerCollection mc = (MarkerCollection) o;
		if(mc.getCurrentMember() != null){
			map.addMapMarker(mc.getCurrentMember());
		}
		for (MapMarker m : mc.getAllCarpools())
			map.addMapMarker(m);
		for (MapMarker m : mc.getAllRelations())
			map.addMapMarker(m);
		for (MapMarker m : mc.getAllInterest())
			map.addMapMarker(m);
		for (MapMarker m : mc.getAllFormations())
			map.addMapMarker(m);
		map.updateUI();
	}
}
