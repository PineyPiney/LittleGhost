// FRAGMENT SHADER INFORMATION
#version 460 core

in vec2 texCoords;

uniform vec4 colour;
uniform float radius;
uniform mat4 model;

out vec4 FragColour;

void main(){
	vec2 size = vec2(abs(model[0][0]), abs(model[1][1]));
	vec2 relativeRadii = vec2(radius) / size;
	vec2 innerBox = vec2(0.5) - relativeRadii;

	// The distance of the texture Coords from the center of the texture
	vec2 centerDist = vec2(abs(texCoords.x - 0.5), abs(texCoords.y - 0.5));
	// The distance of the texture Coords from the center of the nearest curve
	vec2 relTex = centerDist - innerBox;
	// Scale 0 - 1 from center of curve to edge of texture
	vec2 proportionalCurve = relTex / relativeRadii;

	// If the pixel is in one of the corners where the curve is formed
	bool inCorner = !(relTex.x < 0 || relTex.y < 0);

	if(inCorner && pow(proportionalCurve.x, 2) + pow(proportionalCurve.y, 2) > 1){
		discard;
	}

	FragColour = colour;
}