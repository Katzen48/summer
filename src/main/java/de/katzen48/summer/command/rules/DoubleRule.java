package de.katzen48.summer.command.rules;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

class DoubleRule implements ParameterRule<Double>
{
	@Override
	public de.katzen48.summer.command.rules.ParameterRule.TextType getTextType()
	{
		return TextType.FLOAT;
	}

	@Override
	public List<String> getValidValues(CommandSender sender, String part)
	{
		return new ArrayList<>();
	}

	@Override
	public Double getObject(String value)
	{
		try
		{
			return Double.parseDouble(value);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}
}
