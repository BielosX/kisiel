#version 410 core
out vec4 FragColor;

uniform vec4 color;
uniform float alpha;

void main()
{
    FragColor = vec4(color.x, color.y, color.z, alpha);
}
