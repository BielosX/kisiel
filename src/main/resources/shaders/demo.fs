#version 410 core
in vec4 vertColor;
out vec4 FragColor;

uniform vec4 color;
uniform float alpha;

void main()
{
    FragColor = vertColor;
}
