package com.ht.scada.config.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.scada.common.tag.entity.TagCfgTpl;
import com.ht.scada.common.tag.service.TagCfgTplService;
import com.ht.scada.config.scadaconfig.Activator;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * 变量模板配置
 * 
 * @author 赵磊
 * 
 */
public class VariableTemplateConfigView extends ViewPart {
	private static final Logger log = LoggerFactory
			.getLogger(VariableTemplateConfigView.class);

	private static class ViewerLabelProvider_1 extends LabelProvider implements
			ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			TagCfgTpl tagCfgTpl = (TagCfgTpl) element;

			switch (columnIndex) {
			case 0:// 变量名
				return tagCfgTpl.getTagName();
			case 1:// 变量类型
				return tagCfgTpl.getVarType() == null ? null : tagCfgTpl
						.getVarType().getValue();
			case 2:// 变量子类型
				return tagCfgTpl.getSubType();
			case 3:// 变量分组
				return tagCfgTpl.getVarGroup() == null ? null : tagCfgTpl
						.getVarGroup().getValue();
			case 4:// 变量key
				return tagCfgTpl.getVarName();
			case 5:// 功能码
				return String.valueOf(tagCfgTpl.getFunCode());
			case 6:// 数据地址
				return String.valueOf(tagCfgTpl.getDataID());
			case 7:// 字节长度
				return String.valueOf(tagCfgTpl.getByteLen());
			case 8:// 字节偏移量
				return String.valueOf(tagCfgTpl.getByteOffset());
			case 9:// 位偏移量
				return String.valueOf(tagCfgTpl.getBitOffset());
			case 10:// 值类型
				return tagCfgTpl.getDataType() == null ? null : tagCfgTpl
						.getDataType().getValue();
			case 11:// 基数
				return tagCfgTpl.getBaseValue() == null ? "" : String
						.valueOf(tagCfgTpl.getBaseValue());
			case 12:// 系数
				return tagCfgTpl.getCoefValue() == null ? "" : String
						.valueOf(tagCfgTpl.getCoefValue());
			case 13:// 存储规则
				return tagCfgTpl.getStorage();
			case 14:// 触发规则
				return tagCfgTpl.getTrigger();
			case 15:// 最大值
				return tagCfgTpl.getMax() == null ? "" : String
						.valueOf(tagCfgTpl.getMax());
			case 16:// 最小值
				return tagCfgTpl.getMin() == null ? "" : String
						.valueOf(tagCfgTpl.getMin());
			case 17:// 脉冲单位
				return tagCfgTpl.getUnit() == null ? "" : String
						.valueOf(tagCfgTpl.getUnit());

			default:
				break;
			}

