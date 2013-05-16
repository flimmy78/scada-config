package com.ht.scada.config.window;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.common.tag.entity.MajorTag;
import com.ht.scada.common.tag.service.EndTagService;
import com.ht.scada.common.tag.service.MajorTagService;
import com.ht.scada.config.scadaconfig.Activator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * 监控对象关联设备与模板
 * @author 赵磊
 *
 */
public class EndTagDeviceConfigWindow extends ApplicationWindow {
	private static class ViewerLabelProvider extends LabelProvider implements ITableLabelProvider{
		public Image getImage(Object element) {
			return super.getImage(element);
		}
		public String getText(Object element) {
			return super.getText(element);
		}
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public String getColumnText(Object element, int columnIndex) {
			EndTag endTag = (EndTag)element;
			switch (columnIndex) {
			case 0:
				return endTag.getName();
			default:
				break;
			}
			
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	private MajorTagService majorTagService = (MajorTagService) Activator.getDefault()
			.getApplicationContext().getBean("majorTagService");
	private EndTagService endTagService = (EndTagService) Activator.getDefault()
			.getApplicationContext().getBean("endTagService");
	
	private List<EndTag> endTagList = new ArrayList<EndTag>();
	private MajorTag majorTag;

	/**
	 * Create the application window.
	 */
	public EndTagDeviceConfigWindow(MajorTag majorTag) {
		super(null);
		this.majorTag = majorTag;
		endTagList = endTagService.getEndTagByParentId(majorTag.getId());
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
		gridColumn.setText("监控对象类型");
		
		GridViewerColumn gridViewerColumn_4 = new GridViewerColumn(gridTableViewer, SWT.NONE);
		GridColumn gridColumn_4 = gridViewerColumn_4.getColumn();
		gridColumn_4.setWidth(100);
		gridColumn_4.setText("变量模板");
		
		GridViewerColumn gridViewerColumn_2 = new GridViewerColumn(gridTableViewer, SWT.NONE);
		GridColumn gridColumn_2 = gridViewerColumn_2.getColumn();
		gridColumn_2.setWidth(150);
		gridColumn_2.setText("采集通道：序号");
		
		GridViewerColumn gridViewerColumn_3 = new GridViewerColumn(gridTableViewer, SWT.NONE);
		GridColumn gridColumn_3 = gridViewerColumn_3.getColumn();
		gridColumn_3.setWidth(150);
		gridColumn_3.setText("监控设备：名称");
		
		gridTableViewer.setLabelProvider(new ViewerLabelProvider());
		gridTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		gridTableViewer.setInput(endTagList);

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("关联变量模板与设备");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(856, 654);
	}

}
