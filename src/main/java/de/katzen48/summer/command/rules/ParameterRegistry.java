package de.katzen48.summer.command.rules;

import java.util.TreeMap;

public class ParameterRegistry
{
	private static final TreeMap<String, ParameterRule<?>> RULES = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	
	public static void registerRule(String key, ParameterRule<?> rule)
	{
		if(RULES.containsKey(key))
			throw new IllegalArgumentException("Key is already registed");
		
		RULES.put(key, rule);
	}
	
	public static ParameterRule<?> getRule(String key)
	{
		if(!RULES.containsKey(key))
			throw new IllegalArgumentException(key + " is not registered");
		
		return RULES.get(key);
	}
	
	public static String getKey(ParameterRule<?> rule)
	{
		return RULES.entrySet().stream().filter(entry -> entry.getValue() == rule).map(entry -> entry.getKey()).findFirst().orElse(null);
	}
	
	static
	{
		registerRule("player", new PlayerRule());
		registerRule("world", new WorldRule());
		registerRule("int", new IntegerRule());
		registerRule("float", new FloatRule());
		registerRule("double", new DoubleRule());
	}
}
