package srki2k.tweakedexcavation.mixin;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityBucketWheel;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityExcavator;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srki2k.tweakedexcavation.api.crafting.TweakedExcavatorHandler;
import srki2k.tweakedexcavation.api.ihelpers.IExcavatorAddons;
import srki2k.tweakedlib.api.powertier.PowerTier;

@Mixin(value = TileEntityExcavator.class, remap = false)
public abstract class MixinTileEntityExcavator extends TileEntityMultiblockMetal<TileEntityExcavator, IMultiblockRecipe> implements IExcavatorAddons {


    //Shadow Variables
    @Shadow
    public boolean active;


    //Shadow Methods
    @Shadow
    abstract ItemStack digBlocksInTheWay(TileEntityBucketWheel wheel);


    public MixinTileEntityExcavator(MultiblockHandler.IMultiblock mutliblockInstance, int[] structureDimensions, int energyCapacity, boolean redstoneControl) {
        super(mutliblockInstance, structureDimensions, energyCapacity, redstoneControl);
    }

    @Unique
    @Override
    public void initEnergyStorage() {
        PowerTier powerTier =
                TweakedExcavatorHandler.getPowerTier(
                        this.getWorld(), this.getPos().getX() >> 4, this.getPos().getZ() >> 4);

        energyStorage.setCapacity(powerTier.getCapacity());
        energyStorage.setLimitReceive(Integer.min(powerTier.getRft() * 2, powerTier.getCapacity()));
        energyStorage.setMaxExtract(powerTier.getRft());
    }


    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstructed(CallbackInfo ci) {
        energyStorage.setCapacity(Integer.MAX_VALUE);
        energyStorage.setLimitTransfer(0);
    }

    /**
     * @author Srki_2K
     * @reason Implementing Power Tiers and custom block extraction
     */
    @Overwrite
    public void update() {
        super.update();

        if (this.isDummy()) {
            return;
        }

        BlockPos wheelPos = this.getBlockPosForPos(31);
        if (this.world.isRemote && !this.world.isBlockLoaded(wheelPos)) {
            return;
        }

        TileEntity center = this.world.getTileEntity(wheelPos);
        if (!(center instanceof TileEntityBucketWheel)) {
            return;
        }
        float rot;
        int target = -1;
        TileEntityBucketWheel wheel = (TileEntityBucketWheel) center;
        EnumFacing fRot = this.facing.rotateYCCW();
        if (wheel.facing == fRot) {
            if (this.active != wheel.active) {
                this.world.addBlockEvent(wheel.getPos(), wheel.getBlockType(), 0, this.active ? 1 : 0);
            }

            rot = wheel.rotation;
            if (rot % 45.0F > 40.0F) {
                target = Math.round(rot / 360.0F * 8.0F) % 8;
            }
        }

        if (energyStorage.getMaxEnergyStored() == Integer.MAX_VALUE) {
            initEnergyStorage();
        }

        int consumed;
        if (wheel.facing != fRot || wheel.mirrored != this.mirrored) {
            for (int h = -3; h <= 3; ++h) {
                for (consumed = -3; consumed <= 3; ++consumed) {
                    TileEntity te = this.world.getTileEntity(wheelPos.add(0, h, 0).offset(this.facing, consumed));
                    if (te instanceof TileEntityBucketWheel) {
                        ((TileEntityBucketWheel) te).facing = fRot;
                        ((TileEntityBucketWheel) te).mirrored = this.mirrored;
                        te.markDirty();
                        ((TileEntityBucketWheel) te).markContainingBlockForUpdate(null);
                        this.world.addBlockEvent(te.getPos(), te.getBlockType(), 255, 0);
                    }
                }
            }
        }

        if (!this.isRSDisabled()) {

            ExcavatorHandler.MineralMix mineral = ExcavatorHandler.getRandomMineral(this.world, wheelPos.getX() >> 4, wheelPos.getZ() >> 4);

            consumed = energyStorage.getLimitExtract();

            int extracted = this.energyStorage.extractEnergy(consumed, true);

            if (extracted >= consumed) {
                this.energyStorage.extractEnergy(consumed, false);
                this.active = true;
                if (target >= 0) {
                    int targetDown = (target + 4) % 8;
                    NBTTagCompound packet = new NBTTagCompound();
                    if (wheel.digStacks.get(targetDown).isEmpty()) {
                        ItemStack blocking = this.digBlocksInTheWay(wheel);

                        BlockPos lowGroundPos = wheelPos.add(0, -5, 0);

                        if (!blocking.isEmpty()) {
                            wheel.digStacks.set(targetDown, blocking);
                            wheel.markDirty();
                            this.markContainingBlockForUpdate(null);
                        }

                        if (mineral != null) {
                            ItemStack ore = mineral.getRandomOre(Utils.RAND);
                            float configChance = Utils.RAND.nextFloat();
                            float failChance = Utils.RAND.nextFloat();
                            if (!ore.isEmpty() && (double) configChance > Config.IEConfig.Machines.excavator_fail_chance && failChance > mineral.failChance) {
                                wheel.digStacks.set(targetDown, ore);
                                wheel.markDirty();
                                this.markContainingBlockForUpdate(null);
                            }

                            ExcavatorHandler.depleteMinerals(this.world, wheelPos.getX() >> 4, wheelPos.getZ() >> 4);
                        }

                        if (!wheel.digStacks.get(targetDown).isEmpty()) {
                            packet.setInteger("fill", targetDown);
                            packet.setTag("fillStack", wheel.digStacks.get(targetDown).writeToNBT(new NBTTagCompound()));
                        }
                    }

                    if (!wheel.digStacks.get(target).isEmpty()) {
                        this.doProcessOutput(wheel.digStacks.get(target).copy());
                        Block b = Block.getBlockFromItem(wheel.digStacks.get(target).getItem());
                        if (b != null && b != Blocks.AIR) {
                            wheel.particleStack = wheel.digStacks.get(target).copy();
                        }

                        wheel.digStacks.set(target, ItemStack.EMPTY);
                        wheel.markDirty();
                        this.markContainingBlockForUpdate(null);
                        packet.setInteger("empty", target);
                    }

                    if (!packet.isEmpty()) {
                        ImmersiveEngineering.packetHandler.sendToAll(new MessageTileSync(wheel, packet));
                    }
                }
            }
        }

        if (this.active) {
            this.active = false;
        }
    }


}
