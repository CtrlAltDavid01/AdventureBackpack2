package com.darkona.adventurebackpack.common;

import com.darkona.adventurebackpack.block.TileAdventureBackpack;
import com.darkona.adventurebackpack.client.Visuals;
import com.darkona.adventurebackpack.entity.ai.EntityAIAvoidPlayerWithBackpack;
import com.darkona.adventurebackpack.init.ModFluids;
import com.darkona.adventurebackpack.init.ModNetwork;
import com.darkona.adventurebackpack.inventory.BackpackContainer;
import com.darkona.adventurebackpack.inventory.InventoryItem;
import com.darkona.adventurebackpack.network.CowAbilityPacket;
import com.darkona.adventurebackpack.util.LogHelper;
import com.darkona.adventurebackpack.util.Utils;
import com.darkona.adventurebackpack.util.Wearing;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;

import java.util.Iterator;
import java.util.List;

/**
 * Created on 12/10/2014
 *
 * @author Darkona
 * @see com.darkona.adventurebackpack.block.TileAdventureBackpack
 * @see com.darkona.adventurebackpack.item.ItemAdventureBackpack
 * @see com.darkona.adventurebackpack.block.BlockAdventureBackpack
 */
public class BackpackAbilities
{

    public static BackpackAbilities instance = new BackpackAbilities();

    /**
     * Checks if the selected String is a valid ability backpack colorName.
     *
     * @param colorName
     * @return Whether this is a valid ability backpack or not.
     */
    public static boolean hasAbility(String colorName)
    {
        for (String valid : validWearingBackpacks)
        {
            if (valid.equals(colorName))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Executes the ability of any given backpack, be it on the ground or be it on a player.
     *
     * @param player   An entity player, can be null in the case of the tile entity.
     * @param world    This is necessary, so get it from wherever you can inside the class you're calling this.
     * @param backpack An object representing a backpack, either in its ItemStack form or its TileEntity form.
     */
    public void executeAbility(EntityPlayer player, World world, Object backpack)
    {
        if (backpack instanceof ItemStack)
        {
            String colorName = ((ItemStack) backpack).getTagCompound().getString("colorName");
            try
            {
                //This is black magic and shouldn't be attempted by the faint of heart.
                this.getClass()
                        .getMethod("item" + colorName, EntityPlayer.class, World.class, ItemStack.class).
                        invoke(instance, player, world, backpack);
            } catch (Exception oops)
            {
                //NOBODY CARES
            }
        }

        if (backpack instanceof TileAdventureBackpack)
        {
            String colorName = ((TileAdventureBackpack) backpack).getColorName();
            try
            {
                    /*
                        This is witchery, witchery I say!
                        But seriously, if you want to know how this works just pay very close attention:
                        invoke will execute any method of a given class, okay? so this should be obvious.
                        Look at the names of the methods in this class and you'll figure it out.
                        You have to indicate exactly the classes that the method should use as parameters so
                        be very careful with "getMethod".
                     */
                this.getClass()
                        .getMethod("tile" + colorName, World.class, TileAdventureBackpack.class)
                        .invoke(instance, world, backpack);
            } catch (Exception oops)
            {
                //Seriously, nobody cares if this can't work, this is just so the game won't explode.
            }
        }

    }

    /**
     * These are the colorNames of the backpacks that have abilities when being worn.
     */
    private static String[] validWearingBackpacks = {
            "Bat","Squid","Pigman","Cactus", "Cow", "Pig", "Dragon", "Slime", "Chicken", "Wolf", "Ocelot", "Creeper", "Rainbow", "Melon"};

    /**
     * These are the colorNames of the backpacks that have abilities while being blocks. Note that not all the
     * backpacks that have particularities while in block form necessarily have abilities.
     *
     * @see com.darkona.adventurebackpack.block.BlockAdventureBackpack
     */
    private static String[] validTileBackpacks = {"Cactus"};

    /**
     * Detects if a player is under the rain. For detecting when it is Under The Sea (maybe to sing a nice Disney tune)
     * it won't work, there's a different method for that, isInWater
     *
     * @param player The player
     * @return True if the player is outside and it's raining.
     */
    private boolean isUnderRain(EntityPlayer player)
    {
        return player.worldObj.canLightningStrikeAt(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY),
                MathHelper.floor_double(player.posZ))
                || player.worldObj.canLightningStrikeAt(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY + player.height),
                MathHelper.floor_double(player.posZ));
    }

