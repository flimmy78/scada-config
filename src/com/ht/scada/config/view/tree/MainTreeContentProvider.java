package com.ht.scada.config.view.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.common.tag.entity.MajorTag;
import com.ht.scada.common.tag.service.EndTagService;
import com.ht.scada.common.tag.service.MajorTagService;
import com.ht.scada.config.scadaconfig.Activator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


public class MainTreeContentProvider implements ITreeContentProvider {
//	private final Logger log = LoggerFactory.getLogger(MainTreeContentProvider.class);
	
	private MajorTagService majorTagService = (MajorTagService) Activator.getDefault()
			.getApplicationContext().getBean("majorTagService");
	private EndTagService endTagService = (EndTagService) Activator.getDefault()
			.getApplicationContext().getBean("endTagService");
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getChildren(Object parentElement) {
		
		if (parentElement instanceof String) {
			
			String label = (String) parentElement;
//			log.debug(label);
//			return label;
			if (label.equals(RootTreeModel.instanse.labelIndex)) {	//主索引
				List<MajorTag> majorTagList = majorTagService.getRootMajorTag();
				if(majorTagList != null) {
					return majorTagList.toArray();
				}
			}

//			} else if (label.equals("标签类型配置")) {
//				TagTypeModelDaoImpl tagTypeModelDao = new TagTypeModelDaoImpl();
//				List<TagTypeModel> tagTypeModelList = tagTypeModelDao
//						.getAllTagTypeModel();
//				return tagTypeModelList.toArray();
//
//			} else if (label.equals("规约配置")) {
//
//			} else if (label.equals("通道配置")) {
//				return new String[] { "采集通道", "转发通道", "其他通道" };
//
//			} else if (label.equals("窗口配置")) {
//
//			} else if (label.equals("模板配置")) {
//				return new String[] { "标签索引模板", "标签类型模板" };
//			} else if (label.equals("采集通道")) {
//				ChannelModelDaoImpl channelModelDao = new ChannelModelDaoImpl();
//				List<ChannelModel> channelModeList = channelModelDao
//						.getAllChannelModel();
//				return channelModeList.toArray();
//			}
		} else if(parentElement instanceof MajorTag) {
			List<Object> objectList = new ArrayList<Object>();
			List<MajorTag> majorTagList = majorTagService.getMajorTagsByParentId(((MajorTag)parentElement).getId());
			
			if(majorTagList != null) {
				objectList.addAll(majorTagList);
			}
			List<EndTag> endTagList = endTagService.getEndTagByParentId(((MajorTag)parentElement).getId());
			
			//-获得该索引下的一级endTag对象------------
			List<EndTag> endTagListFather = new ArrayList<EndTag>();
			for (int i=0; i<endTagList.size(); i++ ) {
				EndTag temp = endTagList.get(i);
				if (temp.getParent() == null) {
					endTagListFather.add(temp);
				}
			}
			
			if(endTagListFather != null) {
				objectList.addAll(endTagListFather);
			}
			if(!objectList.isEmpty()) {
				return objectList.toArray();
			}
			//--------------
			
		}
		// =若选中的节点是endTag对象------------------------------------------------------------
		else if (parentElement instanceof EndTag ) {
			List<Object> objectList = new ArrayList<Object>();
			
			EndTag temp = ((EndTag)parentElement);						// 获得当前选中的节点
			List<EndTag> endTagList = endTagService.findByParent(temp);	// 该endTag节点的所有子节点
			
			if(endTagList != null) {
				objectList.addAll(endTagList);
			}
			if(!objectList.isEmpty()) {
				return objectList.toArray();
			}
			
		} 
		// ----------------------------------------------------------------------------------------------
		
		
		return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Enum) {
//			return ((RootTreeModel) inputElement).getRoottree();
			return new String[]{RootTreeModel.instanse.labelIndex, RootTreeModel.instanse.otherConfig};
		}
		return null;
	}

	@Override
	public Object getParent(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object arg0) {
		Object[] children = this.getChildren(arg0);
		if (children == null) {
			return false;
		} else if (children.length > 0) {
			return true;
		} else {
			return false;
		}
	}

}
