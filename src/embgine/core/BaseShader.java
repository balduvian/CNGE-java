package embgine.core;

import embgine.graphics.Shader;

public class BaseShader extends Shader{

	public BaseShader() {
		super("res/embgine/shaders/base/base.vs", "res/embgine/shaders/base/base.fs");
	}

}