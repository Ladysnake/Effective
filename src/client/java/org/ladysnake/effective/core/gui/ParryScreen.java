package org.ladysnake.effective.core.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.awt.*;

public class ParryScreen extends Screen {
	public ParryScreen() {
		super(Text.literal(""));
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		context.fillGradient(0, 0, this.width, this.height, 0xAAFFFFFF, 0xAAFFFFFF);

		super.render(context, mouseX, mouseY, delta);
	}

}
