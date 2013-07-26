package com.ht.scada.config.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.ht.scada.common.tag.entity.VarGroupCfg;
import com.ht.scada.common.tag.service.VarGroupCfgService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.LayoutUtil;
import org.eclipse.jface.fieldassist.ControlDecoration;

/**
 * 变量分组配置
 * 
 * @author 赵磊
 * 
 */
public class VariableGroupConfigView extends ViewPart implements
		IPropertyChangeListener {

	private GridTableViewer gridTableViewer;
	private List<VarGroupCfg> varGroupCfgList = new ArrayList<>(); // 变量分组列表

	private VarGroupCfgService varGroupCfgService = (VarGroupCfgService) Activator
			.getDefault().getApplicationContext().getBean("varGroupCfgService");

	public VariableGroupConfigView() {
		varGroupCfgList = varGroupCfgService.getAllVarGroupCfg();
		
//		if (varGroupCfgList == null || varGroupCfgList.isEmpty()) {
//			for (int i = 0; i < VarGroup.values().length; i++) {
//				VarGroupCfg varGroupCfg = new VarGroupCfg();
//				VarGroup value = VarGroup.values()[i];
//				varGroupCfg.setIntvl(-1);
//				varGroupCfg.setName(value.getValue());
//				varGroupCfg.setVarGroup(value);
//				varGroupCfgList.add(varGroupCfg);
//			}
//
//		}
	}

	public static final String ID = "com.ht.scada.config.view.VariableGroupConfigView";

	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		gridTableViewer = new GridTableViewer(parent, SWT.BORDER);
		final Grid grid = gridTableViewer.getGrid();
		grid.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grid.setRowHeaderVisible(true);
		grid.setHeaderVisible(true);

		GridViewerColumn nameGridViewerColumn = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn nameGridColumn = nameGridViewerColumn.getColumn();
		nameGridColumn.setAlignment(SWT.CENTER);
		nameGridColumn.setWidth(100);
		nameGridColumn.setText("名字");

		GridViewerColumn intervalGridViewerColumn = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		
		intervalGridViewerColumn.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				VarGroupCfg vgc = (VarGroupCfg) element;
				return String.valueOf(vgc.getIntvl());
			}

			protected void setValue(Object element, Object value) {
				VarGroupCfg vgc = (VarGroupCfg) element;
				vgc.setIntvl(Integer.valueOf((String) value));
				gridTableViewer.update(vgc, null);
			}
		});
		
		GridColumn intervalGridColumn = intervalGridViewerColumn.getColumn();
		intervalGridColumn.setAlignment(SWT.CENTER);
		intervalGridColumn.setWidth(100);
		intervalGridColumn.setText("存储间隔");
		
		ControlDecoration controlDecoration = new ControlDecoration(grid, SWT.LEFT | SWT.TOP);
		controlDecoration.setDescriptionText("Some description");

		gridTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		gridTableViewer.setLabelProvider(new ViewerLabelProvider());
		gridTableViewer.setInput(varGroupCfgList);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(null);

		final Button btnSave = new Button(composite, SWT.NONE);
		btnSave.setBounds(142, 10, 52, 27);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(VarGroupCfg varGroupCfg : varGroupCfgList){
					varGroupCfgService.create(varGroupCfg);
				}
				MessageDialog.openInformation(btnSave.getShell(), "提示", "保存成功！");
			}
		});
		btnSave.setText(" 保  存 ");

		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.setBounds(265, 10, 52, 27);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				LayoutUtil.hideViewPart();
			}
		});
		btnCancel.setText(" 取  消 ");

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
	}

	private static class ViewerLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			VarGroupCfg varGroupCfg = (VarGroupCfg) element;

			switch (columnIndex) {
			case 0:// 名字
				return varGroupCfg.getValue() == null ? null : varGroupCfg
						.getValue();
			case 1:// 存储间隔
				return varGroupCfg.getIntvl() + "";

			default:
				break;
			}
			return null;
		}
	}

}
