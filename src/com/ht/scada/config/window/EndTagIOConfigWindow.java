package com.ht.scada.config.window;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.common.tag.entity.MajorTag;
import com.ht.scada.common.tag.entity.VarIOInfo;
import com.ht.scada.common.tag.service.EndTagService;
import com.ht.scada.common.tag.service.VarIOInfoService;
import com.ht.scada.common.tag.type.entity.EndTagType;
import com.ht.scada.common.tag.type.service.TypeService;
import com.ht.scada.config.scadaconfig.Activator;

/**
 * 监控对象IO配置
 * @author 赵磊
 *
 */
public class EndTagIOConfigWindow extends ApplicationWindow {
	private class ContentProvider implements ITreeContentProvider{

		
		public void dispose() {
		}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		@Override
		public Object[] getChildren(Object parentElement) {
			if(parentElement instanceof EndTag) {
				EndTag endTag = (EndTag)parentElement;
				List<VarIOInfo> list = varIOInfoService.findByEndTagId(endTag.getId());
				if(list!=null && !list.isEmpty()) {
					System.out.println("个数：" + list.size());
					return list.toArray();
				}
			}
			return null;
		}
		@Override
		public Object getParent(Object element) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public boolean hasChildren(Object element) {
			Object[] children = this.getChildren(element);
			if (children == null) {
				return false;
			} else if (children.length > 0) {
				return true;
			} else {
				return false;
			}
		}
		@Override
		public Object[] getElements(Object inputElement) {
			List<EndTag> list = (List<EndTag>) inputElement;
			return list.toArray();
		}
		
	}
	private class ViewerLabelProvider extends LabelProvider implements ITableLabelProvider{
		public Image getImage(Object element) {
			return super.getImage(element);
		}
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		@Override
		public String getColumnText(Object element, int columnIndex) {
			if(element instanceof VarIOInfo) {
				VarIOInfo var = (VarIOInfo)element;
				switch (columnIndex) {
				case 0:
					return null;
				case 1:
					return var.getVarName();
				case 2:
					return "基数";
				case 3:
					return "系数";
				
				default:
					break;
				}
			}else if(element instanceof EndTag) {
				EndTag endTag = (EndTag)element;
				
				switch (columnIndex) {
				case 0:
					return endTag.getName();
				case 1:
					return null;
				case 2:
					return null;
				
				default:
					break;
				}
			}  
			return null;
		}
	}

	/**
	 * Create the application window.
	 */
	public EndTagIOConfigWindow(MajorTag majorTag) {
		super(null);
		this.majorTag = majorTag;
		endTagList = endTagService.getEndTagByParentId(majorTag.getId());
		endTagTypeList = typeService.getAllEndTagType();
		createActions();
	}
	
	private List<EndTag> endTagList = new ArrayList<EndTag>();
	private MajorTag majorTag;
	private List<EndTagType> endTagTypeList;
	
	private EndTagService endTagService = (EndTagService) Activator.getDefault()
			.getApplicationContext().getBean("endTagService");
	private TypeService typeService = (TypeService) Activator.getDefault()
			.getApplicationContext().getBean("typeService");
	private VarIOInfoService varIOInfoService = (VarIOInfoService)Activator.getDefault()
			.getApplicationContext().getBean("varIOInfoService");

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(container, SWT.NONE);
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("监控对象类型：");
		label.setBounds(10, 3, 84, 17);
		
		Combo combo = new Combo(composite, SWT.NONE);
		combo.setBounds(100, 0, 88, 25);
		
		GridTreeViewer gridTableViewer = new GridTreeViewer(container, SWT.BORDER);
		Grid grid = gridTableViewer.getGrid();
		grid.setRowHeaderVisible(true);
		grid.setHeaderVisible(true);
		grid.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		GridViewerColumn gridViewerColumn = new GridViewerColumn(gridTableViewer, SWT.NONE);
		GridColumn gridColumn_1 = gridViewerColumn.getColumn();
		gridColumn_1.setTree(true);
		gridColumn_1.setWidth(120);
		gridColumn_1.setText("监控对象名字");
		
		GridViewerColumn gridViewerColumn_2 = new GridViewerColumn(gridTableViewer, SWT.NONE);
		GridColumn gridColumn_2 = gridViewerColumn_2.getColumn();
		gridColumn_2.setWidth(100);
		gridColumn_2.setText("变量名");
		
		GridViewerColumn gridViewerColumn_1 = new GridViewerColumn(gridTableViewer, SWT.NONE);
		GridColumn gridColumn = gridViewerColumn_1.getColumn();
		gridColumn.setWidth(100);
		gridColumn.setText("基数");
		
		GridViewerColumn gridViewerColumn_4 = new GridViewerColumn(gridTableViewer, SWT.NONE);
		GridColumn gridColumn_4 = gridViewerColumn_4.getColumn();
		gridColumn_4.setWidth(100);
		gridColumn_4.setText("系数");
		
		gridTableViewer.setContentProvider(new ContentProvider());
		gridTableViewer.setLabelProvider(new ViewerLabelProvider());
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
		newShell.setText("监控对象IO配置");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(856, 654);
	}
	
	private EndTagType getEndTagType(String key) {
		if(endTagList != null && !endTagList.isEmpty()) {
			for(EndTagType type : endTagTypeList) {
				if(key.equals(type.getName()) || key.equals(type.getValue())) {
					return type;
				}
			}
		}
		return null;
	}

}
