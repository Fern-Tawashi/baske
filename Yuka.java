import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.dynamics.RigidBody;

import javax.vecmath.Vector3f;



class Yuka {
	
	Box boxYuka;
	Box boxWall;
	
	World world;
	
	
	Yuka(World world) {
		this.world = world;
		boxYuka = new Box(15.0f, 0.1f, 10.0f,  0.5f, 0.4f, 0.3f);
		boxWall = new Box(0.1f, 3.0f, 5.0f,  0.6f, 0.6f, 0.7f);
	}
	
	
	
	public void birth() {
		{
			Transform transform = new Transform();
			transform.setIdentity();
			transform.origin.set(0f, -9f, 0f);
			RigidBody body = world.addBody(boxYuka.getShape(), 0f, transform);
			body.setRestitution(0.5f);
		}
		
		{
			Transform transform = new Transform();
			transform.setIdentity();
			transform.origin.set(12f, 3f, 0f);
			RigidBody body = world.addBody(boxWall.getShape(), 0f, transform);
			body.setRestitution(0.5f);
		}
	}
	
	
}
