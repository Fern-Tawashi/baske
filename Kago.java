import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;
import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;

import javax.vecmath.Vector3f;



class Kago {
	
	World world;
	Box boxRing;
	Box boxHimo;
	Box boxLink;
	
	
	Kago(World world) {
		this.world = world;
		boxRing = new Box(0.1f, 0.1f, 0.25f,  0.6f, 0.2f, 0.2f);
		boxHimo = new Box(0.1f, 1.0f, 0.1f,  0.6f, 0.6f, 0.6f);
		boxLink = new Box(0.2f, 0.1f, 0.1f,  0.6f, 0.6f, 0.6f);
	}
	
	
	
	public void init() {
	}
	
	
	
	public void birth() {
		
		final int DIV = 8;
		final float r = 1.0f;
		final float xoff = 10.25f;
		
		
		// Ring
		RigidBody[] bodiesRing = new RigidBody[DIV];
		
		for (int i = 0; i < DIV; i++) {
			Transform transform = new Transform();
			transform.setIdentity();
			float x = (float)Math.cos(Math.PI*2/DIV*i)*r + xoff;
			float z = (float)Math.sin(Math.PI*2/DIV*i)*r;
			transform.basis.rotY((float) Math.PI*2/DIV*-i);
			transform.origin.set(x, 2f, z);
			bodiesRing[i] = world.addBody(boxRing.getShape(), 0f, transform);
		}
		
		
		// Himo
		final int DIV2 = 8;
		
		RigidBody[] bodiesHimo = new RigidBody[DIV2];
		
		for (int i = 0; i < DIV2; i++) {
			Transform transform = new Transform();
			transform.setIdentity();
			float x = (float)Math.cos(Math.PI*2/DIV2*i)*r + xoff;
			float z = (float)Math.sin(Math.PI*2/DIV2*i)*r;
			transform.basis.rotY((float) Math.PI*2/DIV2*-i);
			System.out.printf("Himo: %f\t%f\t%f\n", x- xoff, 1.0f, z);
			transform.origin.set(x, 1.0f, z);
			bodiesHimo[i] = world.addBody(boxHimo.getShape(), 0.1f, transform);
			
			Point2PointConstraint joint = new Point2PointConstraint(
				bodiesRing[i], bodiesHimo[i], new Vector3f(0, -0.15f, 0.3f), new Vector3f(0, 1.05f, 0));
			world.addConstraint(joint);
		}
		
		
		// Link
		RigidBody[] bodiesLink = new RigidBody[DIV2];
		final float r2 = 0.6f;
		for (int i = 0; i < DIV2; i++) {
			Transform transform = new Transform();
			transform.setIdentity();

			float x = (float)Math.cos(Math.PI*2/DIV2*i + Math.PI*2/DIV2/2)*r2 + xoff;
			float z = (float)Math.sin(Math.PI*2/DIV2*i + Math.PI*2/DIV2/2)*r2;
			transform.basis.rotY((float) Math.PI*2/DIV2*-i + (float)Math.PI*2/DIV2/2);
			System.out.printf("Link: %f\t%f\t%f\n", x-xoff, 0f, z);
			transform.origin.set(x, 0.0f, z);
			bodiesLink[i] = world.addBody(boxLink.getShape(), 0.1f, transform);
			
			Point2PointConstraint joint = new Point2PointConstraint(
				bodiesLink[i], bodiesHimo[(i+1) % DIV2], new Vector3f(-0.2f, 0f, 0f), new Vector3f(0,- 1.05f, 0));
			world.addConstraint(joint);

			Point2PointConstraint joint2 = new Point2PointConstraint(
				bodiesLink[i], bodiesHimo[i], new Vector3f(0.2f, 0f, 0f), new Vector3f(0, -1.05f, 0));
			world.addConstraint(joint2);
		}
		
	}
	
	
}
