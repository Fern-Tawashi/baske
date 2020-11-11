import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.collision.shapes.CollisionShape;

import java.nio.FloatBuffer;
import javax.vecmath.Vector3f;



class GL {
	
	FloatBuffer qaAmbientLight  = BufferUtils.createFloatBuffer(4).put(new float[] { 0.1f, 0.1f, 0.1f, 1.0f });
	FloatBuffer qaDiffuseLight  = BufferUtils.createFloatBuffer(4).put(new float[] { 0.9f, 0.9f, 0.9f, 1.0f });
	//FloatBuffer qaDiffuseLight  = BufferUtils.createFloatBuffer(4).put(new float[] { 0.3f, 0.3f, 0.3f, 1.0f });
	FloatBuffer qaSpecularLight = BufferUtils.createFloatBuffer(4).put(new float[] { 0.3f, 0.3f, 0.3f, 1.0f });
	FloatBuffer qaLightPosi     = BufferUtils.createFloatBuffer(4).put(new float[] { 0.0f, 0.0f, 1.0f, 0.0f });
	
	private static float[] glMat = new float[16];
	
	//GL11 gl;
	private Qua4 quaP = new Qua4(0, 0, 0, 1f);
	boolean drug;
	float fscale = 1.0f;
	
	
	/** time at last frame */
	long lastFrame;
	
	/** frames per second */
	int fps;
	
	/** last fps time */
	long lastFPS;
	
	
	World world;
	Tama tama;
	Kago kago;
	Yuka yuka;
	Guide guide;
	
	
	GL() {
		try {
			Display.setDisplayMode(new DisplayMode(960, 640));
			Display.setTitle("calc crane");
			Display.setResizable(true);
			Display.create();
		}
		catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		qaAmbientLight.flip();
		qaDiffuseLight.flip();
		qaSpecularLight.flip();
		qaLightPosi.flip();
		
		world = new World();
		yuka = new Yuka(world);
		tama = new Tama(world);
		kago = new Kago(world);
		guide = new Guide(/*gl*/);

	}
	
	
	public void Main() {
		
		init();
		
		getDelta(); // call once before loop to initialise lastFrame
		lastFPS = Util.getTime(); // call before loop to initialise fps timer
		
		world.initPhysics();
		
		yuka.birth();
		kago.birth();
		
		world.clientResetScene();
		
		reshape();
		
		
		while (!Display.isCloseRequested()) {
			int delta = getDelta();
			if (Display.wasResized()) {
				reshape();
			}
			
			// Clear the screen and depth buffer
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			guide.draw(delta);
			world.calcSimulation(this);
			
			pollInput();
			
			Display.update();
			Display.sync(60);
			
			updateFPS();
		}
		
		
		Display.destroy();
	}
	
	
	
	private void init() {
		System.err.println("GL_VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
		System.err.println("GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
		System.err.println("GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT,  qaAmbientLight);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE,  qaDiffuseLight);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, qaSpecularLight);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, qaLightPosi);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		/*
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		*/
		//GL11.glClearColor(0.7f, 0.7f, 0.7f, 0f);
	}
	
	
	
	/* key input */
	private void pollInput() {
		while (Mouse.next()) {
			if (Mouse.getEventButton() == 1) {
				drug = Mouse.getEventButtonState();
			}
			
			if (drug) {
				float dx = (float)Mouse.getEventDX() / Display.getWidth() * 360;
				float dy = (float)Mouse.getEventDY() / Display.getHeight() * 360;
				
				float ax = 0.0f;
				float ay = 0.0f;
					
				float v = (float)Math.sqrt(dx*dx + dy*dy);
				if (v > 0.0f) {
					ax = (float)dy / v * -1;
					ay = (float)dx / v;
				}
				System.out.printf("%f x %f : %f\n", (float)Mouse.getEventX(), (float)Mouse.getEventY(), v);
				quaP = Qua4.rot(quaP, ax, ay, 0, (float)Math.toRadians(v));
				reshape();
			}
			
			int dwh = Mouse.getDWheel();
			if (dwh > 0) {
				fscale *= 1.1f;
				reshape();
			}
			if (dwh < 0) {
				fscale *= 0.9f;
				reshape();
			}
			
		}
		
		
		while (Keyboard.next()) {
			boolean stat = Keyboard.getEventKeyState();
			switch (Keyboard.getEventKey()) {
			case Keyboard.KEY_SPACE:
				if (stat) {
					tama.birth(guide.getDegree());
				}
				break;
			}
		}
		
	}
	
	
	
	private void reshape() {
		float width = Display.getWidth();
		float height = Display.getHeight();
		float h = height / width;
		//System.out.printf("reshape %d x %d : %f\n", (int)width, (int)height, h);
		
		GL11.glViewport(0, 0, (int)width, (int)height);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glFrustum(-1.0f, 1.0f, -h, h, 10.0f, 1000.0f);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0f, 0.0f, -150.0f);
		//GL11.glRotatef(15.0f, 1.0f, 0.0f, 0.0f);

		float[] m = new float[16];
		quaP.toMatrix(m);
		MultMatrix(m);
		
		m[0]  = fscale; m[1]  = 0.0f;   m[2]  = 0.0f;   m[3]  = 0.0f;
		m[4]  = 0.0f;   m[5]  = fscale; m[6]  = 0.0f;   m[7]  = 0.0f;
		m[8]  = 0.0f;   m[9]  = 0.0f;   m[10] = fscale; m[11] = 0.0f;
		m[12] = 0.0f;   m[13] = 0.0f;   m[14] = 0.0f;   m[15] = 1.0f;
		MultMatrix(m);
	}
	
	
	
	/** 
	 * Calculate how many milliseconds have passed 
	 * since last frame.
	 * 
	 * @return milliseconds passed since last frame 
	 */
	int getDelta() {
		long time = Util.getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		
		return delta;
	}
	
	
	
	/**
	 * Calculate the FPS and set it in the title bar
	 */
	void updateFPS() {
		if (Util.getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	
	
	public void drawOpenGL(Transform trans, CollisionShape shape) {
		GL11.glPushMatrix();
		trans.getOpenGLMatrix(glMat);
		MultMatrix(glMat);
		
		Shape obj = (Shape)shape.getUserPointer();
		if (obj != null) {
			obj.draw();
		}
		
		GL11.glPopMatrix();
	}
	
	
	
	static void MultMatrix(float[] m) {
		FloatBuffer floatBuf = BufferUtils.createFloatBuffer(16);
		floatBuf.clear();
		floatBuf.put(m).flip();
		GL11.glMultMatrix(floatBuf);
	}
	
	
	
}
