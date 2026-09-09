package fr.iglee42.resourcefulshulkers.resourcepack;

import fr.iglee42.resourcefulshulkers.RSIds;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.function.Consumer;

import static net.minecraft.server.packs.repository.BuiltInPackSource.fixedResources;

@EventBusSubscriber(modid = RSIds.MODID)
public class RSPackFinder implements RepositorySource {

	private final PackType type;

	public RSPackFinder(PackType type) {

		this.type = type;
	}

	@Override
	public void loadPacks(@NotNull Consumer<Pack> consumer) {
		Path rootPath = PathConstant.ROOT_PATH;
		Pack pack = Pack.readMetaAndCreate(InMemoryPack.getPackInfo(type),fixedResources(new InMemoryPack(type,rootPath)),type,new PackSelectionConfig(true, Pack.Position.TOP,true));
		if (pack != null) {
			consumer.accept(pack);
		}
	}

	@SubscribeEvent
	public static void registerPackRepo(AddPackFindersEvent event){
		event.addRepositorySource(new RSPackFinder(event.getPackType()));
	}
}