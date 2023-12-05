package com.ifpb.devweb.projetodevweb.configuration

import org.jdbi.v3.core.argument.AbstractArgumentFactory
import org.jdbi.v3.core.argument.Argument
import org.jdbi.v3.core.config.ConfigRegistry
import java.sql.Types
import java.util.*

class UUIDArgumentFactory : AbstractArgumentFactory<UUID>(Types.VARCHAR) {
    override fun build(value: UUID, config: ConfigRegistry): Argument {
        return Argument { position, statement, ctx -> statement.setString(position, value.toString()) }
    }
}