package io.github.haykam821.clothesbin.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.haykam821.clothesbin.ui.ClothesbinUi;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public final class ClothesbinCommand {
	private ClothesbinCommand() {
		return;
	}

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("clothesbin").executes(ClothesbinCommand::execute));
	}

	private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ClothesbinUi.INSTANCE.open(context.getSource().getPlayer());
		return Command.SINGLE_SUCCESS;
	}
}
