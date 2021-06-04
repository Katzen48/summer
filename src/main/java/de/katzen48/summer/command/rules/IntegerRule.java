package de.katzen48.summer.command.rules;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

class IntegerRule implements ParameterRule<Integer>
{
	@Override
	public de.katzen48.summer.command.rules.ParameterRule.TextType getTextType()
	{
		return TextType.DECIMAL;
	}

	@Override
	public List<String> getValidValues(CommandSender sender, String part)
	{
		return new ArrayList<>();
	}

	@Override
	public Integer getObject(String value)
	{
		try
		{
			return Integer.parseInt(value);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

}
