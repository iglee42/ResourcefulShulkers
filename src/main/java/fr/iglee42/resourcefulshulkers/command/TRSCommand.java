package fr.iglee42.resourcefulshulkers.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.iglee42.resourcefulshulkers.ResourcefulShulkers;
import fr.iglee42.resourcefulshulkers.aura.ShulkerAura;
import fr.iglee42.resourcefulshulkers.aura.ShulkerAuraManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class TRSCommand {

    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.literal("Aura can't be modified !"));

    public TRSCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("resourcefulshulkers")
                .then(Commands.literal("aura").requires(c->c.hasPermission(4))
                        .then(Commands.literal("add").then(Commands.argument("amount", IntegerArgumentType.integer(1, ShulkerAura.MAX_AURA)).executes(this::addAura).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(this::addAuraWithPos))))
                        .then(Commands.literal("remove").then(Commands.argument("amount", IntegerArgumentType.integer(1, ShulkerAura.MAX_AURA)).executes(this::removeAura).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(this::removeAuraWithPos))))
                        .then(Commands.literal("get").executes(this::getAura).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(this::getAuraWithPos)))));
    }

    private int getAuraWithPos(CommandContext<CommandSourceStack> source) {
        ServerLevel level = source.getSource().getLevel();
        source.getSource().sendSuccess(()->ResourcefulShulkers.PREFIX.copy().append(Component.literal("Current chunk aura : ").withStyle(ChatFormatting.LIGHT_PURPLE).append(Component.literal(""+ShulkerAuraManager.get(level).getAura(source.getArgument("pos",BlockPos.class))).withStyle(ChatFormatting.DARK_PURPLE)).append(Component.literal(" !").withStyle(ChatFormatting.LIGHT_PURPLE))), true);
        return 1;
    }
    private int getAura(CommandContext<CommandSourceStack> source) {
        ServerLevel level = source.getSource().getLevel();
        source.getSource().sendSuccess(()->ResourcefulShulkers.PREFIX.copy().append(Component.literal("Current chunk aura : ").withStyle(ChatFormatting.LIGHT_PURPLE).append(Component.literal(""+ShulkerAuraManager.get(level).getAura(BlockPos.containing(source.getSource().getPosition()))).withStyle(ChatFormatting.DARK_PURPLE)).append(Component.literal(" !").withStyle(ChatFormatting.LIGHT_PURPLE))), true);
        return 1;
    }

    private int addAuraWithPos(CommandContext<CommandSourceStack> source) throws CommandSyntaxException {
        ServerLevel level = source.getSource().getLevel();
        if (ShulkerAuraManager.get(level).insertAura(source.getArgument("pos",BlockPos.class),source.getArgument("amount",Integer.class)) == 0)
            throw ERROR_FAILED.create();
        source.getSource().sendSuccess(()->ResourcefulShulkers.PREFIX.copy().append(Component.literal("Aura successfully added !").withStyle(ChatFormatting.GREEN)), true);
        return 1;
    }

    private int addAura(CommandContext<CommandSourceStack> source) throws CommandSyntaxException {
        ServerLevel level = source.getSource().getLevel();
        if (ShulkerAuraManager.get(level).insertAura(BlockPos.containing(source.getSource().getPosition()),source.getArgument("amount",Integer.class)) == 0)
            throw ERROR_FAILED.create();
        source.getSource().sendSuccess(()->ResourcefulShulkers.PREFIX.copy().append(Component.literal("Aura successfully added !").withStyle(ChatFormatting.GREEN)), true);
        return 1;
    }

    private int removeAuraWithPos(CommandContext<CommandSourceStack> source) throws CommandSyntaxException {
        ServerLevel level = source.getSource().getLevel();
        if (ShulkerAuraManager.get(level).extractAura(source.getArgument("pos",BlockPos.class),source.getArgument("amount",Integer.class)) == 0)
            throw ERROR_FAILED.create();
        source.getSource().sendSuccess(()->ResourcefulShulkers.PREFIX.copy().append(Component.literal("Aura successfully removed !").withStyle(ChatFormatting.GREEN)), true);
        return 1;
    }

    private int removeAura(CommandContext<CommandSourceStack> source) throws CommandSyntaxException {
        ServerLevel level = source.getSource().getLevel();
        if (ShulkerAuraManager.get(level).extractAura(BlockPos.containing(source.getSource().getPosition()),source.getArgument("amount",Integer.class)) == 0)
            throw ERROR_FAILED.create();
        source.getSource().sendSuccess(()->ResourcefulShulkers.PREFIX.copy().append(Component.literal("Aura successfully removed !").withStyle(ChatFormatting.GREEN)), true);
        return 1;
    }


}
