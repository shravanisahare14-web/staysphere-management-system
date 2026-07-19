package model;

import java.io.Serializable;

public class Guest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String guestId;
    private String name;
    private String phoneNumber;
    private String email;
    private String idProof;
    private String idProofNumber;
    private String address;

    public Guest() {}

    public Guest(String guestId, String name, String phoneNumber, 
                 String email, String idProof, String idProofNumber, String address) {
        this.guestId = guestId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.idProof = idProof;
        this.idProofNumber = idProofNumber;
        this.address = address;
    }

    public String getGuestId() { return guestId; }
    public void setGuestId(String guestId) { this.guestId = guestId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getIdProof() { return idProof; }
    public void setIdProof(String idProof) { this.idProof = idProof; }
    public String getIdProofNumber() { return idProofNumber; }
    public void setIdProofNumber(String idProofNumber) { this.idProofNumber = idProofNumber; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
