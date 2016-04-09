package me.jorva.woodenbuckets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemMortarPestle extends Item{

    public ItemMortarPestle(){
        setMaxStackSize(1);
    }


    @SideOnly(Side.CLIENT)
    private IIcon icon;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iIconRegister) {
        icon = iIconRegister.registerIcon("woodenbuckets:mortarpestle");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return icon;
    }

    @Override
    public IIcon getIconFromDamage(int p_77617_1_) {
        return icon;
    }


}