    public void itemBat(EntityPlayer player, World world, ItemStack backpack)
    {
        player.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 1, 2));
    }

    public void itemSquid(EntityPlayer player, World world, ItemStack backpack)
    {
        if(player.isInWater())
        {
            player.addPotionEffect(new PotionEffect(Potion.waterBreathing.getId(), 1, 2));
            player.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 1, 2));
        }
    }

    public void itemIronGolem(EntityPlayer player, World world, ItemStack backpack)
    {

    }

    public void itemPigman(EntityPlayer player, World world, ItemStack backpack)
    {
        player.addPotionEffect(new PotionEffect(Potion.fireResistance.getId(), 1, 0));
    }

    /**
     * Mirroring real life cactii, the Cactus Backpack fills with water slowly or rapidly depending where is the player.
     * If it's raining it will fill 1milibucket of water each tick.
     * If the player is in water it will fill 2milibuckets of water each tick.
     * The quantities can be combined.
     *
     * @param player   The player. No, seriously.
     * @param world    The world the player is in.
     * @param backpack The backpack the player is wearing. This should be rechecked like 20 times by now, so
     *                 I'm not checking.
     */
    public void itemCactus(EntityPlayer player, World world, ItemStack backpack)
    {
        //lastTime is in ticks for this backpack.
        int lastDropTime = (backpack.stackTagCompound.hasKey("lastTime")) ?
                backpack.stackTagCompound.getInteger("lastTime") - 1 : 5;
        int drops = 0;
        if (player.isInWater())
        {
            drops += 2;
        }
        if (isUnderRain(player))
        {
            drops += 1;
        }

        if (lastDropTime <= 0 && drops > 0)
        {
            InventoryItem inv = Wearing.getBackpackInv(player, true);
            FluidStack raindrop = new FluidStack(FluidRegistry.WATER, drops);
            inv.getLeftTank().fill(raindrop, true);
            inv.getRightTank().fill(raindrop, true);
            inv.saveChanges();
            lastDropTime = 5;
        }
        backpack.stackTagCompound.setInteger("lastTime", lastDropTime);
    }

    /**
     * The Pig Backpack will annoy you and your friends! This beautiful design by 豚, will do as the pigs do when they
     * are frolicking around in the green pastures and terrifying slaughterhouses of the Minecraft world, after a random
     * number of seconds. It's not so frequent as I'd like.
     * Translation for pigs: Oink oink oink Oink! squee oink oink Minecraft Oink oink. "Oink" oink oink.
     *
     * @param player   The player
     * @param world    The world object
     * @param backpack The backpack the player is wearing.
     */
    public void itemPig(EntityPlayer player, World world, ItemStack backpack)
    {
        //lastTime is in seconds for this backpack.
        int oinkTime = backpack.stackTagCompound.hasKey("lastTime") ?
                backpack.stackTagCompound.getInteger("lastTime") - 1 : Utils.secondsToTicks(5);
        if (oinkTime <= 0)
        {
            world.playSoundAtEntity(player, "mob.pig.say", 0.8f, 1f);
            oinkTime = Utils.secondsToTicks(world.rand.nextInt(61));
        }
        backpack.stackTagCompound.setInteger("lastTime", oinkTime);
    }

    /**
     * Squishy! The Slime Backpack has an incredibly useless "ability". Makes the player leave a slimy trail of
     * particles whenever he or she is running, and make that splishy splashy squishy sound on each step as well!.
     *
     * @param player
     * @param world
     * @param backpack
     */
    public void itemSlime(EntityPlayer player, World world, ItemStack backpack)
    {
        //lastTime is in Ticks for this backpack.
        if (player.onGround ) {
            if(player.isSprinting())
            {
                int slimeTime = backpack.stackTagCompound.hasKey("lastTime") ?
                        backpack.stackTagCompound.getInteger("lastTime") - 1 : 5;
                if (slimeTime <= 0)
                {
                    world.playSoundAtEntity(player, "mob.slime.small", 0.4F, (world.rand.nextFloat() - world.rand.nextFloat()) * 1F);
                    slimeTime = 5;
                }
                backpack.stackTagCompound.setInteger("lastTime", slimeTime);
            }
        }
    }

    /**
     * The Chicken Backpack will go and *plop* an egg for you randomly each so many seconds. It's very rare though.
     *
     * @param player
     * @param world
     * @param backpack
     */
    public void itemChicken(EntityPlayer player, World world, ItemStack backpack)
    {
        int eggTime = backpack.getTagCompound().hasKey("lastTime") ? backpack.getTagCompound().getInteger("lastTime") - 1 : Utils.secondsToTicks(5);
        if (eggTime <= 0)
        {
            player.playSound("mob.chicken.plop", 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.3F + 1.0F);
            if (!world.isRemote) player.dropItem(Items.egg, 1);
            eggTime = Utils.secondsToTicks(200 + 10 * world.rand.nextInt(10));
        }
        backpack.getTagCompound().setInteger("lastTime", eggTime);
    }

    /**
     * The Melon Backpack, like his cousin the Cactus Backpack, will fill itself, but with delicious
     * and refreshing Melon Juice, if the backpack is wet in any way.
     *
     * @param player
     * @param world
     * @param backpack
     */
    public void itemMelon(EntityPlayer player, World world, ItemStack backpack)
    {
        int lastDropTime = (backpack.stackTagCompound.hasKey("lastTime")) ? backpack.stackTagCompound.getInteger("lastTime") - 1 : 5;

        int drops = 0;
        if (player.isInWater())
        {
            drops += 5;
        }
        if (isUnderRain(player))
        {
            drops += 2;
        }

        if (lastDropTime <= 0 && drops > 0)
        {
            InventoryItem inv = Wearing.getBackpackInv(player, true);
            FluidStack raindrop = new FluidStack(ModFluids.melonJuice, drops);
            inv.getLeftTank().fill(raindrop, true);
            inv.getRightTank().fill(raindrop, true);
            inv.onInventoryChanged();
            lastDropTime = 5;
        }
        backpack.stackTagCompound.setInteger("lastTime", lastDropTime);
    }

    /**
     * The Dragon Backpack does something awesome.
     *
     * @param player
     * @param world
     * @param backpack
     */
    public void itemDragon(EntityPlayer player, World world, ItemStack backpack)
    {
        player.addPotionEffect(new PotionEffect(Potion.damageBoost.getId(), 1, 1));
        player.addPotionEffect(new PotionEffect(Potion.fireResistance.getId(), 1, 0));
        player.addPotionEffect(new PotionEffect(Potion.waterBreathing.getId(), 1, 0));
        player.addPotionEffect(new PotionEffect(Potion.regeneration.getId(), 1, 0));
    }

    /**
     * Sneaky! Scare your friends! Or your enemies!
     * Sneak on another player to make them jump in confusion as they think one of those green bastards is behind him/her.
     * You can only do it once every so often. A couple of minutes. Remember, you must be sneaking.
     *
     * @param player
     * @param world
     * @param backpack
     * @see com.darkona.adventurebackpack.handlers.PlayerEventHandler
     */
    public void itemCreeper(EntityPlayer player, World world, ItemStack backpack)
    {
        //lastTime is in seconds for this ability
        int pssstTime = (backpack.getTagCompound().hasKey("lastTime")) ? backpack.getTagCompound().getInteger("lastTime") - 1 : 20;

        if (pssstTime <= 0)
        {
            pssstTime = 0;
            if (player.isSneaking())
            {
                List<Entity> entities = player.worldObj.getEntitiesWithinAABBExcludingEntity(player,
                        AxisAlignedBB.getBoundingBox(player.posX, player.posY, player.posZ,
                                player.posX + 1.0D, player.posY + 1.0D,
                                player.posZ + 1.0D).expand(5.0D, 1.0D, 5.0D));
                if (entities.isEmpty())
                {
                    pssstTime -= 1;
                    return;
                }

                for (Entity entity : entities)
                {
                    if (entity instanceof EntityPlayer)
                    {
                        if (player.getDistanceToEntity(entity) <= 3)
                        {
                            world.playSoundAtEntity(player, "creeper.primed", 1.2F, 0.5F);
                            pssstTime = Utils.secondsToTicks(120);
                        }
                    }
                }
            }
        } else
        {
            pssstTime--;
        }
        backpack.getTagCompound().setInteger("lastTime", pssstTime);
    }

    /**
     * The Cow Backpack fills itself with milk when there is wheat in the backpack's inventory, but it will do so slowly
     * and will eat the wheat. It's like having a cow in your backpack. Each 16 wheat makes a bucket. It only happens
     * when it is being worn. For not-player related milk generation go get a cow. Moo!
     *
     * @param player
     * @param world
     * @param backpack
     */
    public void itemCow(EntityPlayer player, World world, ItemStack backpack)
    {
        IAdvBackpack inv = new InventoryItem(backpack);
        FluidStack milkStack = new FluidStack(ModFluids.milk, 1);
        BackpackContainer cont = null;
        if(player.openContainer != null && player.openContainer instanceof BackpackContainer){
            cont = (BackpackContainer)player.openContainer;
            if (cont.inventory instanceof InventoryItem && ((InventoryItem)cont.inventory).getParentItemStack().equals(backpack)){
                inv = cont.inventory;
            }
        }
        inv.openInventory();
        if (inv.getLeftTank().fill(milkStack, false) <= 0 && inv.getRightTank().fill(milkStack, false) <= 0)
        {
            return;
        }
        //Set Cow Properties
        NBTTagCompound cowProperties;
        int wheatConsumed = 0;
        int milkTime = - 1;
        if (inv.getExtendedProperties() != null)
        {
            cowProperties = inv.getExtendedProperties();
            if (cowProperties.hasKey("wheatConsumed"))
            {
                wheatConsumed = cowProperties.getInteger("wheatConsumed");
                milkTime = cowProperties.getInteger("milkTime") - 1;
            }
        } else
        {
            cowProperties = new NBTTagCompound();
        }

        int eatTime = inv.getLastTime() == 0 ? Utils.secondsToTicks(1) : inv.getLastTime() - 1;

        if(inv.hasItem(Items.wheat) && eatTime == 0 && milkTime <= 0)
        {
            LogHelper.info("Consuming Wheat in " + ((world.isRemote) ? "Client" : "Server"));
            inv.consumeInventoryItem(Items.wheat);
            if(!world.isRemote)
            {
                EntityPlayerMP playerMP = (EntityPlayerMP) player;
                ModNetwork.net.sendTo(new CowAbilityPacket.CowAbilityMessage(player.getPersistentID().toString(), CowAbilityPacket.CONSUME_WHEAT),playerMP);
            }
            wheatConsumed++;
        }

        int factor = 1;
        if(wheatConsumed == 16)
        {

            wheatConsumed = 0;
            milkTime = (1000 * factor) - factor;
            world.playSoundAtEntity(player, "mob.cow.say", 1f, 1f);
        }

        if (milkTime >= 0 && (milkTime % factor == 0))
        {
            if (inv.getLeftTank().fill(milkStack, true) <= 0)
            {
               inv.getRightTank().fill(milkStack, true);
            }
        }

        cowProperties.setInteger("wheatConsumed", wheatConsumed);
        cowProperties.setInteger("milkTime", milkTime);
        inv.setExtendedProperties(cowProperties);
        inv.setExtendedProperties(cowProperties);
        inv.setLastTime(eatTime);
        if(player.openContainer!=null)player.openContainer.detectAndSendChanges();
        inv.saveChanges();
    }

    /**
     * The Wolf Backpack is a handy one if you're out in the wild. It checks around for any wolves that may lurk around.
     * If any of them gets mad at you, it will smell the scent of it's kin on you and promptly forget about the whole
     * deal. Smelling like dog is awesome.
     *
     * @param player   the player
     * @param world    the world
     * @param backpack the backpack
     */
    @SuppressWarnings("unchecked")
    public void itemWolf(EntityPlayer player, World world, ItemStack backpack)
    {
        //lastTime is in Ticks for this backpack
        int lastCheckTime = (backpack.getTagCompound().hasKey("lastTime")) ? backpack.getTagCompound().getInteger("lastTime") - 1 : 20;

        if (lastCheckTime <= 0)
        {
            List<EntityWolf> wolves = player.worldObj.getEntitiesWithinAABB(
                    EntityWolf.class,
                    AxisAlignedBB.getBoundingBox(player.posX, player.posY, player.posZ,
                            player.posX + 1.0D, player.posY + 1.0D,
                            player.posZ + 1.0D).expand(16.0D, 4.0D, 16.0D));
            if (wolves.isEmpty()) return;

            for (EntityWolf wolf : wolves)
            {
                if (wolf.isAngry() && wolf.getAttackTarget() == player)
                {
                    wolf.setAngry(wolf.isAngry() ? false : false);
                    wolf.setAttackTarget(null);
                    wolf.setRevengeTarget(null);
                    Iterator<?> i2 = wolf.targetTasks.taskEntries.iterator();
                    while (i2.hasNext())
                    {
                        ((EntityAIBase) i2.next()).resetTask();
                    }
                }
            }
            lastCheckTime = 20;
        } else
        {
            lastCheckTime--;
        }
        backpack.getTagCompound().setInteger("lastTime", lastCheckTime);
    }

    /**
     * The Blaze Backpack will make you inmune to fire and lava and burning and heat and... not really. You're supposed
     * to die a fiery death if you are not careful, but this backpack will protect you against those burning fire
     * elemental inhabitants of the Nether. Any blast of fire directed your way will be stopped, deflected or whatever.
     *
     * @param player
     * @param world
     * @param backpack
     */
    public void itemBlaze(EntityPlayer player, World world, ItemStack backpack)
    {

    }

    /**
     * Like actual Ocelots and Cats, the Ocelot Backpack will scare the hell out of Creepers, so they won't creep on you
     * while you're busy doing something else, paying no attention whatsoever at your surroundings like a mindless chicken.
     *
     * @param player
     * @param world
     * @param backpack
     */
    @SuppressWarnings("unchecked")
    public void itemOcelot(EntityPlayer player, World world, ItemStack backpack)
    {
        //lastTime in this backpack is in Ticks.
        int lastCheckTime = (backpack.getTagCompound().hasKey("lastTime")) ? backpack.getTagCompound().getInteger("lastTime") - 1 : 20;

        if (lastCheckTime <= 0)
        {
            List<EntityCreeper> creepers = player.worldObj.getEntitiesWithinAABB(
                    EntityCreeper.class,
                    AxisAlignedBB.getBoundingBox(player.posX, player.posY, player.posZ,
                            player.posX + 1.0D, player.posY + 1.0D,
                            player.posZ + 1.0D).expand(16.0D, 4.0D, 16.0D));

            for (EntityCreeper creeper : creepers)
            {
                boolean set = true;
                EntityAIAvoidPlayerWithBackpack task = new EntityAIAvoidPlayerWithBackpack(creeper, EntityPlayer.class, 10.0F, 1.0, 1.3, "Ocelot");

                for (Object entry : creeper.tasks.taskEntries)
                {
                    if (((EntityAITasks.EntityAITaskEntry) entry).action instanceof EntityAIAvoidPlayerWithBackpack)
                    {
                        set = false;
                    }
                }

                if (set)
                {
                    //System.out.println("Found creeper who doesn't know to fear the backpack, making it a pussy now");
                    creeper.tasks.addTask(3, task);
                }
            }
            lastCheckTime = 20;
        }
        backpack.getTagCompound().setInteger("lastTime", lastCheckTime);
    }

    public void itemRainbow(EntityPlayer player, World world, ItemStack backpack)
    {
        int noteTime = backpack.getTagCompound().getInteger("lastTime") - 1;
        if (noteTime >= 0 && noteTime < Utils.secondsToTicks(147))
        {
            player.setSprinting(true);
            player.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), 1, 2));
            player.addPotionEffect(new PotionEffect(Potion.jump.getId(), 1, 2));
            if (noteTime % 2 == 0)
            {
                Visuals.NyanParticles(player, world);
            }
        }
        backpack.getTagCompound().setInteger("lastTime", noteTime);
    }
    /* ==================================== TILE ABILITIES ==========================================*/

    private void fillWithRain(World world, TileAdventureBackpack backpack, FluidStack fluid, int time){
        if (world.isRaining() && world.canBlockSeeTheSky(backpack.xCoord, backpack.yCoord, backpack.zCoord))
        {
            int dropTime = backpack.getLastTime() - 1;
            if (dropTime <= 0)
            {
                backpack.getRightTank().fill(fluid, true);
                backpack.getLeftTank().fill(fluid, true);
                dropTime = time;
                backpack.markDirty();
            }
            backpack.setLastTime(dropTime);
        }
    }

    /**
     * Like real life cactii, this backpack will fill slowly while it's raining with refreshing water.
     *
     * @param world
     * @param backpack
     */
    public void tileCactus(World world, TileAdventureBackpack backpack)
    {
       fillWithRain(world, backpack,new FluidStack(FluidRegistry.WATER, 2), 5 );
    }


    public void tileMelon(World world, TileAdventureBackpack backpack)
    {
        fillWithRain(world, backpack,new FluidStack(ModFluids.melonJuice, 2), 5 );
    }

}
