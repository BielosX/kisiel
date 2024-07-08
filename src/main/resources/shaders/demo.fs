#version 410 core
in vec2 TexCoord;
out vec4 FragColor;

//uniform vec4 color;
//uniform float alpha;
uniform sampler2D sampler;

void main()
{
    FragColor = texture(sampler, TexCoord);
}
