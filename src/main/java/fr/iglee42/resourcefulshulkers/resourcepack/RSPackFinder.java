package fr.iglee42.resourcefulshulkers.resourcepack;

import fr.iglee42.resourcefulshulkers.RSIds;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.function.Consumer;


@Mod.EventBusSubscriber(modid = RSIds.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RSPackFinder implements RepositorySource {

	private final PackType type;

	public RSPackFinder(PackType type) {

		this.type = type;
	}

	@Override
	public void loadPacks(@NotNull Consumer<Pack> consumer) {
		Path rootPath = PathConstant.ROOT_PATH;
		Pack pack = Pack.readMetaAndCreate("rs_"+type.getDirectory().toLowerCase(),
				Component.literal("RS Builtin Pack"),
				true,
				t->new InMemoryPack(type, rootPath),
				type,
				Pack.Position.TOP,
				PackSource.BUILT_IN);
		if (pack != null) {
			consumer.accept(pack);
		}
	}

	@SubscribeEvent
	public static void registerPackRepo(AddPackFindersEvent event){
		event.addRepositorySource(new RSPackFinder(event.getPackType()));
	}
}