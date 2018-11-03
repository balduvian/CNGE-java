package embgine.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

public class FBO {
    
	private static Window window;
	
    private int id;
    private int depthRenderBufferID;
    private Texture boundTexture;
    
    /**
     * creates a new fbo that is linked to the given texture
     * 
     * @param tex - texture for the fbo
     */
    public FBO(Texture tex) {
        id = glGenFramebuffers();
        
        //bind
        glBindFramebuffer(GL_FRAMEBUFFER, id);
        
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
        
        //binding the texture
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, tex.getId(), 0);
        boundTexture = tex;
        bindDepthRenderBuffer(tex.getWidth(), tex.getHeight());
        
        //unbind
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    
    public static void giveWindow(Window w) {
    	window = w;
    }
    
    /**
     * binds a new replacement texture to the fbo
     * 
     * @param tex - the motherfuckin texture
     */
    public void replaceTexture(Texture tex) {
    	glBindFramebuffer(GL_FRAMEBUFFER, id);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, tex.getId(), 0);
        boundTexture = tex;
        bindDepthRenderBuffer(tex.getWidth(), tex.getHeight());
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    
    /**
     * binds a depth render buffer to the fbo
     * 
     * @param w - width of the render buffer
     * @param h - height of the render buffer
     */
    private void bindDepthRenderBuffer(int w, int h) {
        depthRenderBufferID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, depthRenderBufferID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, w, h);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthRenderBufferID);
    }
    
    /**
     * enables THIS frameBuffer
     */
    public void enable() {
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, id);
        glViewport(0, 0, boundTexture.getWidth(), boundTexture.getHeight());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    
    /**
     * switches the frameBuffer back to the window
     */
    public void disable(Window w) {
    	glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, w.getWidth(), w.getHeight());
        Window.clear();
    }
    
    /**
     * returns the current texture bound to the fbo
     * 
     * @return current texture
     */
    public Texture getBoundTexture() {
        return boundTexture;
    }
    
    /**
     * gets the fbo handle
     * 
     * @return fbo handle
     */
    public int getId() {
        return id;
    }
    
    /**
     * gets the handle for the depth render buffer
     * 
     * @return depth render buffer
     */
    public int getDepthRenderBufferID() {
        return depthRenderBufferID;
    }
}