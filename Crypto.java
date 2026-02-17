import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Crypto
{
    //===========================================================
    //                METHODES UTILES
    //===========================================================

    /**
     * Convertir une chaine de caracteres en tableau de bytes
     */
    public static byte[] strToByte(final String pMsg)
    {
        return pMsg.getBytes();
    } // strToByte

    /**
     * Convertir un tableau de bytes en une chaine de caracteres
     */
    public static String byteToStr(final byte[] pByteArray)
    {
        return new String(pByteArray);
    } // byteToStr()

    /**
     * Ecrire un texte dans un fichier en conservant son contenu
     * si "pAjout=true", ou en l'ecrasant si "pAjout=false"
     */
    public static void writeFile(final String pContent, final String pFile, final boolean pAjout)
    {
        try {
            FileWriter vWriter = new FileWriter(pFile, pAjout);
            vWriter.write(pContent);
            vWriter.close();
        }
        catch (final IOException pE) {
            pE.printStackTrace();
        }
    } // writeFile()

    /**
     * Lire le contenu d'un fichier de nom (pFile)
     * et le retourner dans une String
     */
    public static String readFile(final String pFile)
    {
        String vContent = "";
        try {
            vContent = new String(Files.readAllBytes(Paths.get(pFile)));
        }
        catch (final IOException pE) {
            pE.printStackTrace();
        }
        return vContent;
    } // readFile()

    //===========================================================
    //                FIN METHODES UTILES
    //===========================================================

    /**
     * Calculer le nombre d'occurence de chaque lettre dans un fichier
     * texte
     */
    public static int[] frequences(final String pFile)
    {
        String vTxt = readFile(pFile).toUpperCase();

        /* Tableau de frequences (vOcc[k] : nombre d'occurrences de la
           k-ieme lettre de l'alphabet dans la chaine vTxt) */
        int[] vOcc = new int[26];

        for (int i = 0; i < vTxt.length(); i++) {          // On parcourt le texte
            char c = vTxt.charAt(i);                       // On récupère le caractère
            if (c >= 'A' && c <= 'Z') {                    // Si c'est une lettre
                vOcc[c - 'A']++;                           // On incrémente la fréquence
            }
        }

        // Affichage
        String vFreq = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";       // On crée une chaîne l'alphabet
        String[] vFreqSplit = vFreq.split("");             // On découpe la chaîne en un tableau

        for (int j = 0; j < vOcc.length; j++) {            // On parcourt le tableau des occurrences
            System.out.println(vFreqSplit[j] + " : " + vOcc[j]);
            // Pour chaque lettre, on affiche son nombre d'occurrences
        }

        return vOcc;
    }

    /**
     * Déchiffrer le contenu d'un fichier en appliquant une substitution
     * mono-alphabétique basée sur une table de correspondance de lettres
     */
    public static String subDecrypt(final String pFile, final char[] pKey)
    {
        String vChiffre = readFile(pFile).toUpperCase();   // Lecture + majuscules
        String vClair = "";                                // Texte déchiffré

        for (int i = 0; i < vChiffre.length(); i++) {      // Parcours caractère par caractère
            char c = vChiffre.charAt(i);
            if (c >= 'A' && c <= 'Z') {                    // Si c'est une lettre
                vClair += pKey[c - 'A'];                   // Substitution
            } else {
                vClair += c;                               // Conservation
            }
        }
        return vClair;
    }

    /**
     * Chiffrement / Déchiffrement par XOR
     */
    public static void cryptXor(final String pInfile, final String pOutfile, final String pKey)
    {
        // Lecture du fichier d'entrée
        String vCont = readFile(pInfile);

        // Conversion du texte et de la clé en tableaux de bytes
        byte[] vMsg = strToByte(vCont);
        byte[] vKey = strToByte(pKey);

        // Tableau pour stocker le résultat du chiffrement
        byte[] vRes = new byte[vMsg.length];

        // Application du XOR caractère par caractère
        for (int i = 0; i < vMsg.length; i++) {
            vRes[i] = (byte) (vMsg[i] ^ vKey[i % vKey.length]);
        }

        // Écriture du résultat dans le fichier de sortie
        writeFile(byteToStr(vRes), pOutfile, false);
    } // cryptXor()

    /*=============== Tests et invocations de méthodes ============================*/

    public static void main(final String[] pArgs)
    {
        // Question 3
        char[] vKey = {
                'V','K','Z','M','H','N','O','P','C','Q',
                'R','S','T','Y','I','J','X','D','L','E',
                'G','W','U','A','B','F'
        };
        System.out.println(subDecrypt("q3.txt", vKey));

        /*=============================================================*/

        // Question 4
        char[] vKeyDec6 = {
                'U','V','W','X','Y','Z','A','B','C','D','E','F',
                'G','H','I','J','K','L','M','N','O','P','Q','R','S','T'
        };
        System.out.println(subDecrypt("q4.txt", vKeyDec6));

        /*=============================================================*/

        // Question 6
        cryptXor("q6_clair.txt", "q6_chiffre.txt", "ABCD");

        /*=============================================================*/
        
        // Question 8 : déchiffrement du fichier chiffré 
        // Le XOR est réversible, on utilise donc la même clé 
        cryptXor("q6_chiffre.txt", "q6_dechiffre.txt", "ABCD");
        
        /*=============================================================*/
        
        // Question 9 : attaque par texte clair connu 
        // On sait qu’un fichier HTML commence par "<html><head>" 
        // On utilise donc cette chaîne comme clé pour retrouver la vraie clé 
        cryptXor("q9.txt", "q9_clair.txt", "<html><head>"); 
        
        // Après le premier déchiffrement, on obtient la clé réelle "TRLK" 
        // On relance alors le déchiffrement avec cette clé 
        cryptXor("q9.txt", "q9_clair.txt", "TRLK");
        
        /*=============================================================*/
    } // main()
} // Crypto
