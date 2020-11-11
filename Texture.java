import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;


public class Texture {
	
	private static HashMap<String, Integer> listTex = new HashMap<String, Integer>();
	
	
	private String fname;
	private int texId;
	private int textureUnit;
	
	private ByteBuffer buf;
	private int width;
	private int height;
	
	
	public Texture(String fname, int textureUnit) {
		this.fname = fname;
		this.textureUnit = textureUnit;
		texId = getTexture();
	}
	
	
	public int getTexID() {
		return texId;
	}
	
	
	
	private int getTexture() {
		int texId;
		if (!listTex.containsKey(fname)) {
			texId = loadPNGTexture(fname);
			listTex.put(fname, texId);
			System.out.printf("create texture: %s\t%d\n", fname, texId);
		}
		else {
			texId = listTex.get(fname);
			//System.out.printf("reload texture: %s\t%d\n", fname, texId);
		}
		return texId;
	}
	
	
	
	private int loadPNGTexture(String fname) {
		
		loadImage(fname);
		
		// Create a new texture object in memory and bind it
		int texId = GL11.glGenTextures();
		GL13.glActiveTexture(textureUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		
		// All RGB bytes are aligned to each other and each component is 1 byte
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
		
		// Upload the texture data and generate mip maps (for scaling)
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
		//GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		
		// Setup the ST coordinate system
		//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		
		// Setup what to do when the texture has to be scaled
		//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		
		return texId;
	}
	
	
	
	public void replacePNGTexture(String fname) {
		this.fname = fname;
		loadImage(fname);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		
		// All RGB bytes are aligned to each other and each component is 1 byte
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
		
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
	}
	
	
	
	private void loadImage(String fileImage) {
		try {
			InputStream in = new FileInputStream(fileImage);
			PNGDecoder decoder = new PNGDecoder(in);
			
			width = decoder.getWidth();
			height = decoder.getHeight();
			
			buf = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buf, width * 4, Format.RGBA);
			buf.flip();
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	
	public void destroy() {
		System.out.printf("delete texture: %s\t%d\n", fname, texId);
		GL11.glDeleteTextures(texId);
		listTex.remove(fname);
	}
	
	
	public static void destroyAll() {
		for (Map.Entry<String, Integer> tex : listTex.entrySet()) {
			System.out.printf("delete texture %s : %d\n", tex.getKey(), tex.getValue());
			GL11.glDeleteTextures(tex.getValue());
		}
		listTex.clear();
	}
	
	
}
