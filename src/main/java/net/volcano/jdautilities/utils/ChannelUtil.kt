package net.volcano.jdautilities.utils

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel

object ChannelUtil {

	@JvmStatic
	fun findTextChannel(query: String?, jda: JDA, guild: Guild): TextChannel? {
		var query = query ?: return null
		query = query.trim { it <= ' ' }

		// Try getting a role from the pure discord id if its numerical
		if (query.matches(Regex("(<#)?\\d+>?"))) {
			val channel = jda.getTextChannelById(query.replace("[<#>]".toRegex(), ""))
			if (channel != null) {
				return channel
			}
		}
		var matchedStart = false
		var startMatchPercent = 0.0
		var middleMatchPercent = 0.0
		val possibleChannels = guild.textChannels
		var bestGuess: TextChannel? = null
		for (channel in possibleChannels) {

			// Try role name
			val name = channel.name
			// Can't have a better match than 100%, so return
			if (name.equals(query, ignoreCase = true)) {
				return channel
			}
			val percentage = query.length.toDouble() / name.length.toDouble()
			if (name.lowercase().startsWith(query)) {
				if (percentage > startMatchPercent) {
					bestGuess = channel
					matchedStart = true
					startMatchPercent = percentage
				}
			} else if (!matchedStart && name.lowercase().contains(query)) {
				if (percentage > middleMatchPercent) {
					bestGuess = channel
					middleMatchPercent = percentage
				}
			}
		}
		return bestGuess
	}

}