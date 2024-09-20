/**
 * Classe static encapsulant des outils.
 */
public class Tools {

    /**
     * Convertir un angle en radian en degrés.
     * @param radian L'angle en radian
     */
    public static float radianToDegree(float radian) {
        return (float) (radian * 180 / Math.PI);
    }

    /**
     * Convertir un angle en degrés en radian.
     * @param degree L'angle en degrés
     */
    public static float degreeToRadian(float degree) {
        return (float) (degree * Math.PI / 180);
    }

}
