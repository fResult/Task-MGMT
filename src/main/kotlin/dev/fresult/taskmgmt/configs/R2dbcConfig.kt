package dev.fresult.taskmgmt.configs

import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing

@Configuration
@EnableR2dbcAuditing // Make it PrePersist / PreUpdate for createdAt/updatedAt
class R2dbcConfig
