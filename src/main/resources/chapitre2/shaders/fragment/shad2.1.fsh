#version 330 core
in vec3 ourColor;
in vec2 TexCoord;

out vec4 FragColor;

uniform sampler2D textures[16];   // Tableau de samplers pour les textures

void main() {
    vec4 texColor = vec4(0.0); // Couleur de la texture
    bool textureFound = false; // Indique si une texture a été trouvée

    // Parcourt les textures définies
    for (int i = 0; i < 16; i++) {
        vec4 currentTexColor = texture(textures[i], TexCoord);
        if (currentTexColor.a > 0.0) { // Utiliser la première texture non transparente
            texColor = currentTexColor;
            textureFound = true;
            break;
        }
    }

    // Si aucune texture n'est trouvée, utiliser une couleur par défaut
    if (!textureFound) {
        texColor = vec4(ourColor, 1.0);
    }

    // Multiplie la couleur de la texture par la couleur d'entrée
    FragColor = texColor * vec4(ourColor, 1.0);
}