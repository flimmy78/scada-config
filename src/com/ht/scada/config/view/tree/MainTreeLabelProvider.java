package com.ht.scada.config.view.tree;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.common.tag.entity.MajorTag;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.ImagePath;

public class MainTreeLabelProvider implements ILabelProvider {
	
	
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
		if(object instanceof MajorTag) {
			return Activator.getDefault().getImageDescriptor(ImagePath.MAJOR_TAG_IMAGE).createImage();
		} else if(object instanceof EndTag) {
			return Activator.getDefault().getImageDescriptor(ImagePath.END_TAG_IMAGE).createImage();
		} else if (object instanceof String){
			return Activator.getDefault().getImageDescriptor(ImagePath.MAJOR_TAG_INDEX_IMAGE).createImage();
		}
		
		return null;
	}

	@Override
	public String getText(Object object) {
		if(object instanceof String) {
			return (String)object;
		} else if(object instanceof MajorTag) {
			return ((MajorTag)object).getName();
		} else if(object instanceof EndTag) {
			return ((EndTag)object).getName();
		}
		return null;
	}

}
