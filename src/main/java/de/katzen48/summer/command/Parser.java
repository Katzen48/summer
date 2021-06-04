package de.katzen48.summer.command;

import java.util.List;
import java.util.regex.Pattern;

import de.katzen48.summer.command.rules.ParameterRule;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Builder
@Data
public class Parser
{
	@Singular
	private List<ParameterRule<?>> rules;
	private Pattern pattern;
}
