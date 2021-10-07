package net.volcano.jdautilities.utils

import net.dv8tion.jda.api.entities.Member

fun Iterable<Member>.sortToMemberList(): Iterable<Member> {
	return if (!this.iterator().hasNext()) {
		this
	} else
		this.sortedWith(
			compareBy({ it.roles.first { role -> role.isHoisted }?.position ?: -1 }, { it.effectiveName })
		)
}

fun Member.format(): String {
	return "${this.effectiveName} [${this.id}]"
}