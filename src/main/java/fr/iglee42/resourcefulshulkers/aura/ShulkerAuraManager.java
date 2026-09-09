package fr.iglee42.resourcefulshulkers.aura;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.advancements.RSAdvancements;
import fr.iglee42.resourcefulshulkers.network.data.AuraSyncPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static fr.iglee42.resourcefulshulkers.RSIds.MODID;

@EventBusSubscriber(modid = MODID)
public final class ShulkerAuraManager extends SavedData {

    private final Map<ChunkPos, ShulkerAura> auraByChunk = new HashMap<>();

    private int counter = 0;

    private ShulkerAuraManager() {}

    public ShulkerAuraManager(CompoundTag tag, HolderLookup.Provider provider) {
        ListTag list = tag.getList("entries", Tag.TAG_COMPOUND);
        for (Tag t : list) {
            Entry.CODEC.parse(NbtOps.INSTANCE, t).resultOrPartial(ResourcefulShulkers.LOGGER::error).ifPresent(entry -> auraByChunk.put(entry.pos(), entry.aura()));
        }
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        ListTag list = new ListTag();
        auraByChunk.forEach((chunkPos, aura) -> {
            Entry entry = new Entry(chunkPos, aura);
            Entry.CODEC.encodeStart(NbtOps.INSTANCE, entry).resultOrPartial(ResourcefulShulkers.LOGGER::error).ifPresent(list::add);
        });
        tag.put("entries", list);
        return tag;
    }

    @SubscribeEvent
    public static void onWorldTick(LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide)
            return;
        ShulkerAuraManager manager = ShulkerAuraManager.get(event.getLevel());
        manager.tick(event.getLevel());
    }

    @Nonnull
    public static ShulkerAuraManager get(Level level) {
        if (level.isClientSide) {
            throw new RuntimeException("Shulker Aura can't be accessed on Client Side");
        }
        DimensionDataStorage storage = ((ServerLevel)level).getDataStorage();
        return storage.computeIfAbsent(new Factory<>(ShulkerAuraManager::new,ShulkerAuraManager::new), "auramanager");
    }

    @NotNull
    private ShulkerAura getAuraInternal(BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        return auraByChunk.computeIfAbsent(chunkPos, cp -> new ShulkerAura());
    }

    public int getAura(BlockPos pos) {
        ShulkerAura aura = getAuraInternal(pos);
        return aura.getAura();
    }

    public int extractAura(BlockPos pos, int amount, boolean simulate) {
        ShulkerAura aura = getAuraInternal(pos);

        int extracted = aura.extractAura(amount, simulate);

        if (!simulate && extracted > 0)
            setDirty();

        return extracted;
    }

    public int insertAura(BlockPos pos, int insert, boolean simulate) {
        ShulkerAura aura = getAuraInternal(pos);

        int added = aura.insertAura(insert, simulate);

        if (added > 0 && !simulate)
            setDirty();

        return added;
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
                                RSAdvancements.AURA.awardTo(serverPlayer);
                            }
                    PacketDistributor.sendToPlayer(serverPlayer,new AuraSyncPayload(chunkAura));
                }
            });

        }
    }


    private record Entry(ChunkPos pos, ShulkerAura aura){


        private static final Codec<ChunkPos> CHUNK_POS_CODEC = RecordCodecBuilder.create(instance->
                instance.group(
                        Codec.INT.fieldOf("x").forGetter(pos->pos.x),
                        Codec.INT.fieldOf("z").forGetter(pos->pos.z)
                ).apply(instance,ChunkPos::new)
        );

        private static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance->
                instance.group(
                        CHUNK_POS_CODEC.fieldOf("pos").forGetter(Entry::pos),
                        ShulkerAura.CODEC.fieldOf("aura").forGetter(Entry::aura)
                ).apply(instance,Entry::new)
        );
    }

}