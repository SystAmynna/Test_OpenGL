#version 330 core
out vec4 FragColor;

uniform vec3 lightColor;

void main()
{
    // verifie si lightcolor est definit
    if (lightColor == vec3(0.0))
    {
        FragColor = vec4(1.0, 1.0, 1.0, 1.0); // set all 4 vector values to 1.0
    }
    else {
        FragColor = vec4(lightColor, 1.0); // set all 4 vector values to 1.0
    }

}