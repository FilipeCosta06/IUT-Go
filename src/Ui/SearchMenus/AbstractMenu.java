package Ui.SearchMenus;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Utils.Util;

public abstract class AbstractMenu extends JPanel {

	protected static final long serialVersionUID = -5164242424023505183L;
	protected JPanel m_searchFilters;
	protected JCheckBox m_checkbox;
	protected JPanel m_headerPanel;

	public AbstractMenu(String p_header, boolean p_startsOpen) {
		setLayout(new BorderLayout(0, 0));
		
		m_headerPanel = new JPanel();
		add(m_headerPanel, BorderLayout.NORTH);
		m_headerPanel.setLayout(new BorderLayout(0, 0));
		
		m_checkbox = new JCheckBox(p_header);
		m_checkbox.setAlignmentY(Component.CENTER_ALIGNMENT);
		m_checkbox.setAlignmentX(Component.CENTER_ALIGNMENT);
		ImageIcon checkBox = new ImageIcon(RelationMenu.class.getResource("/Resources/_checkbox_ticked.png"));
		ImageIcon checkBoxUnticked = new ImageIcon(RelationMenu.class.getResource("/Resources/_checkbox_untick.png"));
		m_checkbox.setSelectedIcon(checkBox);
		m_checkbox.setIcon(checkBoxUnticked);
		m_checkbox.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		m_checkbox.setHorizontalAlignment(SwingConstants.LEFT);
		m_checkbox.setSelected(p_startsOpen);
		m_checkbox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		m_checkbox.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(m_checkbox.isSelected()){
					m_searchFilters.setVisible(true);
				}else{
					m_searchFilters.setVisible(false);
				}
				repaint();
				doLayout();
			}
		});
		m_headerPanel.add(m_checkbox);
		
		m_searchFilters = new JPanel();
		add(m_searchFilters, BorderLayout.CENTER);
		m_searchFilters.setLayout(new BorderLayout(0, 0));
		
		if(m_checkbox.isSelected()){
			m_searchFilters.setVisible(true);
		}else{
			m_searchFilters.setVisible(false);
		}
		initialize();
	}
	
	public JCheckBox getCheckbox() { return this.m_checkbox; }
	protected abstract void initialize();
}
