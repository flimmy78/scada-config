package com.ht.scada.config.window;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.ht.scada.common.tag.entity.AcquisitionChannel;
import com.ht.scada.common.tag.entity.AcquisitionDevice;
import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.common.tag.entity.MajorTag;
import com.ht.scada.common.tag.entity.TagCfgTpl;
import com.ht.scada.common.tag.entity.VarIOInfo;
import com.ht.scada.common.tag.service.AcquisitionChannelService;
import com.ht.scada.common.tag.service.AcquisitionDeviceService;
import com.ht.scada.common.tag.service.EndTagService;
import com.ht.scada.common.tag.service.MajorTagService;
import com.ht.scada.common.tag.service.TagCfgTplService;
import com.ht.scada.common.tag.service.VarIOInfoService;
import com.ht.scada.common.tag.type.entity.EndTagType;
import com.ht.scada.common.tag.type.service.TypeService;
import com.ht.scada.config.scadaconfig.Activator;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

/**
 * 监控对象关联设备与模板
 * @author 赵磊
 *
 */
public class EndTagDeviceConfigWindow extends ApplicationWindow {
	private class ViewerLabelProvider extends LabelProvider implements ITableLabelProvider{
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
			case 1:
				return getEndTagType(endTag.getType()).getValue();
			case 2:
				return endTag.getTplName();
			case 3:
				if(endTag.getChannelIdx()==null) {
					return null;
				}
				AcquisitionChannel channel = acquisitionChannelService.getByIdx(endTag.getChannelIdx());
				return channel==null?String.valueOf(endTag.getChannelIdx()):(channel.getName()+":"+endTag.getChannelIdx());
			case 4:
				if(endTag.getChannelIdx()==null || endTag.getDeviceAddr()==null) {
					return null;
				} else {
					channel = acquisitionChannelService.getByIdx(endTag.getChannelIdx());
					if(channel == null) {
						return null;
					} else {
						AcquisitionDevice device = acquisitionDeviceService.getDeviceByChannelIdAndDeviceId(endTag.getChannelIdx(), endTag.getDeviceAddr());
						return device==null?String.valueOf(endTag.getDeviceAddr()):device.getName()+":"+endTag.getDeviceAddr();
					}
					
				}
			default:
				break;
			}
			
