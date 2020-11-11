import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.BufferUtils;

import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;

import javax.vecmath.Vector3f;


/**
 * The tama basic
 * @author tawashi
 * @version 1.0
 */
class Sphere extends Shape {
	
	private VertexData vdObj;
	private FloatBuffer qaDif;
	
	Sphere(float r, float g, float b) {
		qaDif = BufferUtils.createFloatBuffer(4).put(new float[] { r, g, b, 1.0f });
		qaDif.flip();
		
		vdObj = new VertexData("mesh/sphere.txt");
		
		System.out.println("create shape: "+this.getClass().getName());
		colShape = new SphereShape(0.5f);
		//colShape = new ConvexHullShape(vdObj.getPoints());
		colShape.setUserPointer(this);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vdObj.getBufID());
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vdObj.getBuffer(), GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	
	@Override
	protected void draw() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vdObj.getBufID());
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, qaDif);
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 24, 0);
		GL11.glNormalPointer(GL11.GL_FLOAT, 24, 12);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vdObj.getVertexCount());
		
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	
}
