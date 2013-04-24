package com.ht.scada.config.view;

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

import com.ht.scada.common.tag.service.TagService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;

public class AreaIndexView extends ViewPart implements IPropertyChangeListener {

	public AreaIndexView() {
		
	}

	public static final String ID = "com.ht.scada.config.view.AreaIndexView";
	private Text text;

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

		Button btnSave = new Button(parent, SWT.NONE);
		btnSave.setText(" 保  存 ");
		
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IWorkbenchPart part = page.getActivePart();
				if (part instanceof IViewPart)
					page.hideView((IViewPart) part);
				
				System.out.println("btnSave监听添加成功！");
			}
		});

		Button btnCancel = new Button(parent, SWT.NONE);
		btnCancel.setText(" 取  消 ");

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(FirePropertyConstants.MAJOR_ADD)) {
			
		} else if (event.getProperty().equals(FirePropertyConstants.MAJOR_EDIT)) {
			
		}
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
