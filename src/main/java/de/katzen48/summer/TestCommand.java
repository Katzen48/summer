package de.katzen48.summer;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.katzen48.summer.command.CommandExecutor;
import de.katzen48.summer.command.annotations.CommandList;
import de.katzen48.summer.command.annotations.HideFromList;
import de.katzen48.summer.command.annotations.Nonnull;
import de.katzen48.summer.command.annotations.Permission;
import de.katzen48.summer.command.annotations.SubCommand;

@CommandList(checkPermissions=true)
public class TestCommand extends CommandExecutor
{
	@SubCommand(syntax = "test {player} {player}")
	@Permission("test")
	public boolean testCommand(CommandSender sender, Player player1, @Nonnull Player player2)
	{		
		sender.sendMessage(String.format("%s steht auf %s", player1.getName(), player2.getName()));
		
		return true;
	}
	
	@SubCommand(syntax = "test2")
	@HideFromList
	public boolean test2Command(CommandSender sender)
	{
		sender.sendMessage("Adihab stinkt");
		
		return true;
	}
	
	@SubCommand(syntax = "test3 {float}")
	public boolean testCommand(CommandSender sender, Float float1)
	{		
		sender.sendMessage(String.format("integer ist %f", float1));
		
		return true;
	}
	
	@SubCommand(syntax = "test4 {world}")
	public boolean testCommand(CommandSender sender, @Nonnull World world)
	{		
		sender.sendMessage(String.format("world ist %s", world.getName()));
		
		return true;
	}
	
	@SubCommand(syntax = "test4 {world} add")
	public boolean test4Command(CommandSender sender, @Nonnull World world)
	{		
		sender.sendMessage(String.format("world added ist %s", world.getName()));
		
		return true;
	}
}
