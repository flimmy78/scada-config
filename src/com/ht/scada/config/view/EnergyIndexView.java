package com.ht.scada.config.view;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.ht.scada.common.tag.entity.EnergyMinorTag;
import com.ht.scada.common.tag.service.EnergyMinorTagService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.ViewPropertyChange;
import com.ht.scada.config.view.tree.RootTreeModel;

public class EnergyIndexView extends ViewPart implements IPropertyChangeListener {

	public EnergyIndexView() {
	}

	public static final String ID = "com.ht.scada.config.view.EnergyIndexView";
	private Text textIndex;
	private EnergyMinorTag energyMinorTag;

	private EnergyMinorTagService energyMinorTagService = (EnergyMinorTagService) Activator.getDefault()
			.getApplicationContext().getBean("energyMinorTagService");
	private Text textType;

	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.verticalSpacing = 20;
		gl_parent.marginTop = 25;
		gl_parent.marginLeft = 40;
		parent.setLayout(gl_parent);

		Label labIndex = new Label(parent, SWT.NONE);
		labIndex.setText("索引名：");

		textIndex = new Text(parent, SWT.BORDER);
		GridData gd_textIndex = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_textIndex.widthHint = 100;
		textIndex.setLayoutData(gd_textIndex);

		Label labType = new Label(parent, SWT.NONE);
		labType.setText("类型：");

		textType = new Text(parent, SWT.BORDER);
		GridData gd_textType = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_textType.widthHint = 100;
		textType.setLayoutData(gd_textType);
		
		// -------------------------------保存按钮处理----------------
		Button btnSave = new Button(parent, SWT.NONE);
		btnSave.setText(" 保  存 ");

		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (energyMinorTag.getId() == null) {// 新建
					if ("".equals(textIndex.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"索引名字不能为空！");
						return;
					}

					energyMinorTag.setName(textIndex.getText().trim());
					energyMinorTag.setType(textType.getText().trim());
					
					energyMinorTagService.create(energyMinorTag);

					Object parentObject;
					if (energyMinorTag.getParent() == null) {
						parentObject = RootTreeModel.instanse.energyIndex;
					} else {
						parentObject = energyMinorTag.getParent();
					}

					EnergyTreeView.treeViewer.add(parentObject,
							energyMinorTag);
					EnergyTreeView.treeViewer.setExpandedState(
							parentObject, true);

				} else {// 编辑
					if ("".equals(textIndex.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"索引名字不能为空！");
						return;
					}
					energyMinorTag.setName(textIndex.getText().trim());
					energyMinorTag.setType(textType.getText().trim());
				
					energyMinorTagService.update(energyMinorTag);

					EnergyTreeView.treeViewer.update(energyMinorTag, null);

				}

				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				IWorkbenchPart part = page.getActivePart();
				if (part instanceof IViewPart)
					page.hideView((IViewPart) part);
			}
		});
		// -------------------------------取消按钮处理----------------
		Button btnCancel = new Button(parent, SWT.NONE);
		btnCancel.setText(" 取  消 ");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				IWorkbenchPart part = page.getActivePart();
				if (part instanceof IViewPart)
					page.hideView((IViewPart) part);
			}
		});

		ViewPropertyChange.getInstance()
				.addPropertyChangeListener("energy", this);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(FirePropertyConstants.ENERGYMINOR_ADD)) {
			energyMinorTag = new EnergyMinorTag();
			Object object = event.getNewValue();
			if (object instanceof EnergyMinorTag) {
				energyMinorTag.setParent((EnergyMinorTag) object);
			} else {
				energyMinorTag.setParent(null);
			}
			// 初始化控件值
			textIndex.setText("");
			textType.setText("");
			
		} else if (event.getProperty().equals(
				FirePropertyConstants.ENERGYMINOR_EDIT)) {
			energyMinorTag = (EnergyMinorTag) event.getNewValue();

			// 初始化控件值
			textIndex.setText(energyMinorTag.getName());
			
			String typeStr = energyMinorTag.getType();
			if(typeStr == null){
				typeStr = "";
			} else {
			    textType.setText(energyMinorTag.getType());
			}
		}
	}

	@Override
	public void dispose() {
		ViewPropertyChange.getInstance().removePropertyChangeListener("energy");
		super.dispose();
	}

}
