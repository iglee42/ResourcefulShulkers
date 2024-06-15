package fr.iglee42.resourcefulshulkers.resourcepack;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;

import java.nio.file.Path;
import java.util.function.Consumer;

import static net.minecraft.server.packs.repository.BuiltInPackSource.fixedResources;

public class TRSPackFinder implements RepositorySource {


	private static final PackSelectionConfig BUILT_IN_SELECTION_CONFIG = new PackSelectionConfig(false, Pack.Position.TOP, false);

	private final PackType type;

	public TRSPackFinder(PackType type) {

		this.type = type;
	}

	@Override
	public void loadPacks(Consumer<Pack> infoConsumer) {
		Path rootPath = PathConstant.ROOT_PATH;
		Pack pack = Pack.readMetaAndCreate(InMemoryPack.location,fixedResources(new InMemoryPack(rootPath)),type.getVanillaType(),BUILT_IN_SELECTION_CONFIG);
		if (pack != null){
			infoConsumer.accept(pack);
		}
	}


}