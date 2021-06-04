package de.katzen48.summer.command.rules;

import java.util.List;

import org.bukkit.command.CommandSender;

import lombok.AllArgsConstructor;
import lombok.NonNull;

public interface ParameterRule<E>
{
	TextType getTextType();
	
	List<String> getValidValues(CommandSender sender, String part);
	
	E getObject(String value);
	
	
	@AllArgsConstructor
	public static enum TextType
	{
		STRING("(.+)"), DECIMAL("(\\d+)"), FLOAT("([-+]?[0-9]*\\.?[0-9]*)");
		
		@NonNull
		private final String REGEX;
		
		public String getRegex()
		{
			return REGEX;
		}
	}
}
