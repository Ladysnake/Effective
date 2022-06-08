package ladysnake.effective.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import ladysnake.effective.client.Effective;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class DonateToast implements Toast {
    private static final Identifier TEXTURE = new Identifier(Effective.MODID, "textures/gui/donate_toast.png");

    public static void add() {
        ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
        DonateToast toast = toastManager.getToast(DonateToast.class, Toast.TYPE);
        if (toast == null) {
            toastManager.add(new DonateToast());
        }
    }

    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        manager.drawTexture(matrices, 0, 0, 0, 0, getWidth(), getHeight());
        manager.getClient().textRenderer.draw(matrices, Text.literal("Wish to support Effective?"), 34, 7, -256);
        manager.getClient().textRenderer.draw(matrices, Text.literal("Get cool cosmetics for only 5€!"), 34, 18, -1);
        manager.getClient().textRenderer.draw(matrices, Text.literal("More info: illuminations.uuid.gg/donators").setStyle(Style.EMPTY.withColor(Formatting.GREEN)), 34, 29, -1);
        return MinecraftClient.getInstance().currentScreen instanceof TitleScreen ? Visibility.SHOW : Visibility.HIDE;
    }

    @Override
    public Object getType() {
        return TYPE;
    }

    @Override
    public int getWidth() {
        return 240;
    }

    @Override
    public int getHeight() {
        return 43;
    }
}
