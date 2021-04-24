package net.volcano.jdautils.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class RoleUtil {
	
	public static String getRoleString(Role role) {
		return role.getName() + "[" + role.getId() + "] from server " + role.getGuild().getName() + " [" + role.getGuild().getId() + "]";
	}
	
	public static String formatForServer(@Nonnull Role role, @Nullable Guild guild) {
		if (guild != null && role.getGuild().getIdLong() == guild.getIdLong()) {
			return role.getAsMention();
		} else {
			return String.format("%1$s[%2$s] from %3$s[%4$s]",
					role.getName(),
					role.getId(),
					role.getGuild().getName(),
					role.getGuild().getId());
		}
	}
	
	public static String format(Role role, Guild forGuild, boolean useMention) {
		if (role.getGuild().getIdLong() == forGuild.getIdLong()) {
			if (useMention) {
				return role.getAsMention() + " from this server";
			} else {
				return role.getName() + " [" + role.getId() + "] from this server";
			}
		} else {
			if (useMention) {
				return role.getAsMention() + " from " + forGuild.getName() + " [" + forGuild.getId() + "]";
			} else {
				return role.getName() + " [" + role.getId() + "] from " + forGuild.getName() + " [" + forGuild.getId() + "]";
			}
		}
	}
	
	public static String formatForServerWithId(@Nonnull Role role, @Nullable Guild guild) {
		if (guild != null && role.getGuild().getIdLong() == guild.getIdLong()) {
			return String.format("%1$s[%2$s]",
					role.getAsMention(),
					role.getId());
		} else {
			return String.format("%1$s[%2$s] from %3$s[%4$s]",
					role.getName(),
					role.getId(),
					role.getGuild().getName(),
					role.getGuild().getId());
		}
	}
	
	public static Role findRole(String query, JDA jda, @Nullable Guild guild) {
		
		if (query == null) {
			return null;
		}
		
		query = query.trim();
		
		// Try getting a role from the pure discord id if its numerical
		if (query.matches("(<@&)?\\d+>?")) {
			Role role = jda.getRoleById(query.replaceAll("[<@&>]", ""));
			if (role != null) {
				return role;
			}
		}
		
		boolean matchedStart = false;
		double startMatchPercent = 0, middleMatchPercent = 0;
		
		List<Role> possibleRoles = guild != null ? guild.getRoles() : jda.getRoles();
		
		Role bestGuess = null;
		
		for (Role role : possibleRoles) {
			
			// Try role name
			String name = role.getName();
			// Can't have a better match than 100%, so return
			if (name.equalsIgnoreCase(query)) {
				return role;
			}
			double percentage = (double) query.length() / (double) name.length();
			if (name.toLowerCase().startsWith(query)) {
				if (percentage > startMatchPercent) {
					bestGuess = role;
					matchedStart = true;
					startMatchPercent = percentage;
				}
			} else if (!matchedStart && name.toLowerCase().contains(query)) {
				if (percentage > middleMatchPercent) {
					bestGuess = role;
					middleMatchPercent = percentage;
				}
			}
			
		}
		
		return bestGuess;
		
	}
	
	public static List<Member> getMembers(Role role) {
		return role.getGuild().getMembersWithRoles(role);
	}
	
}
