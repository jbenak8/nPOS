/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.rozvrzeni;

/**
 *
 * @author Jan Benák
 */
public final class DE_DE extends Rozvrzeni {
    
    public DE_DE() {
        super(Rozvrzeni.DE_DE, false);
        super.MRTVE_KLAVESY.add(STREDNIK);
        super.MRTVE_KLAVESY.add(CARKA_NAD_PISMENY);
        super.MRTVE_KLAVESY_SHIFT.add(CARKA_NAD_PISMENY);
        super.MRTVE_KLAVESY_CAPS.add(CARKA_NAD_PISMENY);
        setStandardniSadu();
        setShiftSadu();
        setCapsSadu();
        setAltGrSadu();
    }

    @Override
    protected void setStandardniSadu() {
        super.STD_SADA.put(Q, 'q');
        super.STD_SADA.put(W, 'w');
        super.STD_SADA.put(E, 'e');
        super.STD_SADA.put(R, 'r');
        super.STD_SADA.put(T, 't');
        super.STD_SADA.put(Z, 'z');
        super.STD_SADA.put(U, 'u');
        super.STD_SADA.put(I, 'i');
        super.STD_SADA.put(O, 'o');
        super.STD_SADA.put(P, 'p');
        super.STD_SADA.put(A, 'a');
        super.STD_SADA.put(S, 's');
        super.STD_SADA.put(D, 'd');
        super.STD_SADA.put(F, 'f');
        super.STD_SADA.put(G, 'g');
        super.STD_SADA.put(H, 'h');
        super.STD_SADA.put(J, 'j');
        super.STD_SADA.put(K, 'k');
        super.STD_SADA.put(L, 'l');
        super.STD_SADA.put(Y, 'y');
        super.STD_SADA.put(X, 'x');
        super.STD_SADA.put(C, 'c');
        super.STD_SADA.put(V, 'v');
        super.STD_SADA.put(B, 'b');
        super.STD_SADA.put(N, 'n');
        super.STD_SADA.put(M, 'm');
        super.STD_SADA.put(MEZERA, ' ');
        super.STD_SADA.put(CARKA, ',');
        super.STD_SADA.put(TECKA, '.');
        super.STD_SADA.put(POMLCKA, '-');
        super.STD_SADA.put(U_S_KROUZKEM, 'ö');
        super.STD_SADA.put(PARAGRAF, 'ä');
        super.STD_SADA.put(U_DLOUHE, 'ü');
        super.STD_SADA.put(ZAVORKY, '+');
        super.STD_SADA.put(UMLAUT, '#');
        super.STD_SADA.put(STREDNIK, '^');
        super.STD_SADA.put(PLUS, '1');
        super.STD_SADA.put(E_HACEK, '2');
        super.STD_SADA.put(S_HACEK, '3');
        super.STD_SADA.put(C_HACEK, '4');
        super.STD_SADA.put(R_HACEK, '5');
        super.STD_SADA.put(Z_HACEK, '6');
        super.STD_SADA.put(Y_CARKA, '7');
        super.STD_SADA.put(A_CARKA, '8');
        super.STD_SADA.put(I_CARKA, '9');
        super.STD_SADA.put(E_CARKA, '0');
        super.STD_SADA.put(ROVNA_SE, 'ß');
        super.STD_SADA.put(CARKA_NAD_PISMENY, '´');
    }

    @Override
    protected void setShiftSadu() {
        super.SHIFT_SADA.put(Q, 'Q');
        super.SHIFT_SADA.put(W, 'W');
        super.SHIFT_SADA.put(E, 'E');
        super.SHIFT_SADA.put(R, 'R');
        super.SHIFT_SADA.put(T, 'T');
        super.SHIFT_SADA.put(Z, 'Z');
        super.SHIFT_SADA.put(U, 'U');
        super.SHIFT_SADA.put(I, 'I');
        super.SHIFT_SADA.put(O, 'O');
        super.SHIFT_SADA.put(P, 'P');
        super.SHIFT_SADA.put(A, 'A');
        super.SHIFT_SADA.put(S, 'S');
        super.SHIFT_SADA.put(D, 'D');
        super.SHIFT_SADA.put(F, 'F');
        super.SHIFT_SADA.put(G, 'G');
        super.SHIFT_SADA.put(H, 'H');
        super.SHIFT_SADA.put(J, 'J');
        super.SHIFT_SADA.put(K, 'K');
        super.SHIFT_SADA.put(L, 'L');
        super.SHIFT_SADA.put(Y, 'Y');
        super.SHIFT_SADA.put(X, 'X');
        super.SHIFT_SADA.put(C, 'C');
        super.SHIFT_SADA.put(V, 'V');
        super.SHIFT_SADA.put(B, 'B');
        super.SHIFT_SADA.put(N, 'N');
        super.SHIFT_SADA.put(M, 'M');
        super.SHIFT_SADA.put(MEZERA, ' ');
        super.SHIFT_SADA.put(CARKA, ';');
        super.SHIFT_SADA.put(TECKA, ':');
        super.SHIFT_SADA.put(POMLCKA, '_');
        super.SHIFT_SADA.put(U_S_KROUZKEM, 'Ö');
        super.SHIFT_SADA.put(PARAGRAF, 'Ä');
        super.SHIFT_SADA.put(U_DLOUHE, 'Ü');
        super.SHIFT_SADA.put(ZAVORKY, '*');
        super.SHIFT_SADA.put(UMLAUT, '\'');
        super.SHIFT_SADA.put(STREDNIK, '°');
        super.SHIFT_SADA.put(PLUS, '!');
        super.SHIFT_SADA.put(E_HACEK, '"');
        super.SHIFT_SADA.put(S_HACEK, '§');
        super.SHIFT_SADA.put(C_HACEK, '$');
        super.SHIFT_SADA.put(R_HACEK, '%');
        super.SHIFT_SADA.put(Z_HACEK, '&');
        super.SHIFT_SADA.put(Y_CARKA, '/');
        super.SHIFT_SADA.put(A_CARKA, '(');
        super.SHIFT_SADA.put(I_CARKA, ')');
        super.SHIFT_SADA.put(E_CARKA, '=');
        super.SHIFT_SADA.put(ROVNA_SE, '?');
        super.SHIFT_SADA.put(CARKA_NAD_PISMENY, '`');
    }

