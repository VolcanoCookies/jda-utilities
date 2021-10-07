package net.volcano.jdautilities.utils

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.regex.Pattern

object UserUtil {

	/**
	 * Find a user
	 * If ran in a guild, will only look at users in that guild
	 *
	 * @param query the query to find by
	 * @return the best match, or null if none found
	 */
	@JvmStatic
	fun findUser(query: String?, jda: JDA, guild: Guild?): User? {
		var query = query ?: return null
		query = query.trim { it <= ' ' }

		// Return the first occurrence of a mention
		val matcher = Pattern.compile("<@!?(\\d+)>", Pattern.CASE_INSENSITIVE).matcher(query)
		if (matcher.matches()) {
			try {
				return jda.retrieveUserById(matcher.group(1))
					.submit()
					.get()
			} catch (ignored: InterruptedException) {
			} catch (ignored: ExecutionException) {
			}
		}

		// Try getting a user from the pure discord id if its numerical
		if (query.matches(Regex("\\d+"))) {
			try {
				return jda.retrieveUserById(query)
					.submit()
					.get()
			} catch (ignored: InterruptedException) {
			} catch (ignored: ExecutionException) {
			}
		}
		var matchedStart = false
		var startMatchPercent = 0.0
		var middleMatchPercent = 0.0
		return if (guild != null) {
			var bestGuess: Member? = null
			for (member in guild.members) {

				// Try matching whole nickname
				val nickname = member.nickname
				if (nickname != null) {
					// Can't have a better match than 100%, so return
					if (nickname.equals(query, ignoreCase = true)) {
						return member.user
					}
					val percentage = query.length.toDouble() / nickname.length.toDouble()
					if (nickname.lowercase(Locale.getDefault()).startsWith(query)) {
						if (percentage > startMatchPercent) {
							bestGuess = member
							matchedStart = true
							startMatchPercent = percentage
						}
					} else if (!matchedStart && nickname.lowercase(Locale.getDefault()).contains(query)) {
						if (percentage > middleMatchPercent) {
							bestGuess = member
							middleMatchPercent = percentage
						}
					}
				}

				// Try username
				val username = member.user.name
				// Can't have a better match than 100%, so return
				if (username.equals(query, ignoreCase = true)) {
					return member.user
				} else {
					val percentage = query.length.toDouble() / username.length.toDouble()
					if (username.lowercase(Locale.getDefault()).startsWith(query)) {
						if (percentage > startMatchPercent) {
							bestGuess = member
							matchedStart = true
							startMatchPercent = percentage
						}
					} else if (!matchedStart && username.lowercase(Locale.getDefault()).contains(query)) {
						if (percentage > middleMatchPercent) {
							bestGuess = member
							middleMatchPercent = percentage
						}
					}
				}

				// Try tag
				val tag = member.user.asTag
				// Can't have a better match than 100%, so return
				if (tag.equals(query, ignoreCase = true)) {
					return member.user
				} else {
					val percentage = query.length.toDouble() / tag.length.toDouble()
					if (tag.lowercase(Locale.getDefault()).startsWith(query)) {
						if (percentage > startMatchPercent) {
							bestGuess = member
							matchedStart = true
							startMatchPercent = percentage
						}
					} else if (!matchedStart && tag.lowercase(Locale.getDefault()).contains(query)) {
						if (percentage > middleMatchPercent) {
							bestGuess = member
							middleMatchPercent = percentage
						}
					}
				}
			}
			bestGuess?.user
		} else {
			var bestGuess: User? = null
			for (user in jda.userCache) {

				// Try username
				val username = user.name
				// Can't have a better match than 100%, so return
				if (username.equals(query, ignoreCase = true)) {
					return user
				}
				val percentage = query.length.toDouble() / username.length.toDouble()
				if (username.lowercase(Locale.getDefault()).startsWith(query)) {
					if (percentage > startMatchPercent) {
						bestGuess = user
						matchedStart = true
						startMatchPercent = percentage
					}
				} else if (!matchedStart && username.lowercase(Locale.getDefault()).contains(query)) {
					if (percentage > middleMatchPercent) {
						bestGuess = user
						middleMatchPercent = percentage
					}
				}
			}
			bestGuess
		}
	}

}

fun User.format(): String {
	return "${this.asTag} [${this.id}]"
}

fun User.fullString(): String {
	return "${this.asTag} [${this.id}] ${this.asMention}"
}

val User.roles: List<Role>
	get() {
		return this.mutualGuilds.mapNotNull { it.getMember(this) }
			.flatMap { it.roles }
	}

fun User.retrieveRoles(): List<Role> {
	return this.mutualGuilds.flatMap {
		it.retrieveMember(this).submit().get().roles
	}
}