import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;

import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.BoxShape;

import javax.vecmath.Vector3f;


/**
 * The box basic
 * @author tawashi
 * @version 1.1
 */
class Box extends Shape {
	
	private float w, h, d;
	private int nCl;
	
	private FloatBuffer qaDif;
	
	Box(float w, float h, float d, float r, float g, float b) {
		this(w, h, d, r, g, b, 1.0f);
	}
	
	Box(float w, float h, float d, float r, float g, float b, float a) {
		this.w = w;
		this.h = h;
		this.d = d;
		
		// shape
		if (colShape == null) {
			System.out.println("create shape: "+this.getClass().getName());
			colShape = new BoxShape(new Vector3f(w, h, d) );
			colShape.setUserPointer(this);
		}
		
		// color
		qaDif = BufferUtils.createFloatBuffer(4).put(new float[] { r, g, b, a });
		qaDif.flip();
		
		// mesh
		nCl = GL11.glGenLists(1);
		GL11.glNewList(nCl, GL11.GL_COMPILE);
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glNormal3f(0, 0, 1.0f);
			GL11.glVertex3f(-w, -h, d);
			GL11.glVertex3f( w, -h, d);
			GL11.glVertex3f( w,  h, d);
			GL11.glVertex3f(-w,  h, d);
			
			GL11.glNormal3f(0, 0, -1.0f);
			GL11.glVertex3f( w, -h, -d);
			GL11.glVertex3f(-w, -h, -d);
			GL11.glVertex3f(-w,  h, -d);
			GL11.glVertex3f( w,  h, -d);
			
			GL11.glNormal3f(-1.0f, 0, 0);
			GL11.glVertex3f(-w, -h, -d);
			GL11.glVertex3f(-w, -h,  d);
			GL11.glVertex3f(-w,  h,  d);
			GL11.glVertex3f(-w,  h, -d);
			
			GL11.glNormal3f( 1.0f, 0, 0);
			GL11.glVertex3f( w, -h,  d);
			GL11.glVertex3f( w, -h, -d);
			GL11.glVertex3f( w,  h, -d);
			GL11.glVertex3f( w,  h,  d);
			
			GL11.glNormal3f( 0, 1.0f, 0);
			GL11.glVertex3f(-w,  h,  d);
			GL11.glVertex3f( w,  h,  d);
			GL11.glVertex3f( w,  h, -d);
			GL11.glVertex3f(-w,  h, -d);
			
			GL11.glNormal3f( 0, -1.0f, 0);
			GL11.glVertex3f( w, -h,  d);
			GL11.glVertex3f(-w, -h,  d);
			GL11.glVertex3f(-w, -h, -d);
			GL11.glVertex3f( w, -h, -d);
		GL11.glEnd();
		
		GL11.glEndList();
		
	}
	
	
	@Override
	protected void draw() {
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, qaDif);
		
		GL11.glCallList(nCl);
		
	}
	

}
