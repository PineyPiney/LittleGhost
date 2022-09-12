// FRAGMENT SHADER INFORMATION
#version 400 core

#define MAX_FLIES 8

in vec2 texCoords;

uniform sampler2D ourTexture;

uniform vec3 bright;
uniform vec3 dim;
uniform float brightness;

out vec4 FragColour;

void main(){

	vec4 colour = texture(ourTexture, texCoords);
	if(colour.a == 0) discard;

	float heat = colour.a;
	if(colour.g > 0 && colour.r == 0 && colour.b == 0){
		FragColour = vec4((bright * heat) + (dim * (1 - heat)), brightness);
	}
	else{
		FragColour = colour;
	}


}