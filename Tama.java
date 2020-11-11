import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.dynamics.RigidBody;

import javax.vecmath.Vector3f;



class Tama {
	
	Sphere tama;
	
	World world;
	
	
	Tama(World world) {
		this.world = world;
		tama = new Sphere(0.9f, 0.1f, 0.1f);
	}
	
	
	
	public void init() {
	}
	
	
	
	public void birth(float degree) {
		float x = (float)Math.cos(Math.toRadians(degree - 90));
		float y = (float)Math.sin(Math.toRadians(degree - 90));
		
		Transform transform = new Transform();
		transform.setIdentity();
		transform.origin.set(-12.0f, -5.0f, 0.0f);
		RigidBody body = world.addBody(tama.getShape(), 1.0f, transform);
		body.setRestitution(0.3f);
		
		Vector3f linVel = new Vector3f(x, y, 0f);
		linVel.normalize();
		linVel.scale(20f);
		body.setLinearVelocity(linVel);
		//body.setAngularVelocity(new Vector3f(0f, 0f, 0f));
	}
	
	
}
