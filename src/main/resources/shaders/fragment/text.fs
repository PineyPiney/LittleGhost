#version 400 core

out vec4 FragColour;

in vec2 texCoords;

uniform sampler2D ourTexture;
uniform vec4 colour;

void main(){
    vec4 texture = texture(ourTexture, texCoords);
    float ct = texture.r + texture.g + texture.b;
    if(ct < 0.05) discard;

    FragColour = vec4(vec3(colour), ct / 3 * colour.w);
}
