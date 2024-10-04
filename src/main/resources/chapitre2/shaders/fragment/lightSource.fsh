#version 330 core
in vec3 ourColor;
in vec2 TexCoord;

out vec4 FragColor;

void main() {
    FragColor = vec4(1.0, 1.0, 1.0, 1.0); // Définir la couleur de sortie à blanc
}