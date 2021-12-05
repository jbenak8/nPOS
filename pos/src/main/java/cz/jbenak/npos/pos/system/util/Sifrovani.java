package cz.jbenak.npos.pos.system.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sifrovani {

    private final static String KLIC = "PosC_srv1369a,82";
    private final static String IV = "Pos-3A148d12eCf5";

    public static String zakodujHeslo(String heslo) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        Cipher sifra = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(KLIC.getBytes("UTF-8"), "AES");
        sifra.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
        byte[] sifrovano = sifra.doFinal(heslo.getBytes("UTF-8"));
        StringBuilder hex = new StringBuilder();
        for (byte aSifrovano : sifrovano) {
            hex.append(String.format("%02x", aSifrovano));
        }
        return hex.toString();
    }

    public static String decodeString(String codedString) throws SifrovaniException {
        try {
            byte[] binarniString = new byte[codedString.length() / 2];
            int indexHex = 0;
            for (int i = 0; i < binarniString.length; i++) {
                String sub = codedString.substring(indexHex, indexHex + 2);
                binarniString[i] = (byte) Integer.parseInt(sub, 16);
                indexHex += 2;
            }
            Cipher sifra = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
            SecretKeySpec key = new SecretKeySpec(KLIC.getBytes("UTF-8"), "AES");
            sifra.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
            return new String(sifra.doFinal(binarniString), "UTF-8");
        } catch (NumberFormatException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException ex) {
            throw new SifrovaniException(ex);
        }
    }

    public static void main(String[] args) {
        try {
            String s = zakodujHeslo("nPos");
            System.out.println("Kódované heslo: " + s);
            System.out.println("Dekódované heslo: " + decodeString(s));
        } catch (SifrovaniException | UnsupportedEncodingException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            Logger.getLogger(Sifrovani.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
