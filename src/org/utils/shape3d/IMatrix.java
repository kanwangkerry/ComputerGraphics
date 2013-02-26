package org.utils.shape3d;

public interface IMatrix
{
   public void identity();
   public void set(int col, int row, double value);
   public double get(int col, int row);
   public void translate(double x, double y, double z);
   public void rotateX(double radians);
   public void rotateY(double radians);
   public void rotateZ(double radians);
   public void scale(double x, double y, double z);
   public void leftMultiply(IMatrix other);
   public void rightMultiply(IMatrix other);
   public void transform(double[] src, double[] dst);
}

