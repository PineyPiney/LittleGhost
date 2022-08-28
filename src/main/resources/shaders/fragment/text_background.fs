#version 460 core

out vec4 FragColour;

in vec2 texCoords;

uniform sampler2D ourTexture;
uniform vec4 colour;
uniform vec4 backgroundColour;

void main(){
    vec4 texture = texture(ourTexture, texCoords);
    float textOpaqueness = (texture.r + texture.g + texture.b) / 3;
    if(textOpaqueness < 0.05) FragColour = backgroundColour;

    FragColour = colour * textOpaqueness + backgroundColour * (1 - textOpaqueness);
}
