package me.jorva.woodenbuckets;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class ItemWoodenBucket extends Item {

    public Block fillBlock;

    public ItemWoodenBucket(Block block) {
        this.maxStackSize = 1;
        this.fillBlock = block;
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        boolean flag = this.fillBlock == Blocks.air;
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, flag);

        if (movingobjectposition == null) {
            return stack;
        } else {
            FillBucketEvent event = new FillBucketEvent(player, stack, world, movingobjectposition);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return stack;
            }

            if (event.getResult() == Event.Result.ALLOW) {
                if (player.capabilities.isCreativeMode) {
                    return stack;
                }

                if (--stack.stackSize <= 0) {
                    return event.result;
                }

                if (!player.inventory.addItemStackToInventory(event.result)) {
                    player.dropPlayerItemWithRandomChoice(event.result, false);
                }

                return stack;
            }
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                if (!world.canMineBlock(player, i, j, k)) {
                    return stack;
                }

                if (flag) {
                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, stack)) {
                        return stack;
                    }

                    Material material = world.getBlock(i, j, k).getMaterial();
                    int l = world.getBlockMetadata(i, j, k);

                    if (material == Material.water && l == 0) {
                        world.setBlockToAir(i, j, k);
                        return new ItemStack(WoodenBuckets.itemBucketFilled,1);
                    }

                    if (material == Material.lava && l == 0) {
                        if (!world.isRemote) {
                            player.addChatMessage(new ChatComponentText(LanguageRegistry.instance().getStringLocalization("woodenbuckets:burnthands")));
                            player.setFire(5);
//                            return new ItemStack(Blocks.air,1); //TODO: FIX THIS
//                            return null;

                        }

                    }
                } else {
                    if (this.fillBlock == Blocks.air) {
                        return new ItemStack(WoodenBuckets.itemBucketEmpty);
                    }

                    if (movingobjectposition.sideHit == 0) {
                        --j;
                    }

                    if (movingobjectposition.sideHit == 1) {
                        ++j;
                    }

                    if (movingobjectposition.sideHit == 2) {
                        --k;
                    }

                    if (movingobjectposition.sideHit == 3) {
                        ++k;
                    }

                    if (movingobjectposition.sideHit == 4) {
                        --i;
                    }

                    if (movingobjectposition.sideHit == 5) {
                        ++i;
                    }

                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, stack)) {
                        return stack;
                    }

                    if (this.tryPlaceContainedLiquid(world, i, j, k) && !player.capabilities.isCreativeMode) {
                        return new ItemStack(WoodenBuckets.itemBucketEmpty);
                    }
                }
            }

            return stack;
        }
    }

    /**
     * Attempts to place the liquid contained inside the bucket.
     */
    public boolean tryPlaceContainedLiquid(World world, int x, int y, int z) {
        if (this.fillBlock == Blocks.air) {
            return false;
        } else {
            Material material = world.getBlock(x, y, z).getMaterial();
            boolean flag = !material.isSolid();

            if (!world.isAirBlock(x, y, z) && !flag) {
                return false;
            } else {
                if (world.provider.isHellWorld && this.fillBlock == Blocks.flowing_water) {
                    world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                    for (int l = 0; l < 8; ++l) {
                        world.spawnParticle("largesmoke", (double) x + Math.random(), (double) y + Math.random(), (double) z + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                } else {
                    if (!world.isRemote && flag && !material.isLiquid()) {
                        world.func_147480_a(x, y, z, true);
                    }

                    world.setBlock(x, y, z, this.fillBlock, 0, 3);
                }

                return true;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private IIcon icon_empty, icon_filled;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iIconRegister) {
        icon_empty = iIconRegister.registerIcon("woodenbuckets:bucket_empty");
        icon_filled = iIconRegister.registerIcon("woodenbuckets:bucket_filled");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        ItemWoodenBucket item = (ItemWoodenBucket) stack.getItem();
        if (item.fillBlock != Blocks.air)
            return icon_filled;
        else
            return icon_empty;
    }

    @Override
    public IIcon getIconFromDamage(int p_77617_1_) {
        if (this.fillBlock != Blocks.air)
            return icon_filled;
        else
            return icon_empty;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer  player , World world, int x, int y, int z, int side, float hX, float hY, float hZ) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (!world.isRemote) {
            if (itemStack.getItem() == this) {
                if (te instanceof IFluidHandler) {
                    ItemWoodenBucket item = (ItemWoodenBucket) itemStack.getItem();
                    boolean empty = item.fillBlock == Blocks.air;
                    IFluidHandler fluidHandler = (IFluidHandler) te;
                    if (empty) {
                        FluidStack fs = fluidHandler.drain(ForgeDirection.getOrientation(side), 1000, false);
                        if (fs != null) {
                            if (fs.getFluid() == FluidRegistry.WATER) {
                                fluidHandler.drain(ForgeDirection.getOrientation(side), 1000, true);
                                player.setCurrentItemOrArmor(0, new ItemStack(WoodenBuckets.itemBucketFilled));
                                return true;
                            }
                        }
                    } else {
                        int i = fluidHandler.fill(ForgeDirection.getOrientation(side), new FluidStack(FluidRegistry.WATER, 1000), false);
                        if (i > 0) {
                            fluidHandler.fill(ForgeDirection.getOrientation(side), new FluidStack(FluidRegistry.WATER, 1000), true);
                            player.setCurrentItemOrArmor(0, new ItemStack(WoodenBuckets.itemBucketEmpty));

                            return true;
                        }
                    }
                }
            }
        }
        return true;
    }

}
