#version 330 core
out vec4 FragColor;

in vec3 ourColor;
in vec2 TexCoord;

uniform sampler2D texture1;
uniform sampler2D texture2;
uniform vec3 rainbow;
uniform bool damaged;

bool isWhite(vec4 color)
{
    return (color.x <= 1.0 && color.x >= 0.99) &&
    (color.y <= 1.0 && color.y >= 0.99) &&
    (color.z <= 1.0 && color.z >= 0.99) &&
    color.w >= 0.9;

}

void main()
{
    // Récupére la couleur du pixel de la texture1
    vec4 texColor1 = texture(texture2, TexCoord);
    if (texColor1.w == 0.0)
    {
        FragColor = texture(texture1, TexCoord);
    } else {

        // si le pixel sur la texture2 est blanc
        // alors on affiche la couleur de rainbow
        if (isWhite(texColor1.xyzw)) FragColor = vec4(rainbow, 1.0);
        else FragColor = texColor1;

        if (damaged) FragColor += vec4(0.3, -0.2, -0.2, 1.0);

    }


}