package com.ht.scada.config.view.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.ht.scada.common.tag.entity.AreaMinorTag;
import com.ht.scada.common.tag.entity.MajorTag;
import com.ht.scada.common.tag.service.AreaMinorTagService;
import com.ht.scada.common.tag.service.TagService;
import com.ht.scada.config.scadaconfig.Activator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * 分区树内容提供者
 * 
 * @author 陈志强
 * 
 */
public class AreaTreeContentProvider implements ITreeContentProvider {
	// private final Logger log =
	// LoggerFactory.getLogger(MainTreeContentProvider.class);

	private AreaMinorTagService areaMinorTagService = (AreaMinorTagService) Activator
			.getDefault().getApplicationContext()
			.getBean("areaMinorTagService");

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
			String str = (String) parentElement;
			if (str.equals(RootTreeModel.instanse.normalIndex) ){ // 常规分类索引
				List<AreaMinorTag> areaMinorTagList = areaMinorTagService
						.getRootAreaMinorTag();
				if (areaMinorTagList != null) {
					return areaMinorTagList.toArray();
				}
			}
		} else if (parentElement instanceof AreaMinorTag) {
			List<Object> objectList = new ArrayList<Object>();
			List<AreaMinorTag> areaMinorTagList = areaMinorTagService
					.getAreaMinorTagsByParentId(((AreaMinorTag) parentElement)
							.getId());
			if (areaMinorTagList != null) {
				objectList.addAll(areaMinorTagList);
			}
			if (!objectList.isEmpty()) {
				return objectList.toArray();
			}
		}
		return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof String) {
			return new String[]{RootTreeModel.instanse.normalIndex};
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
