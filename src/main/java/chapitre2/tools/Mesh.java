package chapitre2.tools;

import chapitre1.tools.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL13.*;

public class Mesh {

    public static final ArrayList<Mesh> MESHES = new ArrayList<>();

    private Matrix4f model;
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;
    private int vaoId;
    private int vboId;
    private int eboId;
    private int vertexCount;
    private Texture[] textures; // Array of textures
    private Shader3D shader;

    private final HashMap<String, Object> UNIFORMS = new HashMap<>();

    public Mesh(float[] vertices, int[] indices, Texture[] textures, Shader3D shader) {
        MESHES.add(this);
        this.position = new Vector3f(0.0f, 0.0f, 0.0f);
        this.rotation = new Vector3f(0.0f, 0.0f, 0.0f);
        this.scale = new Vector3f(1.0f, 1.0f, 1.0f);
        this.model = new Matrix4f();
        this.textures = textures; // Initialize the texture array
        this.shader = shader;
        initBuffers(vertices, indices);
        updateModelMatrix();
    }

    private void initBuffers(float[] vertices, int[] indices) {
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // VBO
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        if (indices != null) {
            // EBO
            eboId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
            vertexCount = indices.length;
        } else {
            vertexCount = vertices.length / 8; // 3 for position + 3 for color + 2 for texture
        }

        // Configure vertex attributes
        int stride = 8 * Float.BYTES;
        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void updateModelMatrix() {
        model.identity();
        model.translate(position);
        model.rotateX((float) Math.toRadians(rotation.x));
        model.rotateY((float) Math.toRadians(rotation.y));
        model.rotateZ((float) Math.toRadians(rotation.z));
        model.scale(scale);
    }

    public void render() {
        updateModelMatrix();

        if (shader == null) return;
        shader.use();
        shader.setMat4f("model", model);

        // Charger les uniformes stockés
        for (Map.Entry<String, Object> entry : UNIFORMS.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Integer) shader.setInt(key, (int) value);
            if (value instanceof Float) shader.setFloat(key, (float) value);
            if (value instanceof Vector3f) shader.setVec3(key, (Vector3f) value);
            if (value instanceof Matrix4f) shader.setMat4f(key, (Matrix4f) value);
        }

        if (textures != null) {

            // Lier et activer les textures
            for (int i = 0; i < textures.length; i++) {
                if (textures[i] != null) {
                    glActiveTexture(GL_TEXTURE0 + i);
                    textures[i].bind();
                    shader.setInt("textures[" + i + "]", i);
                }
            }
        }



        // Dessiner le maillage
        glBindVertexArray(vaoId);
        if (eboId != 0) {
            glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
        } else {
            glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        }
        glBindVertexArray(0);
    }

    private boolean noTexture() {
        for (Texture texture : textures) {
            if (texture != null) {
                return false;
            }
        }
        return true;
    }

    public void cleanup() {
        glDeleteBuffers(vboId);
        if (eboId != 0) {
            glDeleteBuffers(eboId);
        }
        glDeleteVertexArrays(vaoId);
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        updateModelMatrix();
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
        updateModelMatrix();
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
        updateModelMatrix();
    }

    public Matrix4f getModelMatrix() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setShader(Shader3D shader) {
        this.shader = shader;
    }

    public void addUniform(String name, Object value) {
        UNIFORMS.put(name, value);
    }
}