package me.jorva.woodenbuckets;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@Mod(modid = "woodenbuckets", version = "1.0", name = "Wooden Buckets")
public class WoodenBuckets {

    public static ItemWoodenBucket itemBucketEmpty;
    public static ItemWoodenBucket itemBucketFilled;

    public static ItemMortarPestle itemMortarPestle;
    public static ItemWaterproofCover itemWaterproofCover;
    
    @EventHandler
    public void init(FMLInitializationEvent event){
        itemBucketEmpty = (ItemWoodenBucket) new ItemWoodenBucket(Blocks.air).setUnlocalizedName("woodenBucket");
        itemBucketFilled = (ItemWoodenBucket) new ItemWoodenBucket(Blocks.flowing_water).setUnlocalizedName("woodenBucketFilled");
        GameRegistry.registerItem(itemBucketEmpty, "woodenBucket");
        GameRegistry.registerItem(itemBucketFilled, "woodenBucketFilled");

        itemMortarPestle = (ItemMortarPestle) new ItemMortarPestle().setUnlocalizedName("mortarpestle");
        itemWaterproofCover = (ItemWaterproofCover) new ItemWaterproofCover().setUnlocalizedName("waterproofCover");
        GameRegistry.registerItem(itemMortarPestle, "mortarpestel");
        GameRegistry.registerItem(itemWaterproofCover, "waterproofCover");

        GameRegistry.addRecipe(new ItemStack(itemMortarPestle, 1), new Object[]{
                "  s","wsw"," w ",
                's', Items.stick, 'w', Blocks.planks
        });

        GameRegistry.addRecipe(new ItemStack(itemWaterproofCover, 3), new Object[]{
                "sss"," m ","sss",
                's', Items.wheat_seeds, 'm', itemMortarPestle
        });

        GameRegistry.addRecipe(new ItemStack(itemMortarPestle, 1), new Object[]{
                "  s","wsw"," w ",
                's', Items.stick, 'w', Blocks.planks
        });

        GameRegistry.addRecipe(new ItemStack(itemBucketEmpty, 1), new Object[]{
                "w w","lwl"," l ",
                'w', itemWaterproofCover, 'l', Blocks.log
        });


    }
}
