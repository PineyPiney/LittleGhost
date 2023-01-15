// FRAGMENT SHADER INFORMATION
#version 400 core

in vec2 texCoords;

uniform sampler2D ourTexture;
uniform float amount;

out vec4 FragColour;

void main(){
	if(texCoords.x > amount) discard;
	FragColour = texture(ourTexture, texCoords);
}