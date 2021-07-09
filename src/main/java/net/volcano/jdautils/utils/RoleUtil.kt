package net.volcano.jdautils.utils

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role

object RoleUtil {

	@JvmStatic
	fun getRoleString(role: Role): String {
		return role.name + "[" + role.id + "] from server " + role.guild.name + " [" + role.guild.id + "]"
	}

	@JvmStatic
	fun format(role: Role, forGuild: Guild, useMention: Boolean = true, noId: Boolean = false): String {
		return if (role.guild.idLong == forGuild.idLong) {
			if (useMention) {
				role.asMention + " from this guild"
			} else {
				if (noId) {
					"${role.name} from this guild"
				} else {
					"${role.name} [${role.id}] from this guild"
				}
			}
		} else {
			if (noId) {
				"${role.name} from ${role.guild.name}"
			} else {
				"${role.name} [${role.id}] from ${role.guild.name} [${role.guild.id}]"
			}
		}
	}

	@JvmStatic
	fun formatForServerWithId(role: Role, guild: Guild?): String {
		return if (guild != null && role.guild.idLong == guild.idLong) {
			String.format(
				"%1\$s[%2\$s]",
				role.asMention,
				role.id
			)
		} else {
			String.format(
				"%1\$s[%2\$s] from %3\$s[%4\$s]",
				role.name,
				role.id,
				role.guild.name,
				role.guild.id
			)
		}
	}

	@JvmStatic
	fun findRole(query: String?, jda: JDA, guild: Guild?): Role? {
		var query = query ?: return null
		query = query.trim { it <= ' ' }

		// Try getting a role from the pure discord id if its numerical
		if (query.matches(Regex("(<@&)?\\d+>?"))) {
			val role = jda.getRoleById(query.replace("[<@&>]".toRegex(), ""))
			if (role != null) {
				return role
			}
		}
		var matchedStart = false
		var startMatchPercent = 0.0
		var middleMatchPercent = 0.0
		val possibleRoles = guild?.roles ?: jda.roles
		var bestGuess: Role? = null
		for (role in possibleRoles) {

			// Try role name
			val name = role.name
			// Can't have a better match than 100%, so return
			if (name.equals(query, ignoreCase = true)) {
				return role
			}
			val percentage = query.length.toDouble() / name.length.toDouble()
			if (name.lowercase().startsWith(query)) {
				if (percentage > startMatchPercent) {
					bestGuess = role
					matchedStart = true
					startMatchPercent = percentage
				}
			} else if (!matchedStart && name.lowercase().contains(query)) {
				if (percentage > middleMatchPercent) {
					bestGuess = role
					middleMatchPercent = percentage
				}
			}
		}
		return bestGuess
	}
	
	@JvmStatic
	fun getMembers(role: Role): List<Member> {
		return role.guild.getMembersWithRoles(role)
	}
}