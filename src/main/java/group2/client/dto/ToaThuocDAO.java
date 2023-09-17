package group2.client.dto;


public class ToaThuocDAO {
    private int thuocID;
    private int quantity;
    private int sang;
    private int chieu;
    private int trua;
    private int toi;

    public ToaThuocDAO(int thuocID, int quantity, int sang, int trua, int chieu, int toi) {
        this.thuocID = thuocID;
        this.quantity = quantity;
        this.sang = sang;
        this.trua = trua;
        this.chieu = chieu;
        this.toi = toi;
    }
    public int getThuocID() {
        return thuocID;
    }

    public void setThuocID(int thuocID) {
        this.thuocID = thuocID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSang() {
        return sang;
    }

    public void setSang(int sang) {
        this.sang = sang;
    }

    public int getChieu() {
        return chieu;
    }

    public void setChieu(int chieu) {
        this.chieu = chieu;
    }

    public int getTrua() {
        return trua;
    }

    public void setTrua(int trua) {
        this.trua = trua;
    }

    public int getToi() {
        return toi;
    }

    public void setToi(int toi) {
        this.toi = toi;
    }
}

