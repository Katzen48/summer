package de.katzen48.summer.command;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.katzen48.summer.command.rules.ParameterRegistry;
import de.katzen48.summer.command.rules.ParameterRule;
import lombok.Data;

@Data
class SubCommand
{
	private final Parser parser;
	private final Method method;
	private final CommandExecutor executor;
	private final String[] permissions;
	private boolean hideFromList;
	private boolean[] parameterNotNullable;
	
	public SubCommand(Parser parser, Method method, CommandExecutor executor, String[] permissions, boolean hideFromList)
	{		
		if(parser.getRules().size() != (method.getParameterCount() - 1))
			throw new IllegalStateException(String.format("The argument count of %s.%s() does not match it's syntax", method.getDeclaringClass().getName(), method.getName()));
		
		this.parser = parser;
		this.method = method;
		this.executor = executor;
		this.permissions = permissions;
		this.hideFromList = hideFromList;
		this.parameterNotNullable = new boolean[parser.getRules().size()];
		
		setParameterNullability();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, Matcher matcher)
	{
		if(!hasPermission(sender, true))
		{
			return true;
		}
		
		try
		{
			ArrayList<Object> parameters = new ArrayList<>();
			
			parameters.add(sender);
			
			for(int i = 1 ; i <= matcher.groupCount() ; i++)
			{
				ParameterRule<?> rule = parser.getRules().get(i - 1);
				String value = matcher.group(i);
				Object object = rule.getObject(value); // 
				
				if(object == null && parameterNotNullable[i - 1])
				{
					sender.sendMessage(String.format(ChatColor.RED + "The %s %s is invalid", ParameterRegistry.getKey(rule), value)); // TODO add i18n
					
					return true;
				}
				
				parameters.add(object);
			}
				
			
			Object returnValue = method.invoke(executor, parameters.toArray(new Object[parameters.size()]));
			
			if(returnValue instanceof Boolean)
				return (boolean) returnValue;
			
			return returnValue != null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			return true;
		}
	}
	
	public boolean hasPermission(CommandSender sender)
	{
		return hasPermission(sender, false);
	}
	
	public boolean hasPermission(CommandSender sender, boolean notifyUser)
	{
		for(String permission : permissions)
		{
			if(!sender.hasPermission(permission))
			{	
				if(notifyUser)
					sendMissingPermissionMessage(sender, permission);
				
				return false;
			}	
		}
		
		return true;
	}
	
	private void sendMissingPermissionMessage(CommandSender sender, String permission)
	{
		sender.sendMessage(executor.getPermissionMessage(sender, permission));
	}
	
	private void setParameterNullability()
	{		
		for(int i = 1 ; i <= (method.getParameterCount() - 1) ; i++)
			parameterNotNullable[i - 1] = isParameterNotNullable(method.getParameters()[i].getAnnotations());
	}
	
	private boolean isParameterNotNullable(Annotation[] annotations)
	{		
		return Arrays.asList(annotations).stream().anyMatch(annotation -> 
		{
			String className = annotation.annotationType().getSimpleName();
			
			return className.equalsIgnoreCase("notnull") || className.equalsIgnoreCase("nonnull");
		});
	}
}
