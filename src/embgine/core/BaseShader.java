package embgine.core;

import embgine.graphics.Shader;

public class BaseShader extends Shader{

	public BaseShader() {
		super("res/embgine/shader/base/base.vs", "res/embgine/shader/base/base.fs");
	}

}