			return null;
		}
	}
	
	private EndTagService endTagService = (EndTagService) Activator.getDefault()
			.getApplicationContext().getBean("endTagService");
	private TypeService typeService = (TypeService) Activator.getDefault()
			.getApplicationContext().getBean("typeService");
	private TagCfgTplService tagCfgTplService = (TagCfgTplService) Activator
			.getDefault().getApplicationContext().getBean("tagCfgTplService");
	private VarIOInfoService varIOInfoService = (VarIOInfoService) Activator
			.getDefault().getApplicationContext().getBean("varIOInfoService");
	private AcquisitionChannelService acquisitionChannelService = (AcquisitionChannelService) Activator
			.getDefault().getApplicationContext()
			.getBean("acquisitionChannelService");
	private AcquisitionDeviceService acquisitionDeviceService = (AcquisitionDeviceService) Activator.getDefault()
			.getApplicationContext().getBean("acquisitionDeviceService");
	
	private List<EndTag> endTagList = new ArrayList<EndTag>();
	private MajorTag majorTag;
	private List<EndTagType> endTagTypeList;
	private String[] tplNameArray = new String[]{""};
	

	/**
	 * Create the application window.
	 */
	public EndTagDeviceConfigWindow(MajorTag majorTag) {
		super(null);
		this.majorTag = majorTag;
		endTagList = endTagService.getEndTagByParentId(majorTag.getId());
		
		endTagTypeList = typeService.getAllEndTagType();
		
		List<String> tplNameList = tagCfgTplService.findAllTplName();
		if(tplNameList != null && !tplNameList.isEmpty()) {
			tplNameArray = new String[tplNameList.size() + 1];
			tplNameArray[0] = "";
			int i=1;
			for(String name : tplNameList) {
				tplNameArray[i] = name;
				i++;
			}
		}
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
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label = new Label(composite, SWT.NONE);
		label.setBounds(10, 3, 84, 17);
		label.setText("监控对象类型：");
		
		Combo combo = new Combo(composite, SWT.NONE);
		combo.setBounds(100, 0, 88, 25);
		
		final GridTableViewer gridTableViewer = new GridTableViewer(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		
		
		final Grid grid = gridTableViewer.getGrid();
		grid.setRowHeaderVisible(true);
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
		gridViewerColumn_4
		.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new ComboBoxCellEditor(grid,
						tplNameArray);
				return ce;
			}

			protected Object getValue(Object element) {
				EndTag endTag = (EndTag) element;
				if (endTag.getTplName()!=null && !"".equals(endTag.getTplName())) {
					for(int i=1;i<=tplNameArray.length;i++) {
						if(endTag.getTplName().equals(tplNameArray[i])) {
							return i;
						}
					}
				}
				return 0;
			}

			protected void setValue(Object element, Object value) {
				EndTag endTag = (EndTag) element;
				int index = (int)value;
				if (index<=0) {
					endTag.setTplName(null);
				} else {
					endTag.setTplName(tplNameArray[index]);
				}

				gridTableViewer.refresh();
			}
		});
		
		GridViewerColumn gridViewerColumn_2 = new GridViewerColumn(gridTableViewer, SWT.NONE);
		GridColumn gridColumn_2 = gridViewerColumn_2.getColumn();
		gridColumn_2.setWidth(150);
		gridColumn_2.setText("采集通道：序号");
		gridViewerColumn_2.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				EndTag endTag = (EndTag) element;
				return endTag.getChannelIdx()==null?"":String.valueOf(endTag.getChannelIdx());
			}

			protected void setValue(Object element, Object value) {
				EndTag endTag = (EndTag) element;
				String v = (String)value;
				endTag.setChannelIdx(v.equals("")?null:Integer.valueOf(v));
				
				gridTableViewer.refresh();
			}
		});
		
		GridViewerColumn gridViewerColumn_3 = new GridViewerColumn(gridTableViewer, SWT.NONE);
		GridColumn gridColumn_3 = gridViewerColumn_3.getColumn();
		gridColumn_3.setWidth(150);
		gridColumn_3.setText("监控设备：地址");
		gridViewerColumn_3.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(grid);
				return ce;
			}

			protected Object getValue(Object element) {
				EndTag endTag = (EndTag) element;
				return endTag.getDeviceAddr()==null?"":String.valueOf(endTag.getDeviceAddr());
			}

			protected void setValue(Object element, Object value) {
				EndTag endTag = (EndTag) element;
				String v = (String)value;
				endTag.setDeviceAddr(v.equals("")?null:Integer.valueOf(v));
				
				AcquisitionDevice device = acquisitionDeviceService.getDeviceByChannelIdAndDeviceId(endTag.getChannelIdx(), endTag.getDeviceAddr());
				endTag.setDevice(device);
				
				gridTableViewer.refresh();
			}
		});
		
		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite_1.setLayout(new GridLayout(2, false));
		
		Button btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				endTagService.saveAll(endTagList);
				//初始化变量IO
				for(EndTag endTag : endTagList) {
					if(endTag.getTplName() != null && !"".equals(endTag.getTplName())) {
						List<TagCfgTpl> tagCfgTplList = tagCfgTplService.getAllTagTpl();
						if(tagCfgTplList != null && !tagCfgTplList.isEmpty()) {
							for(TagCfgTpl t : tagCfgTplList) {
								if(t.getVarType().equalsIgnoreCase("YC")) {
									VarIOInfo varIOInfo = varIOInfoService.getByEndTagIdAndVarName(endTag.getId(), t.getVarName());
									if(varIOInfo == null) {
										varIOInfo = new VarIOInfo();
										varIOInfo.setBaseValue(0);
										varIOInfo.setCoefValue(1);
										varIOInfo.setEndTag(endTag);
										varIOInfo.setVarName(t.getVarName());
										varIOInfoService.create(varIOInfo);
									}
								} else if(t.getVarType().equalsIgnoreCase("YM")) {
									VarIOInfo varIOInfo = varIOInfoService.getByEndTagIdAndVarName(endTag.getId(), t.getVarName());
									if(varIOInfo == null) {
										varIOInfo = new VarIOInfo();
										varIOInfo.setBaseValue(0);
										varIOInfo.setCoefValue(1);
										varIOInfo.setEndTag(endTag);
										varIOInfo.setVarName(t.getVarName());
										varIOInfoService.create(varIOInfo);
									}
								} else if(t.getVarType().equalsIgnoreCase("YK")) {
									VarIOInfo varIOInfo = varIOInfoService.getByEndTagIdAndVarName(endTag.getId(), t.getVarName());
									if(varIOInfo == null) {
										varIOInfo = new VarIOInfo();
										varIOInfo.setBaseValue(0);
										varIOInfo.setCoefValue(1);
										varIOInfo.setEndTag(endTag);
										varIOInfo.setVarName(t.getVarName());
										varIOInfoService.create(varIOInfo);
									}
								} else if(t.getVarType().equalsIgnoreCase("YT")) {
									VarIOInfo varIOInfo = varIOInfoService.getByEndTagIdAndVarName(endTag.getId(), t.getVarName());
									if(varIOInfo == null) {
										varIOInfo = new VarIOInfo();
										varIOInfo.setBaseValue(0);
										varIOInfo.setCoefValue(1);
										varIOInfo.setEndTag(endTag);
										varIOInfo.setVarName(t.getVarName());
										varIOInfoService.create(varIOInfo);
									}
								}
							}
						}
					}
				}
				
				
				MessageDialog.openInformation(getShell(), "提示", "保存成功！");
			}
		});
		btnNewButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnNewButton.setBounds(0, 0, 80, 27);
		btnNewButton.setText("  保   存  ");
		
		Button button = new Button(composite_1, SWT.NONE);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				close();
			}
		});
		button.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		button.setText("  取   消  ");
		button.setBounds(0, 0, 64, 27);
		
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
