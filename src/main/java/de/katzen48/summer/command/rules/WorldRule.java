package de.katzen48.summer.command.rules;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

class WorldRule implements ParameterRule<World>
{
	@Override
	public de.katzen48.summer.command.rules.ParameterRule.TextType getTextType()
	{
		return TextType.STRING;
	}

	@Override
	public List<String> getValidValues(CommandSender sender, String part)
	{
		return Bukkit.getWorlds().stream().filter(world -> world.getName().startsWith(part)).map(world -> world.getName()).collect(Collectors.toList());
	}

	@Override
	public World getObject(String value)
	{
		return Bukkit.getWorld(value);
	}
}
