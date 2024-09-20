/**
 * Classe static encapsulant des outils.
 */
public class Tools {

    /**
     * Convertir un angle en radian en degr�s.
     * @param radian L'angle en radian
     */
    public static float radianToDegree(float radian) {
        return (float) (radian * 180 / Math.PI);
    }

    /**
     * Convertir un angle en degr�s en radian.
     * @param degree L'angle en degr�s
     */
    public static float degreeToRadian(float degree) {
        return (float) (degree * Math.PI / 180);
    }

}
