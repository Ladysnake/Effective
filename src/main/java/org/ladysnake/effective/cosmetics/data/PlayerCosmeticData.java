package org.ladysnake.effective.cosmetics.data;

import com.google.gson.JsonElement;

public class PlayerCosmeticData {
	private final int red1;
	private final int green1;
	private final int blue1;
	private final int red2;
	private final int green2;
	private final int blue2;
	private final String aura;
	private final String overhead;
	private final String pet;

	public PlayerCosmeticData(JsonElement aura, JsonElement color1, JsonElement color2, JsonElement overhead, JsonElement pet) {
		if (aura.isJsonNull()) {
			this.aura = null;
		} else {
			this.aura = aura.getAsString();
		}
		if (color1.isJsonNull()) {
			this.red1 = 0;
			this.green1 = 0;
			this.blue1 = 0;
		} else {
			this.red1 = Integer.valueOf(color1.getAsString().substring(1, 3), 16);
			this.green1 = Integer.valueOf(color1.getAsString().substring(3, 5), 16);
			this.blue1 = Integer.valueOf(color1.getAsString().substring(5), 16);
		}
		if (color2.isJsonNull()) {
			this.red2 = 0;
			this.green2 = 0;
			this.blue2 = 0;
		} else {
			this.red2 = Integer.valueOf(color2.getAsString().substring(1, 3), 16);
			this.green2 = Integer.valueOf(color2.getAsString().substring(3, 5), 16);
			this.blue2 = Integer.valueOf(color2.getAsString().substring(5), 16);
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
	}

	public String getAura() {
		return aura;
	}

	public int getColor1Red() {
		return red1;
	}

	public int getColor1Green() {
		return green1;
	}

	public int getColor1Blue() {
		return blue1;
	}

	public int getColor2Red() {
		return red2;
	}

	public int getColor2Green() {
		return green2;
	}

	public int getColor2Blue() {
		return blue2;
	}


	public String getOverhead() {
		return overhead;
	}

	public String getPet() {
		return pet;
	}
}
