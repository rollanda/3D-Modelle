/** 
 * OBJ.java
 * (c) Copyright 04-2016 Robert K�lpmann
 *  Parserklasse zum einlesen und speichern von OBJ-Files
 *  Dependencies:
 *  	Vertex - Klasse (generic.classes.gfx.Vertex 
 *  	Vector4 - Klasse (generic.math.Vector4)
 *  	
 *  	java.util
 *  	java.io
 *  
 *  @author Robert K�lpmann
 *  @version 1.0
 *  
 *  ANMERKUNG: �berarbeitete Version f�r Studienprojekt -> Muss getestet werden!
 */

package obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class OBJ
{
	private int faceIndicesNr;		//Anzahl der Indices eines Faces (2-4)
	private int verticesNr;			//Anzahl der Vertices
	private int facesNr; 			//Anzahl der Faces
	private Vertex vertexArray[];	//Vertices
	private int faceArray[][];		//Faces
	
	public void setFaceIndicesNr(int faceIndicesNr)		{this.faceIndicesNr = faceIndicesNr;}
	public void setVerticesNr(int verticesNr)			{this.verticesNr = verticesNr;}
	public void setFacesNr(int facesNr)					{this.facesNr = facesNr;}
	public void setVertexArray(Vertex vertexArray[])	{this.vertexArray = vertexArray;}
	public void setFaceArray(int faceArray[][])			{this.faceArray = faceArray;}	
	
	public int getFaceIndicesNr()			{return faceIndicesNr;}
	public int getVerticesNr()				{return verticesNr;}
	public int getFacesNr()					{return facesNr;}
	public Vertex getVertex(int i)			{return vertexArray[i];}
	public int[] getFaceArray()				{return convertTo1D();}	
	
	public OBJ(){};
	public OBJ(int verticesNr, int facesNr, Vertex[] vertexArray, int[][] faceArray)
	{
		setFaceIndicesNr(faceArray[0].length);
		setVerticesNr(verticesNr);
		setFacesNr(facesNr);
		setVertexArray(vertexArray);
		setFaceArray(faceArray);
	}
	
	/* Wandelt 2-dimensionales IndexArray in ein 1-dimensionales*/
	public int[] convertTo1D()
	{
		int aFace[] = new int[facesNr * faceIndicesNr];
		for (int i=0; i<facesNr; i++)
		{
			aFace[i*faceIndicesNr] = faceArray[i][0]; 
			aFace[i*faceIndicesNr+1] = faceArray[i][1];
			if (faceIndicesNr >= 3) aFace[i*faceIndicesNr+2] = faceArray[i][2];
			if (faceIndicesNr >= 4) aFace[i*faceIndicesNr + 3] = faceArray[i][3];
		}
		return aFace;
	}
	
	
	/* Funktion zum Laden eines Obj Files
	 * @param Dateiname
	 * @return Ein OBJ-File
	 */
	public static OBJ load(String filename)
	{
		String splitted[] = null;
		String splitted3[] = null;
		ArrayList<Vector4<Float>> vertexList = new ArrayList<>();	//Format x/y/z
		ArrayList<Vector4<Float>> textureList = new ArrayList<>();	//Format x/y
		ArrayList<Vector4<Float>> normalList = new ArrayList<>();	//Format x/y/z
		ArrayList<Vector4<Float>> faceList1 = new ArrayList<>();	//Format v v v
		ArrayList<Vector4<Float>> faceList2 = new ArrayList<>();	//Format v/vt v/vt v/vt
		ArrayList<Vector4<Float>> faceList3 = new ArrayList<>();	//Format v/vt/vn v/vt/vn v/vt/vn
		ArrayList<Vector4<Float>> faceList4 = new ArrayList<>();	//Nicht benutzt! Nur der Vollst�ndigkeit halber (Vektor4<>)
				
		File file = new File(filename);
		
		int faceIndicesNrLocal = 0;
		
		try
		{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while((s = br.readLine()) != null)
			{
				splitted = s.split(" ");
				//Liest Koordinaten eines Vertex ein (3D)
				if (splitted[0].equals("v")) vertexList.add(new Vector4<>(	Float.valueOf(splitted[1]), 
																			Float.valueOf(splitted[2]), 
																			Float.valueOf(splitted[3]),
																			0.0f));				
				//Liest Koordinaten einer Texture ein (2D)
				else if (splitted[0].equals("vt")) 
				{
				textureList.add(new Vector4<>(  Float.valueOf(splitted[1]),
												Float.valueOf(splitted[2]),
												0.f, 0.f));
				}
				//Liest Koordinaten einer Normalen ein (3D)
				else if (splitted[0].equals("vn")) 
				{
				normalList.add(new Vector4<>(  	Float.valueOf(splitted[1]),
												Float.valueOf(splitted[2]),
												Float.valueOf(splitted[3]), 
												0.0f));
				}
				
				//Liest Indices einer Face/eine Fl�che ein
				else if (splitted[0].equals("f")) 
				{
					faceIndicesNrLocal = splitted.length - 1; //Anzahl der Indices in einer Zeile (2 - 4) FaceDarstellung
					splitted3 = new String[faceIndicesNrLocal];
					for(int i = 0; i<faceIndicesNrLocal; i++)
					{
						String splitted2[] = splitted[i+1].split("/");
						splitted3[i] = splitted2[0];
					}
					if (faceIndicesNrLocal == 1)
					{
						faceList1.add(new Vector4<>(	Float.valueOf(splitted3[0]),
														0.0f, 0.0f, 0.0f));
					}
					else if (faceIndicesNrLocal == 2)
					{
						faceList2.add(new Vector4<>(	Float.valueOf(splitted3[0]),
														Float.valueOf(splitted3[1]),
														0.0f, 0.0f));
					}
					else if (faceIndicesNrLocal == 3)
					{
						faceList3.add(new Vector4<>(	Float.valueOf(splitted3[0]),
														Float.valueOf(splitted3[1]),
														Float.valueOf(splitted3[2]),
														0.0f));
					}
					else if (faceIndicesNrLocal == 4)
					{
						faceList4.add(new Vector4<Float>(	Float.valueOf(splitted3[0]),
															Float.valueOf(splitted3[1]),
															Float.valueOf(splitted3[2]),
															Float.valueOf(splitted3[3])));						
					}
				}
			}
			
			
			// Vereinheitlichung des Formats zu v/vt/vn
			// Speichern der Vertices in einer Liste
			Vertex vertexArray[] = new Vertex[vertexList.size()];

			for (int j=0; j<vertexList.size(); j++)
			{
				if(textureList.isEmpty() && normalList.isEmpty())				
				{
					//Welchen NormalenStandard?
					vertexArray[j] = new Vertex(vertexList.get(j), new Vector4<>(0.f, 1.f, 0.f, 0.f), new Vector4<>(0.f, 1.f,0.f,0.f));
				}
				else if  (textureList.isEmpty())
				{
					vertexArray[j] = new Vertex(vertexList.get(j), new Vector4<>(0.f, 1.f, 0.f, 0.f));
				}
				else vertexArray[j] = new Vertex(vertexList.get(j), textureList.get(j), normalList.get(j));
			}
			
			//Speichern der Faces in einer 2-Dimensionalen Liste
			int indexArray[][];
			if (faceIndicesNrLocal == 1) 
			{
				indexArray = new int[faceList1.size()][1];
			
				for (int j=0; j<faceList1.size(); j++)
				{
					indexArray[j][0] = faceList1.get(j).getX().intValue() - 1;
				}
			}
			if (faceIndicesNrLocal == 2) 
			{
				indexArray = new int[faceList2.size()][2];
			
				for (int j=0; j<faceList2.size(); j++)
				{
					indexArray[j][0] = faceList2.get(j).getX().intValue() - 1;
					indexArray[j][1] = faceList2.get(j).getY().intValue() - 1;
				}
			}
			else if (faceIndicesNrLocal == 3) 
			{
				indexArray = new int[faceList3.size()][3];
			
				for (int j=0; j<faceList3.size(); j++)
				{
					indexArray[j][0] = faceList3.get(j).getX().intValue() - 1;
					indexArray[j][1] = faceList3.get(j).getY().intValue() - 1;
					indexArray[j][2] = faceList3.get(j).getZ().intValue() - 1;
				}
			}
			else if (faceIndicesNrLocal == 4) 
			{
				indexArray = new int[faceList4.size()][4];
				for (int j=0; j<faceList4.size(); j++)
				{
					indexArray[j][0] = faceList4.get(j).getX().intValue() - 1;
					indexArray[j][1] = faceList4.get(j).getY().intValue() - 1;
					indexArray[j][2] = faceList4.get(j).getZ().intValue() - 1;
					indexArray[j][3] = faceList4.get(j).getW().intValue() - 1;
				}
			}
			else indexArray = null;
			
			br.close();
			fr.close();
			return new OBJ(vertexArray.length, indexArray.length, vertexArray, indexArray);
		}
		catch (IOException e)
		{
			
		}
		return null;
	}
	
	/*
		Speichert die Vertecies in einer Datei
	*/
	public void saveTo(String filename)
	{
		File file = new File(filename);
		
		try
		{
			FileWriter fw = new FileWriter(file);
			fw.write("# commentar\r\n");			
			for (int j = 0; j<this.verticesNr; j++)
			{
				fw.write("v");
				fw.write(" " + this.vertexArray[j].pos.x);
				fw.write(" " + this.vertexArray[j].pos.y);
				fw.write(" " + this.vertexArray[j].pos.z);
				fw.write("\r\n");
			}
			
			for (int j = 0; j<this.verticesNr; j++)
			{
				fw.write("vt");
				fw.write(" " + this.vertexArray[j].tex.x);
				fw.write(" " + this.vertexArray[j].tex.y);				
				fw.write("\r\n");
			}
			for (int j = 0; j<this.verticesNr; j++)
			{
				fw.write("vn");
				fw.write(" " + this.vertexArray[j].norm.x); 
				fw.write(" " + this.vertexArray[j].norm.y); 
				fw.write(" " + this.vertexArray[j].norm.z); 
				fw.write("\r\n");
			}
			fw.close();
		}
		catch(IOException e)
		{
			
		}
	}	
	
	/*
	 * F�gt einer OBJ Datei Faces hinzu
	 */
	public void writeFacesToObj(String fileName, int[] indices, int size)
	{
		File file = new File(fileName);
		int actIndex = 1;
		
		try
		{
			FileWriter fw = new FileWriter(file, true);
			for (int i = 0; i<(indices.length/size);i++)
			{
				fw.append("f");
				for(int j = 0; j<size; j++)
				{
					fw.append(" " + (int)(indices[i*size + j] + actIndex) + "//" + (int)(indices[i*size+j] + actIndex)); 
				}
				fw.append("\r\n");
			}
			actIndex = actIndex + indices.length;
			fw.close();
		}
		catch(IOException e)
		{
		}
	}
	
}