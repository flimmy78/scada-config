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

import com.ht.scada.common.tag.entity.MajorTag;
import com.ht.scada.common.tag.service.TagService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.ViewPropertyChange;
import com.ht.scada.config.view.tree.RootTreeModel;

public class MainIndexView extends ViewPart implements IPropertyChangeListener {

	public MainIndexView() {
	}

	public static final String ID = "com.ht.scada.config.view.MainIndexView";
	private Text text;
	private MajorTag majorTag;

	private TagService tagService = (TagService) Activator.getDefault()
			.getApplicationContext().getBean("tagService");

	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.verticalSpacing = 20;
		gl_parent.marginTop = 25;
		gl_parent.marginLeft = 40;
		parent.setLayout(gl_parent);

		Label label = new Label(parent, SWT.NONE);
		label.setText("索引名：");

		text = new Text(parent, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_text.widthHint = 100;
		text.setLayoutData(gd_text);

		Button btnNewButton = new Button(parent, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (majorTag.getId() == null) {// 新建
					if ("".equals(text.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"索引名字不能为空！");
						return;
					}

					majorTag.setName(text.getText().trim());
					tagService.createMajorTag(majorTag);
					
					Object parentObject;
					if(majorTag.getParent() == null) {
						parentObject = RootTreeModel.instanse.labelIndex;
					} else {
						parentObject = majorTag.getParent();
					}
					ScadaObjectTreeView.treeViewer.add(parentObject, majorTag);
					ScadaObjectTreeView.treeViewer.setExpandedState(parentObject, true);

				} else {// 编辑
					if ("".equals(text.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"索引名字不能为空！");
						return;
					}
					majorTag.setName(text.getText().trim());
					tagService.updateMajorTag(majorTag);
					
					ScadaObjectTreeView.treeViewer.update(majorTag, null);

				}

//				ScadaObjectTreeView.treeViewer.refresh();

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
		btnNewButton_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		btnNewButton_1.setText(" 取  消 ");

		ViewPropertyChange.getInstance()
				.addPropertyChangeListener("main", this);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(FirePropertyConstants.MAJOR_ADD)) {
			majorTag = new MajorTag();
			Object object = event.getNewValue();
			if (object instanceof MajorTag) {
				majorTag.setParent((MajorTag) object);
			} else {
				majorTag.setParent(null);
			}
		} else if (event.getProperty().equals(FirePropertyConstants.MAJOR_EDIT)) {
			majorTag = (MajorTag) event.getNewValue();
			
			//初始化控件值
			text.setText(majorTag.getName());
			
		}
	}

	@Override
	public void dispose() {
		ViewPropertyChange.getInstance().removePropertyChangeListener("main");
		super.dispose();
	}

}
