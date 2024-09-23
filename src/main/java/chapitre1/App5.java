package chapitre1;

import chapitre1.tools.App;
import chapitre1.tools.Objet;
import chapitre1.tools.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;

public class App5 extends App {

    int texture1, VAO;
    Shader shader;

    Vector3f cameraPos, cameraFront, cameraUp;
    Matrix4f view;

    float deltaTime = 0.0f;
    float lastFrame = 0.0f;

    boolean firstMouse = true;
    float yaw = -90.0f;
    float pitch = 0.0f;
    float lastX = width / 2.0f;
    float lastY = height / 2.0f;
    float fov = 45.0f;

    /**
     * Construteur de la classe chapitre1.tools.App.
     *
     * @param width
     * @param height
     * @param title
     */
    public App5(int width, int height, String title) {
        super(width, height, title);
    }

    @Override
    protected void ini() {

        // A INTEGRE DANS APP PLUS TARD

        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        GLFW.glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
            if (firstMouse) {
                lastX = (float) xpos;
                lastY = (float) ypos;
                firstMouse = false;
            }

            float xoffset = (float) xpos - lastX;
            float yoffset = lastY - (float) ypos;
            lastX = (float) xpos;
            lastY = (float) ypos;

            float sensitivity = 0.03f;
            xoffset *= sensitivity;
            yoffset *= sensitivity;

            yaw += xoffset;
            pitch += yoffset;

            if (pitch > 89.0f) {
                pitch = 89.0f;
            }
            if (pitch < -89.0f) {
                pitch = -89.0f;
            }

            Vector3f direction = new Vector3f();
            direction.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
            direction.y = (float) Math.sin(Math.toRadians(pitch));
            direction.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
            cameraFront = direction.normalize();
        });

        GLFW.glfwSetScrollCallback(window, (window, xoffset, yoffset) -> {
            fov -= (float) yoffset;
            if (fov < 1.0f) fov = 1.0f;
            if (fov > 45.0f) fov = 45.0f;
        });

        // --------------------------------------

        float [] vertices = {
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
        };

        int VBO;
        VAO = GL30.glGenVertexArrays();
        VBO = GL30.glGenBuffers();

        GL30.glBindVertexArray(VAO);

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, VBO);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertices, GL30.GL_STATIC_DRAW);

        GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 5 * Float.BYTES, 0);
        GL30.glEnableVertexAttribArray(0);

        GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        GL30.glEnableVertexAttribArray(1);


        texture1 = Objet.loadTexture("wood2.png", false);

        shader = new Shader("shadCube.vsh", "shadCube.fsh");
        shader.use();
        shader.setInt("texture1", 0);

        cameraPos = new Vector3f(0.0f, 0.0f, 3.0f);
        cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);





    }

    @Override
    protected void processInput() {

        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
            GLFW.glfwSetWindowShouldClose(window, true);
        }

        float cameraSpeed = 2.5f * deltaTime;
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            cameraPos.add(new Vector3f(cameraFront).mul(cameraSpeed));
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            cameraPos.sub(new Vector3f(cameraFront).mul(cameraSpeed));
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            cameraPos.sub(new Vector3f(cameraFront).cross(cameraUp).normalize().mul(cameraSpeed));
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            cameraPos.add(new Vector3f(cameraFront).cross(cameraUp).normalize().mul(cameraSpeed));
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
            cameraPos.add(new Vector3f(cameraUp).mul(cameraSpeed));
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
            cameraPos.sub(new Vector3f(cameraUp).mul(cameraSpeed));
        }

    }

    @Override
    protected void render() {

        float currentFrame = (float) GLFW.glfwGetTime();
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;

        GL30.glActiveTexture(texture1);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture1);

        shader.use();


        Matrix4f model = new Matrix4f();
        Matrix4f projection = new Matrix4f();

        projection.perspective((float) Math.toRadians(fov), (float) width / (float) height, 0.1f, 100.0f);

        view = new Matrix4f();
        view = new Matrix4f().lookAt(cameraPos, cameraPos.add(cameraFront, new Vector3f()), cameraUp);

        shader.setMat4("model", model.get(new float[16]));
        shader.setMat4("view", view.get(new float[16]));
        shader.setMat4("projection", projection.get(new float[16]));

        GL30.glBindVertexArray(VAO);

        Vector3f [] cubePositions = {
                new Vector3f(0.0f, 0.0f, 0.0f),
                new Vector3f(2.0f, 5.0f, -15.0f),
                new Vector3f(-1.5f, -2.2f, -2.5f),
                new Vector3f(-3.8f, -2.0f, -12.3f),
                new Vector3f(2.4f, -0.4f, -3.5f),
                new Vector3f(-1.7f, 3.0f, -7.5f),
                new Vector3f(1.3f, -2.0f, -2.5f),
                new Vector3f(1.5f, 2.0f, -2.5f),
                new Vector3f(1.5f, 0.2f, -1.5f),
                new Vector3f(-1.3f, 1.0f, -1.5f)
        };

        for (int i = 0; i < cubePositions.length; i++) {
            model = new Matrix4f();
            model.translate(cubePositions[i]);
            float angle = 20.0f * i;

            float [] vect = {1.0f, 0.3f, 0.5f};
            float length = (float) Math.sqrt(vect[0] * vect[0] + vect[1] * vect[1] + vect[2] * vect[2]);
            model.rotate((float) (GLFW.glfwGetTime() * Math.toRadians(angle)), vect[0] / length, vect[1] / length, vect[2] / length);

            shader.setMat4("model", model.get(new float[16]));
            GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, 36);
        }

    }

    public static void main(String[] args) {
        new App5(1600, 1200, "chapitre1.App5");
    }
}
