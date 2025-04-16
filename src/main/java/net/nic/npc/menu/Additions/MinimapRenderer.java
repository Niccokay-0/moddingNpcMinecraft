package net.nic.npc.menu.Additions;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.MapColor;

public class MinimapRenderer {
    public static final int SIZE_PX = 128;

    public static void renderMinimap(GuiGraphics graphics, Level world, BlockPos center, float scale, int screenWidth, int screenHeight, net.minecraft.client.gui.Font font) {
        if (world == null) return;

        int detailStep = 2;
        float pixelSize = detailStep * scale;
        int displaySize = SIZE_PX;

        int startX = screenWidth - displaySize - 48;
        int startY = (screenHeight / 2) - (displaySize / 2);

        graphics.fill(startX - 1, startY - 1, startX + displaySize + 1, startY + displaySize + 1, 0xFF000000);

        int blocksPerSide = (int)(displaySize / scale);
        BlockPos topLeft = center.offset(-blocksPerSide / 2, 0, -blocksPerSide / 2);
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int dx = 0; dx < blocksPerSide; dx += detailStep) {
            for (int dz = 0; dz < blocksPerSide; dz += detailStep) {
                int worldX = topLeft.getX() + dx;
                int worldZ = topLeft.getZ() + dz;
                int surfaceY = world.getHeight(Heightmap.Types.WORLD_SURFACE, worldX, worldZ);
                mutablePos.set(worldX, surfaceY - 1, worldZ);
                BlockState state = world.getBlockState(mutablePos);
                MapColor color = state.getMapColor(world, mutablePos);

                int colorRGB;
                if (color != null) {
                    int mapCol = color.col;
                    int r = (mapCol >> 16) & 0xFF;
                    int g = (mapCol >> 8) & 0xFF;
                    int b = mapCol & 0xFF;
                    colorRGB = 0xFF000000 | (r << 16) | (g << 8) | b;
                } else {
                    colorRGB = 0xFF3355FF;
                }

                float fx = startX + dx * scale;
                float fy = startY + dz * scale;
                int x = Math.round(fx);
                int y = Math.round(fy);
                int size = Math.round(pixelSize);

                graphics.fill(x, y, x + size, y + size, colorRGB);

                if ((worldX & 15) == 0 || (worldZ & 15) == 0) {
                    graphics.fill(x, y, x + size, y + size, 0x99FFFFFF);
                }
            }
        }

        int centerX = startX + displaySize / 2;
        int centerY = startY + displaySize / 2;
        graphics.fill(centerX - 2, centerY - 2, centerX + 2, centerY + 2, 0xFFFF00FF);

        graphics.drawString(font, "N", startX + displaySize / 2 - 3, startY - 10, 0xFFFFFF);
        graphics.drawString(font, "S", startX + displaySize / 2 - 3, startY + displaySize + 2, 0xFFFFFF);
        graphics.drawString(font, "W", startX - 10, startY + displaySize / 2 - 3, 0xFFFFFF);
        graphics.drawString(font, "E", startX + displaySize + 2, startY + displaySize / 2 - 3, 0xFFFFFF);
    }
}
