package net.volcano.jdautils.utils

import net.dv8tion.jda.api.entities.Member

fun Iterable<Member>.sortToMemberList() : Iterable<Member> {
	return this.sortedWith(
		compareBy({ it.roles.first { role -> role.isHoisted }?.position ?: -1 }, { it.effectiveName })
	)
}

fun Member.format() : String {
	return "${this.effectiveName} [${this.id}]"
}