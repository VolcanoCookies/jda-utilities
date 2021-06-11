package net.volcano.jdautils.utils

import net.dv8tion.jda.api.entities.Member

object MemberUtil {

	fun sortToMemberList(members: List<Member>): List<Member> {

		return members.sortedWith(
			compareBy({ it.roles.first { role -> role.isHoisted }?.position ?: -1 }, { it.effectiveName })
		)

	}

}
