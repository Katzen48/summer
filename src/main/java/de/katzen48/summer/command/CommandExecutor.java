package de.katzen48.summer.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import de.katzen48.summer.command.annotations.CommandList;
import de.katzen48.summer.command.annotations.HideFromList;
import de.katzen48.summer.command.annotations.Permission;
import de.katzen48.summer.command.rules.ParameterRegistry;
import de.katzen48.summer.command.rules.ParameterRule;

public class CommandExecutor implements TabExecutor
{
	public static final Pattern PARAMETER_PATTERN = Pattern.compile("\\{(.+)\\}");
	
	protected String listPattern = "/%(command) %(subcommand) - %(description)";
	
	private boolean showCommandList;
	private boolean checkPermissionsInCommandList;
	private final TreeMap<String,SubCommand> subCommands = new TreeMap<>(comparator); 
	
	public CommandExecutor()
	{
		if((showCommandList = getClass().isAnnotationPresent(CommandList.class)))
		{
			checkPermissionsInCommandList = getClass().getAnnotation(CommandList.class).checkPermissions();
		}
		
		addSubcommands(getSubCommandMethods());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if((args.length < 1 || (args[0].equalsIgnoreCase("help") && !subCommands.containsKey("help"))) && showCommandList)
		{
			sender.sendMessage(getCommandList(sender, command));
			
			return true;
		}
		
		String string = String.join(" ", args);
		
		for(SubCommand subCommand : subCommands.values())
		{			
			Matcher matcher = subCommand.getParser().getPattern().matcher(string);
			
			if(!matcher.matches())
				continue;
			
			return subCommand.onCommand(sender, command, label, matcher);
		}
		
		sender.sendMessage(ChatColor.RED + "Command not found");
		
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		ArrayList<String> strings = new ArrayList<>();
		
		subCommands.forEach((name, subCommand) -> 
		{
			if(hide(sender, subCommand) || (args.length > 0 && !name.startsWith(args[0])))
				return;
			
			strings.add(name);
		});
		
		return strings;
	}
	
	public String getPermissionMessage(CommandSender sender, String permission)
	{
		// TODO add i18n
		
		return String.format("%sYou don't have permission to perform that command. Please ask the server administrator to grant you %s%s", ChatColor.RED, ChatColor.GRAY, permission);
	}
	
	public String[] getCommandList(CommandSender sender, Command command)
	{
		ArrayList<String> listLines = new ArrayList<>();
		
		subCommands.forEach((name, subCommand) -> 
		{
			if(hide(sender, subCommand))
				return;
			
			TreeMap<String,String> values = new TreeMap<>();
			values.put("command", command.getName());
			values.put("subcommand", name);
			values.put("description", "Description");
			
			StrSubstitutor subst = new StrSubstitutor(values, "%(", ")");
			
			listLines.add(subst.replace(listPattern));
		});
		
		return listLines.toArray(new String[listLines.size()]);
	}
	
	private boolean hide(CommandSender sender, SubCommand subCommand)
	{
		return subCommand.isHideFromList() || (checkPermissionsInCommandList && !subCommand.hasPermission(sender));
	}
	
	private Method[] getSubCommandMethods()
	{
		List<Method> methods = Arrays.asList(getClass().getMethods()).stream()
				.filter(method -> method.isAnnotationPresent(de.katzen48.summer.command.annotations.SubCommand.class))
				.collect(Collectors.toList());
		
		return methods.toArray(new Method[methods.size()]);
	}
	
	private void addSubcommands(Method[] subCommandMethods)
	{
		for(Method method : subCommandMethods)
		{
			de.katzen48.summer.command.annotations.SubCommand command = method.getAnnotation(de.katzen48.summer.command.annotations.SubCommand.class);
			
			if(subCommands.containsKey(command.syntax()))
				throw new IllegalArgumentException("Command patterns must be unique");
			
			ArrayList<String> permissions = new ArrayList<>();			
			for(Permission permission : method.getAnnotationsByType(Permission.class))
			{
				permissions.add(permission.value());
			}
			
			subCommands.put(command.syntax(), new SubCommand(compileParser(command.syntax()), method, this, permissions.toArray(new String[permissions.size()]), method.isAnnotationPresent(HideFromList.class)));
		}
	}
	
	private Parser compileParser(String syntax)
	{
		if(syntax.isEmpty())
			throw new IllegalArgumentException("Patterns is empty");
		
		String[] parts = syntax.split(" ");
		StringJoiner regexJoiner = new StringJoiner(" ");
		ArrayList<ParameterRule<?>> rules = new ArrayList<>();
		
		regexJoiner.add(String.format("(?:%s)", parts[0]));
		
		for(int i = 1 ; i < parts.length ; i++)
		{
			String part = parts[i];
			
			Matcher matcher = PARAMETER_PATTERN.matcher(part);
			
			if(!matcher.matches())
			{
				regexJoiner.add(String.format("(?:%s)", part));
				
				continue;
			}
			
			ParameterRule<?> rule = ParameterRegistry.getRule(matcher.group(1));
			
			regexJoiner.add(rule.getTextType().getRegex());
			rules.add(rule);
		}
		
		return Parser.builder().pattern(Pattern.compile(regexJoiner.toString())).rules(rules).build();
	}
	
	private static final Comparator<String> comparator = new Comparator<String>()
	{
		@Override
		public int compare(String o1, String o2)
		{
			if(o1.equalsIgnoreCase(o2))
			{
				return 0;
			}
			else 
			{
				int comparison = Integer.compare(o2.length(), o1.length());;
				
				return comparison == 0 ? o1.compareTo(o2) : comparison; 
			}	
		}
	};
}
