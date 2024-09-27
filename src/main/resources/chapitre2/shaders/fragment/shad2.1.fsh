#version 330 core
in vec3 ourColor;
in vec2 TexCoord;

out vec4 FragColor;

uniform sampler2D textures[16]; // Tableau de samplers pour les textures

void main() {
    vec4 texColor = vec4(0.0);
    for (int i = 0; i < 16; i++) {
        vec4 currentTexColor = texture(textures[i], TexCoord);
        if (currentTexColor.a > 0.0) { // Utiliser la première texture non transparente
            texColor = currentTexColor;
            break;
        }
    }
    FragColor = texColor * vec4(ourColor, 1.0);
}