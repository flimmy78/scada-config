package com.ht.scada.config.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;

/**
 * 变量模板配置
 * @author 赵磊
 *
 */
public class VariableTemplateConfigView extends ViewPart implements IPropertyChangeListener {

	public VariableTemplateConfigView() {

	}
	
	private MenuManager menuMng;

	public static final String ID = "com.ht.scada.config.view.VariableTemplateConfigView";
	private Text text_muban_name;

	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm sashForm = new SashForm(parent, SWT.BORDER | SWT.SMOOTH);
		sashForm.setSashWidth(1);
		
		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		menuMng = new MenuManager();
		menuMng.setRemoveAllWhenShown(true);
		
		Group group = new Group(composite, SWT.NONE);
		group.setText("变量模板");
		group.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ListViewer listViewer = new ListViewer(group, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		menuMng.addMenuListener(new MenuListener(listViewer));
		
				list.setMenu(menuMng.createContextMenu(list)); // 添加菜单
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		
		Group group_2 = new Group(composite_1, SWT.NONE);
		group_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		group_2.setText("模板信息");
		group_2.setBounds(0, 0, 70, 84);
		
		Label label = new Label(group_2, SWT.NONE);
		label.setBounds(10, 24, 56, 17);
		label.setText("模板名：");
		
		text_muban_name = new Text(group_2, SWT.BORDER);
		text_muban_name.setBounds(79, 21, 96, 23);
		
		Group group_1 = new Group(composite_1, SWT.NONE);
		group_1.setLayout(new GridLayout(1, false));
		group_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		group_1.setText("变量信息");
		
		Composite composite_4 = new Composite(group_1, SWT.NONE);
		composite_4.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Grid grid = new Grid(composite_4, SWT.BORDER);
		grid.setHeaderVisible(true);
		grid.setColumnScrolling(true);
		grid.setCellSelectionEnabled(true);
		grid.setRowHeaderVisible(true);
		
		
		GridColumn gridColumn = new GridColumn(grid, SWT.NONE);
		gridColumn.setText("变量名");
		gridColumn.setWidth(70);
		
		GridColumnGroup gridColumnGroup = new GridColumnGroup(grid, SWT.TOGGLE);
		gridColumnGroup.setText("变量属性");
		
		GridColumn gridColumn_6 = new GridColumn(gridColumnGroup, SWT.NONE);
		gridColumn_6.setText("变量类型");
		gridColumn_6.setWidth(60);
		
		GridColumn gridColumn_1 = new GridColumn(gridColumnGroup, SWT.NONE);
		gridColumn_1.setText("子类型");
		gridColumn_1.setWidth(70);
		
		GridColumn gridColumn_2 = new GridColumn(gridColumnGroup, SWT.NONE);
		gridColumn_2.setText("变量分组");
		gridColumn_2.setWidth(70);
		
		GridColumn gridColumn_3 = new GridColumn(gridColumnGroup, SWT.NONE);
		gridColumn_3.setText("变量标志");
		gridColumn_3.setWidth(70);
		
		GridColumn gridColumn_4 = new GridColumn(gridColumnGroup, SWT.NONE);
		gridColumn_4.setText("值类型");
		gridColumn_4.setWidth(70);
		
		GridColumnGroup gridColumnGroup_1 = new GridColumnGroup(grid, SWT.TOGGLE);
		gridColumnGroup_1.setText("IO信息");
		
		GridColumn gridColumn_5 = new GridColumn(gridColumnGroup_1, SWT.NONE);
		gridColumn_5.setText("功能码");
		gridColumn_5.setWidth(50);
		
		GridColumn gridColumn_7 = new GridColumn(gridColumnGroup_1, SWT.NONE);
		gridColumn_7.setText("数据地址");
		gridColumn_7.setWidth(60);
		
		GridColumn gridColumn_8 = new GridColumn(gridColumnGroup_1, SWT.NONE);
		gridColumn_8.setText("字节长度");
		gridColumn_8.setWidth(60);
		
		GridColumn gridColumn_9 = new GridColumn(gridColumnGroup_1, SWT.NONE);
		gridColumn_9.setText("字节偏移量");
		gridColumn_9.setWidth(75);
		
		GridColumn gridColumn_10 = new GridColumn(gridColumnGroup_1, SWT.NONE);
		gridColumn_10.setText("位偏移量");
		gridColumn_10.setWidth(70);
		
		GridColumn gridColumn_11 = new GridColumn(gridColumnGroup_1, SWT.NONE);
		gridColumn_11.setText("基数");
		gridColumn_11.setWidth(40);
		
		GridColumn gridColumn_12 = new GridColumn(gridColumnGroup_1, SWT.NONE);
		gridColumn_12.setText("系数");
		gridColumn_12.setWidth(40);
		
		GridColumnGroup gridColumnGroup_2 = new GridColumnGroup(grid, SWT.TOGGLE);
		gridColumnGroup_2.setText("扩展信息");
		
		GridColumn gridColumn_13 = new GridColumn(gridColumnGroup_2, SWT.NONE);
		gridColumn_13.setText("存储规则");
		gridColumn_13.setWidth(65);
		
		GridColumn gridColumn_14 = new GridColumn(gridColumnGroup_2, SWT.NONE);
		gridColumn_14.setText("触发规则");
		gridColumn_14.setWidth(65);
		
		GridColumn gridColumn_15 = new GridColumn(gridColumnGroup_2, SWT.NONE);
		gridColumn_15.setText("最大值");
		gridColumn_15.setWidth(60);
		
		GridColumn gridColumn_16 = new GridColumn(gridColumnGroup_2, SWT.NONE);
		gridColumn_16.setWidth(60);
		gridColumn_16.setText("最小值");
		
		GridColumn gridColumn_17 = new GridColumn(gridColumnGroup_2, SWT.NONE);
		gridColumn_17.setWidth(65);
		gridColumn_17.setText("脉冲单位");
		
		Composite composite_3 = new Composite(group_1, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnNewButton_1 = new Button(composite_3, SWT.NONE);
		btnNewButton_1.setBounds(668, 0, 80, 27);
		btnNewButton_1.setText(" 导入变量词典 ");
		
		Button button_2 = new Button(composite_3, SWT.NONE);
		button_2.setText("导出变量词典 ");
		button_2.setBounds(765, 0, 80, 27);
		
		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite_2.setBounds(0, 0, 64, 64);
		
		Button button = new Button(composite_2, SWT.NONE);
		button.setBounds(0, 10, 80, 27);
		button.setText("新建模板");
		
		Button btnNewButton = new Button(composite_2, SWT.NONE);
		btnNewButton.setBounds(96, 10, 80, 27);
		btnNewButton.setText("删除模板");
		
		Button button_1 = new Button(composite_2, SWT.NONE);
		button_1.setBounds(194, 10, 80, 27);
		button_1.setText("保存模板");
		sashForm.setWeights(new int[] {78, 953});

	}
	
	private class MenuListener implements IMenuListener {
		private ListViewer listViewer;

		public MenuListener(ListViewer listViewer) {
			this.listViewer = listViewer;
		}

		@Override
		public void menuAboutToShow(IMenuManager manager) {
			IStructuredSelection selection = (IStructuredSelection) listViewer.getSelection();
			if (!selection.isEmpty()) {
				createContextMenu(selection.getFirstElement());
			} else {//新建
				Action action = new Action() {
					public void run() {
					}
				};
				action.setText("新建变量模板");
				menuMng.add(action);
			}
		}

		/**
		 * 右键菜单内容
		 * 
		 * @param
		 */
		private void createContextMenu(final Object selectedObject) {
			if (selectedObject instanceof String) {
				
				

			}
		}

	}


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
