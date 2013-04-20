package com.ht.scada.config.view;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.common.tag.entity.MajorTag;
import com.ht.scada.common.tag.service.TagService;
import com.ht.scada.common.tag.well.consts.EndTagType;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.ViewPropertyChange;
import com.ht.scada.config.view.tree.RootTreeModel;

public class EndTagView extends ViewPart implements IPropertyChangeListener {
	
	public EndTagView() {
		int lenght = EndTagType.values().length;
		endTagType = new String[lenght];
		for(int i=0;i<lenght;i++) {
			endTagType[i] = EndTagType.values()[i].getValue();
		}
			
	}

	public static final String ID = "com.ht.scada.config.view.EndTagView";
	private Text text_name;
	private EndTag endTag;
	private String endTagType[];
	
	private TagService tagService = (TagService) Activator.getDefault().getApplicationContext().getBean("tagService");
	private Table table;
	
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.verticalSpacing = 20;
		gl_parent.marginTop = 25;
		gl_parent.marginLeft = 40;
		parent.setLayout(gl_parent);
		
		Label label = new Label(parent, SWT.NONE);
		label.setText("监控对象名：");
		
		text_name = new Text(parent, SWT.BORDER);
		GridData gd_text_name = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_name.widthHint = 100;
		text_name.setLayoutData(gd_text_name);
		
		Label label_1 = new Label(parent, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("监控对象类型：");
		
		final Combo combo = new Combo(parent, SWT.NONE);
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo.widthHint = 100;
		combo.setLayoutData(gd_combo);
		combo.setItems(endTagType);
		
		TableViewer tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		GridData gd_table = new GridData(SWT.LEFT, SWT.FILL, true, true, 2, 1);
		gd_table.widthHint = 220;
		table.setLayoutData(gd_table);
		
		Button btnNewButton = new Button(parent, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(endTag.getId() == null) {//新建
					if("".equals(text_name.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误", "监控对象名字不能为空！");
						return;
					}
					
					endTag.setName(text_name.getText().trim());
					endTag.setType(combo.getText());
					
					tagService.createEndTag(endTag);
					
					ScadaObjectTreeView.treeViewer.add(endTag.getMajorTag(), endTag);
					ScadaObjectTreeView.treeViewer.setExpandedState(endTag.getMajorTag(), true);
					
				} else {//编辑
					if("".equals(text_name.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误", "监控对象名字不能为空！");
						return;
					}
					
					endTag.setName(text_name.getText().trim());
					endTag.setType(combo.getText());
					
					tagService.updateEndTag(endTag);
					
					ScadaObjectTreeView.treeViewer.update(endTag, null);
				}
				
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				IWorkbenchPart part = page.getActivePart();
				if (part instanceof IViewPart)
					page.hideView((IViewPart) part);
			}
		});
		btnNewButton.setText(" 保  存 ");
		
		Button btnNewButton_1 = new Button(parent, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				IWorkbenchPart part = page.getActivePart();
				if (part instanceof IViewPart)
					page.hideView((IViewPart) part);
			}
		});
		btnNewButton_1.setText(" 取  消 ");
		
		ViewPropertyChange.getInstance().addPropertyChangeListener("endtag", this);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getProperty().equals(FirePropertyConstants.ENDTAG_ADD)) {
			endTag = new EndTag();
			Object object = event.getNewValue();
			if(object instanceof MajorTag) {
				endTag.setMajorTag((MajorTag)object);
			}
		} else if(event.getProperty().equals(FirePropertyConstants.ENDTAG_EDIT)) {
			endTag = (EndTag)event.getNewValue();
			
			//初始化控件值
			text_name.setText(endTag.getName());
		}
	}

	@Override
	public void dispose() {
		ViewPropertyChange.getInstance().removePropertyChangeListener("endtag");
		super.dispose();
	}
	
	
}
