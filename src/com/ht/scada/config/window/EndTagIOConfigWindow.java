package com.ht.scada.config.window;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;

/**
 * 监控对象IO配置
 * @author 赵磊
 *
 */
public class EndTagIOConfigWindow extends ApplicationWindow {

	/**
	 * Create the application window.
	 */
	public EndTagIOConfigWindow() {
		super(null);
		createActions();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		GridTableViewer gridTableViewer = new GridTableViewer(container, SWT.BORDER);
		Grid grid = gridTableViewer.getGrid();
		grid.setHeaderVisible(true);
		grid.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		GridViewerColumn gridViewerColumn = new GridViewerColumn(gridTableViewer, SWT.NONE);
		GridColumn gridColumn_1 = gridViewerColumn.getColumn();
		gridColumn_1.setWidth(120);
		gridColumn_1.setText("监控对象名字");
		
		GridViewerColumn gridViewerColumn_1 = new GridViewerColumn(gridTableViewer, SWT.NONE);
		GridColumn gridColumn = gridViewerColumn_1.getColumn();
		gridColumn.setWidth(100);
		gridColumn.setText("基数");
		
		GridViewerColumn gridViewerColumn_4 = new GridViewerColumn(gridTableViewer, SWT.NONE);
		GridColumn gridColumn_4 = gridViewerColumn_4.getColumn();
		gridColumn_4.setWidth(100);
		gridColumn_4.setText("系数");

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			EndTagIOConfigWindow window = new EndTagIOConfigWindow();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("监控对象IO配置");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(856, 654);
	}

}