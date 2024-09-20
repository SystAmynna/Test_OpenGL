import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.junit.jupiter.api.Test;

/**
 * Tester JOML
 */
public class TestJOML {

    /**
     * Tester un vecteur 4 avec une matrice de translation
     */
    @Test
    public void test1() {
        // Création d'un vecteur 4
        Vector4f vec = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);

        // Création d'une matrice de translation
        Matrix4f trans = new Matrix4f().identity().translate(1.0f, 1.0f, 0.0f);

        // application de la transformation à la matrice
        Vector4f result = trans.transform(vec, new Vector4f());

        // Affichage du résultat
        System.out.println(result.x + " " + result.y + " " + result.z);

        // Vérification
        assert result.x == 2.0f;
        assert result.y == 1.0f;
        assert result.z == 0.0f;
    }

}
