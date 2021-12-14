package ladysnake.effective.client.data;

import com.google.gson.JsonElement;

public class PlayerCosmeticData {
    private final int colorRed;
    private final int colorGreen;
    private final int colorBlue;
    private String aura;
    private String overhead;
    private boolean drip;
    private String pet;

    public PlayerCosmeticData(JsonElement aura, JsonElement color, JsonElement overhead, JsonElement drip, JsonElement pet) {
        if (aura.isJsonNull()) {
            this.aura = null;
        } else {
            this.aura = aura.getAsString();
        }
        if (color.isJsonNull()) {
            this.colorRed = 0;
            this.colorGreen = 0;
            this.colorBlue = 0;
        } else {
            this.colorRed = Integer.valueOf(color.getAsString().substring(1, 3), 16);
            this.colorGreen = Integer.valueOf(color.getAsString().substring(3, 5), 16);
            this.colorBlue = Integer.valueOf(color.getAsString().substring(5), 16);
        }
        if (overhead.isJsonNull()) {
            this.overhead = null;
        } else {
            this.overhead = overhead.getAsString();
        }
        if (pet.isJsonNull()) {
            this.pet = null;
        } else {
            this.pet = pet.getAsString();
        }
        if (drip.isJsonNull()) {
            this.drip = false;
        } else {
            this.drip = drip.getAsBoolean();
        }
    }

    public String getAura() {
        return aura;
    }

    public int getColorRed() {
        return colorRed;
    }

    public int getColorBlue() {
        return colorBlue;
    }

    public int getColorGreen() {
        return colorGreen;
    }

    public boolean isDrip() {
        return drip;
    }

    public String getOverhead() {
        return overhead;
    }

    public String getPet() {
        return pet;
    }
}
