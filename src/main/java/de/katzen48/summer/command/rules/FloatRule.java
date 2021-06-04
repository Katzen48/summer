package de.katzen48.summer.command.rules;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

class FloatRule implements ParameterRule<Float>
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
	public Float getObject(String value)
	{
		try
		{
			return Float.parseFloat(value);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}
}
