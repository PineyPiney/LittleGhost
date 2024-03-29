// VERTEX SHADER INFORMATION
#version 400 core
layout (location = 0) in vec2 aPos;
layout (location = 1) in vec2 aTexCoord;

uniform mat4 model;

out vec2 texCoords;
out mat4 Fmodel;

void main(){
	Fmodel = model;
	texCoords = aTexCoord;
	gl_Position = model * vec4(aPos, 0.0, 1.0);
}