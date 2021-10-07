package net.volcano.jdautilities.utils

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role

object RoleUtil {

	@JvmStatic
	fun findRole(query: String?, jda: JDA, guild: Guild?): Role? {
		var query = query?.lowercase() ?: return null
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

}

fun Role.getFullString(): String {
	return "${this.name} [${this.id}] from ${this.guild.name} [${this.guild.id}]"
}

fun Role.format(forGuild: Guild = this.guild, useMention: Boolean = true, noId: Boolean = false): String {
	return if (this.guild.idLong == forGuild.idLong) {
		if (useMention) {
			this.asMention
		} else {
			if (noId) {
				"${this.name} from this guild"
			} else {
				"${this.name} [${this.id}] from this guild"
			}
		}
	} else {
		if (noId) {
			"${this.name} from ${this.guild.name}"
		} else {
			"${this.name} [${this.id}] from ${this.guild.name} [${this.guild.id}]"
		}
	}
}

val Role.members: List<Member>
	get() {
		return this.guild.getMembersWithRoles(this)
	}