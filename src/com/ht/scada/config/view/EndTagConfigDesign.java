package com.ht.scada.config.view;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.ViewPropertyChange;

/**
 * 组态设计页面
 * @author 王蓬
 * @time 2013.12.20
 */
public class EndTagConfigDesign extends ViewPart  implements IPropertyChangeListener {

	public static final String ID = "com.ht.scada.config.view.ConfigDesign"; //$NON-NLS-1$
	private Text text;

	private EndTag endTag;		// 监控节点对象
	public EndTagConfigDesign() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		{
			Label lblNewLabel = new Label(container, SWT.NONE);
			lblNewLabel.setBounds(10, 10, 72, 17);
			lblNewLabel.setText("监控对象名：");
		}
		{
			text = new Text(container, SWT.BORDER);
			text.setBounds(88, 7, 86, 23);
		}

		createActions();
		initializeToolBar();
		initializeMenu();
		
		ViewPropertyChange.getInstance().addPropertyChangeListener("endtagConfig",	//"endtagConfig随便起的监听的名字"
				this);
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Auto-generated method stub
		if (event.getProperty().equals(FirePropertyConstants.CONFIG_DESIGN)) {
			endTag = (EndTag) event.getNewValue();

			// 初始化控件值
			text.setText(endTag.getName());
		} 
	}

}
