import java.io.*;
import java.util.ArrayList;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

import org.lwjgl.opengl.GL15;

import javax.vecmath.Vector3f;
import com.bulletphysics.util.ObjectArrayList;


class VertexData {
	
	private String fname;
	
	//private static HashMap<String, Mesh> listBuf = new HashMap<String, Mesh>();
	private Mesh mesh;
	private int vbId;
	
	private  ObjectArrayList<Vector3f> points = new ObjectArrayList<Vector3f>();
	
	
	
	VertexData(String fname) {
		vbId = GL15.glGenBuffers();
		
		//if (!listBuf.containsKey(fname)) {
			System.out.println("create: "+fname);
			mesh = createMeshFromFile(fname);
		//}
		
		this.fname = fname;
	}
	
	
	
	protected Mesh createMeshFromFile(String fname) {
		
		int nVertexCount = 0;
		ArrayList<Float> listFloat = new ArrayList<Float>();
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(fname)));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] v = strLine.split(",");
				
				// [WARNING] workaround
				if (v.length != 6 && v.length != 8) {
					throw new Exception("invalid float count");
				}
				
				points.add(new Vector3f( Float.parseFloat(v[0].trim()), Float.parseFloat(v[1].trim()), Float.parseFloat(v[2].trim()) ));
					
				for (String s : v) {
					listFloat.add(Float.parseFloat(s.trim()));
				}
				
				nVertexCount++;
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} finally {
			try {
				br.close();
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		ByteBuffer bb = ByteBuffer.allocateDirect(Float.SIZE / Byte.SIZE * listFloat.size());
		bb.order(ByteOrder.LITTLE_ENDIAN);
		for (Float f : listFloat) {
			bb.putFloat(f);
		}
		bb.flip();
		FloatBuffer fb = bb.asFloatBuffer();
		
		//System.out.printf("VTX: %d\t%d\n", nCount, bb.limit());
		return new Mesh(fb, nVertexCount, fb.limit() / nVertexCount);
	}
	
	
	
	public int getVertexCount() {
		// *** SUSPICIOUS *** using limit() as size getting.
		//int nCount = listBuf.get(fname).getVertexCount();
		int nCount = mesh.getVertexCount();
		return nCount;
	}
	
	
	
	public FloatBuffer getBuffer() {
		//return listBuf.get(fname).getBuffer();
		return mesh.getBuffer();
	}
	
	
	public int getBufID() {
		return vbId;
	}
	
	
	public ObjectArrayList<Vector3f> getPoints() {
		return points;
	}
	
	public void destroy() {
		GL15.glDeleteBuffers(vbId);
	}
	
}



class Mesh {
	private int nVertexCount;
	private int nFloatCountPerLine;
	private FloatBuffer fb;
	
	
	Mesh(FloatBuffer fb, int nVertexCount, int nFloatCountPerLine) {
		System.out.printf("nVertexCount: %d\tnFloatCountPerLine: %d\tfloat: %d\n", nVertexCount, nFloatCountPerLine, fb.limit());
		this.fb = fb;
		this.nVertexCount = nVertexCount;
		this.nFloatCountPerLine = nFloatCountPerLine;
	}
	
	public int getVertexCount() {
		return nVertexCount;
	}
	
	public int getFloatCountPerLine() {
		return nFloatCountPerLine;
	}
	
	public FloatBuffer getBuffer() {
		return fb;
	}
	
}
