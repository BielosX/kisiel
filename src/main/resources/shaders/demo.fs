#version 410 core
in vec3 vertColor;
out vec4 FragColor;

uniform vec4 color;
uniform float alpha;

void main()
{
    FragColor = vec4(vertColor.r, vertColor.g, vertColor.b, 1.0f);
}
