package net.volcano.jdautils.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserUtil {
	
	public static String getUserString(User user) {
		return user.getAsTag() + " [" + user.getId() + "] " + user.getAsMention();
	}
	
	public static Set<Role> getAllRoles(User user) {
		var futures = user.getMutualGuilds()
				.stream()
				.map(g -> g.retrieveMember(user).submit())
				.collect(Collectors.toList());
		var list = new ArrayList<Member>();
		for (CompletableFuture<Member> future : futures) {
			try {
				list.add(future.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return list.stream()
				.flatMap(member -> member.getRoles().stream())
				.collect(Collectors.toSet());
	}
	
	/**
	 * Find a user
	 * If ran in a guild, will only look at users in that guild
	 *
	 * @param query the query to find by
	 * @return the best match, or null if none found
	 */
	@Nullable
	public static User findUser(String query, JDA jda, @Nullable Guild guild) {
		
		if (query == null) {
			return null;
		}
		
		query = query.trim();
		
		// Return the first occurrence of a mention
		Matcher matcher = Pattern.compile("<@(\\d+)>", Pattern.CASE_INSENSITIVE).matcher(query);
		if (matcher.matches()) {
			try {
				return jda.retrieveUserById(matcher.group(1))
						.submit()
						.get();
			} catch (InterruptedException | ExecutionException ignored) {
			}
		}
		
		// Try getting a user from the pure discord id if its numerical
		if (query.matches("\\d+")) {
			try {
				return jda.retrieveUserById(query)
						.submit()
						.get();
			} catch (InterruptedException | ExecutionException ignored) {
			}
		}
		
		boolean matchedStart = false;
		double startMatchPercent = 0, middleMatchPercent = 0;
		
		if (guild != null) {
			
			Member bestGuess = null;
			
			for (Member member : guild.getMembers()) {
				
				// Try matching whole nickname
				String nickname = member.getNickname();
				if (nickname != null) {
					// Can't have a better match than 100%, so return
					if (nickname.equalsIgnoreCase(query)) {
						return member.getUser();
					}
					double percentage = (double) query.length() / (double) nickname.length();
					if (nickname.toLowerCase().startsWith(query)) {
						if (percentage > startMatchPercent) {
							bestGuess = member;
							matchedStart = true;
							startMatchPercent = percentage;
						}
					} else if (!matchedStart && nickname.toLowerCase().contains(query)) {
						if (percentage > middleMatchPercent) {
							bestGuess = member;
							middleMatchPercent = percentage;
						}
					}
				}
				
				// Try username
				String username = member.getUser().getName();
				// Can't have a better match than 100%, so return
				if (username.equalsIgnoreCase(query)) {
					return member.getUser();
				}
				double percentage = (double) query.length() / (double) username.length();
				if (username.toLowerCase().startsWith(query)) {
					if (percentage > startMatchPercent) {
						bestGuess = member;
						matchedStart = true;
						startMatchPercent = percentage;
					}
				} else if (!matchedStart && username.toLowerCase().contains(query)) {
					if (percentage > middleMatchPercent) {
						bestGuess = member;
						middleMatchPercent = percentage;
					}
				}
				
			}
			
			if (bestGuess != null) {
				return bestGuess.getUser();
			} else {
				return null;
			}
			
		} else {
			
			User bestGuess = null;
			
			for (User user : jda.getUserCache()) {
				
				// Try username
				String username = user.getName();
				// Can't have a better match than 100%, so return
				if (username.equalsIgnoreCase(query)) {
					return user;
				}
				double percentage = (double) query.length() / (double) username.length();
				if (username.toLowerCase().startsWith(query)) {
					if (percentage > startMatchPercent) {
						bestGuess = user;
						matchedStart = true;
						startMatchPercent = percentage;
					}
				} else if (!matchedStart && username.toLowerCase().contains(query)) {
					if (percentage > middleMatchPercent) {
						bestGuess = user;
						middleMatchPercent = percentage;
					}
				}
				
			}
			
			return bestGuess;
			
		}
	}
	
	public static String format(User user) {
		return user.getAsTag() + " [" + user.getId() + "]";
	}
	
	public static String format(Member member) {
		return format(member.getUser());
	}
	
}
