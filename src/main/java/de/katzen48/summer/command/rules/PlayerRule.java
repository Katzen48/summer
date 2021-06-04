package de.katzen48.summer.command.rules;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class PlayerRule implements ParameterRule<Player>
{
	@Override
	public de.katzen48.summer.command.rules.ParameterRule.TextType getTextType()
	{
		return TextType.STRING;
	}

	@Override
	public List<String> getValidValues(CommandSender sender, String part)
	{
		return Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().startsWith(part)).map(player -> player.getName()).collect(Collectors.toList());
	}

	@Override
	public Player getObject(String value)
	{
		return Bukkit.getPlayer(value);
	}
}
