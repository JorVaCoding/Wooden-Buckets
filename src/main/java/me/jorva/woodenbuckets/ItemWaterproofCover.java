package me.jorva.woodenbuckets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemWaterproofCover extends Item{
    @SideOnly(Side.CLIENT)
    private IIcon icon;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iIconRegister) {
        icon = iIconRegister.registerIcon("woodenbuckets:waterproofCover");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return icon;
    }
}
