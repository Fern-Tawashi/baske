import com.bulletphysics.collision.shapes.CollisionShape;


/**
 * The Shape abstract
 * @author tawashi
 * @version 1.0
 */
abstract class Shape {

	protected CollisionShape colShape;
	
	public CollisionShape getShape() {
		return colShape;
	}
	
	abstract protected void draw();
	
	
}
