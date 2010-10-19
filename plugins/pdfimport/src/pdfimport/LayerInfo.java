package pdfimport;

import java.awt.Color;

public class LayerInfo{
	public double width;
	public Color color;
	public Color fillColor;
	public boolean fill;
	public boolean stroke;

	public int nr;	
	public int divider;
	
	@Override
	public int hashCode()
	{
		int code =  Double.toString(width).hashCode() ^ this.divider;
		
		if (this.fill) {
			code ^= this.fillColor.hashCode();
		}
		
		if (this.stroke) {
			code ^= this.color.hashCode();
		}
		
		return code;
	}
	
	public boolean equals(Object o)
	{
		LayerInfo l = (LayerInfo) o;
		boolean eq = 
			this.width == l.width && 
			this.divider == l.divider && 
			this.fill == l.fill &&
			this.stroke == l.stroke;
		
		if (this.fill){
			eq &= this.fillColor.equals(l.fillColor); 
		}
		
		if (this.stroke) {			 
			eq &= this.color.equals(l.color);			
		}
		
		return eq;
	}

	public LayerInfo copy() {
		LayerInfo result = new LayerInfo();
		result.color = this.color;
		result.fillColor = this.fillColor;
		result.width = this.width;
		result.divider = this.divider;
		result.fill = this.fill;
		result.stroke = this.stroke;
		return result;
	}
	
}