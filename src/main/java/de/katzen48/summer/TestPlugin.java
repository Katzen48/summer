package de.katzen48.summer;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

@Author("Katzen48@chrotos.net")
@Plugin(name="SummerTest", version="1.0-SNAPSHOT")
@Commands(value = {@Command(name="test")})
public class TestPlugin extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		getCommand("test").setExecutor(new TestCommand());
	}
}
