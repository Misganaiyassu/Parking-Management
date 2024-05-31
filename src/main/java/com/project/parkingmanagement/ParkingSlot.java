package com.project.parkingmanagement;

public class ParkingSlot {
//    private int slotId;
//    private boolean isOccupied;
//
//    public ParkingSlot(int slotId, boolean isOccupied) {
//        this.slotId = slotId;
//        this.isOccupied = isOccupied;
//    }
//
//    public int getSlotId() {
//        return slotId;
//    }
//
//    public boolean isOccupied() {
//        return isOccupied;
//    }
//
//    public void setOccupied(boolean occupied) {
//        isOccupied = occupied;
//    }

    private int slotId;
    private boolean occupied;
    private String licensePlate;

    public ParkingSlot(int slotId, boolean occupied) {
        this.slotId = slotId;
        this.occupied = occupied;
        this.licensePlate = null;
    }

    public int getSlotId() {
        return slotId;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
