import java.util.Arrays;
import java.util.Collections;

public class Enigma
{
    /**
     * Convertit une chaîne de lettres en positions (A=0, ..., Z=25)
     */
    public static int[] strToPos(final String pMsg)
    {
        String vS = pMsg.toUpperCase();          // Passage en majuscules
        int[] vPos = new int[vS.length()];      // Tableau des positions

        for (int i = 0; i < vPos.length; i++) {
            vPos[i] = vS.charAt(i) - 'A';       // Conversion lettre → position
        }
        return vPos;
    } // strToPos()

    /**
     * Convertit un tableau de positions en chaîne de lettres
     */
    public static String posToStr(final int[] pPos)
    {
        String vS = "";
        for (int i = 0; i < pPos.length; i++) {
            vS += (char) ('A' + pPos[i]);       // Conversion position → lettre
        }
        return vS;
    } // posToStr()

    public static void main(final String[] args)
    {
        // Définition des trois rotors
        Integer[][] vR1 = {
            {17,4,19,21,7,11,3,-5,7,9,-10,9,17,6,-6,-2,-4,-7,-12,-5,3,4,-21,-16,-2,-21},
            {10,21,5,-17,21,-4,12,16,6,-3,7,-7,4,2,5,-7,-11,-17,-9,-6,-9,-19,2,-3,-21,-4}
        };

        Integer[][] vR2 = {
            {25,7,17,-3,13,19,12,3,-1,11,5,-5,-7,10,-2,1,-2,4,-17,-8,-16,-18,-9,-1,-22,-16},
            {3,17,22,18,16,7,5,1,-7,16,-3,8,2,9,2,-5,-1,-13,-12,-17,-11,-4,1,-10,-19,-25}
        };

        Integer[][] vR3 = {
            {12,-1,23,10,2,14,5,-5,9,-2,-13,10,-2,-8,10,-6,6,-16,2,-1,-17,-5,-14,-9,-20,-10},
            {1,16,5,17,20,8,-2,2,14,6,2,-5,-12,-10,9,10,5,-9,1,-14,-2,-10,-6,13,-10,-23}
        };

        // Définition du réflecteur
        Integer[] vRF = {
            25,23,21,19,17,15,13,11,9,7,5,3,1,-1,-3,-5,-7,-9,-11,-13,-15,-17,-19,-21,-23,-25
        };

        /*=============================================================*/

        // Création de la machine Enigma
        Rotor vRotor = new Rotor(vR1, vR2, vR3, vRF);

        // Affichage des rotors
        System.out.println("Rotor 1 : " + Arrays.deepToString(vRotor.aRotor1[0]));
        System.out.println("          " + Arrays.deepToString(vRotor.aRotor1[1]) + "\n");

        System.out.println("Rotor 2 : " + Arrays.deepToString(vRotor.aRotor2[0]));
        System.out.println("          " + Arrays.deepToString(vRotor.aRotor2[1]) + "\n");

        System.out.println("Rotor 3 : " + Arrays.deepToString(vRotor.aRotor3[0]));
        System.out.println("          " + Arrays.deepToString(vRotor.aRotor3[1]) + "\n");

        // Affichage du réflecteur
        System.out.println("Reflecteur : " + Arrays.toString(vRotor.aReflecteur));

        /*=============================================================*/

        // Texte chiffré à déchiffrer
        String texteChiffre = "OFWEFPGCUWUYQWIVQUXCYOSQBLRE";

        // Conversion du texte en positions
        int[] pos = strToPos(texteChiffre);
        int[] res = new int[pos.length];

        // Traitement de chaque lettre
        for (int i = 0; i < pos.length; i++) {

            int p = pos[i];

            // Passage aller dans les rotors
            p = (p + vRotor.aRotor1[1][p]) % 26;
            p = (p + vRotor.aRotor2[1][p]) % 26;
            p = (p + vRotor.aRotor3[1][p]) % 26;

            // Passage dans le réflecteur
            p = (p + vRotor.aReflecteur[p]) % 26;

            // Passage retour dans les rotors
            p = (p + vRotor.aRotor3[0][p]) % 26;
            p = (p + vRotor.aRotor2[0][p]) % 26;
            p = (p + vRotor.aRotor1[0][p]) % 26;

            // Sauvegarde de la lettre déchiffrée
            res[i] = (p + 26) % 26;

            // Rotation du rotor actif après chaque lettre
            vRotor.decalRotor(-1, 2);
        }

        // Conversion et affichage du texte clair
        System.out.println(posToStr(res));

        /*=============================================================*/
    } // main()
} // Enigma

class Rotor  // classe interne
{
    // Rotors et réflecteur
    Integer[][] aRotor1;
    Integer[][] aRotor2;
    Integer[][] aRotor3;
    Integer[]   aReflecteur;

    int aRotorActif;
    int[] aPosition;

    /**
     * Initialisation des rotors et du réflecteur
     */
    public Rotor(final Integer[][] pR1, final Integer[][] pR2,
                 final Integer[][] pR3, final Integer[] pReflect)
    {
        this.aRotor1 = pR1;
        this.aRotor2 = pR2;
        this.aRotor3 = pR3;
        this.aReflecteur = pReflect;
        this.aRotorActif = 0;
        this.aPosition = new int[] {0, 0, 0};
    } // Rotor()

    /**
     * Décale le rotor actif d'une position
     */
    public void decalRotor(final int pDirection, final int pRotorActif)
    {
        switch (pRotorActif) {
            case 1:
                Collections.rotate(Arrays.asList(this.aRotor1[0]), pDirection);
                Collections.rotate(Arrays.asList(this.aRotor1[1]), pDirection);
                this.aPosition[0] = (this.aPosition[0] + 1) % 26;
                break;

            case 2:
                Collections.rotate(Arrays.asList(this.aRotor2[0]), pDirection);
                Collections.rotate(Arrays.asList(this.aRotor2[1]), pDirection);
                this.aPosition[1] = (this.aPosition[1] + 1) % 26;
                break;

            case 3:
                Collections.rotate(Arrays.asList(this.aRotor3[0]), pDirection);
                Collections.rotate(Arrays.asList(this.aRotor3[1]), pDirection);
                this.aPosition[2] = (this.aPosition[2] + 1) % 26;
                break;

            default:
                System.out.println("erreur: rotor non identifié");
                System.exit(0);
        }
    } // decalRotor()
} // Rotor