			return null;
		}
	}

	private static class ViewerLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			return super.getImage(element);
		}

		public String getText(Object element) {
			if (element instanceof String) {
				return (String) element;
			}
			return super.getText(element);
		}
	}

	public VariableTemplateConfigView() {
		tplNameList = tagCfgTplService.findAllTplName();
	}

	private TagCfgTplService tagCfgTplService = (TagCfgTplService) Activator
			.getDefault().getApplicationContext().getBean("tagCfgTplService");

	private MenuManager menuMng;

	public static final String ID = "com.ht.scada.config.view.VariableTemplateConfigView";
	private Text text_tpl_name;
	private ListViewer listViewer_1;
	private List<String> tplNameList; // 所有变量模板名字
	private List<TagCfgTpl> tagCfgTplList = new ArrayList<>(); // 当前模板所有变量
	private GridTableViewer gridTableViewer;

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

		listViewer_1 = new ListViewer(group, SWT.BORDER | SWT.V_SCROLL);
		listViewer_1
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection selection = (IStructuredSelection) event
								.getSelection();
						if (!selection.isEmpty()) {
							String tplName = (String) selection
									.getFirstElement();
							initTplInfo(tplName);
						}

					}
				});
		org.eclipse.swt.widgets.List list = listViewer_1.getList();
		menuMng.addMenuListener(new MenuListener(listViewer_1));

		list.setMenu(menuMng.createContextMenu(list)); // 添加菜单
		listViewer_1.setLabelProvider(new ViewerLabelProvider());

		listViewer_1.setContentProvider(ArrayContentProvider.getInstance());
		listViewer_1.setInput(tplNameList);

		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));

		Group group_2 = new Group(composite_1, SWT.NONE);
		group_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));
		group_2.setText("模板信息");
		group_2.setBounds(0, 0, 70, 84);

		Label label = new Label(group_2, SWT.NONE);
		label.setBounds(10, 24, 56, 17);
		label.setText("模板名：");

		text_tpl_name = new Text(group_2, SWT.BORDER);
		text_tpl_name.setBounds(79, 21, 96, 23);

		Group group_1 = new Group(composite_1, SWT.NONE);
		group_1.setLayout(new GridLayout(1, false));
		group_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		group_1.setText("变量信息");

		Composite composite_3 = new Composite(group_1, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Button btnNewButton_1 = new Button(composite_3, SWT.NONE);
		btnNewButton_1.setBounds(668, 0, 80, 27);
		btnNewButton_1.setText(" 导入变量词典 ");

		Button button_2 = new Button(composite_3, SWT.NONE);
		button_2.setText("导出变量词典 ");
		button_2.setBounds(765, 0, 80, 27);

		Label lblNewLabel = new Label(composite_3, SWT.NONE);
		lblNewLabel.setBounds(10, 8, 61, 17);
		lblNewLabel.setText("变量类型：");

		Combo combo = new Combo(composite_3, SWT.NONE);
		combo.setBounds(77, 2, 71, 25);

		Label label_1 = new Label(composite_3, SWT.NONE);
		label_1.setBounds(177, 8, 61, 17);
		label_1.setText("变量分组：");

		Combo combo_1 = new Combo(composite_3, SWT.NONE);
		combo_1.setBounds(241, 2, 88, 25);

		Composite composite_4 = new Composite(group_1, SWT.NONE);
		composite_4.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		gridTableViewer = new GridTableViewer(composite_4, SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);
		Grid grid = gridTableViewer.getGrid();
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

		GridColumnGroup gridColumnGroup_1 = new GridColumnGroup(grid,
				SWT.TOGGLE);
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

		GridColumn gridColumn_4 = new GridColumn(gridColumnGroup_1, SWT.NONE);
		gridColumn_4.setText("值类型");
		gridColumn_4.setWidth(70);

		GridColumn gridColumn_11 = new GridColumn(gridColumnGroup_1, SWT.NONE);
		gridColumn_11.setText("基数");
		gridColumn_11.setWidth(40);

		GridColumn gridColumn_12 = new GridColumn(gridColumnGroup_1, SWT.NONE);
		gridColumn_12.setText("系数");
		gridColumn_12.setWidth(40);

		GridColumnGroup gridColumnGroup_2 = new GridColumnGroup(grid,
				SWT.TOGGLE);
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
		
		Menu menu = new Menu(grid);
		grid.setMenu(menu);
		
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText("添加变量");
		
		MenuItem menuItem_1 = new MenuItem(menu, SWT.NONE);
		menuItem_1.setText("删除变量");

		gridTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		gridTableViewer.setLabelProvider(new ViewerLabelProvider_1());
		gridTableViewer.setInput(tagCfgTplList);

		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
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
		sashForm.setWeights(new int[] { 78, 953 });

	}

	private class MenuListener implements IMenuListener {
		private ListViewer listViewer;

		public MenuListener(ListViewer listViewer) {
			this.listViewer = listViewer;
		}

		@Override
		public void menuAboutToShow(IMenuManager manager) {
			IStructuredSelection selection = (IStructuredSelection) listViewer
					.getSelection();
			if (!selection.isEmpty()) {
				createContextMenu(selection.getFirstElement());
			} else {// 新建
				Action action = new Action() {
					public void run() {
						addTpl();
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
				Action action = new Action() {
					public void run() {
						addTpl();
					}
				};
				action.setText("新建变量模板");
				menuMng.add(action);

				action = new Action() {
					public void run() {
					}
				};
				action.setText("删除变量模板");
				menuMng.add(action);

			}
		}

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * 初始化模板信息
	 * 
	 * @param tplName
	 */
	private void initTplInfo(String tplName) {
		text_tpl_name.setText(tplName);
		if (!"".equals(tplName)) {
			initVariableByTplName(tplName);
		}
	}

	private void initVariableByTplName(String tplName) {
		tagCfgTplList.clear();

		tagCfgTplList = tagCfgTplService.findVariablesByTplName(tplName);
		log.debug("变量个数：" + tagCfgTplList.size());

		gridTableViewer.setInput(tagCfgTplList);
		gridTableViewer.refresh();

	}

	/**
	 * 新建变量模板
	 */
	private void addTpl() {
		text_tpl_name.setText("");

	}
}
