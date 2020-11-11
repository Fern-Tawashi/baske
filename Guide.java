import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.util.glu.Sphere;


class Guide {

	private FloatBuffer qaDif = BufferUtils.createFloatBuffer(4).put(new float[] { 0.75f, 0, 0, 1.0f });
	private float curDeg = 0;
	
	//private GL11 gl
	
	Guide(/*GL11 gl*/) {
		//this.gl = gl;
		qaDif.flip();
	}
	
	void draw(int delta) {
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, qaDif);
		
		float x = (float)Math.cos(Math.toRadians(curDeg - 90)) * 2.0f;
		float y = (float)Math.sin(Math.toRadians(curDeg - 90)) * 2.0f;
		GL11.glBegin(GL11.GL_LINES);
			GL11.glNormal3f( 0, 0, 1 );
			GL11.glVertex3f(-12.0f, -5, 0);
			GL11.glVertex3f(-12.0f + x, -5 + y, 0);
		GL11.glEnd();
		
		curDeg = (curDeg + delta / 8) % 180;
	}
	
	
	public float getDegree() {
		return curDeg;
	}
}
