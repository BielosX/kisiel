#version 410 core
in vec2 TexCoord;
out vec4 FragColor;

//uniform vec4 color;
//uniform float alpha;
uniform sampler2D firstTexture;
uniform sampler2D secondTexture;

void main()
{
    FragColor = mix(texture(firstTexture, TexCoord), texture(secondTexture, TexCoord), 0.2);
}