    @Override
    protected void setCapsSadu() {
        super.CAPS_SADA.put(Q, 'Q');
        super.CAPS_SADA.put(W, 'W');
        super.CAPS_SADA.put(E, 'E');
        super.CAPS_SADA.put(R, 'R');
        super.CAPS_SADA.put(T, 'T');
        super.CAPS_SADA.put(Z, 'Z');
        super.CAPS_SADA.put(U, 'U');
        super.CAPS_SADA.put(I, 'I');
        super.CAPS_SADA.put(O, 'O');
        super.CAPS_SADA.put(P, 'P');
        super.CAPS_SADA.put(A, 'A');
        super.CAPS_SADA.put(S, 'S');
        super.CAPS_SADA.put(D, 'D');
        super.CAPS_SADA.put(F, 'F');
        super.CAPS_SADA.put(G, 'G');
        super.CAPS_SADA.put(H, 'H');
        super.CAPS_SADA.put(J, 'J');
        super.CAPS_SADA.put(K, 'K');
        super.CAPS_SADA.put(L, 'L');
        super.CAPS_SADA.put(Y, 'Y');
        super.CAPS_SADA.put(X, 'X');
        super.CAPS_SADA.put(C, 'C');
        super.CAPS_SADA.put(V, 'V');
        super.CAPS_SADA.put(B, 'B');
        super.CAPS_SADA.put(N, 'N');
        super.CAPS_SADA.put(M, 'M');
        super.CAPS_SADA.put(MEZERA, ' ');
        super.CAPS_SADA.put(CARKA, ';');
        super.CAPS_SADA.put(TECKA, ':');
        super.CAPS_SADA.put(POMLCKA, '-');
        super.CAPS_SADA.put(U_S_KROUZKEM, 'Ö');
        super.CAPS_SADA.put(PARAGRAF, 'Ä');
        super.CAPS_SADA.put(U_DLOUHE, 'Ü');
        super.CAPS_SADA.put(ZAVORKY, '*');
        super.CAPS_SADA.put(UMLAUT, '\'');
        super.CAPS_SADA.put(STREDNIK, '^');
        super.CAPS_SADA.put(PLUS, '!');
        super.CAPS_SADA.put(E_HACEK, '"');
        super.CAPS_SADA.put(S_HACEK, '§');
        super.CAPS_SADA.put(C_HACEK, '$');
        super.CAPS_SADA.put(R_HACEK, '%');
        super.CAPS_SADA.put(Z_HACEK, '&');
        super.CAPS_SADA.put(Y_CARKA, '/');
        super.CAPS_SADA.put(A_CARKA, '(');
        super.CAPS_SADA.put(I_CARKA, ')');
        super.CAPS_SADA.put(E_CARKA, '=');
        super.CAPS_SADA.put(ROVNA_SE, '?');
        super.CAPS_SADA.put(CARKA_NAD_PISMENY, '´');
    }

    @Override
    protected void setAltGrSadu() {
        super.ALT_GR_SADA.put(Q, '@');
        super.ALT_GR_SADA.put(E, '€');
        super.ALT_GR_SADA.put(M, 'µ');
        super.ALT_GR_SADA.put(ZAVORKY, '~');
        super.ALT_GR_SADA.put(E_HACEK, '²');
        super.ALT_GR_SADA.put(S_HACEK, '³');
        super.ALT_GR_SADA.put(Y_CARKA, '{');
        super.ALT_GR_SADA.put(A_CARKA, '[');
        super.ALT_GR_SADA.put(I_CARKA, ']');
        super.ALT_GR_SADA.put(E_CARKA, '}');
        super.ALT_GR_SADA.put(ROVNA_SE, '\\');
    }
    
}
