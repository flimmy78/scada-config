package com.ht.scada.config.view.tree;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.ht.scada.common.tag.entity.EnergyMinorTag;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.ImagePath;
/**
 * 能耗标签提供者
 * @author 陈志强
 *
 */
public class EnergyTreeLabelProvider implements ILabelProvider {
	
	
	@Override
	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getImage(Object object) {
		if(object instanceof EnergyMinorTag) {
			return Activator.getDefault().getImageDescriptor(ImagePath.ENERGY_MINOR_IMAGE).createImage();
		} else if(object instanceof String) {
			return Activator.getDefault().getImageDescriptor(ImagePath.ENERGY_INDEX_IMAGE).createImage();
		}
		return null;
	}

	@Override
	public String getText(Object object) {
		if(object instanceof String) {
			return (String)object;
		} else if(object instanceof EnergyMinorTag) {
			return ((EnergyMinorTag)object).getName();
		} 
		return null;
	}

}
