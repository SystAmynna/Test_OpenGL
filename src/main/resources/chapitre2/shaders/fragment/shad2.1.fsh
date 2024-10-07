#version 330 core
in vec3 ourColor;
in vec2 TexCoord;

out vec4 FragColor;

uniform sampler2D textures[16];   // Tableau de samplers pour les textures
uniform int numTextures;          // Nombre de textures actives

void main() {
    vec4 texColor = vec4(0.0);
    // Parcourt uniquement les textures définies (numTextures)
    for (int i = 0; i < numTextures; i++) {
        vec4 currentTexColor = texture(textures[i], TexCoord);
        if (currentTexColor.a > 0.0) { // Utiliser la première texture non transparente
            texColor = currentTexColor;
            break;
        }
    }
    // Multiplie la couleur de la texture par la couleur d'entrée
    FragColor = texColor * vec4(ourColor, 1.0);
}
