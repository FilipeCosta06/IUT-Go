package Controllers;


import java.io.File;
import java.util.ArrayList;

import Data.InterestManager;
import Data.LinkManager;
import Data.SerialManager;
import Data.TravelManager;
import Member.Member;
import Online.FTPManager;
import Online.SQLManager;
import Travels.Travel;
import Ui.Application;
import Ui.Commons.SplashScreen;
import Ui.Forms.Form;
import Utils.Address;
import Utils.Formation;
import Utils.Mood;
import Utils.MyCoordinate;
import Utils.Util;
import fr.unice.iut.info.methodo.maps.Coordinate;

public class Controller {
	// ----------------------------------------- Mod�le --------------------------------------
	private Member m_currentMember;
	private ArrayList<Member> m_members;
	private InterestManager m_interests;
	private TravelManager m_travels;
	
	// ---------------------------------------- Vues -----------------------------------------
	private Form m_startScreen;
	private Application m_appScreen;
	
	// ---------------------------------------- Singleton -----------------------------------
	private static Controller m_controller = new Controller();
	
	private Controller() {
		FTPManager.initConnection();
		SQLManager.initConnection();
		SplashScreen.getInstance().setVisible(true);
		retrieveAllSerializedData();
		this.m_interests = new InterestManager();
		this.m_startScreen = new Form();
	}
	public static Controller getInstance() { return m_controller; }
	
	// --------------------------------------- Accesseurs -----------------------------------
	public ArrayList<Member> getMembers() { return this.m_members; }
	
	public void setCurrentMember(Member m) { this.m_currentMember = m; }
	public Member getCurrentMember() { return this.m_currentMember; }
	public InterestManager getInterestManager() { return this.m_interests; }
	public TravelManager getTravelManager() { return this.m_travels; }
	
	// -------------------------------------- M�thode --------------------------------------
	public void start() {
		this.m_startScreen.setVisible(true);
	}
	
	public boolean registerMember(String login, String pass, String lastname, String firstname) {
		
		boolean loginAlreadyUsed = false;
		for(Member m : this.m_members)
			if(m.getLogin().equals(login)) {
				loginAlreadyUsed = true;
				break;
			}
		
		if(loginAlreadyUsed) return false;
		
		Member m = new Member();
		m.setFirstname(firstname);
		m.setLastname(lastname);
		m.setLogin(login);
		m.setPassword(pass);
		
		SQLManager.insertMember(m);
		SerialManager.save(m, Util.getAndCreateAppdataPath()+File.separator+m.getId()+".dat");
		FTPManager.uploadMember(m);

		this.m_members.add(m);
		return true;
	}
	
	public boolean canLogMember(String login, String pass) {
		Member memberTest = null;
		for(Member m : this.m_members) {
			if(m.getLogin().equals(login) && m.getPassword().equals(pass)) {
				memberTest = m;
				break;
			}
		}
		if(memberTest != null) {
			this.m_currentMember = memberTest;
			this.m_currentMember.recordPosition();
			this.m_startScreen.dispose();
			this.m_appScreen = new Application();
			this.m_appScreen.setVisible(true);
		}
		return memberTest != null;
	}
	
	public boolean canAddRelation(String firstname, String lastname, String kindOfRelation) {
		Member relationToAdd = null;
		String fnCompared = firstname.toUpperCase();
		String lnCompared = lastname.toUpperCase();
		for(Member m : this.m_members) {
			String fnToCompare = m.getFirstname().toUpperCase(); 
			String lnToCompare = m.getLastname().toUpperCase(); 
			if(fnToCompare.equals(fnCompared) && lnToCompare.equals(lnCompared) && m != this.m_currentMember) {
				relationToAdd = m;
				LinkManager lm = LinkManager.getInstance();
				lm.createLink(this.m_currentMember, m, kindOfRelation);
				break;
			}
		}
		return relationToAdd != null;
	}
	
	public String giveBackPassword(String firstname, String lastname) {
		String passToGive = new String();
		for(Member m : this.m_members) {
			if(m.getFirstname().toUpperCase().equals(firstname.toUpperCase()))
					if(m.getLastname().toUpperCase().equals(lastname.toUpperCase()))
					{ passToGive = m.getPassword(); break; }
		}
		return passToGive;
	}
	
	public void fillInformationsForMember(String nick, String formation, Mood m, String add) {
		this.m_currentMember.setNickname(nick);
		this.m_currentMember.setFormation(new Formation(formation));
		this.m_currentMember.setMood(m);
		this.m_currentMember.setAddress(new Address(add));
	}
	
	public void retrieveAllSerializedData() {
		this.m_members = SerialManager.getAllMembers();
		this.m_interests = SerialManager.getInterestManager();
		this.m_travels = new TravelManager();
		SplashScreen.getInstance().setVisible(false);
	}
	
	public void serializeAllBeforeClose() {
		for(Member m : this.m_members) {
			SerialManager.save(m, Util.getAndCreateAppdataPath()+File.separator+m.getId()+".dat");
			FTPManager.uploadMember(m);
		}
		if(m_interests.size() > 0) {
			SerialManager.save(m_interests, Util.getAndCreateAppdataPath()+File.separator+"im.dat");
			FTPManager.uploadInterestManager();
		}

		FTPManager.closeConnection();
		SQLManager.closeConnection();
	}
	public void disconnectUser() {
		this.serializeAllBeforeClose();
		this.m_appScreen.dispose();
		this.m_currentMember = null;
		this.m_startScreen = new Form();
		this.m_startScreen.setVisible(true);
	}
	
	public void addPointOfInterest(String name, String desc, Coordinate c){
		m_interests.createInterestPoint(name, desc, c.getLat(), c.getLon());
		SerialManager.save(m_interests, Util.getAndCreateAppdataPath()+File.separator+"im.dat");
	}
	
	public void addPointOfInterest(Member m, int seats, String cStart, String cEnd, Coordinate c){
		m_travels.addTravel(new Travel(m, seats, new MyCoordinate(c.getLat(), c.getLon()), cStart, cEnd));
	}
}