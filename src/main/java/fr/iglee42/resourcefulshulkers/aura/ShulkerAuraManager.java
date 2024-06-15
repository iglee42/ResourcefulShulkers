package fr.iglee42.resourcefulshulkers.aura;

import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.network.data.AuraSyncPayload;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ShulkerAuraManager extends SavedData {

    private final Map<ChunkPos, ShulkerAura> manaMap = new HashMap<>();

    private int counter = 0;

    public ShulkerAuraManager() {
    }


    @Nonnull
    public static ShulkerAuraManager get(Level level) {
        if (level.isClientSide) {
            throw new RuntimeException("You can't access to client side!");
        }
        DimensionDataStorage storage = ((ServerLevel)level).getDataStorage();
        return storage.computeIfAbsent(new Factory<>(ShulkerAuraManager::new, ShulkerAuraManager::new, DataFixTypes.CHUNK), "auramanager");
    }

    @NotNull
    private ShulkerAura getAuraInternal(BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        return manaMap.computeIfAbsent(chunkPos, cp -> new ShulkerAura());
    }

    public int getAura(BlockPos pos) {
        ShulkerAura aura = getAuraInternal(pos);
        return aura.getAura();
    }

    public int extractAura(BlockPos pos, int extract) {
        ShulkerAura aura = getAuraInternal(pos);
        int present = aura.getAura();
        if (present > 0) {
            if (present >= extract) {
                aura.setAura(present - extract);
                setDirty();
                return extract;
            } else {
                aura.setAura(0);
                setDirty();
                return present;
            }
        } else {
            return 0;
        }
    }
    public int insertAura(BlockPos pos, int insert) {
        ShulkerAura aura = getAuraInternal(pos);
        int present = aura.getAura();
        if (present < ShulkerAura.MAX_AURA) {
            if (present <= ShulkerAura.MAX_AURA - insert) {
                aura.setAura(present + insert);
                setDirty();
                return insert;
            } else {
                aura.setAura(ShulkerAura.MAX_AURA);
                setDirty();

                return ShulkerAura.MAX_AURA - present;
            }
        } else {
            return 0;
        }
    }


    public void tick(Level level) {
        counter--;
        if (counter <= 0) {
            counter = 10;
                    // Synchronize the mana to the players in this world
                    level.players().forEach(player -> {
                        if (player instanceof ServerPlayer serverPlayer) {
                            //int playerMana = serverPlayer.getCapability(PlayerAuraProvider.PLAYER_AURA)
                                    //.map(PlayerAura::getAura)
                                    //.orElse(-1);
                            int chunkAura = getAura(serverPlayer.blockPosition());
                            if (chunkAura > 0) {
                                AdvancementHolder adv = serverPlayer.getServer().getAdvancements().get(new ResourceLocation(ResourcefulShulkers.MODID,"aura"));
                                Iterator<String> it = serverPlayer.getAdvancements().getOrStartProgress(adv).getRemainingCriteria().iterator();
                                while (it.hasNext()){
                                    String criteria = it.next();
                                    serverPlayer.getAdvancements().award(adv,criteria);
                                }
                            }
                            PacketDistributor.sendToPlayer(serverPlayer,new AuraSyncPayload(chunkAura));
                }
            });

        }
    }


    public ShulkerAuraManager(CompoundTag tag,HolderLookup.Provider provider) {
        ListTag list = tag.getList("aura", Tag.TAG_COMPOUND);
        for (Tag t : list) {
            CompoundTag manaTag = (CompoundTag) t;
            ShulkerAura aura = new ShulkerAura(manaTag.getInt("aura"));
            ChunkPos pos = new ChunkPos(manaTag.getInt("x"), manaTag.getInt("z"));
            manaMap.put(pos, aura);
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        ListTag list = new ListTag();
        manaMap.forEach((chunkPos, aura) -> {
            CompoundTag manaTag = new CompoundTag();
            manaTag.putInt("x", chunkPos.x);
            manaTag.putInt("z", chunkPos.z);
            manaTag.putInt("aura", aura.getAura());
            list.add(manaTag);
        });
        tag.put("aura", list);
        return tag;
    }


}