package com.ht.scada.config.view.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.ht.scada.common.tag.entity.EnergyMinorTag;
import com.ht.scada.common.tag.service.EnergyMinorTagService;
import com.ht.scada.config.scadaconfig.Activator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
/**
 *  能耗内容提供者
 * @author 陈志强
 *
 */
public class EnergyTreeContentProvider implements ITreeContentProvider {
	// private final Logger log =
	// LoggerFactory.getLogger(MainTreeContentProvider.class);

	private EnergyMinorTagService energyMinorTagService = (EnergyMinorTagService) Activator.getDefault()
			.getApplicationContext().getBean("energyMinorTagService");

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
			// TODO 
			System.out.println("String ");
		} else if (parentElement instanceof EnergyMinorTag) {
			List<Object> objectList = new ArrayList<Object>();
			List<EnergyMinorTag> energyMinorTagList = energyMinorTagService
					.getEnergyMinorTagsByParentId(((EnergyMinorTag) parentElement)
							.getId());
			if (energyMinorTagList != null) {
				objectList.addAll(energyMinorTagList);
			}
			if (!objectList.isEmpty()) {
				return objectList.toArray();
			}
		}
		return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement.equals(RootTreeModel.instanse.energyIndex)) { // 能耗分类索引
			List<EnergyMinorTag> energyMinorTagList = energyMinorTagService
					.getRootEnergyMinorTag();
			if (energyMinorTagList != null) {
				return energyMinorTagList.toArray();
			}
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
