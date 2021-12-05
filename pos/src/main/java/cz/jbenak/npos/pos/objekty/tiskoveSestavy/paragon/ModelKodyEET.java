package cz.jbenak.npos.pos.objekty.tiskoveSestavy.paragon;

public class ModelKodyEET {
    private String bkp;
    private String pkp_fik;

    public String getBkp() {
        return bkp;
    }

    public void setBkp(String bkp) {
        this.bkp = "BKP: " + bkp;
    }

    public String getPkp_fik() {
        return pkp_fik;
    }

    public void setPkp_fik(boolean fik, String pkp_fik) {
        this.pkp_fik = (fik ? "FIK: " : "PKP: ") + pkp_fik;
    }
}
