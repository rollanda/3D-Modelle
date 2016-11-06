/** 
 * Vertex.java
 * (c) Copyright 04-2016 Robert K�lpmann
 *  Vertex-Klasse zum Speichern von Vertecies
 *  Dependencies:
 *  	Vector4 - Klasse
 * 
 *  - pos, tex, norm
 *  
 *  @author Robert K�lpmann
 *  @version 1.0
 */

package obj;

public class Vertex
{	
	//Aus Gr�nden der Bequemlichkeit: public, statt Getter+Setter
	public Vector4<Float> pos;
	public Vector4<Float> tex;
	public Vector4<Float> norm;
	
	public Vertex(){}
	
	public Vertex(Vector4<Float> pos, Vector4<Float> tex)
	{
		this.pos = pos;
		this.tex = tex;
		this.norm = new Vector4<>(0.1f); // Problem: welchen Wert f�r z W�hlen? (am besten im Moment 0.1f)
	}

	public Vertex(Vector4<Float> pos, Vector4<Float> tex, Vector4<Float> norm)
	{
		this.pos = pos;
		this.tex = tex;
		this.norm = norm;
	}

}