// FRAGMENT SHADER INFORMATION
#version 400 core

in vec2 texCoords;

uniform vec4 colour;
uniform float radius;
uniform mat4 model;

out vec4 FragColour;

void main(){
	vec2 size = vec2(abs(model[0][0]), abs(model[1][1]));

	// Distance from the corner to the center of the curve as a proportion of the size of the bubble
	vec2 relativeRadii = vec2(radius) / size;
	// Vector from the center of the box to the center of the curves
	vec2 innerBox = vec2(0.5) - relativeRadii;

	// The vector of the texture Coords from the center of the texture
	vec2 centerDist = vec2(abs(texCoords.x - 0.5), abs(texCoords.y - 0.5));
	// The vector of the texture Coords from the center of the nearest curve
	vec2 absPointToCurveCenter = centerDist - innerBox;
	// Scale 0 - 1 from center of curve to edge of texture
	// By dividing the distance from the point to the center of the circle by the relative distance from corner to center
	// The distance is the relative distance from the center of the curve to the edge of the box
	vec2 proportionalCurve = absPointToCurveCenter / relativeRadii;

	// If the pixel is in one of the corners where the curve is formed
	bool inCorner = absPointToCurveCenter.x > 0 && absPointToCurveCenter.y > 0;

	if(inCorner && pow(proportionalCurve.x, 2) + pow(proportionalCurve.y, 2) > 1){
		discard;
	}

	FragColour = colour;
}