package org.utils.shape;

import org.utils.transform.Matrix;

public interface IGeometry {
	public void addChild(IGeometry child);
	public IGeometry getChild(int i);
	public Matrix getMatrix();
	public int getNumChild();
	public void removeChild(IGeometry child);	
}
