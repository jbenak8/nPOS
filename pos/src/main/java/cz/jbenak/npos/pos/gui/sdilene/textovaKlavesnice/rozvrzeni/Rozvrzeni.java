/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.rozvrzeni;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Jan Benák
 */
public abstract class Rozvrzeni {

    static final int CS_CZ = 0;
    static final int EN_US = 1;
    static final int DE_DE = 2;
    public static final int Q = 100;
    public static final int W = 101;
    public static final int E = 102;
    public static final int R = 103;
    public static final int T = 104;
    public static final int Z = 105;
    public static final int U = 106;
    public static final int I = 107;
    public static final int O = 108;
    public static final int P = 109;
    public static final int A = 110;
    public static final int S = 111;
    public static final int D = 112;
    public static final int F = 113;
    public static final int G = 114;
    public static final int H = 115;
    public static final int J = 116;
    public static final int K = 117;
    public static final int L = 118;
    public static final int Y = 119;
    public static final int X = 120;
    public static final int C = 121;
    public static final int V = 122;
    public static final int B = 123;
    public static final int N = 124;
    public static final int M = 125;
    public static final int MEZERA = 126;
    public static final int CARKA = 127;
    public static final int TECKA = 128;
    public static final int POMLCKA = 129;
    public static final int U_S_KROUZKEM = 130;
    public static final int PARAGRAF = 131;
    public static final int U_DLOUHE = 133;
    public static final int ZAVORKY = 134;
    public static final int UMLAUT = 135;
    public static final int STREDNIK = 136;
    public static final int PLUS = 137;
    public static final int E_HACEK = 138;
    public static final int S_HACEK = 139;
    public static final int C_HACEK = 140;
    public static final int R_HACEK = 141;
    public static final int Z_HACEK = 142;
    public static final int Y_CARKA = 143;
    public static final int A_CARKA = 144;
    public static final int I_CARKA = 145;
    public static final int E_CARKA = 146;
    public static final int ROVNA_SE = 147;
    public static final int CARKA_NAD_PISMENY = 148;
    private final String[] ZKRATKY_NA_PREPINAC = {"CES", "ENG", "DEU"};
    private final int TYP;
    private final boolean VYCHOZI;
    final HashMap<Integer, Character> STD_SADA;
    final HashMap<Integer, Character> SHIFT_SADA;
    final HashMap<Integer, Character> CAPS_SADA;
    final HashMap<Integer, Character> ALT_GR_SADA;
    final ArrayList<Integer> MRTVE_KLAVESY; // pro STD a CAPS
    final ArrayList<Integer> MRTVE_KLAVESY_SHIFT;
    final ArrayList<Integer> MRTVE_KLAVESY_ALT;
    final ArrayList<Integer> MRTVE_KLAVESY_CAPS;

    Rozvrzeni(int typ, boolean vychozi) {
        this.TYP = typ;
        this.VYCHOZI = vychozi;
        this.STD_SADA = new HashMap<>();
        this.SHIFT_SADA = new HashMap<>();
        this.CAPS_SADA = new HashMap<>();
        this.ALT_GR_SADA = new HashMap<>();
        this.MRTVE_KLAVESY = new ArrayList<>();
        this.MRTVE_KLAVESY_SHIFT = new ArrayList<>();
        this.MRTVE_KLAVESY_ALT = new ArrayList<>();
        this.MRTVE_KLAVESY_CAPS = new ArrayList<>();
    }
    
    public int getTyp() {
        return this.TYP;
    }
    
    public boolean jeVychozi() {
        return this.VYCHOZI;
    }
    
    public String getZkraktuNaKlavesu() {
        return this.ZKRATKY_NA_PREPINAC[this.TYP];
    }

    public HashMap<Integer, Character> getStandardniSadu() {
        return this.STD_SADA;
    }

    public HashMap<Integer, Character> getShiftSadu() {
        return this.SHIFT_SADA;
    }

    public HashMap<Integer, Character> getCapsSadu() {
        return this.CAPS_SADA;
    }

    public HashMap<Integer, Character> getAltGrSadu() {
        return this.ALT_GR_SADA;
    }
    
    public ArrayList<Integer> getMrtveKlavesy() {
        return this.MRTVE_KLAVESY;
    }
    
    public ArrayList<Integer> getMrtveKlavesyShift() {
        return this.MRTVE_KLAVESY_SHIFT;
    }
    
    public ArrayList<Integer> getMrtveKlavesyAltGr() {
        return this.MRTVE_KLAVESY_ALT;
    }

    public ArrayList<Integer> getMrtveKlavesyCaps() {
        return this.MRTVE_KLAVESY_CAPS;
    }

    protected abstract void setStandardniSadu();

    protected abstract void setShiftSadu();

    protected abstract void setCapsSadu();

    protected abstract void setAltGrSadu();
}
